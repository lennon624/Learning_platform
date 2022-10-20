package com.xk.sb.sys.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xk.sb.common.utils.DateTool;
import com.xk.sb.sys.entity.Course;
import com.xk.sb.sys.entity.Message;
import com.xk.sb.sys.entity.User;
import com.xk.sb.sys.mapper.CourseMapper;
import com.xk.sb.sys.mapper.MessageMapper;
import com.xk.sb.sys.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.xk.sb.common.constants.Result;
import com.xk.sb.sys.mapper.TalkingsMapper;
import com.xk.sb.sys.entity.Talkings;

import org.springframework.web.bind.annotation.RestController;
import com.xk.sb.common.tools.BaseController;

/**
 * <p>
 * 前端控制器 chrimer
 * </p>
 *
 * @author 实训三组
 * @since 2020-08-31
 */
@RestController
@RequestMapping("/sys/talkings")
public class TalkingsController extends BaseController {
    @Autowired
    private TalkingsMapper talkingsMapper;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private CourseMapper courseMapper;

    @Autowired
    private MessageMapper messageMapper;


    Logger logger = LoggerFactory.getLogger(getClass());


    @ApiImplicitParams({
            @ApiImplicitParam(name = "userId", paramType = "query", value = "用户id", required = true),
            @ApiImplicitParam(name = "courseId", paramType = "query", value = "课程id", required = true),
            @ApiImplicitParam(name = "talkingContent", paramType = "query", value = "发表的讨论内容", required = true)
    })
    @ApiOperation(value = "发表讨论")
    @RequestMapping(method = RequestMethod.POST, value = "/reportTalking")
    public Result reportTalking(String userId, String courseId, String talkingContent) {
        Result result = new Result();

        Talkings talking = new Talkings();

        //获得时间
        String date = DateTool.getCurrTime();
        date = date.substring(0, 4) + "年" + date.substring(4, 6) + "月" + date.substring(6, 8)
                + "日 " + date.substring(8, 11) + ":" + date.substring(11, 13) + ":" + date.substring(13, 15);

        talking.setUserId(userId);
        talking.setCourseId(courseId);
        talking.setIsRootTalking("1");
        talking.setTalkingContent(talkingContent);
        talking.setDate(date);
        talkingsMapper.insert(talking);

        logger.info("成功发表讨论！");
        result.success("成功发表讨论！");

        return result;
    }

    @ApiImplicitParams({
            @ApiImplicitParam(name = "courseId", paramType = "query", value = "题目id PS：非空-单个题目的talking 空-全部", required = true),
    })
    @ApiOperation(value = "获取评论列表")
    @RequestMapping(method = RequestMethod.POST, value = "/getTalkings")
    public Result getTalkings(String courseId) {
        Result result = new Result();

        QueryWrapper<Talkings> talkingsQueryWrapper = new QueryWrapper<>();

        //获得单个题目的讨论
        talkingsQueryWrapper
                .eq("is_root_talking", "1")
                .eq("course_id", courseId)
                .orderByDesc("date");
        //一级讨论
        List<Talkings> rootList = talkingsMapper.selectList(talkingsQueryWrapper);
        if (rootList.size() <= 0) {
            logger.info("暂无评论");
        } else {
            Map[] rootMaps = new HashMap[rootList.size()];
            for (int i = 0; i < rootList.size(); i++) {
                rootMaps[i] = new HashMap();
                //获得用户名
                QueryWrapper<User> userQueryWrapper = new QueryWrapper<>();
                userQueryWrapper.eq("id", rootList.get(i).getUserId());
                User user = userMapper.selectOne(userQueryWrapper);
                //获得题目实体
                QueryWrapper<Course> questionsQueryWrapper = new QueryWrapper<>();
                questionsQueryWrapper.eq("id", rootList.get(i).getCourseId());
                Course course = courseMapper.selectOne(questionsQueryWrapper);

                rootMaps[i].put("id", rootList.get(i).getId());
                rootMaps[i].put("replyUserId", rootList.get(i).getUserId());
                rootMaps[i].put("username", user.getUsername());
                rootMaps[i].put("talkingContent", rootList.get(i).getTalkingContent());
                rootMaps[i].put("date", rootList.get(i).getDate());
                rootMaps[i].put("getGoods", rootList.get(i).getGetGoods());
                rootMaps[i].put("courseId", course.getId());
                rootMaps[i].put("courseName", course.getCourseName());

                //二级讨论
                talkingsQueryWrapper = new QueryWrapper<>();
                talkingsQueryWrapper
                        .eq("talking_id_parent", rootList.get(i).getId())
                        .orderByDesc("date");
                List<Talkings> childList = talkingsMapper.selectList(talkingsQueryWrapper);
                if (childList.size() <= 0) {
                    rootMaps[i].put("replyCnt", childList.size());//子讨论个数
                    logger.info("暂无子评论");
                } else {
                    rootMaps[i].put("replyCnt", childList.size());//子讨论个数

                    Map[] childMaps = new HashMap[childList.size()];
                    ArrayList<Object> replyList = new ArrayList<>();
                    for (int j = 0; j < childList.size(); j++) {
                        childMaps[j] = new HashMap();
                        //获得用户（回复方和被回复方
                        userQueryWrapper = new QueryWrapper<>();
                        userQueryWrapper.eq("id", childList.get(j).getUserId());
                        user = userMapper.selectOne(userQueryWrapper);
                        userQueryWrapper = new QueryWrapper<>();
                        userQueryWrapper.eq("id", childList.get(j).getUserIdBereply());
                        User user1 = userMapper.selectOne(userQueryWrapper);

                        childMaps[j].put("id", childList.get(j).getId());
                        childMaps[j].put("replyUserId", user.getId());
                        childMaps[j].put("username", user.getUsername());
                        childMaps[j].put("username1", user1.getUsername());
                        childMaps[j].put("talkingIdParent", rootList.get(i).getId());
                        childMaps[j].put("talkingContent", childList.get(j).getTalkingContent());
                        childMaps[j].put("date", childList.get(j).getDate());
                        childMaps[j].put("getGoods", childList.get(j).getGetGoods());

                        replyList.add(childMaps[j]);
                    }
                    rootMaps[i].put("replys", replyList);
                }
            }
            result.setData(rootMaps);
        }
        return result;
    }

