package com.opshop.entity;

import lombok.Data;

import java.util.List;

/**
 * 商品详情数据结构
 * Created by zhouxj6112 on 2019/1/4.
 */
@Data
public class ProductDetailModel {
    /**
     * 基础分类信息
     */
    @Data
    public static class Category {
        private String categoryId;
        private String categoryName;
    }

    /**
     * 商品属性集
     */
    @Data
    public static class Attribute {
        /**
         * 属性对应的值
         */
        @Data
        public static class AttributeFeature {
            private String featureId;
            private String featureName;
            private String featureNameZH;
            private String featureNameTH;
            private String featureNameID;
            private String imgUrl;
        }

        private String attributeId;
        private String attributeName;
        private String attributeNameZH;
        private String attributeNameTH;
        private String attributeNameID;
        private List<AttributeFeature> features;
    }

    /**
     * sku信息
     */
    @Data
    public static class ProductSku {
        /**
         * 自增id
         */
        private Long id;
        private Long productId;
        /**
         * 实际的sku id
         */
        private String skuId;
        /**
         * 其它平台原始的id
         */
        private String aliSkuId;
        private List<Attribute.AttributeFeature> attrs;
        /**
         * 起批量
         */
        private Integer limit;
        private Integer count;
        /**
         * 进货价 (中国:人民币,存数据库的时候需要变成美元)
         */
        List<SkuPrice> pricesIn;
        /**
         * 售卖价 (美国:美元)
         */
        List<SkuPrice> pricesOut;
        /**
         * 售卖价 (泰国:美元)
         */
        List<SkuPrice> pricesOutTH;
        /**
         * 售卖价 (印尼:美元)
         */
        List<SkuPrice> pricesOutID;

        /**
         * sku价格
         */
        @Data
        public static class SkuPrice {
            /**
             * 起批量
             */
            private Integer limit;
            private Double price;
        }
    }

    /**
     * 商品属性
     */
    @Data
    public static class ProductProps {
        private Integer propId;
        private String propKey;
        private String propKeyZH;
        private String propKeyTH;
        private String propKeyID;
        private PropsValue propValue;

        @Data
        public static class PropsValue {
            private Integer valueId;
            private String propValue;
            private String propValueZH;
            private String propValueTH;
            private String propValueID;
        }
    }

    private Long productId;
    private String pmsCode;
    /**
     * 拉取的时候,在原有平台上的id
     */
    private String otherPlatformId;
    private String from;
    private String fromUrl;
    private String fromShopName;
    private String title;
    private String titleZH;
    private String titleTH;
    private String titleID;
    private String[] images;
    /**
     * 主图,用于列表页展示用的
     */
    private String mainImage;
    /**
     * 介绍视频url
     */
    private String videoUrl;
    /**
     * 销售价格区间
     */
    private String priceRange;
    /**
     * 商品状态
     */
    private String status;
    /**
     * 收集到其它平台的分类id (需要与我们的基础分类做对应关系)
     */
    private Category collectCategory;
    /**
     * 商品的基础分类
     */
    private Category baseCategory;
    private List<Attribute> attributes;
    private List<ProductSku> skuList;
    /**
     * 属性集合
     */
    private List<ProductProps> propList;
}
