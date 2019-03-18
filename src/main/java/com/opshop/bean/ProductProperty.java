package com.opshop.bean;

import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;
import lombok.Data;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * Created by zhouxj6112 on 2019/1/5.
 */
@TableName("product_property")
@Data
public class ProductProperty extends Model<ProductProperty> {

    @TableId(value = "property_id", type = IdType.AUTO)
    private Integer propertyId;
    private String from;
    @TableField("base_category_id")
    private Integer baseCategoryId;
    @TableField("property_key")
    private String propertyKey;
    @TableField("property_key_zh")
    private String propertyKeyZH;
    @TableField("property_key_th")
    private String propertyKeyTH;
    @TableField("property_key_id")
    private String propertyKeyID;
    @TableField("create_time")
    private Timestamp createTime;
    @TableField("update_time")
    private Timestamp updateTime;
    @TableField(exist = false)
    private String baseCategoryName;

    @Override
    protected Serializable pkVal() {
        return this.propertyId;
    }
}
