package com.opshop.bean;

import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@TableName("product_attribute")
@Data
public class ProductAttribute extends Model<ProductAttribute> {

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
    private Integer level;
    @TableField(value = "parent_id")
    private Integer parentId;
    private String from;
    private String name;
    @TableField(value = "name_zh")
    private String nameZh;
    @TableField(value = "name_th")
    private String nameTh;
    @TableField(value = "name_id")
    private String nameId;
    @TableField(value = "create_time")
    private Date createTime;
    @TableField(value = "update_time")
    private Date updateTime;

    @Override
    protected Serializable pkVal() {
        return this.id;
    }
}