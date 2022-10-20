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
 * @author 实训三组
 * @since 2020-08-31
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@ApiModel(value="Talkings对象", description="")
public class Talkings extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "题目id")
    private String courseId;

    @ApiModelProperty(value = "是否根级别讨论 1-是 0-否")
    private String isRootTalking;

    @ApiModelProperty(value = "父级别讨论id")
    private String talkingIdParent;

    @ApiModelProperty(value = "回复的用户id（主动方）")
    private String userId;

    @ApiModelProperty(value = "被回复的用户id（被动方）")
    private String userIdBereply;

    @ApiModelProperty(value = "讨论文本内容")
    private String talkingContent;

    @ApiModelProperty(value = "日期时间")
    private String date;

    @ApiModelProperty(value = "获赞数")
    private Integer getGoods;

    @ApiModelProperty(value = "父讨论用户id（回复哪个用户）")
    private String userIdParent;


}
