package com.xk.sb.sys.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xk.sb.common.tools.mail.Email;
import com.xk.sb.sys.entity.User;
import com.xk.sb.sys.mapper.UserMapper;
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
import com.xk.sb.sys.mapper.EmailCodeMapper;
import com.xk.sb.sys.entity.EmailCode;

import org.springframework.web.bind.annotation.RestController;
import com.xk.sb.common.tools.BaseController;

/**
 * <p>
 * 前端控制器 chrimer
 * </p>
 *
 * @author 实训三组
 * @since 2020-06-20
 */
@RestController
@RequestMapping("/sys/email-code")
public class EmailCodeController extends BaseController {
    @Autowired
    private EmailCodeMapper emailCodeMapper;

    @Autowired
    private UserMapper userMapper;

    private final static Logger logger = LoggerFactory.getLogger(UserController.class);

    @ApiImplicitParams({
            @ApiImplicitParam(name = "pageNum", paramType = "query", value = "当前页码", required = true),
            @ApiImplicitParam(name = "pageSize", paramType = "query", value = "每页显示记录数", required = true)
    })
    @ApiOperation(value = "分页获取EmailCode信息")
    @RequestMapping(method = RequestMethod.POST, value = "/page")
    public Result page(
            Integer pageNum,
            Integer pageSize) {
        Result result = new Result();
        Page<EmailCode> page = new Page<EmailCode>(pageNum, pageSize);
        EmailCode parms = new EmailCode();
        QueryWrapper<EmailCode> warpper = new QueryWrapper<EmailCode>(parms);

        result.setData(emailCodeMapper.selectPage(page, warpper));
        return result;
    }

    @ApiOperation(value = "保存修改EmailCode信息")
    @RequestMapping(method = RequestMethod.POST, value = "/save")
    public Result save(@RequestBody EmailCode entity) {
        Result result = new Result();
        if (entity.getId() == null) {
            emailCodeMapper.insert(entity);
        } else {
            emailCodeMapper.updateById(entity);
        }
        return result;
    }

    @ApiOperation(value = "按id删除EmailCode，可以传入多个id用，隔开")
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
        emailCodeMapper.deleteBatchIds(deleteIds);
        return result;
    }

    @ApiOperation(value = "验证邮箱验证码")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "email", paramType = "String", value = "邮箱", required = true),
            @ApiImplicitParam(name = "email_code", paramType = "String", value = "邮箱验证码", required = true)
    })
    @RequestMapping(method = RequestMethod.POST, value = "/checkEmailCode")
    public Result checkEmailCode(String email, String email_code) {
        Result result = new Result();

        EmailCode param = new EmailCode();
        QueryWrapper<EmailCode> emailCodeQueryWrapper = new QueryWrapper<EmailCode>(param);
        emailCodeQueryWrapper.eq("email", email);
        EmailCode emailCode = emailCodeMapper.selectOne(emailCodeQueryWrapper);
        String emailCodeFromUser = email_code;

        if (emailCode != null) {
            // 验证验证码的正确性
            if (!emailCodeFromUser.equals(emailCode.getCode())) {
                logger.info("验证码错误");
                result.fail("验证码错误");
            } else { // 验证验证码的有效性
                long currentTime = System.currentTimeMillis(); // 系统当前时间
                if (currentTime - Long.parseLong(emailCode.getDate()) <= 300000) { // 验证码在有效时间内(5分钟)
                    logger.info("验证码有效：" + emailCode.getCode());

                    // 获取邮箱对应的用户信息
                    User user = new User();
                    QueryWrapper<User> userQueryWrapper = new QueryWrapper<>(user);
                    userQueryWrapper.eq("email", email);
                    user = userMapper.selectOne(userQueryWrapper);
                    if (user != null) {
                        logger.info(user.getUsername());
                    }
                    result.setData(user);

                    result.success("验证成功");
                } else {
                    logger.info("验证码已失效");
                    result.success("验证码已失效");
                }
            }
        } else {
            logger.info("未找到该邮箱的验证码");
            result.fail("未找到该邮箱的验证码");
        }
        return result;
    }

}
