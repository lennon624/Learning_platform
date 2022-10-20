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
@ApiModel(value = "ChildCategory对象", description = "")
public class ChildCategory extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "该分类对应的父级id")
    private String parentCategoryId;

    @ApiModelProperty(value = "分类名称")
    private String categoryName;


}
