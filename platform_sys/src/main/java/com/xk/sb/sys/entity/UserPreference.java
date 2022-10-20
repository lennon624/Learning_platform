package com.xk.sb.sys.entity;

import com.xk.sb.common.tools.BaseEntity;

import java.time.LocalDateTime;

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
@ApiModel(value = "UserPreference对象", description = "")
public class UserPreference extends BaseEntity {

    private static final long serialVersionUID = 1L;

    private String userId;

    private String courseId;

    private String val;

    private String time;

    private String userIdLong;

    private String courseIdLong;


}
