package com.xk.sb.sys.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xk.sb.sys.entity.ChildCategory;
import com.xk.sb.sys.entity.Course;
import com.xk.sb.sys.mapper.ChildCategoryMapper;
import com.xk.sb.sys.mapper.CourseMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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

import com.xk.sb.common.constants.Result;
import com.xk.sb.sys.mapper.ParentCategoryMapper;
import com.xk.sb.sys.entity.ParentCategory;

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
@RequestMapping("/sys/parent-category")
public class ParentCategoryController extends BaseController {
    @Autowired
    private ParentCategoryMapper parentCategoryMapper;

    @Autowired
    private ChildCategoryMapper childCategoryMapper;

    @Autowired
    private CourseMapper courseMapper;

    Logger logger = LoggerFactory.getLogger(getClass());

    @ApiImplicitParams({
            @ApiImplicitParam(name = "pageNum", paramType = "query", value = "当前页码", required = true),
            @ApiImplicitParam(name = "pageSize", paramType = "query", value = "每页显示记录数", required = true)
    })
    @ApiOperation(value = "分页获取ParentCategory信息")
    @RequestMapping(method = RequestMethod.POST, value = "/page")
    public Result page(
            Integer pageNum,
            Integer pageSize) {
        Result result = new Result();
        Page<ParentCategory> page = new Page<ParentCategory>(pageNum, pageSize);
        ParentCategory parms = new ParentCategory();
        QueryWrapper<ParentCategory> wrapper = new QueryWrapper<ParentCategory>(parms);

        result.setData(parentCategoryMapper.selectPage(page, wrapper));
        return result;
    }

    @ApiImplicitParams({
    })
    @ApiOperation(value = "获取所有父级类别")
    @RequestMapping(method = RequestMethod.POST, value = "/getAllParentCategory")
    public Result getAllParentCategory() {
        Result result = new Result();
        ParentCategory parentCategory = new ParentCategory();
        QueryWrapper<ParentCategory> parentCategoryQueryWrapper = new QueryWrapper<ParentCategory>(parentCategory);
        List<ParentCategory> parentCategories = parentCategoryMapper.selectList(parentCategoryQueryWrapper);
        if (parentCategories.size() > 0) {
            parentCategories = addEtc(parentCategories);
            result.success("获取所有父级类别成功");
            result.setData(parentCategories);
        } else {
            result.fail("无父级类别信息");
        }
        return result;
    }

    @ApiOperation(value = "保存修改ParentCategory信息")
    @RequestMapping(method = RequestMethod.POST, value = "/save")
    public Result save(@RequestBody ParentCategory entity) {
        Result result = new Result();
        if (entity.getId() == null) {
            parentCategoryMapper.insert(entity);
        } else {
            parentCategoryMapper.updateById(entity);
        }
        return result;
    }

    @ApiOperation(value = "按id删除ParentCategory，可以传入多个id用，隔开")
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
        parentCategoryMapper.deleteBatchIds(deleteIds);
        return result;
    }

    @ApiImplicitParams({
            @ApiImplicitParam(name = "parent_category_id", paramType = "String", value = "父级类别的id", required = true)
    })
    @ApiOperation(value = "按id获取父类别")
    @RequestMapping(method = RequestMethod.POST, value = "/getParentCategoryById")
    public Result getParentCategoryById(String parent_category_id) {
        Result result = new Result();

        ParentCategory parentCategory = new ParentCategory();
        QueryWrapper<ParentCategory> parentCategoryQueryWrapper = new QueryWrapper<ParentCategory>(parentCategory);
        parentCategoryQueryWrapper.eq("id", parent_category_id);

        // 获取数据
        List<ParentCategory> parentCategoryList = parentCategoryMapper.selectList(parentCategoryQueryWrapper);

        if (parentCategoryList.size() > 0) {
            parentCategoryList = addEtc(parentCategoryList);
            result.setData(parentCategoryList.get(0));
        } else {
            result.fail("未找到该id对应的类别");
        }
        return result;
    }

    /**
     * 封装父类别信息中的etc
     *
     * @param parentCategoryList 父类别列表
     * @return 带有子类信息的父类别列表
     */
    public List<ParentCategory> addEtc(List<ParentCategory> parentCategoryList) {
        // 获取父类下所有子类
        for (int i = 0; i < parentCategoryList.size(); i++) {
            ChildCategory childCategory = new ChildCategory();
            QueryWrapper<ChildCategory> childCategoryQueryWrapper = new QueryWrapper<>(childCategory);
            childCategoryQueryWrapper.eq("parent_category_id", parentCategoryList.get(i).getId());
            List<ChildCategory> list = childCategoryMapper.selectList(childCategoryQueryWrapper);
            if (list.size() > 0) {
                for (ChildCategory child : list) {
                    Course course = new Course();
                    QueryWrapper<Course> courseQueryWrapper = new QueryWrapper<>(course);
                    courseQueryWrapper.eq("child_category_id", child.getId());
                    List<Course> courseList = courseMapper.selectList(courseQueryWrapper);
                    if (courseList.size() > 0) {
                        child.put("courseList", courseList);
                    }
                }
                parentCategoryList.get(i).put("children", list);
            } else {
                parentCategoryList.get(i).put("children", "未找到父类别下的子类别信息");
            }
            parentCategoryList.get(i).put("children", list);
        }
        return parentCategoryList;
    }
}