    @ApiImplicitParams({
            @ApiImplicitParam(name = "talkingId", paramType = "query", value = "讨论id", required = true),
    })
    @ApiOperation(value = "根据讨论id获取讨论列表（本身的和子列表）")
    @RequestMapping(method = RequestMethod.POST, value = "/getTalkingsByTalkingId")
    public Result getTalkingsByTalkingId(String talkingId) {
        Result result = new Result();

        Talkings talking = talkingsMapper.selectById(talkingId);
//        HashMap rootMap = new HashMap();
        HashMap[] rootMap = new HashMap[1];
        rootMap[0] = new HashMap();

        //获得用户名
        QueryWrapper<User> userQueryWrapper = new QueryWrapper<>();
        userQueryWrapper.eq("id", talking.getUserId());
        User user = userMapper.selectOne(userQueryWrapper);
        //获得题目实体
        QueryWrapper<Course> questionsQueryWrapper = new QueryWrapper<>();
        questionsQueryWrapper.eq("id", talking.getCourseId());
        Course course = courseMapper.selectOne(questionsQueryWrapper);

        rootMap[0].put("id", talking.getId());
        rootMap[0].put("replyUserId", talking.getUserId());
        rootMap[0].put("username", user.getUsername());
        rootMap[0].put("talkingContent", talking.getTalkingContent());
        rootMap[0].put("date", talking.getDate());
        rootMap[0].put("getGoods", talking.getGetGoods());
        rootMap[0].put("courseId", course.getId());
        rootMap[0].put("courseName", course.getCourseName());
        rootMap[0].put("isRootTalking", talking.getIsRootTalking());

        //二级讨论
        QueryWrapper<Talkings> talkingsQueryWrapper = new QueryWrapper<>();
        talkingsQueryWrapper
                .eq("talking_id_parent", talking.getId())
                .orderByDesc("date");
        List<Talkings> childList = talkingsMapper.selectList(talkingsQueryWrapper);
        if (childList.size() <= 0) {
            rootMap[0].put("replyCnt", childList.size());//子讨论个数
            logger.info("暂无子评论");
        } else {
            rootMap[0].put("replyCnt", childList.size());//子讨论个数

            Map[] childMaps = new HashMap[childList.size()];
            ArrayList<Object> replyList = new ArrayList<>();
            for (int j = 0; j < childList.size(); j++) {
                childMaps[j] = new HashMap();
                //获得用户（回复方和被回复方
                userQueryWrapper = new QueryWrapper<>();
                userQueryWrapper.eq("id", childList.get(j).getUserId());
                user = userMapper.selectOne(userQueryWrapper);
                userQueryWrapper = new QueryWrapper<>();
                userQueryWrapper.eq("id", childList.get(j).getUserIdBereply());
                User user1 = userMapper.selectOne(userQueryWrapper);

                childMaps[j].put("id", childList.get(j).getId());
                childMaps[j].put("replyUserId", user.getId());
                childMaps[j].put("username", user.getUsername());
                childMaps[j].put("username1", user1.getUsername());
                childMaps[j].put("talkingIdParent", talking.getId());
                childMaps[j].put("talkingContent", childList.get(j).getTalkingContent());
                childMaps[j].put("date", childList.get(j).getDate());
                childMaps[j].put("getGoods", childList.get(j).getGetGoods());
                childMaps[j].put("isRootTalking", childList.get(j).getIsRootTalking());

                replyList.add(childMaps[j]);
            }
            rootMap[0].put("replys", replyList);
        }


        result.setData(rootMap);

        return result;
    }

