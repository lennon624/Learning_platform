package com.xk.sb.sys.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xk.sb.sys.entity.Course;
import com.xk.sb.sys.entity.Video;
import com.xk.sb.sys.mapper.CourseMapper;
import com.xk.sb.sys.mapper.VideoMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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
import com.xk.sb.sys.mapper.ChapterMapper;
import com.xk.sb.sys.entity.Chapter;

import org.springframework.web.bind.annotation.RestController;
import com.xk.sb.common.tools.BaseController;

/**
 * <p>
 * 前端控制器 chrimer
 * </p>
 *
 * @author 实训三组
 * @since 2020-06-30
 */
@RestController
@RequestMapping("/sys/chapter")
public class ChapterController extends BaseController {
    @Autowired
    private ChapterMapper chapterMapper;

    @Autowired
    private CourseMapper courseMapper;

    @Autowired
    private VideoMapper videoMapper;

    Logger logger = LoggerFactory.getLogger(getClass());

    @Value("${web.local}")
    private String typeLocal;

    @ApiImplicitParams({
            @ApiImplicitParam(name = "pageNum", paramType = "query", value = "当前页码", required = true),
            @ApiImplicitParam(name = "pageSize", paramType = "query", value = "每页显示记录数", required = true)
    })
    @ApiOperation(value = "分页获取Chapter信息")
    @RequestMapping(method = RequestMethod.POST, value = "/page")
    public Result page(
            Integer pageNum,
            Integer pageSize) {
        Result result = new Result();
        Page<Chapter> page = new Page<Chapter>(pageNum, pageSize);
        Chapter parms = new Chapter();
        QueryWrapper<Chapter> wrapper = new QueryWrapper<Chapter>(parms);

        result.setData(chapterMapper.selectPage(page, wrapper));
        return result;
    }

    @ApiImplicitParams({
    })
    @ApiOperation(value = "获取全部Chapter信息")
    @RequestMapping(method = RequestMethod.POST, value = "/getAllItems")
    public Result getAllItems() {
        Result result = new Result();
        Chapter parms = new Chapter();
        QueryWrapper<Chapter> wrapper = new QueryWrapper<Chapter>(parms);
        List<Chapter> chapterList = chapterMapper.selectList(wrapper);

        result.setData(chapterList);
        return result;
    }

    @ApiOperation(value = "保存修改Chapter信息")
    @RequestMapping(method = RequestMethod.POST, value = "/save")
    public Result save(@RequestBody Chapter entity) {
        Result result = new Result();
        if (entity.getId() == null) {
            chapterMapper.insert(entity);
        } else {
            chapterMapper.updateById(entity);
        }
        return result;
    }

    @ApiOperation(value = "按id删除Chapter，可以传入多个id用，隔开")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "ids", paramType = "query", value = "传入的id串，用，隔开", required = true)
    })
    @RequestMapping(method = RequestMethod.DELETE, value = "/delete")
    public Result delete(String ids) {
        Result result = new Result();
        List<String> deleteIds = new ArrayList<String>();
        for (String id : ids.split(",")) {
            deleteIds.add(id);
        }
        chapterMapper.deleteBatchIds(deleteIds);
        return result;
    }

    @ApiOperation(value = "按课程id获取章节信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "courseId", paramType = "String", value = "课程id", required = true)
    })
    @RequestMapping(method = RequestMethod.POST, value = "/getChapterByCourseId")
    public Result getChapterByCourseId(String courseId) {
        Result result = new Result();

        Course course = new Course();
        QueryWrapper<Course> courseQueryWrapper = new QueryWrapper<>(course);
        courseQueryWrapper.eq("id", courseId);
        course = courseMapper.selectOne(courseQueryWrapper);
        if (course == null) {
            result.fail("未找到该课程");
        } else {
            Chapter chapter = new Chapter();
            QueryWrapper<Chapter> chapterQueryWrapper = new QueryWrapper<>(chapter);
            chapterQueryWrapper.eq("course_id", course.getId());
            List<Chapter> chapterList = chapterMapper.selectList(chapterQueryWrapper);
            if (chapterList.size() == 0) {
                result.fail("该课程下无章节信息");
            } else {
                // 附加信息
                chapterList = addEtc(chapterList);
                result.success("获取章节成功");
                result.setData(chapterList);
            }
        }
        return result;
    }

    /**
     * 附加章节信息etc
     */
    public List<Chapter> addEtc(List<Chapter> chapterList) {
        //etc的内容
        Map[] ectMap = new HashMap[chapterList.size()];
        for (int i = 0; i < chapterList.size(); i++) {
            ectMap[i] = new HashMap();
            Video video = new Video();
            QueryWrapper<Video> videoQueryWrapper = new QueryWrapper<>(video);
            videoQueryWrapper.eq("chapter_id", chapterList.get(i).getId());
            video = videoMapper.selectOne(videoQueryWrapper);
            if (video != null) {
                // 附加视频id
                ectMap[i].put("video_id", video.getId());
                // 附加视频资源名
                ectMap[i].put("video_name", video.getVideoSrc());
                // 附加视频播放链接

                // 上传附件
                String localPath = "";
                if ("true".equals(typeLocal)) {
                    // 本地路径
                    localPath = "http://localhost:8686/xk/sys/video/play?src=";
                } else {
                    // 服务器上的路径
                    localPath = "http://121.199.64.203/xk/sys/video/play?src=";
                }
                ectMap[i].put("video_link", video.getVideoSrc());
            } else {
                ectMap[i].put("video_id", "该章节未发布视频资源");
            }
            chapterList.get(i).setEtc(ectMap[i]);
        }
        return chapterList;
    }

}
