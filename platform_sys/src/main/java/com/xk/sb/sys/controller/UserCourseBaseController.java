package com.xk.sb.sys.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xk.sb.sys.entity.Course;
import com.xk.sb.sys.entity.User;
import com.xk.sb.sys.mapper.CourseMapper;
import com.xk.sb.sys.mapper.UserMapper;
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
import com.xk.sb.sys.mapper.UserCourseBaseMapper;
import com.xk.sb.sys.entity.UserCourseBase;

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
@RequestMapping("/sys/user-course-base")
public class UserCourseBaseController extends BaseController {
    @Autowired
    private UserCourseBaseMapper userCourseBaseMapper;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private CourseMapper courseMapper;

    Logger logger = LoggerFactory.getLogger(getClass());

    @ApiOperation(value = "用户浏览课程")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userId", paramType = "String", value = "用户id", required = true),
            @ApiImplicitParam(name = "courseId", paramType = "String", value = "课程id", required = true)
    })
    @RequestMapping(method = RequestMethod.POST, value = "/browse")
    public Result browse(String userId, String courseId) {
        Result result = new Result();

        if (userExists(userId)) {
            if (courseExists(courseId)) {
                UserCourseBase userCourseBase = new UserCourseBase();
                QueryWrapper<UserCourseBase> userCourseBaseQueryWrapper = new QueryWrapper<>(userCourseBase);

                // 获取用户对课程的偏好数据
                userCourseBaseQueryWrapper.eq("user_id", userId).eq("course_id", courseId);
                List<UserCourseBase> userCourseBaseList = userCourseBaseMapper.selectList(userCourseBaseQueryWrapper);
                if (userCourseBaseList.size() > 0) {
                    // 已存在该用户对课程的数据
                    userCourseBase = userCourseBaseList.get(0);
                    // 最多记录三次浏览
                    if (userCourseBase.getBrowse() < 3) {
                        if (userCourseBase.getBrowse() == null) {
                            userCourseBase.setBrowse(1);
                        } else {
                            userCourseBase.setBrowse(userCourseBase.getBrowse() + 1);
                        }
                        userCourseBaseMapper.updateById(userCourseBase);
                    }
                } else {
                    // 不存在该用户对课程的数据，插入数据
                    userCourseBase.setUserId(userId).setCourseId(courseId).setBrowse(1);
                    userCourseBaseMapper.insert(userCourseBase);
                }
                logger.info("用户id" + userId + "浏览了课程id" + courseId);
                logger.info("用户浏览该课程的次数为" + userCourseBase.getBrowse());
                result.success("用户id" + userId + "浏览了课程id" + courseId);
            } else {
                result.fail("课程不存在");
            }

        } else {
            result.fail("用户不存在");
        }
        return result;
    }

    @ApiOperation(value = "用户在课程页停留")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userId", paramType = "String", value = "用户id", required = true),
            @ApiImplicitParam(name = "courseId", paramType = "String", value = "课程id", required = true),
            @ApiImplicitParam(name = "duration", paramType = "int", value = "持续时间（秒）", required = true)

    })
    @RequestMapping(method = RequestMethod.POST, value = "/duration")
    public Result duration(String userId, String courseId, int duration) {
        Result result = new Result();

        if (userExists(userId)) {
            if (courseExists(courseId)) {
                UserCourseBase userCourseBase = new UserCourseBase();
                QueryWrapper<UserCourseBase> userCourseBaseQueryWrapper = new QueryWrapper<>(userCourseBase);

                // 获取用户对课程的偏好数据
                userCourseBaseQueryWrapper.eq("user_id", userId).eq("course_id", courseId);
                List<UserCourseBase> userCourseBaseList = userCourseBaseMapper.selectList(userCourseBaseQueryWrapper);
                if (userCourseBaseList.size() > 0) {
                    // 已存在该用户对课程的数据
                    userCourseBase = userCourseBaseList.get(0);
                    // 用户第一次在课程页停留
                    if (userCourseBase.getDuration() == null) {
                        userCourseBase.setDuration(duration);
                        userCourseBaseMapper.updateById(userCourseBase);
                    } else {
                        if (duration >= 60) {
                            userCourseBase.setDuration(60);
                            userCourseBaseMapper.updateById(userCourseBase);
                        } else {
                            if (duration > userCourseBase.getDuration()) {
                                userCourseBase.setDuration(duration);
                                userCourseBaseMapper.updateById(userCourseBase);
                            }
                        }
                    }
                } else {
                    // 不存在该用户对课程的数据，插入数据
                    userCourseBase.setUserId(userId).setCourseId(courseId).setDuration(duration);
                    userCourseBaseMapper.insert(userCourseBase);
                }
                logger.info("用户id" + userId + "本次在课程id" + courseId + "页面停留" + duration + "秒");
                logger.info("用户在该课程页最长停留" + userCourseBase.getDuration() + "秒");
                result.success("用户id" + userId + "本次在课程id" + courseId + "页面停留" + duration + "秒");
            } else {
                result.fail("课程不存在");
            }

        } else {
            result.fail("用户不存在");
        }
        return result;
    }

    @ApiOperation(value = "用户加入课程")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userId", paramType = "String", value = "用户id", required = true),
            @ApiImplicitParam(name = "courseId", paramType = "String", value = "课程id", required = true)

    })
    @RequestMapping(method = RequestMethod.POST, value = "/join")
    public Result join(String userId, String courseId) {
        Result result = new Result();

        if (userExists(userId)) {
            if (courseExists(courseId)) {
                UserCourseBase userCourseBase = new UserCourseBase();
                QueryWrapper<UserCourseBase> userCourseBaseQueryWrapper = new QueryWrapper<>(userCourseBase);

                // 获取用户对课程的偏好数据
                userCourseBaseQueryWrapper.eq("user_id", userId).eq("course_id", courseId);
                List<UserCourseBase> userCourseBaseList = userCourseBaseMapper.selectList(userCourseBaseQueryWrapper);
                if (userCourseBaseList.size() > 0) {
                    // 已存在该用户对课程的数据
                    userCourseBase = userCourseBaseList.get(0);
                    if (userCourseBase.getJoinCourse() == null || userCourseBase.getJoinCourse() == 0) {
                        userCourseBase.setJoinCourse(1);
                        userCourseBaseMapper.updateById(userCourseBase);
                    }
                } else {
                    // 不存在该用户对课程的数据，插入数据
                    userCourseBase.setUserId(userId).setCourseId(courseId).setJoinCourse(1);
                    userCourseBaseMapper.insert(userCourseBase);
                }
                logger.info("用户id" + userId + "加入了课程id" + courseId);
                result.success("用户id" + userId + "加入了课程id" + courseId);
            } else {
                result.fail("课程不存在");
            }

        } else {
            result.fail("用户不存在");
        }
        return result;
    }

    @ApiOperation(value = "用户退出课程")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userId", paramType = "String", value = "用户id", required = true),
            @ApiImplicitParam(name = "courseId", paramType = "String", value = "课程id", required = true)

    })
    @RequestMapping(method = RequestMethod.POST, value = "/quit")
    public Result quit(String userId, String courseId) {
        Result result = new Result();

        if (userExists(userId)) {
            if (courseExists(courseId)) {
                UserCourseBase userCourseBase = new UserCourseBase();
                QueryWrapper<UserCourseBase> userCourseBaseQueryWrapper = new QueryWrapper<>(userCourseBase);

                // 获取用户对课程的偏好数据
                userCourseBaseQueryWrapper.eq("user_id", userId).eq("course_id", courseId);
                List<UserCourseBase> userCourseBaseList = userCourseBaseMapper.selectList(userCourseBaseQueryWrapper);
                if (userCourseBaseList.size() > 0) {
                    // 已存在该用户对课程的数据
                    userCourseBase = userCourseBaseList.get(0);
                    if (userCourseBase.getJoinCourse() == null || userCourseBase.getJoinCourse() == 1) {
                        userCourseBase.setJoinCourse(0);
                        userCourseBaseMapper.updateById(userCourseBase);
                    }
                } else {
                    // 不存在该用户对课程的数据，插入数据
                    userCourseBase.setUserId(userId).setCourseId(courseId).setJoinCourse(0);
                    userCourseBaseMapper.insert(userCourseBase);
                }
                logger.info("用户id" + userId + "退出了课程id" + courseId);
                result.success("用户id" + userId + "退出了课程id" + courseId);
            } else {
                result.fail("课程不存在");
            }
        } else {
            result.fail("用户不存在");
        }
        return result;
    }

    @ApiOperation(value = "用户分享课程")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userId", paramType = "String", value = "用户id", required = true),
            @ApiImplicitParam(name = "courseId", paramType = "String", value = "课程id", required = true)

    })
    @RequestMapping(method = RequestMethod.POST, value = "/share")
    public Result share(String userId, String courseId) {
        Result result = new Result();

        if (userExists(userId)) {
            if (courseExists(courseId)) {
                UserCourseBase userCourseBase = new UserCourseBase();
                QueryWrapper<UserCourseBase> userCourseBaseQueryWrapper = new QueryWrapper<>(userCourseBase);

                // 获取用户对课程的偏好数据
                userCourseBaseQueryWrapper.eq("user_id", userId).eq("course_id", courseId);
                List<UserCourseBase> userCourseBaseList = userCourseBaseMapper.selectList(userCourseBaseQueryWrapper);
                if (userCourseBaseList.size() > 0) {
                    // 已存在该用户对课程的数据
                    userCourseBase = userCourseBaseList.get(0);
                    if (userCourseBase.getShare() == null) {
                        userCourseBase.setShare(1);
                        userCourseBaseMapper.updateById(userCourseBase);
                    } else {
                        // 最多记录2次
                        if (userCourseBase.getShare() < 2) {
                            userCourseBase.setShare(userCourseBase.getShare() + 1);
                            userCourseBaseMapper.updateById(userCourseBase);
                        }
                    }
                } else {
                    // 不存在该用户对课程的数据，插入数据
                    userCourseBase.setUserId(userId).setCourseId(courseId).setShare(1);
                    userCourseBaseMapper.insert(userCourseBase);
                }
                logger.info("用户id" + userId + "分享了课程id" + courseId);
                logger.info("用户" + userId + "共分享了课程" + userCourseBase.getShare() + "次");
                result.success("用户id" + userId + "分享了课程id" + courseId);
            } else {
                result.fail("课程不存在");
            }

        } else {
            result.fail("用户不存在");
        }
        return result;
    }

    @ApiOperation(value = "用户评论课程")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userId", paramType = "String", value = "用户id", required = true),
            @ApiImplicitParam(name = "courseId", paramType = "String", value = "课程id", required = true)

    })
    @RequestMapping(method = RequestMethod.POST, value = "/comment")
    public Result comment(String userId, String courseId) {
        Result result = new Result();

        if (userExists(userId)) {
            if (courseExists(courseId)) {
                UserCourseBase userCourseBase = new UserCourseBase();
                QueryWrapper<UserCourseBase> userCourseBaseQueryWrapper = new QueryWrapper<>(userCourseBase);

                // 获取用户对课程的偏好数据
                userCourseBaseQueryWrapper.eq("user_id", userId).eq("course_id", courseId);
                List<UserCourseBase> userCourseBaseList = userCourseBaseMapper.selectList(userCourseBaseQueryWrapper);
                if (userCourseBaseList.size() > 0) {
                    // 已存在该用户对课程的数据
                    userCourseBase = userCourseBaseList.get(0);
                    if (userCourseBase.getComment() == null || userCourseBase.getComment() == 0) {
                        userCourseBase.setComment(1);
                        userCourseBaseMapper.updateById(userCourseBase);
                    }
                } else {
                    // 不存在该用户对课程的数据，插入数据
                    userCourseBase.setUserId(userId).setCourseId(courseId).setComment(1);
                    userCourseBaseMapper.insert(userCourseBase);
                }
                logger.info("用户id" + userId + "评论了课程id" + courseId);
                result.success("用户id" + userId + "评论了课程id" + courseId);
            } else {
                result.fail("课程不存在");
            }

        } else {
            result.fail("用户不存在");
        }
        return result;
    }

    @ApiOperation(value = "用户不喜欢课程")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userId", paramType = "String", value = "用户id", required = true),
            @ApiImplicitParam(name = "courseId", paramType = "String", value = "课程id", required = true)

    })
    @RequestMapping(method = RequestMethod.POST, value = "/dislike")
    public Result dislike(String userId, String courseId) {
        Result result = new Result();

        if (userExists(userId)) {
            if (courseExists(courseId)) {
                UserCourseBase userCourseBase = new UserCourseBase();
                QueryWrapper<UserCourseBase> userCourseBaseQueryWrapper = new QueryWrapper<>(userCourseBase);

                // 获取用户对课程的偏好数据
                userCourseBaseQueryWrapper.eq("user_id", userId).eq("course_id", courseId);
                List<UserCourseBase> userCourseBaseList = userCourseBaseMapper.selectList(userCourseBaseQueryWrapper);
                if (userCourseBaseList.size() > 0) {
                    // 已存在该用户对课程的数据
                    userCourseBase = userCourseBaseList.get(0);
                    if (userCourseBase.getDislike() == null || userCourseBase.getDislike() == 0) {
                        userCourseBase.setDislike(1);
                        userCourseBaseMapper.updateById(userCourseBase);
                    }
                } else {
                    // 不存在该用户对课程的数据，插入数据
                    userCourseBase.setUserId(userId).setCourseId(courseId).setDislike(1);
                    userCourseBaseMapper.insert(userCourseBase);
                }
                logger.info("用户id" + userId + "不喜欢了课程id" + courseId);
                result.success("用户id" + userId + "不喜欢了课程id" + courseId);
            } else {
                result.fail("课程不存在");
            }

        } else {
            result.fail("用户不存在");
        }
        return result;
    }


    @ApiOperation(value = "按id删除UserCourseBase，可以传入多个id用，隔开")
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
        userCourseBaseMapper.deleteBatchIds(deleteIds);
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
     * @param courseId 课程id
     * @return true 如果课程已存在
     */
    public boolean courseExists(String courseId) {
        Course course = new Course();
        QueryWrapper<Course> courseQueryWrapper = new QueryWrapper<>(course);
        courseQueryWrapper.eq("id", courseId);
        return courseMapper.selectCount(courseQueryWrapper) > 0;
    }
}
