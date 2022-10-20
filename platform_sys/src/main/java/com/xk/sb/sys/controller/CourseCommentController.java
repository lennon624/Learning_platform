package com.xk.sb.sys.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xk.sb.sys.entity.CourseSelection;
import org.apache.ibatis.executor.ResultExtractor;
import org.apache.ibatis.reflection.ArrayUtil;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMethod;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.logging.Logger;

import com.xk.sb.common.constants.Result;
import com.xk.sb.sys.mapper.CourseCommentMapper;
import com.xk.sb.sys.entity.CourseComment;

import org.springframework.web.bind.annotation.RestController;
import com.xk.sb.common.tools.BaseController;

/**
 * <p>
 * 前端控制器 chrimer
 * </p>
 *
 * @author 实训三组
 * @since 2020-06-22
 */
@RestController
@RequestMapping("/sys/course-comment")
public class CourseCommentController extends BaseController {
    @Autowired
    private CourseCommentMapper courseCommentMapper;

    private final static org.slf4j.Logger logger = LoggerFactory.getLogger(UserController.class);

    @ApiImplicitParams({
            @ApiImplicitParam(name = "courseId", paramType = "String", value = "课程id", required = true)
    })
    @ApiOperation(value = "根据课程id返回所有评论列表")
    @RequestMapping(method = RequestMethod.POST, value = "/getCommentsById")
    public Result getCommentsById(String courseId) {
        Result result = new Result();

        QueryWrapper<CourseComment> courseCommentQueryWrapper = new QueryWrapper<>();
        courseCommentQueryWrapper.eq("course_id", courseId);
        List<CourseComment> courseComments = courseCommentMapper.selectList(courseCommentQueryWrapper);

        if (courseComments.size() > 0) {
            result.success("评论列表加载成功");
            result.setData(courseComments);
        }else {
            result.success("此课程暂时没有评论...");
            logger.info("此课程暂时没有评论...");
        }

        return result;
    }

    @ApiImplicitParams({
            @ApiImplicitParam(name = "pageNum", paramType = "query", value = "当前页码", required = true),
            @ApiImplicitParam(name = "pageSize", paramType = "query", value = "每页显示记录数", required = true)
    })
    @ApiOperation(value = "分页获取CourseComment信息")
    @RequestMapping(method = RequestMethod.POST, value = "/page")
    public Result page(
            Integer pageNum,
            Integer pageSize) {
        Result result = new Result();
        Page<CourseComment> page = new Page<CourseComment>(pageNum, pageSize);
        CourseComment parms = new CourseComment();
        QueryWrapper<CourseComment> wrapper = new QueryWrapper<CourseComment>(parms);

        result.setData(courseCommentMapper.selectPage(page, wrapper));
        return result;
    }

    @ApiOperation(value = "保存修改CourseComment信息")
    @RequestMapping(method = RequestMethod.POST, value = "/save")
    public Result save(@RequestBody CourseComment entity) {
        Result result = new Result();
        if (entity.getId() == null) {
            courseCommentMapper.insert(entity);
        } else {
            courseCommentMapper.updateById(entity);
        }
        return result;
    }

    @ApiOperation(value = "按id删除CourseComment，可以传入多个id用，隔开")
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
        courseCommentMapper.deleteBatchIds(deleteIds);
        return result;
    }

}
