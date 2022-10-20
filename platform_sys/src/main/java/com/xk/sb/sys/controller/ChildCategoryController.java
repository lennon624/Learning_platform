package com.xk.sb.sys.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xk.sb.sys.entity.Course;
import com.xk.sb.sys.entity.InterestSelection;
import com.xk.sb.sys.entity.ParentCategory;
import com.xk.sb.sys.mapper.CourseMapper;
import com.xk.sb.sys.mapper.InterestSelectionMapper;
import com.xk.sb.sys.mapper.ParentCategoryMapper;
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
import com.xk.sb.sys.mapper.ChildCategoryMapper;
import com.xk.sb.sys.entity.ChildCategory;

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
@RequestMapping("/sys/child-category")
public class ChildCategoryController extends BaseController {
    @Autowired
    private ChildCategoryMapper childCategoryMapper;

    @Autowired
    private CourseMapper courseMapper;

    @Autowired
    private ParentCategoryMapper parentCategoryMapper;

    @Autowired
    private InterestSelectionMapper interestSelectionMapper;

    Logger logger = LoggerFactory.getLogger(getClass());

    @ApiImplicitParams({
            @ApiImplicitParam(name = "pageNum", paramType = "query", value = "当前页码", required = true),
            @ApiImplicitParam(name = "pageSize", paramType = "query", value = "每页显示记录数", required = true)
    })
    @ApiOperation(value = "分页获取ChildCategory信息")
    @RequestMapping(method = RequestMethod.POST, value = "/page")
    public Result page(
            Integer pageNum,
            Integer pageSize) {
        Result result = new Result();
        Page<ChildCategory> page = new Page<ChildCategory>(pageNum, pageSize);
        ChildCategory parms = new ChildCategory();
        QueryWrapper<ChildCategory> wrapper = new QueryWrapper<ChildCategory>(parms);

        result.setData(childCategoryMapper.selectPage(page, wrapper));
        return result;
    }

    @ApiImplicitParams({
            @ApiImplicitParam(name = "studentId", paramType = "String", value = "学生id，可选。如果传入该参数" +
                    "则会在每个子类别的etc中附加该学生是否选择了该类别，true：已选；false：未选")
    })
    @ApiOperation(value = "获取所有子类别")
    @RequestMapping(method = RequestMethod.POST, value = "/getChildCategory")
    public Result getChildCategory(String studentId) {
        Result result = new Result();

        ChildCategory childCategory = new ChildCategory();
        QueryWrapper<ChildCategory> childCategoryQueryWrapper = new QueryWrapper<ChildCategory>(childCategory);
        childCategoryQueryWrapper.ne("id", "");
        List<ChildCategory> childCategories = childCategoryMapper.selectList(childCategoryQueryWrapper);
        if (childCategories.size() == 0) {
            result.fail("未找到子类");
            logger.info("未找到");
        } else {
            if (studentId != null) {
                for (ChildCategory category : childCategories) {
                    InterestSelection interestSelection = new InterestSelection();
                    QueryWrapper<InterestSelection> interestSelectionQueryWrapper = new QueryWrapper<>(interestSelection);
                    interestSelectionQueryWrapper
                            .eq("student_id", studentId)
                            .eq("child_category_id", category.getId());
                    interestSelection = interestSelectionMapper.selectOne(interestSelectionQueryWrapper);
                    category.setEtc(interestSelection != null);
                }
            }
            result.success("获取子类成功");
            result.setData(childCategories);
        }
        return result;
    }

    @ApiImplicitParams({
            @ApiImplicitParam(name = "parent_category_id", paramType = "String", value = "父级类别的id", required = true)
    })
    @ApiOperation(value = "根据父级类别的id获取该类别下的所有子类别")
    @RequestMapping(method = RequestMethod.POST, value = "/getChildCategoryByParentId")
    public Result getChildCategoryByParentId(String parent_category_id) {
        Result result = new Result();

        ChildCategory childCategory = new ChildCategory();
        QueryWrapper<ChildCategory> childCategoryQueryWrapper = new QueryWrapper<ChildCategory>(childCategory);
        childCategoryQueryWrapper.eq("parent_category_id", parent_category_id);
        List<ChildCategory> childCategories = childCategoryMapper.selectList(childCategoryQueryWrapper);
        if (childCategories.size() == 0) {
            result.fail("未找到该分类下的子类");
            logger.info("前端传来的parent_category_id是：" + parent_category_id + "该分类下没有子类");
        } else {
            result.success("获取子类成功");
            result.setData(childCategories);
        }
        return result;
    }

    @ApiImplicitParams({
            @ApiImplicitParam(name = "course_id", paramType = "String", value = "课程id", required = true)
    })
    @ApiOperation(value = "根据课程id获取子类别以及相关信息")
    @RequestMapping(method = RequestMethod.POST, value = "/getChildCategoryByCourseId")
    public Result getChildCategoryByCourseId(String course_id) {
        Result result = new Result();

        // 获取该课程的信息
        Course course = new Course();
        QueryWrapper<Course> courseQueryWrapper = new QueryWrapper<>(course);
        courseQueryWrapper.eq("id", course_id);
        course = courseMapper.selectOne(courseQueryWrapper);

        if (course == null) {
            result.fail("找不到该课程");
        } else {
            // 获取对应子类
            ChildCategory childCategory = new ChildCategory();
            QueryWrapper<ChildCategory> childCategoryQueryWrapper = new QueryWrapper<>(childCategory);
            childCategoryQueryWrapper.eq("id", course.getChildCategoryId());
            childCategory = childCategoryMapper.selectOne(childCategoryQueryWrapper);
            if (childCategory == null) {
                result.fail("未找到课程对应的子类别");
            } else {
                // 获取父类别信息
                ParentCategory parentCategory = new ParentCategory();
                QueryWrapper<ParentCategory> parentCategoryQueryWrapper = new QueryWrapper<ParentCategory>(parentCategory);
                parentCategoryQueryWrapper.eq("id", childCategory.getParentCategoryId());
                parentCategory = parentCategoryMapper.selectOne(parentCategoryQueryWrapper);
                Map<String, Object> data = new HashMap<>();
                data.put("course_info", course);
                data.put("child_category_info", childCategory);
                data.put("parent_category_info", parentCategory);
                result.setData(data);
                result.success("根据课程id获取子类别信息成功");
            }
        }
        return result;
    }

    @ApiOperation(value = "保存修改ChildCategory信息")
    @RequestMapping(method = RequestMethod.POST, value = "/save")
    public Result save(@RequestBody ChildCategory entity) {
        Result result = new Result();
        if (entity.getId() == null) {
            childCategoryMapper.insert(entity);
        } else {
            childCategoryMapper.updateById(entity);
        }
        return result;
    }

    @ApiOperation(value = "按id删除ChildCategory，可以传入多个id用，隔开")
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
        childCategoryMapper.deleteBatchIds(deleteIds);
        return result;
    }

}
