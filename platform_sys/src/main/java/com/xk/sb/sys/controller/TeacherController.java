package com.xk.sb.sys.controller;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xk.sb.common.utils.UploadFileUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.xk.sb.common.constants.Result;
import com.xk.sb.sys.mapper.TeacherMapper;
import com.xk.sb.sys.entity.Teacher;

import com.xk.sb.common.tools.BaseController;
import org.springframework.web.multipart.MultipartFile;

/**
 * <p>
 * 前端控制器 chrimer
 * </p>
 *
 * @author 实训三组
 * @since 2020-06-22
 */
@RestController
@RequestMapping("/sys/teacher")
public class TeacherController extends BaseController {
    @Autowired
    private TeacherMapper teacherMapper;

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @ApiImplicitParams({
            @ApiImplicitParam(name = "pageNum", paramType = "query", value = "当前页码", required = true),
            @ApiImplicitParam(name = "pageSize", paramType = "query", value = "每页显示记录数", required = true)
    })
    @ApiOperation(value = "分页获取Teacher信息")
    @RequestMapping(method = RequestMethod.POST, value = "/page")
    public Result page(
            Integer pageNum,
            Integer pageSize) {
        Result result = new Result();
        Page<Teacher> page = new Page<Teacher>(pageNum, pageSize);
        Teacher parms = new Teacher();
        QueryWrapper<Teacher> wrapper = new QueryWrapper<Teacher>(parms);

        result.setData(teacherMapper.selectPage(page, wrapper));
        return result;
    }

    @ApiOperation(value = "保存修改Teacher信息")
    @RequestMapping(method = RequestMethod.POST, value = "/save")
    public Result save(@RequestBody Teacher entity) {
        Result result = new Result();
        if (entity.getId() == null) {
            teacherMapper.insert(entity);
        } else {
            teacherMapper.updateById(entity);
        }
        return result;
    }

    @ApiOperation(value = "按id删除Teacher，可以传入多个id用，隔开")
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
        teacherMapper.deleteBatchIds(deleteIds);
        return result;
    }

    @ApiOperation(value = "上传文件")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "file", paramType = "MultipartFile", value = "文件", required = true),
            @ApiImplicitParam(name = "fileName", paramType = "String", value = "文件名", required = true)
    })
    @RequestMapping(method = RequestMethod.POST, value = "/uploadFile")
    public Result uploadFile(@RequestParam("file") MultipartFile file, String fileName) {
        Result result = new Result();

        logger.info("文件名: " + fileName);
        Map<String, String> data = UploadFileUtil.uploadFile(file, fileName);

        String uploadSuccess = "true";
        String uploadResult = data.get("success");
        String uploadMessage = data.get("message");

        if (uploadResult.equals(uploadSuccess)) {
            result.success(uploadMessage);
        } else {
            result.fail(uploadMessage);
        }

        return result;
    }

}
