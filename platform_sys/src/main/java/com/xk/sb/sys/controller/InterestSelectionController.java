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
import com.xk.sb.sys.mapper.InterestSelectionMapper;
import com.xk.sb.sys.entity.InterestSelection;

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
@RequestMapping("/sys/interest-selection")
public class InterestSelectionController extends BaseController {
    @Autowired
    private InterestSelectionMapper interestSelectionMapper;

    @ApiImplicitParams({
            @ApiImplicitParam(name = "pageNum", paramType = "query", value = "当前页码", required = true),
            @ApiImplicitParam(name = "pageSize", paramType = "query", value = "每页显示记录数", required = true)
    })
    @ApiOperation(value = "分页获取InterestSelection信息")
    @RequestMapping(method = RequestMethod.POST, value = "/page")
    public Result page(
            Integer pageNum,
            Integer pageSize) {
        Result result = new Result();
        Page<InterestSelection> page = new Page<InterestSelection>(pageNum, pageSize);
        InterestSelection parms = new InterestSelection();
        QueryWrapper<InterestSelection> wrapper = new QueryWrapper<InterestSelection>(parms);

        result.setData(interestSelectionMapper.selectPage(page, wrapper));
        return result;
    }

    @ApiOperation(value = "保存修改InterestSelection信息")
    @RequestMapping(method = RequestMethod.POST, value = "/save")
    public Result save(@RequestBody InterestSelection entity) {
        Result result = new Result();
        if (entity.getId() == null) {
            interestSelectionMapper.insert(entity);
        } else {
            interestSelectionMapper.updateById(entity);
        }
        return result;
    }

    @ApiOperation(value = "按id删除InterestSelection，可以传入多个id用，隔开")
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
        interestSelectionMapper.deleteBatchIds(deleteIds);
        return result;
    }

}
