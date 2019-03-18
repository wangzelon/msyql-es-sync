package com.opshop.bean;

import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@TableName("product")
@Data
public class Product extends Model<Product> {
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
    @TableField("ali_spu_id")
    private String aliSpuId;
    @TableField("pms_code")
    private String pmsCode;
    @TableField("title")
    private String title;
    @TableField("title_zh")
    private String titleZh;
    @TableField("title_th")
    private String titleTh;
    @TableField("title_id")
    private String titleId;
    @TableField("desc")
    private String desc;
    /**
     * 销售价格范围
     */
    @TableField("price_range")
    private String priceRange;
    @TableField("price_range_th")
    private String priceRangeTH;
    @TableField("price_range_id")
    private String priceRangeID;
    @TableField("images")
    private String images;
    @TableField("from")
    private String from;
    @TableField("from_url")
    private String fromUrl;
    @TableField("from_shop_name")
    private String fromShopName;
    @TableField("status")
    private String status;
    @TableField("category_collect_id")
    private Integer categoryCollectId;
    @TableField("category_collect_name")
    private String categoryCollectName;
    @TableField("base_category_id")
    private Integer baseCategoryId;
    @TableField("base_category_name")
    private String baseCategoryName;
    @TableField("rich_text")
    private String richText;
    @TableField("main_image")
    private String mainImage;
    @TableField("video_url")
    private String videoUrl;
    @TableField(exist = false)
    private Date sendTime;

    @Override
    protected Serializable pkVal() {
        return id;
    }
}