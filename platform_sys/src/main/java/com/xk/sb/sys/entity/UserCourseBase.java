package com.xk.sb.sys.entity;

import com.xk.sb.common.tools.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * chrimer
 * </p>
 *
 * @author 实训三组
 * @since 2020-09-12
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@ApiModel(value = "UserCourseBase对象", description = "")
public class UserCourseBase extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "用户id")
    private String userId;

    @ApiModelProperty(value = "课程id")
    private String courseId;

    @ApiModelProperty(value = "浏览次数（0，3）")
    private Integer browse;

    @ApiModelProperty(value = "停留时间：秒数")
    private Integer duration;

    @ApiModelProperty(value = "加入课程：0未加入，1加入")
    private Integer joinCourse;

    @ApiModelProperty(value = "评论：0未评论，1评论")
    private Integer comment;

    @ApiModelProperty(value = "不喜欢：1不喜欢")
    private Integer dislike;

    @ApiModelProperty(value = "分享次数（0，2）")
    private Integer share;


}
