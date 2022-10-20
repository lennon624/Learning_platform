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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.xk.sb.common.constants.Result;
import com.xk.sb.sys.mapper.HomeworkMapper;
import com.xk.sb.sys.entity.Homework;

import org.springframework.web.bind.annotation.RestController;
import com.xk.sb.common.tools.BaseController;

/**
 * <p>
 * 前端控制器 chrimer
 * </p>
 *
 * @author 实训三组
 * @since 2020-07-01
 */
@RestController
@RequestMapping("/sys/homework")
public class HomeworkController extends BaseController {
    @Autowired
    private HomeworkMapper homeworkMapper;

    Logger logger = LoggerFactory.getLogger(getClass());

    @ApiImplicitParams({
            @ApiImplicitParam(name = "pageNum", paramType = "query", value = "当前页码", required = true),
            @ApiImplicitParam(name = "pageSize", paramType = "query", value = "每页显示记录数", required = true)
    })
    @ApiOperation(value = "分页获取Homework信息")
    @RequestMapping(method = RequestMethod.POST, value = "/page")
    public Result page(
            Integer pageNum,
            Integer pageSize) {
        Result result = new Result();
        Page<Homework> page = new Page<Homework>(pageNum, pageSize);
        Homework parms = new Homework();
        QueryWrapper<Homework> wrapper = new QueryWrapper<Homework>(parms);

        result.setData(homeworkMapper.selectPage(page, wrapper));
        return result;
    }

    @ApiImplicitParams({
    })
    @ApiOperation(value = "获取全部Homework信息")
    @RequestMapping(method = RequestMethod.POST, value = "/getAllItems")
    public Result getAllItems() {
        Result result = new Result();
        Homework parms = new Homework();
        QueryWrapper<Homework> wrapper = new QueryWrapper<Homework>(parms);
        List<Homework> homeworkList = homeworkMapper.selectList(wrapper);

        result.setData(homeworkList);
        return result;
    }

    @ApiOperation(value = "保存修改Homework信息")
    @RequestMapping(method = RequestMethod.POST, value = "/save")
    public Result save(@RequestBody Homework entity) {
        Result result = new Result();
        if (entity.getId() == null) {
            homeworkMapper.insert(entity);
        } else {
            homeworkMapper.updateById(entity);
        }
        return result;
    }

    @ApiOperation(value = "按id删除Homework，可以传入多个id用，隔开")
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
        homeworkMapper.deleteBatchIds(deleteIds);
        return result;
    }

}
