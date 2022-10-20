package com.xk.sb.sys.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xk.sb.common.recommend.ItemBased;
import com.xk.sb.common.utils.DateTool;
import com.xk.sb.common.utils.RandomUtils;
import com.xk.sb.sys.entity.*;
import com.xk.sb.sys.mapper.*;
import org.apache.mahout.cf.taste.common.TasteException;
import org.apache.mahout.cf.taste.impl.model.MemoryIDMigrator;
import org.apache.mahout.cf.taste.recommender.RecommendedItem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMethod;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.xk.sb.common.constants.Result;

import org.springframework.web.bind.annotation.RestController;
import com.xk.sb.common.tools.BaseController;

/**
 * <p>
 * 前端控制器 chrimer
 * </p>
 *
 * @author 实训三组
 * @since 2020-09-12
 */
@RestController
@RequestMapping("/sys/user-preference")
public class UserPreferenceController extends BaseController {
    @Autowired
    private UserPreferenceMapper userPreferenceMapper;

    @Autowired
    private UserCourseBaseMapper userCourseBaseMapper;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private CourseMapper courseMapper;

    @Autowired
    private StudentMapper studentMapper;

    @Autowired
    private TeacherMapper teacherMapper;

    @Autowired
    private CourseController courseController;

    Logger logger = LoggerFactory.getLogger(getClass());

