package com.xk.sb.sys.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xk.sb.common.constants.Result;
import com.xk.sb.common.tools.BaseController;
import com.xk.sb.common.utils.FileUtils;
import com.xk.sb.sys.entity.*;
import com.xk.sb.sys.mapper.*;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 前端控制器 chrimer
 * </p>
 *
 * @author 实训三组
 * @since 2020-06-22
 */
@RestController
@RequestMapping("/sys/student")
public class StudentController extends BaseController {
    @Autowired
    private StudentMapper studentMapper;

    @Autowired
    private CourseMapper courseMapper;

    @Autowired
    private CourseSelectionMapper courseSelectionMapper;

    @Autowired
    private ChapterMapper chapterMapper;

    @Autowired
    private HomeworkMapper homeworkMapper;

    @Autowired
    private InterestSelectionMapper interestSelectionMapper;

    @Autowired
    private CourseCommentMapper courseCommentMapper;

    @Autowired
    private StudentController studentController;

    @Value("${web.local}")
    private String typeLocal;

    Logger logger = LoggerFactory.getLogger(getClass());

    @ApiImplicitParams({
            @ApiImplicitParam(name = "pageNum", paramType = "query", value = "当前页码", required = true),
            @ApiImplicitParam(name = "pageSize", paramType = "query", value = "每页显示记录数", required = true)
    })
    @ApiOperation(value = "分页获取Student信息")
    @RequestMapping(method = RequestMethod.POST, value = "/page")
    public Result page(
            Integer pageNum,
            Integer pageSize) {
        Result result = new Result();
        Page<Student> page = new Page<Student>(pageNum, pageSize);
        Student parms = new Student();
        QueryWrapper<Student> wrapper = new QueryWrapper<Student>(parms);

        result.setData(studentMapper.selectPage(page, wrapper));
        return result;
    }

    @ApiOperation(value = "保存修改Student信息")
    @RequestMapping(method = RequestMethod.POST, value = "/save")
    public Result save(@RequestBody Student entity) {
        Result result = new Result();
        if (entity.getId() == null) {
            studentMapper.insert(entity);
        } else {
            studentMapper.updateById(entity);
        }
        return result;
    }

