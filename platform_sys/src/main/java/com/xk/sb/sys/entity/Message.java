package com.xk.sb.sys.entity;

import com.xk.sb.common.tools.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 *  chrimer
 * </p>
 *
 * @author cch
 * @since 2020-09-13
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@ApiModel(value="Message对象", description="")
public class Message extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "消息类型关联id（如果是讨论消息就传讨论id, 其他id同）")
    private String relateId;

    @ApiModelProperty(value = "消息类型 目前有：talking/course")
    private String messageType;

    @ApiModelProperty(value = "消息发送方id")
    private String senderId;

    @ApiModelProperty(value = "消息接收方id")
    private String receiverId;

    @ApiModelProperty(value = "消息内容")
    private String content;

    @ApiModelProperty(value = "日期")
    private String date;

    @ApiModelProperty(value = "是否已读 1-已读 0-为读（默认0未读")
    private String isView;


}
