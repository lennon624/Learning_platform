package com.xk.sb.sys.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xk.sb.sys.entity.*;
import com.xk.sb.sys.mapper.*;
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
@RequestMapping("/sys/course")
public class CourseController extends BaseController {
    @Autowired
    private CourseMapper courseMapper;

    @Autowired
    private InterestSelectionMapper interestSelectionMapper;

    @Autowired
    private TeacherMapper teacherMapper;

    @Autowired
    private SchoolMapper schoolMapper;

    @Autowired
    private StudentMapper studentMapper;

    @Autowired
    private ChildCategoryMapper childCategoryMapper;

    @Autowired
    private CourseSelectionMapper courseSelectionMapper;

    Logger logger = LoggerFactory.getLogger(getClass());

    @ApiImplicitParams({
            @ApiImplicitParam(name = "pageNum", paramType = "query", value = "当前页码", required = true),
            @ApiImplicitParam(name = "pageSize", paramType = "query", value = "每页显示记录数", required = true)
    })
    @ApiOperation(value = "分页获取Course信息")
    @RequestMapping(method = RequestMethod.POST, value = "/page")
    public Result page(
            Integer pageNum,
            Integer pageSize) {
        Result result = new Result();
        Page<Course> page = new Page<Course>(pageNum, pageSize);
        Course parms = new Course();
        QueryWrapper<Course> wrapper = new QueryWrapper<Course>(parms);

        Page data = courseMapper.selectPage(page, wrapper);
        List<Course> courseList = data.getRecords();

        // 附加额外信息
        courseList = addEtc(courseList);

        data.setRecords(courseList);

        result.setData(courseMapper.selectPage(page, wrapper));
        return result;
    }

    @ApiImplicitParams({
    })
    @ApiOperation(value = "获取全部Course信息")
    @RequestMapping(method = RequestMethod.POST, value = "/getAllItems")
    public Result getAllItems() {
        Result result = new Result();
        Course parms = new Course();
        QueryWrapper<Course> wrapper = new QueryWrapper<Course>(parms);
        List<Course> courseList = courseMapper.selectList(wrapper);

        result.setData(courseList);
        return result;
    }

    @ApiOperation(value = "保存修改Course信息")
    @RequestMapping(method = RequestMethod.POST, value = "/save")
    public Result save(@RequestBody Course entity) {
        Result result = new Result();
        if (entity.getId() == null) {
            courseMapper.insert(entity);
        } else {
            courseMapper.updateById(entity);
        }
        return result;
    }

