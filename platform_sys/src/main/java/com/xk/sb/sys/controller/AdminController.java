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
import com.xk.sb.sys.mapper.AdminMapper;
import com.xk.sb.sys.entity.Admin;

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
@RequestMapping("/sys/admin")
public class AdminController extends BaseController {
    @Autowired
    private AdminMapper adminMapper;

    @ApiImplicitParams({
            @ApiImplicitParam(name = "pageNum", paramType = "query", value = "当前页码", required = true),
            @ApiImplicitParam(name = "pageSize", paramType = "query", value = "每页显示记录数", required = true)
    })
    @ApiOperation(value = "分页获取Admin信息")
    @RequestMapping(method = RequestMethod.POST, value = "/page")
    public Result page(
            Integer pageNum,
            Integer pageSize) {
        Result result = new Result();
        Page<Admin> page = new Page<Admin>(pageNum, pageSize);
        Admin parms = new Admin();
        QueryWrapper<Admin> wrapper = new QueryWrapper<Admin>(parms);

        result.setData(adminMapper.selectPage(page, wrapper));
        return result;
    }

    @ApiOperation(value = "保存修改Admin信息")
    @RequestMapping(method = RequestMethod.POST, value = "/save")
    public Result save(@RequestBody Admin entity) {
        Result result = new Result();
        if (entity.getId() == null) {
            adminMapper.insert(entity);
        } else {
            adminMapper.updateById(entity);
        }
        return result;
    }

    @ApiOperation(value = "按id删除Admin，可以传入多个id用，隔开")
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
        adminMapper.deleteBatchIds(deleteIds);
        return result;
    }

}
