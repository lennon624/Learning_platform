package com.xk.sb.sys.controller;

import com.aliyuncs.dysmsapi.model.v20170525.SendSmsResponse;
import com.aliyuncs.exceptions.ClientException;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xk.sb.common.tools.mail.Email;
import com.xk.sb.common.tools.mail.SendMail;
import com.xk.sb.common.utils.AliyunSmsUtils;
import com.xk.sb.sys.entity.*;
import com.xk.sb.sys.mapper.*;
import org.apache.ibatis.scripting.xmltags.ForEachSqlNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

import java.util.*;

import com.alibaba.fastjson.JSONObject;

import com.xk.sb.common.constants.Result;

import com.xk.sb.common.tools.BaseController;

/**
 * <p>
 * 前端控制器 chrimer
 * </p>
 *
 * @author 实训三组
 * @since 2020-06-19
 */
@RestController
@RequestMapping("/sys/user")
public class UserController extends BaseController {
    @Autowired
    private UserMapper userMapper;

    @Autowired
    private StudentMapper studentMapper;

    @Autowired
    private EmailCodeMapper emailCodeMapper;

    @Autowired
    private TelCodeMapper telCodeMapper;

    @Autowired
    private ChildCategoryMapper childCategoryMapper;

    @Autowired
    private InterestSelectionMapper interestSelectionMapper;

    private final static Logger logger = LoggerFactory.getLogger(UserController.class);

    @ApiImplicitParams({
            @ApiImplicitParam(name = "username", paramType = "query", value = "用户名/邮箱/手机号", required = true),
            @ApiImplicitParam(name = "password", paramType = "query", value = "密码", required = true)
    })
    @ApiOperation(value = "用户登录")
    @RequestMapping(method = RequestMethod.POST, value = "/login")
    public Result login(String username, String password) {

        Result result = new Result();

        User user = new User();
        QueryWrapper<User> userQueryWrapper = new QueryWrapper<User>(user);

        //判断是邮箱还是手机号的正则表达式
        String email = "^\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*$";
        String tel = "^[1][34578]\\d{9}$";

        if (username.matches(email)) { // 用户以邮箱方式登录
            logger.info("用户以邮箱方式登录");
            user.setEmail(username);
            user.setPassword(password);
        } else if (username.matches(tel)) { // 用户以手机号方式登录
            logger.info("用户以手机号方式登录");
            user.setTel(username);
            user.setPassword(password);
        } else { // 用户以用户名方式登录
            logger.info("用户以用户名方式登录");
            user.setUsername(username);
            user.setPassword(password);
        }

        User userExit = userMapper.selectOne(new QueryWrapper<User>(user));
        if (userExit == null) {
            result.fail("用户名或密码错误!");
        } else {
            if (userExit.getStatus().equals("0")) {
                result.fail("账号已被禁用，请联系管理员处理");
            } else {
                logger.info("用户登录成功，用户名：" + userExit.getUsername());
                result.success("登录成功");

                Student student = new Student();
                QueryWrapper<Student> studentQueryWrapper = new QueryWrapper<>(student);
                studentQueryWrapper.eq("user_id", userExit.getId());
                student = studentMapper.selectOne(studentQueryWrapper);
                if (student != null) {
                    userExit.setEtc(student);
                }

                Map<String, Object> map = new HashMap<String, Object>();
                map.put("user", userExit);
                result.setData(map);
            }
        }

        return result;
    }

