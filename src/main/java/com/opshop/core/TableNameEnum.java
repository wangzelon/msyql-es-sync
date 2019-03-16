package com.opshop.core;

/**
 * created by wangzelong 2019/3/16 14:10
 */
public enum TableNameEnum {

    PRODUCT("product", "商品表"), PRODUCT_ATTRIBUTE("product_attribute", "商品规格表"),
    PRODUCT_ATTRIBUTE_LIST("product_attribute_list", "商品规格值"), PRODUCT_COLOR_IMG("product_color_img", "图片属性"),
    PRODUCT_PROPERTY("product_property", "商品属性"),
    PRODUCT_PROPERTY_LIST("product_property_list", "商品属性值"), PRODUCT_SKU("product_sku", "商品规格");

    private String tableName;
    private String tableDesc;

    TableNameEnum(String tableName, String tableDesc) {
        this.tableName = tableName;
        this.tableDesc = tableDesc;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public String getTableDesc() {
        return tableDesc;
    }

    public void setTableDesc(String tableDesc) {
        this.tableDesc = tableDesc;
    }
}
