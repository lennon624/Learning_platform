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
 * @since 2020-06-20
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@ApiModel(value = "EmailCode对象", description = "")
public class EmailCode extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "邮箱")
    private String email;

    @ApiModelProperty(value = "邮箱对应的验证码，当邮箱每次发送验证码时，会向数据库查找这个邮箱如果邮箱不存在则新建一条邮箱数据并生成验证码；若邮箱已存在，则更新这个邮箱对应的验证码。")
    private String code;

    @ApiModelProperty(value = "验证码发送的时间，用于校验验证码的有限性：在XX分钟内有效。")
    private String date;


}
