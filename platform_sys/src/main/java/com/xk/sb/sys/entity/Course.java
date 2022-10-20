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
 * @since 2020-06-22
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@ApiModel(value="Course对象", description="")
public class Course extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "课程名")
    private String courseName;

    @ApiModelProperty(value = "任课教师")
    private String teacherId;

    @ApiModelProperty(value = "课程介绍")
    private String introduction;

    @ApiModelProperty(value = "课程对应的分类")
    private String childCategoryId;

    @ApiModelProperty(value = "课程公告")
    private String notice;

    @ApiModelProperty(value = "课程审核状态：0待审核；1审核通过；-1审核不通过")
    private String status;

    @ApiModelProperty(value = "图片文件名")
    private String imgSrc;


}