    @ApiImplicitParams({
            @ApiImplicitParam(name = "entity", paramType = "User", value = "用户实体，在entity.etc中包含json格式的邮箱验证码{emailCode:xxxx}", required = true)
    })
    @ApiOperation(value = "用户注册")
    @RequestMapping(method = RequestMethod.POST, value = "/register")
    public Result register(@RequestBody User entity) {
        Result result = new Result();

        User user = new User();
        QueryWrapper<User> userQueryWrapper;


        if (entity.getId() == null) {
            userQueryWrapper = new QueryWrapper<User>(user);
            userQueryWrapper.eq("email", entity.getEmail());
            User emailExists = userMapper.selectOne(userQueryWrapper);
            userQueryWrapper = new QueryWrapper<User>(user);
            userQueryWrapper.eq("username", entity.getUsername());
            User userExists = userMapper.selectOne(userQueryWrapper);

            // 用户名已被注册
            if (userExists != null) {
                result.fail("用户名已被注册");
                return result;
            }

            // 邮箱已被注册
            if (emailExists != null) {
                result.fail("邮箱已被注册");
                return result;
            }

            // 字符串转为json对象
            JSONObject jsonObject = JSONObject.parseObject(entity.getEtc().toString());
            // 获取到etc内容
            Map etc = JSONObject.parseObject(jsonObject.toJSONString());
            // 获取到用户输入的邮箱验证码
            String emailCodeFromUser = etc.get("emailCode").toString();

            // 验证码校验
            EmailCode emailCode = new EmailCode();
            QueryWrapper<EmailCode> emailCodeQueryWrapper = new QueryWrapper<EmailCode>(emailCode);
            emailCodeQueryWrapper.eq("email", entity.getEmail());
            emailCode = emailCodeMapper.selectOne(emailCodeQueryWrapper);
            // 验证验证码的正确性
            if (!emailCodeFromUser.equals(emailCode.getCode())) {
                logger.info("验证码错误");
                result.fail("验证码错误");
            } else { // 验证验证码的有效性
                // 系统当前时间
                long currentTime = System.currentTimeMillis();
                // 验证码的有效时间(5分钟)
                int validTime = 300000;
                // 验证码在有效时间内
                if (currentTime - Long.parseLong(emailCode.getDate()) <= validTime) {
                    logger.info("验证码有效：" + emailCode.getCode());
                    entity.setStatus("1");
                    userMapper.insert(entity);

                    // 新用户默认为学生
                    Student student = new Student();
                    student.setUserId(entity.getId());
                    studentMapper.insert(student);
                    student.setName("学生id_" + student.getId());
                    studentMapper.updateById(student);
                    logger.info("用户注册成功，用户名：" + entity.getUsername());
                    entity.setEtc(student);
                    result.setData(entity);
                    result.success("注册成功");
                } else { // 验证码已过期
                    result.fail("验证码已过期，请重新获取");
                    logger.info("验证码已过期");
                }
            }
        }
        return result;
    }

    @ApiImplicitParams({
            @ApiImplicitParam(name = "email", paramType = "String", value = "用户邮箱", required = true),
            @ApiImplicitParam(name = "email_code", paramType = "String", value = "邮箱验证码", required = true)
    })
    @ApiOperation(value = "用户邮箱验证")
    @RequestMapping(method = RequestMethod.POST, value = "/checkEmailCode")
    public Result checkEmailCode(String email, String email_code, String type) {
        Result result = new Result();
        EmailCode emailCode = new EmailCode();
        QueryWrapper<EmailCode> emailCodeQueryWrapper = new QueryWrapper<EmailCode>(emailCode);
        emailCodeQueryWrapper.eq("email", email);
        emailCode = emailCodeMapper.selectOne(emailCodeQueryWrapper);
        // 获取到用户输入的邮箱验证码
        String emailCodeFromUser = email_code;

        if (emailCode != null) {
            // 验证验证码的正确性
            if (!emailCodeFromUser.equals(emailCode.getCode())) {
                logger.info("验证码错误");
                result.fail("验证码错误");
            } else { // 验证验证码的有效性
                // 系统当前时间
                long currentTime = System.currentTimeMillis();
                // 验证码有效时间
                int validTime = 300000;
                // 验证码在有效时间内(5分钟)
                if (currentTime - Long.parseLong(emailCode.getDate()) <= validTime) {
                    logger.info("验证码有效：" + emailCode.getCode());

                    if (type == null || "".equals(type)) {
                        // 获取邮箱对应的用户信息
                        User user = new User();
                        QueryWrapper<User> userQueryWrapper = new QueryWrapper<>(user);
                        userQueryWrapper.eq("email", email);
                        user = userMapper.selectOne(userQueryWrapper);
                        if (user == null) {
                            result.fail("该邮箱未注册");
                        } else {
                            result.success("验证成功");
                        }
                        result.setData(user);
                    } else {
                        result.success("验证成功");
                    }


                } else {
                    logger.info("验证码已失效");
                    result.fail("验证码已失效");
                }
            }
        } else {
            logger.info("未找到该邮箱的验证码");
            result.fail("未找到该邮箱的验证码");
        }
        return result;
    }

