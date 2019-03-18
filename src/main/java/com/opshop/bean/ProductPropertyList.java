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
 * Created by zhouxj6112 on 2019/1/6.
 */
@TableName("product_property_list")
@Data
public class ProductPropertyList extends Model<ProductPropertyList> {
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    @TableField("spu_id")
    private Long spuId;
    @TableField("property_id")
    private Integer propertyId;
    @TableField("property_value")
    private String propertyValue;
    @TableField("property_value_zh")
    private String propertyValueZH;
    @TableField("property_value_th")
    private String propertyValueTH;
    @TableField("property_value_id")
    private String propertyValueID;
    @TableField("create_time")
    private Timestamp createTime;
    @TableField("update_time")
    private Timestamp updateTime;

    @Override
    protected Serializable pkVal() {
        return this.id;
    }
}
