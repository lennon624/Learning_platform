package ${package.Controller};

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
import ${package.Mapper}.${table.mapperName};
import ${package.Entity}.${entity};

<#if restControllerStyle>
import org.springframework.web.bind.annotation.RestController;
<#else>
import org.springframework.stereotype.Controller;
</#if>
<#if superControllerClassPackage??>
import ${superControllerClassPackage};
</#if>

/**
 * <p>
 * ${table.comment!} 前端控制器 chrimer
 * </p>
 *
 * @author ${author}
 * @since ${date}
 */
<#if restControllerStyle>
@RestController
<#else>
@Controller
</#if>
@RequestMapping("<#if package.ModuleName??>/${package.ModuleName}</#if>/<#if controllerMappingHyphenStyle??>${controllerMappingHyphen}<#else>${table.entityPath}</#if>")
<#if kotlin>
class ${table.controllerName}<#if superControllerClass??> : ${superControllerClass}()</#if>
<#else>
<#if superControllerClass??>
public class ${table.controllerName} extends ${superControllerClass} {
<#else>
public class ${table.controllerName} {
</#if>
    @Autowired
    private ${table.mapperName} ${table.entityPath}Mapper;

Logger logger = LoggerFactory.getLogger(getClass());

@ApiImplicitParams({
@ApiImplicitParam(name = "pageNum",paramType = "query",value = "当前页码",required = true),
@ApiImplicitParam(name = "pageSize",paramType = "query",value = "每页显示记录数",required = true)
})
@ApiOperation(value = "分页获取${entity}信息")
    @RequestMapping(method= RequestMethod.POST,value="/page")
    public Result page(
        Integer pageNum,
Integer pageSize) {
Result result = new Result();
Page<${entity}> page = new Page<${entity}>(pageNum, pageSize);
${entity} parms = new ${entity}();
QueryWrapper<${entity}> wrapper = new QueryWrapper<${entity}>(parms);

result.setData(${table.entityPath}Mapper.selectPage(page, wrapper));
return result;
}

@ApiImplicitParams({
})
@ApiOperation(value = "获取全部${entity}信息")
@RequestMapping(method= RequestMethod.POST,value="/getAllItems")
public Result getAllItems() {
Result result = new Result();
${entity} parms = new ${entity}();
QueryWrapper<${entity}> wrapper = new QueryWrapper<${entity}>(parms);
List<${entity}> ${table.entityPath}List = ${table.entityPath}Mapper.selectList(wrapper);

result.setData(${table.entityPath}List);
return result;
}

@ApiOperation(value = "保存修改${entity}信息")
@RequestMapping(method= RequestMethod.POST,value="/save")
public Result save(@RequestBody ${entity} entity) {
Result result = new Result();
if (entity.getId() == null) {
${table.entityPath}Mapper.insert(entity);
} else {
${table.entityPath}Mapper.updateById(entity);
}
        return result;
    }

    @ApiOperation(value = "按id删除${entity}，可以传入多个id用，隔开")
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
        ${table.entityPath}Mapper.deleteBatchIds(deleteIds);
        return result;
    }

}
</#if>
