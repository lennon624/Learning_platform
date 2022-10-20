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
 * @since 2020-06-22
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@ApiModel(value = "Teacher对象", description = "")
public class Teacher extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "该教师的账号对应的id")
    private String userId;

    @ApiModelProperty(value = "该教师所在学校的id")
    private String schoolId;

    @ApiModelProperty(value = "教师姓名")
    private String name;

    @ApiModelProperty(value = "教育经历")
    private String eduExperience;

    @ApiModelProperty(value = "教学领域")
    private String eduArea;


}
