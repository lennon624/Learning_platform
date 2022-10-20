package com.xk.sb.sys.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.ArrayList;
import java.util.List;

import com.xk.sb.common.constants.Result;
import com.xk.sb.sys.mapper.SchoolMapper;
import com.xk.sb.sys.entity.School;

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
@RequestMapping("/sys/school")
public class SchoolController extends BaseController {
    @Autowired
    private SchoolMapper schoolMapper;

    @ApiImplicitParams({
            @ApiImplicitParam(name = "pageNum", paramType = "query", value = "当前页码", required = true),
            @ApiImplicitParam(name = "pageSize", paramType = "query", value = "每页显示记录数", required = true)
    })
    @ApiOperation(value = "分页获取School信息")
    @RequestMapping(method = RequestMethod.POST, value = "/page")
    public Result page(
            Integer pageNum,
            Integer pageSize) {
        Result result = new Result();
        Page<School> page = new Page<School>(pageNum, pageSize);
        School parms = new School();
        QueryWrapper<School> wrapper = new QueryWrapper<School>(parms);

        result.setData(schoolMapper.selectPage(page, wrapper));
        return result;
    }

    @ApiOperation(value = "保存修改School信息")
    @RequestMapping(method = RequestMethod.POST, value = "/save")
    public Result save(@RequestBody School entity) {
        Result result = new Result();
        if (entity.getId() == null) {
            schoolMapper.insert(entity);
        } else {
            schoolMapper.updateById(entity);
        }
        return result;
    }

    @ApiOperation(value = "按id删除School，可以传入多个id用，隔开")
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
        schoolMapper.deleteBatchIds(deleteIds);
        return result;
    }

    @ApiOperation(value = "按id获取School")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "school_id", paramType = "String", value = "学校id", required = true)
    })
    @RequestMapping(method = RequestMethod.POST, value = "/getSchoolById")
    public Result getSchoolById(String school_id) {
        Result result = new Result();

        School school = new School();
        QueryWrapper<School> schoolQueryWrapper = new QueryWrapper<>(school);
        schoolQueryWrapper.eq("id", school_id);
        school = schoolMapper.selectOne(schoolQueryWrapper);
        if (school == null) {
            result.fail("未找到该学校");
        } else {
            result.setData(school);
        }
        return result;
    }
}
