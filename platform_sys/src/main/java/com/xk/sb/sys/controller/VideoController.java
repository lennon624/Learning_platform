package com.xk.sb.sys.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xk.sb.sys.entity.Video;
import com.xk.sb.sys.mapper.VideoMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.xk.sb.common.constants.Result;

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
@RequestMapping("/sys/video")
public class VideoController extends BaseController {
    @Autowired
    private VideoMapper videoMapper;

    Logger logger = LoggerFactory.getLogger(getClass());

    @ApiImplicitParams({
            @ApiImplicitParam(name = "pageNum", paramType = "query", value = "当前页码", required = true),
            @ApiImplicitParam(name = "pageSize", paramType = "query", value = "每页显示记录数", required = true)
    })
    @ApiOperation(value = "分页获取Video信息")
    @RequestMapping(method = RequestMethod.POST, value = "/page")
    public Result page(
            Integer pageNum,
            Integer pageSize) {
        Result result = new Result();
        Page<Video> page = new Page<Video>(pageNum, pageSize);
        Video parms = new Video();
        QueryWrapper<Video> wrapper = new QueryWrapper<Video>(parms);

        result.setData(videoMapper.selectPage(page, wrapper));
        return result;
    }

    @ApiImplicitParams({
    })
    @ApiOperation(value = "获取全部Video信息")
    @RequestMapping(method = RequestMethod.POST, value = "/getAllItems")
    public Result getAllItems() {
        Result result = new Result();
        Video parms = new Video();
        QueryWrapper<Video> wrapper = new QueryWrapper<Video>(parms);
        List<Video> videoList = videoMapper.selectList(wrapper);

        result.setData(videoList);
        return result;
    }

    @ApiOperation(value = "保存修改Video信息")
    @RequestMapping(method = RequestMethod.POST, value = "/save")
    public Result save(@RequestBody Video entity) {
        Result result = new Result();
        if (entity.getId() == null) {
            videoMapper.insert(entity);
        } else {
            videoMapper.updateById(entity);
        }
        return result;
    }

    @ApiOperation(value = "按id删除Video，可以传入多个id用，隔开")
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
        videoMapper.deleteBatchIds(deleteIds);
        return result;
    }

    @ApiOperation(value = "按章节id查找对应的视频")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "chapterId", paramType = "String", value = "章节id", required = true)
    })
    @RequestMapping(method = RequestMethod.POST, value = "/getVideoSrcByChapterId")
    public Result getVideoSrcByChapterId(String chapterId) {
        Result result = new Result();

        Video video = new Video();
        QueryWrapper<Video> videoQueryWrapper = new QueryWrapper<>(video);
        videoQueryWrapper.eq("chapter_id", chapterId);
        video = videoMapper.selectOne(videoQueryWrapper);
        if (video == null) {
            result.fail("该章节未发布视频");
        } else {
            // 获取src
            String videoSrc = video.getVideoSrc();
            result.success("获取章节视频成功");
            // 服务器上的链接
//            result.setData("http://121.199.64.203/xk/sys/video/play?src=" + videoSrc);
            // 本地的链接
            result.setData(videoSrc);
        }
        return result;
    }

}
