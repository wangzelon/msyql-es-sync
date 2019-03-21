package com.opshop.bean;

import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
@TableName("product_attribute_list")
public class ProductAttributeList extends Model<ProductAttributeList> {
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
    @TableField("attribute_id")
    private Integer attributeId;
    @TableField("value")
    private String value;
    @TableField("value_zh")
    private String valueZh;
    @TableField("value_th")
    private String valueTh;
    @TableField("value_id")
    private String valueId;
    @TableField("create_time")
    private Date createTime;
    @TableField("update_time")
    private Date updateTime;

    @Override
    protected Serializable pkVal() {
        return this.id;
    }
}