    @ApiOperation(value = "按id删除Student，可以传入多个id用，隔开")
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
        studentMapper.deleteBatchIds(deleteIds);
        return result;
    }

    @ApiOperation(value = "将课程加入学生的选课列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "student_id", paramType = "String", value = "学生id", required = true),
            @ApiImplicitParam(name = "course_id", paramType = "String", value = "课程id", required = true)
    })
    @RequestMapping(method = RequestMethod.POST, value = "/addCourse2List")
    public Result addCourse2List(String student_id, String course_id) {
        Result result = new Result();
        Student student = new Student();
        QueryWrapper<Student> studentQueryWrapper = new QueryWrapper<>(student);
        studentQueryWrapper.eq("id", student_id);
        student = studentMapper.selectOne(studentQueryWrapper);
        if (student == null) {
            result.setData("未找到该学生");
        } else {
            Course course = new Course();
            QueryWrapper<Course> courseQueryWrapper = new QueryWrapper<>(course);
            courseQueryWrapper.eq("id", course_id);
            course = courseMapper.selectOne(courseQueryWrapper);
            if (course == null) {
                result.setData("未找到该课程");
            } else {
                CourseSelection selection = new CourseSelection();
                QueryWrapper<CourseSelection> courseSelectionQueryWrapper = new QueryWrapper<>(selection);
                courseSelectionQueryWrapper
                        .eq("student_id", student_id)
                        .eq("course_id", course_id);
                List<CourseSelection> selectionList = courseSelectionMapper.selectList(courseSelectionQueryWrapper);
                if (selectionList.size() == 0) {
                    // 插入选课数据
                    CourseSelection courseSelection = new CourseSelection();
                    courseSelection.setStudentId(student_id);
                    courseSelection.setCourseId(course_id);
                    courseSelectionMapper.insert(courseSelection);
                }
                result.setData("成功加入我的选课列表");
            }
        }
        return result;
    }

    @ApiOperation(value = "将课程移出学生的选课列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "student_id", paramType = "String", value = "学生id", required = true),
            @ApiImplicitParam(name = "course_id", paramType = "String", value = "课程id", required = true)
    })
    @RequestMapping(method = RequestMethod.POST, value = "/removeCourseFromList")
    public Result removeCourseFromList(String student_id, String course_id) {
        Result result = new Result();

        Student student = new Student();
        QueryWrapper<Student> studentQueryWrapper = new QueryWrapper<>(student);
        studentQueryWrapper.eq("id", student_id);
        student = studentMapper.selectOne(studentQueryWrapper);
        if (student == null) {
            result.setData("未找到该学生");
        } else {
            Course course = new Course();
            QueryWrapper<Course> courseQueryWrapper = new QueryWrapper<>(course);
            courseQueryWrapper.eq("id", course_id);
            course = courseMapper.selectOne(courseQueryWrapper);
            if (course == null) {
                result.setData("未找到该课程");
            } else {
                // 删除该选课
                CourseSelection courseSelection = new CourseSelection();
                QueryWrapper<CourseSelection> courseSelectionQueryWrapper = new QueryWrapper<>(courseSelection);
                courseSelectionQueryWrapper.eq("course_id", course_id).eq("student_id", student_id);
                courseSelection = courseSelectionMapper.selectOne(courseSelectionQueryWrapper);
                courseSelectionMapper.deleteById(courseSelection);
                result.setData("成功将课程移出我的选课列表");
            }
        }

        return result;
    }

    @ApiOperation(value = "学生做作业")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "file", paramType = "MultipartFile", value = "附件"),
            @ApiImplicitParam(name = "fileName", paramType = "String", value = "附件名"),
            @ApiImplicitParam(name = "chapterId", paramType = "String", value = "章节id", required = true),
            @ApiImplicitParam(name = "studentId", paramType = "String", value = "学生id", required = true)
    })
    @RequestMapping(method = RequestMethod.POST, value = "/doHomework")
    public Result uploadFile(@RequestParam("file") MultipartFile file,
                             String studentId,
                             String chapterId) {
        Result result = new Result();


        if (studentId == null) {
            result.fail("非法的学生id");
        } else {
            Student student = new Student();
            QueryWrapper<Student> studentQueryWrapper = new QueryWrapper<>(student);
            studentQueryWrapper.eq("id", studentId);
            student = studentMapper.selectOne(studentQueryWrapper);
            if (student == null) {
                result.fail("未找到该学生");
            } else {
                Chapter chapter = new Chapter();
                QueryWrapper<Chapter> chapterQueryWrapper = new QueryWrapper<>(chapter);
                chapterQueryWrapper.eq("id", chapterId);
                chapter = chapterMapper.selectOne(chapterQueryWrapper);
                if (chapter == null) {
                    result.fail("未找到该章节");
                } else {
                    // 上传附件
                    String localPath = "";
                    if ("true".equals(typeLocal)) {
                        // 本地路径
                        localPath = "/src/main/resources/files/homework";
                    } else {
                        // 服务器上的路径
                        localPath = "/home/static/homework";
                    }
                    // 获得文件名字
                    String fileName = file.getOriginalFilename();
                    logger.info("上传附件中");
                    String newFileName = FileUtils.upload(file, localPath, fileName);
                    if (newFileName != null) {
                        //上传成功
                        logger.info("上传成功");

                        Homework homework = new Homework();
                        homework.setChapterId(chapterId);
                        homework.setStudentId(studentId);
                        if (fileName != null) {
                            homework.setSrc(newFileName);
                        }
                        homeworkMapper.insert(homework);
                        result.success("作业提交成功");

                    } else {
                        // 上传失败
                        logger.info("上传失败");
                        result.success("作业提交失败，请检查网络");
                    }
                }
            }
        }
        return result;
    }

    @ApiOperation(value = "学生选择兴趣")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "ids", paramType = "String", value = "兴趣(子类别)id，可以传入多个id用英文逗号,隔开", required = true),
            @ApiImplicitParam(name = "studentId", paramType = "String", value = "学生id", required = true),
    })
    @RequestMapping(method = RequestMethod.POST, value = "/selectInterests")
    public Result selectInterests(String ids, String studentId) {
        Result result = new Result();
        List<String> interestIds = new ArrayList<>();
        for (String id : ids.split(",")) {
            interestIds.add(id);
        }
        if (interestIds.size() > 0) {
            for (String id : interestIds) {
                InterestSelection interestSelection = new InterestSelection();
                QueryWrapper<InterestSelection> interestSelectionQueryWrapper = new QueryWrapper<>(interestSelection);
                interestSelectionQueryWrapper
                        .eq("student_id", studentId)
                        .eq("child_category_id", id);
                List<InterestSelection> selectionList = interestSelectionMapper.selectList(interestSelectionQueryWrapper);
                if (selectionList.size() == 0) {
                    InterestSelection selection = new InterestSelection();
                    selection.setChildCategoryId(id);
                    selection.setStudentId(studentId);
                    interestSelectionMapper.insert(selection);
                }
            }
        } else {
            result.fail("未选择兴趣");
        }
        return result;
    }

    @ApiOperation(value = "学生添加兴趣")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "childCategoryId", paramType = "String", value = "兴趣(子类别)id", required = true),
            @ApiImplicitParam(name = "studentId", paramType = "String", value = "学生id", required = true),
    })
    @RequestMapping(method = RequestMethod.POST, value = "/addInterest")
    public Result addInterest(String childCategoryId, String studentId) {
        Result result = new Result();
        Student student = new Student();
        QueryWrapper<Student> studentQueryWrapper = new QueryWrapper<>(student);
        studentQueryWrapper.eq("id", studentId);
        student = studentMapper.selectOne(studentQueryWrapper);
        if (student != null) {
            InterestSelection interestSelection = new InterestSelection();
            QueryWrapper<InterestSelection> interestSelectionQueryWrapper = new QueryWrapper<>(interestSelection);
            interestSelectionQueryWrapper
                    .eq("student_id", studentId)
                    .eq("child_category_id", childCategoryId);
            interestSelection = interestSelectionMapper.selectOne(interestSelectionQueryWrapper);
            if (interestSelection == null) {
                interestSelection = new InterestSelection();
                interestSelection.setStudentId(studentId);
                interestSelection.setChildCategoryId(childCategoryId);
                interestSelectionMapper.insert(interestSelection);
                result.success("兴趣更新成功");
            }
        }
        return result;
    }

    @ApiOperation(value = "学生移除兴趣")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "childCategoryId", paramType = "String", value = "兴趣(子类别)id", required = true),
            @ApiImplicitParam(name = "studentId", paramType = "String", value = "学生id", required = true),
    })
    @RequestMapping(method = RequestMethod.POST, value = "/removeInterest")
    public Result removeInterest(String childCategoryId, String studentId) {
        Result result = new Result();
        Student student = new Student();
        QueryWrapper<Student> studentQueryWrapper = new QueryWrapper<>(student);
        studentQueryWrapper.eq("id", studentId);
        student = studentMapper.selectOne(studentQueryWrapper);
        if (student != null) {
            InterestSelection interestSelection = new InterestSelection();
            QueryWrapper<InterestSelection> interestSelectionQueryWrapper = new QueryWrapper<>(interestSelection);
            interestSelectionQueryWrapper
                    .eq("student_id", studentId)
                    .eq("child_category_id", childCategoryId);
            interestSelection = interestSelectionMapper.selectOne(interestSelectionQueryWrapper);
            if (interestSelection != null) {
                interestSelectionMapper.deleteById(interestSelection.getId());
                result.success("兴趣更新成功");
            }
        }
        return result;
    }

    @ApiOperation(value = "查询课程是否在学生的选课列表里")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "courseId", paramType = "String", value = "课程id", required = true),
            @ApiImplicitParam(name = "studentId", paramType = "String", value = "学生id", required = true)
    })
    @RequestMapping(method = RequestMethod.POST, value = "/queryMyList")
    public Result queryMyList(String studentId, String courseId) {
        Result result = new Result();

        Student student = new Student();
        QueryWrapper<Student> studentQueryWrapper = new QueryWrapper<>(student);
        studentQueryWrapper.eq("id", studentId);
        student = studentMapper.selectOne(studentQueryWrapper);
        if (student != null) {

            CourseSelection courseSelection = new CourseSelection();
            QueryWrapper<CourseSelection> courseSelectionQueryWrapper = new QueryWrapper<>(courseSelection);
            courseSelectionQueryWrapper.eq("course_id", courseId).eq("student_id", studentId);
            courseSelection = courseSelectionMapper.selectOne(courseSelectionQueryWrapper);
            if (courseSelection != null) {
                result.setData("true");
                result.success("该课程在学生的选课列表");
            } else {
                result.setData("false");
                result.success("该课程不在学生的选课列表");
            }
        } else {
            result.fail("未找到该学生");
        }
        return result;
    }

    @ApiOperation(value = "发表课程评论")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "comment", paramType = "String", value = "评论内容", required = true),
            @ApiImplicitParam(name = "courseId", paramType = "String", value = "课程id", required = true),
            @ApiImplicitParam(name = "username", paramType = "String", value = "用户名", required = true),
    })
    @RequestMapping(method = RequestMethod.POST, value = "/comment")
    public Result comment(String comment, String courseId, String username) {
        Result result = new Result();

        CourseComment courseComment = new CourseComment();
        courseComment.setComment(comment);
        courseComment.setCourseId(courseId);
        courseComment.setFromUserId(username);
        courseCommentMapper.insert(courseComment);
        result.success("评论发表成功！");
        return result;
    }

    @ApiOperation(value = "学生批量删除选课")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "studentId", paramType = "String", value = "学生id", required = true),
            @ApiImplicitParam(name = "ids", paramType = "String", value = "传入的id串，用，隔开", required = true)
    })
    @RequestMapping(method = RequestMethod.POST, value = "/removeCourses")
    public Result removeCourse(String studentId, String ids) {
        Result result = new Result();

        Student student = new Student();
        QueryWrapper<Student> studentQueryWrapper = new QueryWrapper<>(student);
        studentQueryWrapper.eq("id", studentId);
        if (studentMapper.selectCount(studentQueryWrapper) > 0) {
            List<String> deleteIds = new ArrayList<String>();
            for (String id : ids.split(",")) {
                deleteIds.add(id);
            }

            for (String id : deleteIds) {
                studentController.removeCourseFromList(studentId, id);
            }
            result.success("删除选课成功");
        } else {
            result.fail("学生不存在");
        }
        return result;
    }
}