    @ApiOperation(value = "按id删除Course，可以传入多个id用，隔开")
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
        courseMapper.deleteBatchIds(deleteIds);
        return result;
    }

    @ApiOperation(value = "获取学生兴趣下的课程")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "student_id", paramType = "query", value = "传入的学生id", required = true)
    })
    @RequestMapping(method = RequestMethod.POST, value = "/getCourseByInterest")
    public Result getCourseByInterest(String student_id) {
        Result result = new Result();

        Student student = new Student();
        QueryWrapper<Student> studentQueryWrapper = new QueryWrapper<>(student);
        studentQueryWrapper.eq("id", student_id);
        student = studentMapper.selectOne(studentQueryWrapper);
        if (student == null) {
            result.fail("未找到该id对应的学生");
        } else {
            // 获取该学生的所有兴趣的id
            InterestSelection interestSelection = new InterestSelection();
            QueryWrapper<InterestSelection> interestSelectionQueryWrapper = new QueryWrapper<InterestSelection>(interestSelection);
            interestSelectionQueryWrapper.eq("student_id", student_id);
            List<InterestSelection> interestSelectionList = interestSelectionMapper.selectList(interestSelectionQueryWrapper);
            if (interestSelectionList.size() > 0) {
                // 获取该兴趣id下的所有课程信息
                Course course = new Course();
                QueryWrapper<Course> courseQueryWrapper;
                List<Course> courseList = new ArrayList<Course>();

                for (int i = 0; i < interestSelectionList.size(); i++) {
                    courseQueryWrapper = new QueryWrapper<Course>(course);
                    courseQueryWrapper.eq("child_category_id", interestSelectionList.get(i).getChildCategoryId());
                    List<Course> list = courseMapper.selectList(courseQueryWrapper);
                    if (list.size() > 0) {
                        // 附加额外信息
                        list = addEtc(list);
                        courseList.addAll(list);
                    }
                }

                if (courseList.size() > 0) {
                    // 去除学生已选的课程
                    for (int i = 0; i < courseList.size(); i++) {
                        CourseSelection courseSelection = new CourseSelection();
                        QueryWrapper<CourseSelection> courseSelectionQueryWrapper = new QueryWrapper<>(courseSelection);
                        courseSelectionQueryWrapper
                                .eq("student_id", student_id)
                                .eq("course_id", courseList.get(i).getId());
                        courseSelection = courseSelectionMapper.selectOne(courseSelectionQueryWrapper);
                        if (courseSelection != null) {
                            courseList.remove(i);
                        }
                    }
                    logger.info("根据兴趣获取课程信息成功");
                    result.success("根据兴趣获取课程信息成功");
                    result.setData(courseList);
                } else {
                    logger.info("未找到该分类下的课程");
                    result.fail("未找到该分类下的课程");
                }
            } else {
                logger.info("该学生未选择兴趣");
                result.fail("该学生未选择兴趣");
            }
        }

        return result;
    }

    @ApiImplicitParams({
            @ApiImplicitParam(name = "category_id", paramType = "String", value = "类别id", required = true)
    })
    @ApiOperation(value = "根据类别获取所有课程信息")
    @RequestMapping(method = RequestMethod.POST, value = "/getCourseByCategory")
    public Result getCourseByCategory(String category_id) {
        Result result = new Result();
        Course course = new Course();
        QueryWrapper<Course> courseQueryWrapper = new QueryWrapper<Course>(course);
        courseQueryWrapper.eq("child_category_id", category_id);

        List<Course> courseList = courseMapper.selectList(courseQueryWrapper);

        if (courseList.size() > 0) {
            // 附加额外信息
            courseList = addEtc(courseList);
            result.setData(courseList);
            result.success("获取课程信息成功");
        } else {
            result.fail("未找到该分类下的课程");
        }
        return result;
    }

    @ApiImplicitParams({
            @ApiImplicitParam(name = "course_name", paramType = "query", value = "课程名", required = true)
    })
    @ApiOperation(value = "根据课程名查找课程")
    @RequestMapping(method = RequestMethod.POST, value = "/getCourseByName")
    public Result getCourseByName(String course_name) {
        Result result = new Result();
        Course param = new Course();
        QueryWrapper<Course> courseQueryWrapper = new QueryWrapper<Course>(param);
        courseQueryWrapper.like("course_name", course_name);
        List<Course> courseList = courseMapper.selectList(courseQueryWrapper);
        if (courseList.size() > 0) {
            // 附加额外信息
            courseList = addEtc(courseList);
            result.setData(courseList);
        } else {
            result.fail("未找到匹配课程");
        }
        return result;
    }

    @ApiImplicitParams({
            @ApiImplicitParam(name = "course_id", paramType = "String", value = "课程名", required = true)
    })
    @ApiOperation(value = "根据课程id查找课程")
    @RequestMapping(method = RequestMethod.POST, value = "/getCourseById")
    public Result getCourseById(String course_id) {
        Result result = new Result();
        Course param = new Course();
        QueryWrapper<Course> courseQueryWrapper = new QueryWrapper<Course>(param);
        courseQueryWrapper.eq("id", course_id);
        List<Course> courseList = courseMapper.selectList(courseQueryWrapper);
        if (courseList.size() > 0) {
            // 附加额外信息
            courseList = addEtc(courseList);
            result.setData(courseList);
        } else {
            result.fail("未找到该课程");
        }
        return result;
    }

    @ApiImplicitParams({
            @ApiImplicitParam(name = "parent_category_id", paramType = "String", value = "父级类别id", required = true)
    })
    @ApiOperation(value = "获取父级类别下所有课程信息")
    @RequestMapping(method = RequestMethod.POST, value = "/getCourseByParentCategoryId")
    public Result getCourseByParentCategoryId(String parent_category_id) {
        Result result = new Result();

        // 获取父级类别下所有子类别
        ChildCategory childCategory = new ChildCategory();
        QueryWrapper<ChildCategory> childCategoryQueryWrapper = new QueryWrapper<ChildCategory>(childCategory);
        childCategoryQueryWrapper.eq("parent_category_id", parent_category_id);
        List<ChildCategory> childCategories = childCategoryMapper.selectList(childCategoryQueryWrapper);

        // 分别获取每个子类下的课程信息
        if (childCategories.size() > 0) {
            List<Course> courseList = new ArrayList<>();
            for (ChildCategory category : childCategories) {
                Course course = new Course();
                QueryWrapper<Course> courseQueryWrapper = new QueryWrapper<>(course);
                courseQueryWrapper.eq("child_category_id", category.getId());
                List<Course> list = courseMapper.selectList(courseQueryWrapper);
                if (list.size() > 0) {
                    list = addEtc(list);
                    courseList.addAll(list);
                }
            }

            if (courseList.size() > 0) {
                result.success("获取课程成功");
                result.setData(courseList);
            } else {
                result.fail("未找到该类别下的课程");
            }
        }
        return result;
    }

    @ApiOperation(value = "查询学生所有选课")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "studentId", paramType = "String", value = "学生id", required = true)
    })
    @RequestMapping(method = RequestMethod.POST, value = "/getMyCourseList")
    public Result getMyCourseList(String studentId) {
        Result result = new Result();
        CourseSelection courseSelection = new CourseSelection();
        QueryWrapper<CourseSelection> courseSelectionQueryWrapper = new QueryWrapper<>(courseSelection);
        courseSelectionQueryWrapper.eq("student_id", studentId);
        List<CourseSelection> courseSelectionList = courseSelectionMapper.selectList(courseSelectionQueryWrapper);
        if (courseSelectionList.size() < 0) {
            result.fail("你还未选择课程哦");
        } else {
            List<Course> courseList = new ArrayList<>();
            for (CourseSelection selection : courseSelectionList) {
                Course course = new Course();
                QueryWrapper<Course> courseQueryWrapper = new QueryWrapper<>(course);
                courseQueryWrapper.eq("id", selection.getCourseId());
                course = courseMapper.selectOne(courseQueryWrapper);
                if (course != null) {
                    courseList.add(course);
                }
            }
            courseList = addEtc(courseList);
            result.setData(courseList);
            result.success("获取选课列表成功");
        }
        return result;
    }

    @ApiOperation(value = "获取热门课程")
    @ApiImplicitParams({
    })
    @RequestMapping(method = RequestMethod.POST, value = "/getPopularCourses")
    public Result getPopularCourses() {
        Result result = new Result();

        // 获取选课最多的前10条课程id
        List<CourseSelection> popList = courseSelectionMapper.getPopCourseList();
        List<Course> courseList = new ArrayList<>();
        if (popList.size() > 0) {
            for (CourseSelection popCourse : popList) {
                Course course = courseMapper.selectById(popCourse.getCourseId());
                courseList.add(course);
            }
        }

        courseList = addEtc(courseList);

        result.setData(courseList);
        return result;
    }


    /**
     * 添加课程对应的教师、学校信息
     *
     * @param courseList 课程列表
     * @return 带有教师、学校信息的课程列表
     */
    public List<Course> addEtc(List<Course> courseList) {
        // 获取课程对应的教师、学校信息
        for (int i = 0; i < courseList.size(); i++) {
            Teacher teacher = new Teacher();
            School school = new School();
            QueryWrapper<Teacher> teacherQueryWrapper = new QueryWrapper<Teacher>(teacher);
            teacherQueryWrapper.eq("id", courseList.get(i).getTeacherId());
            teacher = teacherMapper.selectOne(teacherQueryWrapper);
            if (teacher == null) {
                courseList.get(i).put("teacher_id", null);
                courseList.get(i).put("teacher_name", "未找到该课的教师");
            } else {
                courseList.get(i).put("teacher_id", teacher.getId());
                courseList.get(i).put("teacher_name", teacher.getName());
                QueryWrapper<School> schoolQueryWrapper = new QueryWrapper<School>(school);
                schoolQueryWrapper.eq("id", teacher.getSchoolId());
                school = schoolMapper.selectOne(schoolQueryWrapper);
                if (school != null) {
                    courseList.get(i).put("school_id", school.getId());
                    courseList.get(i).put("school_name", school.getName());
                }
            }
        }
        return courseList;
    }
}
