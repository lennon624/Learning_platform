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
 * @since 2020-07-01
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@ApiModel(value = "Homework对象", description = "")
public class Homework extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "学生id")
    private String studentId;

    @ApiModelProperty(value = "章节id")
    private String chapterId;

    @ApiModelProperty(value = "作业正文")
    private String text;

    @ApiModelProperty(value = "附件名")
    private String src;


}