    @ApiImplicitParams({
            @ApiImplicitParam(name = "user_id", paramType = "String", value = "用户id", required = true),
            @ApiImplicitParam(name = "newPassword", paramType = "String", value = "新密码", required = true)
    })
    @ApiOperation(value = "用户重置密码")
    @RequestMapping(method = RequestMethod.POST, value = "/resetPassword")
    public Result recovery(String user_id, String newPassword) {
        Result result = new Result();

        logger.info(user_id);

        User user = new User();
        QueryWrapper<User> userQueryWrapper = new QueryWrapper<User>(user);
        userQueryWrapper.eq("id", user_id);
        user = userMapper.selectOne(userQueryWrapper);
        if (user == null) {
            result.fail("用户不存在");
        } else {
            // 设置新密码
            user.setPassword(newPassword);
            userMapper.updateById(user);
            result.success("重置密码成功");
        }
        return result;
    }

    @ApiImplicitParams({
            @ApiImplicitParam(name = "pageNum", paramType = "query", value = "当前页码", required = true),
            @ApiImplicitParam(name = "pageSize", paramType = "query", value = "每页显示记录数", required = true)
    })
    @ApiOperation(value = "分页获取User信息")
    @RequestMapping(method = RequestMethod.POST, value = "/page")
    public Result page(
            Integer pageNum,
            Integer pageSize) {
        Result result = new Result();
        Page<User> page = new Page<User>(pageNum, pageSize);
        User parms = new User();
        QueryWrapper<User> wrapper = new QueryWrapper<User>(parms);

        result.setData(userMapper.selectPage(page, wrapper));
        return result;
    }


