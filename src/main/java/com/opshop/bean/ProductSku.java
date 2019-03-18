package com.opshop.bean;


import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;
import lombok.Data;

import java.io.Serializable;

@TableName("product_sku")
@Data
public class ProductSku extends Model<ProductSku> {
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
    @TableField("product_id")
    private Integer productId;
    @TableField("sku_id")
    private String skuId;
    @TableField("attribute_list_ids")
    private String attributeListIds;
    @TableField("ali_sku_id")
    private String aliSkuId;
    @TableField("wholesale_limit")
    private Integer wholesaleLimit;
    @TableField("wholesale_count")
    private Integer wholesaleCount;
    @TableField("price_in")
    private String priceIn;
    @TableField("price_json")
    private String priceJson;
    @TableField("price_json_cn")
    private String priceJsonCN;
    @TableField("price_json_th")
    private String priceJsonTH;
    @TableField("price_json_id")
    private String priceJsonID;


    @Override
    protected Serializable pkVal() {
        return this.id;
    }
}