    @ApiOperation(value = "更新用户偏好")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userId", paramType = "String", value = "用户id", required = true)
    })
    @RequestMapping(method = RequestMethod.POST, value = "/updateUserPreference")
    public Result updateUserPreference(String userId) {
        Result result = new Result();

        if (userExists(userId)) {
            UserCourseBase userCourseBase = new UserCourseBase();
            QueryWrapper<UserCourseBase> userCourseBaseQueryWrapper = new QueryWrapper<>(userCourseBase);
            userCourseBaseQueryWrapper.eq("user_id", userId);
            List<UserCourseBase> userCourseBaseList = userCourseBaseMapper.selectList(userCourseBaseQueryWrapper);
            if (userCourseBaseList.size() > 0) {
                for (UserCourseBase base : userCourseBaseList) {
                    // 计算评分
                    double score = calculateScore(base);

                    UserPreference userPreference = new UserPreference();
                    QueryWrapper<UserPreference> userPreferenceQueryWrapper = new QueryWrapper<>(userPreference);
                    userPreferenceQueryWrapper
                            .eq("user_id", userId)
                            .eq("course_id", base.getCourseId());
                    userPreference = userPreferenceMapper.selectOne(userPreferenceQueryWrapper);

                    // 如果暂无用户偏好数据
                    if (userPreference == null) {
                        UserPreference preference = new UserPreference();
                        MemoryIDMigrator thing2long = new MemoryIDMigrator();
                        Long userIdLong = thing2long.toLongID(base.getUserId());
                        Long courseIdLong = thing2long.toLongID(base.getCourseId());
                        preference.setUserId(base.getUserId())
                                .setCourseId(base.getCourseId())
                                .setVal(String.valueOf(score))
                                .setTime(DateTool.getCurrTime())
                                .setUserIdLong(String.valueOf(userIdLong))
                                .setCourseIdLong(String.valueOf(courseIdLong));
                        userPreferenceMapper.insert(preference);
                    } else {
                        userPreference.setVal(String.valueOf(score));
                        userPreferenceMapper.updateById(userPreference);
                    }
                }
                logger.info("更新了用户id" + userId + "的偏好数据");
                result.success("更新了用户id" + userId + "的偏好数据");
            } else {
                result.fail("用户未有相关数据");
            }
        } else {
            result.fail("用户不存在");
        }
        return result;
    }

    @ApiOperation(value = "更新所有用户偏好")
    @ApiImplicitParams({
    })
    @RequestMapping(method = RequestMethod.POST, value = "/updateAllUserPreference")
    public Result updateAllUserPreference() {
        Result result = new Result();

        User user = new User();
        QueryWrapper<User> userQueryWrapper = new QueryWrapper<>(user);
        List<User> userList = userMapper.selectList(userQueryWrapper);

        if (userList.size() > 0) {
            for (User u : userList) {
                if (userExists(u.getId())) {
                    UserCourseBase userCourseBase = new UserCourseBase();
                    QueryWrapper<UserCourseBase> userCourseBaseQueryWrapper = new QueryWrapper<>(userCourseBase);
                    userCourseBaseQueryWrapper.eq("user_id", u.getId());
                    List<UserCourseBase> userCourseBaseList = userCourseBaseMapper.selectList(userCourseBaseQueryWrapper);
                    if (userCourseBaseList.size() > 0) {
                        for (UserCourseBase base : userCourseBaseList) {
                            // 计算评分
                            double score = calculateScore(base);

                            UserPreference userPreference = new UserPreference();
                            QueryWrapper<UserPreference> userPreferenceQueryWrapper = new QueryWrapper<>(userPreference);
                            userPreferenceQueryWrapper
                                    .eq("user_id", u.getId())
                                    .eq("course_id", base.getCourseId());
                            userPreference = userPreferenceMapper.selectOne(userPreferenceQueryWrapper);

                            // 如果暂无用户偏好数据
                            if (userPreference == null) {
                                UserPreference preference = new UserPreference();
                                MemoryIDMigrator thing2long = new MemoryIDMigrator();
                                Long userIdLong = thing2long.toLongID(base.getUserId());
                                Long courseIdLong = thing2long.toLongID(base.getCourseId());
                                preference.setUserId(base.getUserId())
                                        .setCourseId(base.getCourseId())
                                        .setVal(String.valueOf(score))
                                        .setTime(DateTool.getCurrTime())
                                        .setUserIdLong(String.valueOf(userIdLong))
                                        .setCourseIdLong(String.valueOf(courseIdLong));
                                userPreferenceMapper.insert(preference);
                            } else {
                                userPreference.setVal(String.valueOf(score));
                                userPreferenceMapper.updateById(userPreference);
                            }
                        }
                        logger.info("更新了用户id" + u.getId() + "的偏好数据");
                        result.success("更新了用户id" + u.getId() + "的偏好数据");
                    } else {
                        result.fail("用户未有相关数据");
                    }
                } else {
                    result.fail("用户不存在");
                }
            }
        }
        return result;
    }

    @ApiOperation(value = "获取用户推荐课程")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userId", paramType = "String", value = "用户id", required = true),
            @ApiImplicitParam(name = "howMany", paramType = "String", value = "推荐数量", required = true)
    })
    @RequestMapping(method = RequestMethod.POST, value = "/getRecommendedList")
    public Result getRecommendedList(String userId, String howMany) {
        Result result = new Result();

        if (userExists(userId)) {
            UserPreference userPreference = new UserPreference();
            QueryWrapper<UserPreference> userPreferenceQueryWrapper = new QueryWrapper<>(userPreference);
            userPreferenceQueryWrapper.eq("user_id", userId);
            if (userPreferenceMapper.selectCount(userPreferenceQueryWrapper) > 0) {
                // mahout数据模型只支持long类型的数据，故这里需要把string类型替换为long
                MemoryIDMigrator thing2long = new MemoryIDMigrator();
                Long userIdLong = thing2long.toLongID(userId);
                try {
                    // 基于物品的推荐
                    ItemBased itemBased = new ItemBased();
                    List<RecommendedItem> recommendedItemList = itemBased.getRecommendedItemList(String.valueOf(userIdLong), Integer.parseInt(howMany));
                    List<Course> courseList = new ArrayList<>();
                    for (RecommendedItem item : recommendedItemList) {
                        // 获取的物品id为long类型，所以根据long_id找到对应的uuid
                        QueryWrapper<UserPreference> preferenceQueryWrapper = new QueryWrapper<>(new UserPreference());
                        preferenceQueryWrapper.eq("course_id_long", item.getItemID());
                        UserPreference preference = userPreferenceMapper.selectList(preferenceQueryWrapper).get(0);
                        Course course = new Course();
                        QueryWrapper<Course> courseQueryWrapper = new QueryWrapper<>(course);
                        courseQueryWrapper.eq("id", preference.getCourseId());
                        course = courseMapper.selectOne(courseQueryWrapper);
                        if (course != null) {
                            courseList.add(course);
                        }
                    }
                    if (courseList.size() > 0) {
                        courseList = courseController.addEtc(courseList);
                        // 随机选取3个推荐课程
                        result.setData(randomCourse(courseList, 3));
                        result.success("用户" + userId + "的课程推荐列表");
                    } else {
                        // 如果算法未推荐出课程，则获取用户兴趣中的课程。
                        logger.info("用户" + userId + "无推荐课程，获取兴趣课程中");
                        courseList = (List<Course>) courseController.getCourseByInterest(getStudentIdByUserId(userId)).getData();
                        // 随机选取3个推荐课程
                        result.setData(randomCourse(courseList, 3));
                    }
                } catch (TasteException | SQLException e) {
                    e.printStackTrace();
                }
            } else {
                // 如果算法未推荐出课程，则获取用户兴趣中的课程。
                logger.info("用户" + userId + "无偏好数据，获取兴趣课程中");
                List<Course> courseList = (List<Course>) courseController.getCourseByInterest(getStudentIdByUserId(userId)).getData();
                if (courseList.size() > 3) {
                    int fromIndex = (int) (Math.random() * (courseList.size() - 3));
                    courseList = courseList.subList(fromIndex, fromIndex + 3);
                }
                result.setData(courseList);
            }
        } else {
            result.fail("用户不存在");
        }
        return result;
    }

    @ApiOperation(value = "按id删除UserPreference，可以传入多个id用，隔开")
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
        userPreferenceMapper.deleteBatchIds(deleteIds);
        return result;
    }

    /**
     * @param userId 用户id
     * @return true 如果用户已存在
     */
    public boolean userExists(String userId) {
        User user = new User();
        QueryWrapper<User> userQueryWrapper = new QueryWrapper<>(user);
        userQueryWrapper.eq("id", userId);
        return userMapper.selectCount(userQueryWrapper) > 0;
    }

    /**
     * 计算用户对物品的评分
     *
     * @param base 用户与物品的相关数据
     * @return 分数
     */
    public double calculateScore(UserCourseBase base) {
        // 浏览次数*0.1 + 页面停留时间*0.03 + 加入课程*3 + 评论*0.5 + 分享次数*0.2
        double score;
        score = (base.getBrowse() == null ? 0 : base.getBrowse()) * 0.1
                + (base.getDuration() == null ? 0 : base.getDuration()) * 0.03
                + (base.getJoinCourse() == null ? 0 : base.getJoinCourse()) * 3
                + (base.getComment() == null ? 0 : base.getComment()) * 0.5
                + (base.getShare() == null ? 0 : base.getShare()) * 0.2;
        if (base.getDislike() != null && base.getDislike() == 1) {
            score = 0;
        }
        return score;
    }

    /**
     * 根据用户id获取学生id
     *
     * @param userId 用户id
     * @return 学生id
     */
    public String getStudentIdByUserId(String userId) {
        Student student = new Student();
        QueryWrapper<Student> studentQueryWrapper = new QueryWrapper<>(student);
        studentQueryWrapper.eq("user_id", userId);
        student = studentMapper.selectOne(studentQueryWrapper);
        logger.info("学生id是" + student.getId());
        return student.getId();
    }


    /**
     * 随机选取n个推荐课程
     *
     * @param courseList 课程列表
     * @param n          选取的数量
     * @return 课程列表
     */
    public List<Course> randomCourse(List<Course> courseList, int n) {
        List<Course> recommendedList = new ArrayList<>();
        if (courseList.size() > n) {
            // 随机选取3个推荐
            int[] index = RandomUtils.randomIntNumbersBetween(0, courseList.size(), n);
            for (int i : index) {
                recommendedList.add(courseList.get(i - 1));
            }
        }
        return recommendedList;
    }
}