    @ApiOperation(value = "添加用户")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "entity", paramType = "User", value = "用户实体", required = true)
    })
    @RequestMapping(method = RequestMethod.POST, value = "/save")
    public Result save(@RequestBody User entity) {
        Result result = new Result();
        if (entity.getId() == null) {
            userMapper.insert(entity);
        } else {
            userMapper.updateById(entity);
        }
        return result;
    }

    @ApiOperation(value = "按id删除User，可以传入多个id用，隔开")
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
        userMapper.deleteBatchIds(deleteIds);
        return result;
    }

    @ApiOperation(value = "向传入的邮箱发送邮箱验证码")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "email", paramType = "String", value = "传入的用户邮箱", required = true),
            @ApiImplicitParam(name = "type", paramType = "String", value = "邮件类型：0发送注册邮件；1发送重置密码邮件", required = true)
    })
    @RequestMapping(method = RequestMethod.POST, value = "/sendEmailCode")
    public Result sendEmailCode(String email, String type) {
        Result result = new Result();
        if (email == "") {
            result.fail("邮箱不能为空");
            return result;
        }
        EmailCode emailCode = new EmailCode();
        QueryWrapper<EmailCode> emailCodeQueryWrapper = new QueryWrapper<EmailCode>(emailCode);

        // 从数据库获得该邮箱的条目
        emailCodeQueryWrapper.eq("email", email);
        List<EmailCode> emailCodes = emailCodeMapper.selectList(emailCodeQueryWrapper);

        // 随机生成验证码(1000-9999)
        Random random = new Random();
        String code = String.valueOf(random.nextInt(9000) + 1000);
        // 系统的当前时间，用于有效性验证
        String currentTime = Long.toString(System.currentTimeMillis());

        // 如果该邮箱已存在于数据库中，则更新该邮箱的验证码信息
        if (emailCodes.size() > 0) {
            emailCodes.get(0).setCode(code);
            emailCodes.get(0).setDate(currentTime);
            // 更新数据
            emailCodeMapper.updateById(emailCodes.get(0));
        } else { //如果该邮箱是第一次请求验证码
            emailCode.setEmail(email);
            emailCode.setCode(code);
            emailCode.setDate(currentTime);
            // 插入新数据
            emailCodeMapper.insert(emailCode);
        }

        logger.info("正在向" + email + "发送验证码");

        String info = "";
        if (type.equals(String.valueOf(Email.TYPE_RECOVERY))) {
            info = "你正在申请重置在线学习账户密码。<br>验证码：<br>" + code + "<br>有效时间为5分钟，请妥善保管。";
        } else if (type.equals(String.valueOf(Email.TYPE_REGISTER))) {
            info = "你正在注册一个在线学习账户。<br>验证码：<br>" + code + "<br>有效时间为5分钟，请妥善保管。";
        }


        Email mail = new Email();
        // 设置收件人
        mail.setRecipient(email);
        // 设置发件人昵称
        mail.setNickname("在线学习平台");
        // 设置邮件主题
        mail.setSubject("验证邮件");
        // 设置邮件内容
        mail.setInfo(info);
        // 设置邮件类型
        mail.setType(Email.TYPE_REGISTER);

        try {
            // 发送邮件是一件非常耗时的事情，因此这里开辟了另一个线程来专门发送邮件
            SendMail sendmail = new SendMail(mail);
            // 启动线程，线程启动之后就会执行run方法来发送邮件
            sendmail.start();
            result.success("邮件发送成功，请注意查收！");
        } catch (Exception e) {
            result.fail("邮件发送失败");
            e.printStackTrace();
        }

        return result;
    }

    @ApiOperation(value = "向传入的手机号发送短信息验证码")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "phone", paramType = "query", value = "传入的用户手机号", required = true)
    })
    @RequestMapping(method = RequestMethod.POST, value = "/sendTelCode")
    public Result sendTelCode(String tel) {
        Result result = new Result();

        if (tel == "") {
            result.fail("手机号不能为空");
            return result;
        }

        TelCode telCode = new TelCode();
        QueryWrapper<TelCode> telCodeQueryWrapper = new QueryWrapper<TelCode>(telCode);

        // 从数据库获得该邮箱的条目
        telCodeQueryWrapper.eq("tel", tel);
        List<TelCode> telCodes = telCodeMapper.selectList(telCodeQueryWrapper);

        //随机生成四位数验证码
        AliyunSmsUtils.setNewcode();
        String code = Integer.toString(AliyunSmsUtils.getNewcode());
        //发送验证码
        SendSmsResponse response = null;
        try {
            response = AliyunSmsUtils.sendSms(tel, code);
            result.success("短信验证码发送成功！");
        } catch (ClientException e) {
            e.printStackTrace();
            result.fail("短信验证码发送失败！");
        }
        // 系统的当前时间，用于有效性验证
        String currentTime = Long.toString(System.currentTimeMillis());
        // 如果该邮箱已存在于数据库中，则更新该邮箱的验证码信息
        if (telCodes.size() > 0) {
            telCodes.get(0).setCode(code);
            telCodes.get(0).setDate(currentTime);
            // 更新数据
            telCodeMapper.updateById(telCodes.get(0));
        } else { //如果该手机号是第一次请求验证码
            telCode.setTel(tel);
            telCode.setCode(code);
            telCode.setDate(currentTime);
            // 插入新数据
            telCodeMapper.insert(telCode);
        }

        return result;
    }

    @ApiOperation(value = "手机验证登录")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "tel", paramType = "query", value = "传入的用户手机号", required = true),
            @ApiImplicitParam(name = "inputCode", paramType = "query", value = "用户输入的验证码", required = true)
    })
    @RequestMapping(method = RequestMethod.POST, value = "/loginByTel")
    public Result loginByTel(String tel, String inputCode) {
        Result result = new Result();

        // 验证码校验
        TelCode telCode = new TelCode();
        QueryWrapper<TelCode> telCodeQueryWrapper = new QueryWrapper<TelCode>(telCode);
        telCodeQueryWrapper.eq("tel", tel);
        telCode = telCodeMapper.selectOne(telCodeQueryWrapper);
        // 验证验证码的正确性
        if (!inputCode.equals(telCode.getCode())) {
            logger.info("验证码错误");
            result.fail("验证码错误");
        } else { // 验证验证码的有效性
            long currentTime = System.currentTimeMillis(); // 系统当前时间
            if (currentTime - Long.parseLong(telCode.getDate()) <= 3000000) { // 验证码在有效时间内(5分钟)
                if (true) { // 验证码在有效时间内(5分钟)
                    logger.info("验证码有效：" + telCode.getCode());
                    result.success("登陆成功！");

                    //判断此手机号是否绑定账号
                    User user = new User();
                    QueryWrapper<User> userQueryWrapper = new QueryWrapper<>(user);
                    userQueryWrapper.eq("tel", tel);
                    User userExists = userMapper.selectOne(userQueryWrapper);

                    HashMap<String, Object> map = new HashMap<>();

                    if (userExists == null) {//首次登录并注册
                        user.setUsername("初始用户名" + 1000 + (int) (Math.random() * (99999999 - 1000 + 1)));
                        user.setPassword(null);
                        user.setTel(tel);
                        user.setStatus("1");
                        userMapper.insert(user);
                        map.put("isNew", 1);//1-新用户 前端跳转到信息完善界面
                        map.put("user", user);
                    } else {
                        map.put("user", userExists);
                        map.put("isNew", 0);//0-老用户
                    }

//                    userExists.setEtc(user);
                    result.setData(map);


                } else { // 验证码已过期
                    result.fail("验证码已过期，请重新获取");
                    logger.info("验证码已过期");
                }
            }
            return result;
        }
        return result;
    }

    @ApiOperation(value = "完善个人信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "entity", paramType = "User", value = "传入的信息完善表单", required = true)
    })
    @RequestMapping(method = RequestMethod.POST, value = "/refineInfo")
    public Result refineInfo(String username, String password, String email, String tel, String interests) {
        Result result = new Result();

        QueryWrapper<User> userQueryWrapper = new QueryWrapper<>();
        userQueryWrapper.eq("tel", tel);
        User user = userMapper.selectOne(userQueryWrapper);

        //更新user表的信息
        user.setUsername(username);
        user.setPassword(password);
        user.setEmail(email);
        UpdateWrapper<User> userUpdateWrapper = new UpdateWrapper<>();
        userUpdateWrapper.eq("id", user.getId());
        int update = userMapper.update(user, userUpdateWrapper);
        logger.info("user表信息更新成功,更新了 " + update + "条");

        //兴趣科目，用于课程推荐
        interests = interests.replace("[", "");
        interests = interests.replace("]", "");
        interests = interests.replace("\"", "");
        String[] interestsArr = interests.split(",");//child_category 的id

        QueryWrapper<ChildCategory> childCategoryQueryWrapper;
        ChildCategory childCategory;
        InterestSelection interestSelection;
        for (String interest : interestsArr) {
//            System.out.println(interest + " ");
            childCategoryQueryWrapper = new QueryWrapper<>();
            childCategoryQueryWrapper.eq("id", interest);
            childCategory = childCategoryMapper.selectOne(childCategoryQueryWrapper);

            if (childCategory != null) {
                interestSelection = new InterestSelection();
                interestSelection.setStudentId(user.getId());
                interestSelection.setChildCategoryId(childCategory.getId());
                interestSelectionMapper.insert(interestSelection);
                logger.info("id为 " + user.getId() + " 的用户增加兴趣科目 " + childCategory.getCategoryName());
            }
        }

        HashMap<String, Object> map = new HashMap<String, Object>();
        map.put("user", user);

        result.setData(map);
        result.success("个人信息完善成功");

        return result;
    }

    @ApiOperation(value = "设置个人信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", paramType = "String", value = "用户id", required = true),
            @ApiImplicitParam(name = "email", paramType = "String", value = "邮箱", required = true),
            @ApiImplicitParam(name = "tel", paramType = "String", value = "手机号", required = true),
    })
    @RequestMapping(method = RequestMethod.POST, value = "/editSetting")
    public Result editSetting(String id, String email, String tel) {
        Result result = new Result();

        QueryWrapper<User> userQueryWrapper = new QueryWrapper<>();
        userQueryWrapper.eq("id", id);

        User user = userMapper.selectOne(userQueryWrapper);
        user.setEmail(email);
        user.setTel(tel);

        UpdateWrapper<User> userUpdateWrapper = new UpdateWrapper<>();
        userUpdateWrapper.eq("id", id);
        userMapper.update(user, userUpdateWrapper);

        logger.info("个人信息更新成功！");
        result.success("个人信息更新成功");

        return result;
    }
}











