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
import com.xk.sb.sys.mapper.TelCodeMapper;
import com.xk.sb.sys.entity.TelCode;

import org.springframework.web.bind.annotation.RestController;
import com.xk.sb.common.tools.BaseController;

/**
 * <p>
 *  前端控制器 chrimer
 * </p>
 *
 * @author 实训三组
 * @since 2020-06-23
 */
@RestController
@RequestMapping("/sys/tel-code")
public class TelCodeController extends BaseController {
    @Autowired
    private TelCodeMapper telCodeMapper;

Logger logger = LoggerFactory.getLogger(getClass());

@ApiImplicitParams({
@ApiImplicitParam(name = "pageNum",paramType = "query",value = "当前页码",required = true),
@ApiImplicitParam(name = "pageSize",paramType = "query",value = "每页显示记录数",required = true)
})
@ApiOperation(value = "分页获取TelCode信息")
    @RequestMapping(method= RequestMethod.POST,value="/page")
    public Result page(
        Integer pageNum,
Integer pageSize) {
Result result = new Result();
Page<TelCode> page = new Page<TelCode>(pageNum, pageSize);
TelCode parms = new TelCode();
QueryWrapper<TelCode> wrapper = new QueryWrapper<TelCode>(parms);

result.setData(telCodeMapper.selectPage(page, wrapper));
return result;
}

@ApiImplicitParams({
})
@ApiOperation(value = "获取全部TelCode信息")
@RequestMapping(method= RequestMethod.POST,value="/getAllItems")
public Result getAllItems() {
Result result = new Result();
TelCode parms = new TelCode();
QueryWrapper<TelCode> wrapper = new QueryWrapper<TelCode>(parms);
List<TelCode> telCodeList = telCodeMapper.selectList(wrapper);

result.setData(telCodeList);
return result;
}

@ApiOperation(value = "保存修改TelCode信息")
@RequestMapping(method= RequestMethod.POST,value="/save")
public Result save(@RequestBody TelCode entity) {
Result result = new Result();
if (entity.getId() == null) {
telCodeMapper.insert(entity);
} else {
telCodeMapper.updateById(entity);
}
        return result;
    }

    @ApiOperation(value = "按id删除TelCode，可以传入多个id用，隔开")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "ids",paramType = "query",value = "传入的id串，用，隔开",required = true)
    })
    @RequestMapping(method= RequestMethod.DELETE,value="/delete")
    public Result delete( String ids) {
        Result result = new Result();
        List<String> deleteIds = new ArrayList<String>();
        for (String id : ids.split(",")) {
            deleteIds.add(id);
        }
        telCodeMapper.deleteBatchIds(deleteIds);
        return result;
    }

}