    @ApiImplicitParams({
            @ApiImplicitParam(name = "userId", paramType = "query", value = "用户id", required = true),
            @ApiImplicitParam(name = "replyUserId", paramType = "query", value = "回复的用户id，即将变成被回复的用户id", required = true),
            @ApiImplicitParam(name = "talkingIdParent", paramType = "query", value = "父级讨论id", required = true),
            @ApiImplicitParam(name = "replyTxt", paramType = "query", value = "回复内容", required = true),
    })
    @ApiOperation(value = "回复讨论")
    @RequestMapping(method = RequestMethod.POST, value = "/replyTalking")
    public Result replyTalking(String userId, String replyUserId, String talkingIdParent, String replyTxt) {
        Result result = new Result();

        Talkings talking = new Talkings();

        QueryWrapper<Talkings> talkingsQueryWrapper = new QueryWrapper<>();
        talkingsQueryWrapper.eq("id", talkingIdParent);
        Talkings parentTalking = talkingsMapper.selectOne(talkingsQueryWrapper);

        //获得时间
        String date = DateTool.getCurrTime();
        date = date.substring(0, 4) + "年" + date.substring(4, 6) + "月" + date.substring(6, 8)
                + "日 " + date.substring(8, 11) + ":" + date.substring(11, 13) + ":" + date.substring(13, 15);

        //讨论表add讨论
        talking.setCourseId(parentTalking.getCourseId());
        talking.setIsRootTalking("0");
        talking.setTalkingIdParent(talkingIdParent);
        talking.setUserId(userId);
        talking.setUserIdBereply(replyUserId);
        talking.setTalkingContent(replyTxt);
        talking.setDate(date);
        talkingsMapper.insert(talking);

        //消息表add消息
        Message message = new Message();
        message.setRelateId(talking.getId());
        message.setMessageType("talking");
        message.setSenderId(userId);
        message.setReceiverId(replyUserId);
        message.setDate(date);
        message.setIsView("0");
        messageMapper.insert(message);
        logger.info("成功添加讨论消息");


        logger.info("成功回复讨论！");
        result.success("成功回复讨论！");

        return result;
    }

    @ApiImplicitParams({
            @ApiImplicitParam(name = "talkingId", paramType = "query", value = "讨论id", required = true),
    })
    @ApiOperation(value = "删除讨论")
    @RequestMapping(method = RequestMethod.POST, value = "/deleteTalking")
    public Result deleteTalking(String talkingId) {
        Result result = new Result();

        QueryWrapper<Talkings> talkingsQueryWrapper = new QueryWrapper<>();
        talkingsQueryWrapper.eq("id", talkingId);
        Talkings talking = talkingsMapper.selectOne(talkingsQueryWrapper);

        if (talking.getIsRootTalking().equals("1")) {//根级讨论
            talkingsQueryWrapper = new QueryWrapper<>();
            talkingsQueryWrapper.eq("talking_id_parent", talkingId);
            List<Talkings> talkingsList = talkingsMapper.selectList(talkingsQueryWrapper);

            ArrayList<String> deleteIds = new ArrayList<>();
            if (talkingsList.size() > 0) {//父级别下有回复
                for (Talkings talkings : talkingsList) {
                    deleteIds.add(talkings.getId());
                }
            }
            deleteIds.add(talkingId);
            talkingsMapper.deleteBatchIds(deleteIds);

        } else {//子级别讨论
            talkingsMapper.deleteById(talkingId);
        }

        logger.info("成功删除相应讨论或回复！");
        result.success("成功删除相应讨论或回复！");

        return result;
    }

}
