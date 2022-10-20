package com.xk.sb.sys.controller;

import com.xk.sb.common.constants.Result;
import com.xk.sb.common.tools.BaseController;
import com.xk.sb.sys.entity.Message;
import com.xk.sb.sys.entity.Talkings;
import com.xk.sb.sys.entity.User;
import com.xk.sb.sys.mapper.MessageMapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xk.sb.sys.mapper.TalkingsMapper;
import com.xk.sb.sys.mapper.UserMapper;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * <p>
 * 前端控制器 chrimer
 * </p>
 *
 * @author cch
 * @since 2020-09-13
 */
@RestController
@RequestMapping("/sys/message")
public class MessageController extends BaseController {
    @Autowired
    private MessageMapper messageMapper;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private TalkingsMapper talkingsMapper;

    Logger logger = LoggerFactory.getLogger(getClass());

    @ApiImplicitParams({
            @ApiImplicitParam(name = "userId", paramType = "query", value = "用户id", required = true),
            @ApiImplicitParam(name = "messageType", paramType = "query", value = "消息类型", required = true)
    })
    @ApiOperation(value = "通过userId获取Message信息")
    @RequestMapping(method = RequestMethod.POST, value = "/getMessage")
    public Result getMessage(String userId, String messageType) {
        Result result = new Result();

        QueryWrapper<Message> messageQueryWrapper = new QueryWrapper<>();
        messageQueryWrapper.eq("receiver_id", userId)
                .eq("message_type", messageType)
                .orderByDesc("date");
        List<Message> messages = messageMapper.selectList(messageQueryWrapper);


        HashMap[] maps = null;
        if (messages.size() <= 0) {
            logger.info("暂无消息");
        } else if (messageType.equalsIgnoreCase("talking")) {
            logger.info("成功获得" + messages.size() + "条通知消息");
            maps = new HashMap[messages.size()];
            Message iMessage;
            for (int i = 0; i < maps.length; i++) {

                iMessage = messages.get(i);
                maps[i] = new HashMap();

                maps[i].put("id", iMessage.getId());
                maps[i].put("talkingIdParent", talkingsMapper.selectById(iMessage.getRelateId()).getTalkingIdParent());
                maps[i].put("senderName", userMapper.selectById(iMessage.getSenderId()).getUsername());
                maps[i].put("content", talkingsMapper.selectById(iMessage.getRelateId()).getTalkingContent());
                maps[i].put("date", iMessage.getDate());
                maps[i].put("isView", iMessage.getIsView());

            }
        } else if (messageType.equalsIgnoreCase("course")) {
            //课程消息
            result.setData(null);
        }

        result.setData(maps);

        return result;
    }

    @ApiImplicitParams({
            @ApiImplicitParam(name = "messageId", paramType = "query", value = "消息id", required = true),
    })
    @ApiOperation(value = "消息是否已读状态切换")
    @RequestMapping(method = RequestMethod.POST, value = "/isViewTransfer")
    public Result isViewTransfer(String messageId) {
        Result result = new Result();

        Message message = messageMapper.selectById(messageId);
        if (message.getIsView().equalsIgnoreCase("0")){
            message.setIsView("1");
            messageMapper.updateById(message);
        }

        return result;
    }


}
