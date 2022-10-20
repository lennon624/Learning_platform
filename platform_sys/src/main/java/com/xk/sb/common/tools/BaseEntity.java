package com.xk.sb.common.tools;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
//import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * 基础实体类
 * @author bill
 *
 */
@Data
public class BaseEntity implements Serializable {

//    @ApiModelProperty(value ="UUID，自动生成")
    @TableId(value = "id", type = IdType.UUID)
    private String id;

//    @ApiModelProperty(value ="etc，不需要传入数据")
    @TableField(exist = false)
    private Object etc;



    @SuppressWarnings("unchecked")
    public void put(String key, Object value) {
        if(etc == null) {
            etc = new HashMap<String, Object>();
        }
        ((Map<String, Object>) etc).put(key, value);
    }
}
