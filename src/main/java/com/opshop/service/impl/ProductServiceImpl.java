package com.opshop.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.opshop.bean.*;
import com.opshop.entity.ProductDetailModel;
import com.opshop.mapper.*;
import com.opshop.service.ProductService;
import javafx.geometry.Pos;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
@Transactional(rollbackFor = Exception.class)
public class ProductServiceImpl extends ServiceImpl<IProductMapper, Product> implements ProductService {

    @Autowired
    IProductMapper productMapper;
    @Autowired
    ProductAttributeMapper attributeMapper;
    @Autowired
    ProductAttributeListMapper attributeListMapper;
    @Autowired
    ProductSkuMapper productSkuMapper;
    @Autowired
    IProductPropertyMapper propertyMapper
    @Autowired
    IProductPropertyListMapper propertyListMapper;

    public ProductDetailModel addProduct(ProductDetailModel productDetailModel, Integer id) {
        Product product = productMapper.selectById(id);
        productDetailModel.setTitle(product.getTitle());
        productDetailModel.setTitleID(product.getTitleId());
        productDetailModel.setTitleTH(product.getTitleTh());
        productDetailModel.setTitleZH(product.getTitleZh());
        productDetailModel.setProductId(Long.valueOf(id.toString()));
        productDetailModel.setPmsCode(product.getPmsCode());
        productDetailModel.setOtherPlatformId(product.getAliSpuId());
        productDetailModel.setFrom(product.getFrom());
        productDetailModel.setFromShopName(product.getFromShopName());
        productDetailModel.setFromUrl(product.getFromUrl());

        JSONArray array = JSON.parseArray(product.getImages());
        String[] strings = array.stream().toArray(String[]::new);
        productDetailModel.setImages(strings);

        productDetailModel.setMainImage(product.getMainImage());
        productDetailModel.setVideoUrl(product.getVideoUrl());
        productDetailModel.setPriceRange(product.getPriceRange());
        productDetailModel.setStatus(product.getStatus());

        //插入来源分类
        ProductDetailModel.Category collectCategory = new ProductDetailModel.Category();
        collectCategory.setCategoryId(product.getCategoryCollectId().toString());
        collectCategory.setCategoryName(product.getCategoryCollectName());
        productDetailModel.setCollectCategory(collectCategory);
        //插入基础分类
        ProductDetailModel.Category baseCategory = new ProductDetailModel.Category();
        baseCategory.setCategoryId(product.getBaseCategoryId().toString());
        baseCategory.setCategoryName(product.getBaseCategoryName());
        productDetailModel.setBaseCategory(baseCategory);
        //插入sku
        List<ProductDetailModel.ProductSku> productSkus = queryProductSku(id);
        productDetailModel.setSkuList(productSkus);
        //插入attribute
        productDetailModel.setAttributes(queryAttribute(productSkus));
        //插入list<property>
        productDetailModel.setPropList(queryProperty(product.getPropertyIds()));
        return productDetailModel;
    }


    //通过商品id查询ProductSku表
    public List<ProductDetailModel.ProductSku> queryProductSku(Integer id) {
        EntityWrapper<ProductSku> wrapper = new EntityWrapper<>();
        wrapper.eq("product_id", id);
        List<ProductSku> skus = productSkuMapper.selectList(wrapper);

        ArrayList<ProductDetailModel.ProductSku> productSkus = new ArrayList<>();


        for (ProductSku sku : skus) {
            ProductDetailModel.ProductSku productSku = new ProductDetailModel.ProductSku();
            productSku.setId(sku.getId().longValue());
            productSku.setAliSkuId(sku.getAliSkuId());
            productSku.setProductId(id.longValue());
            productSku.setAliSkuId(sku.getSkuId());
            productSku.setLimit(sku.getWholesaleLimit());
            productSku.setCount(sku.getWholesaleCount());


            List list = JSONArray.parseObject(sku.getPriceIn(), List.class);
            productSku.setPricesIn(list);
            List priceTH = JSONArray.parseObject(sku.getPriceJsonTH(), List.class);
            productSku.setPricesOutTH(priceTH);
            List priceCN = JSONArray.parseObject(sku.getPriceJsonCN(), List.class);
            productSku.setPricesOutCN(priceCN);
            List priceID = JSONArray.parseObject(sku.getPriceJsonID(), List.class);
            productSku.setPricesOutID(priceID);
            List priceJson = JSONArray.parseObject(sku.getPriceJson(), List.class);
            productSku.setPricesJson(priceJson);


            //通过sku表中的attributeListIds查询ProductAttributeList
            String[] split = sku.getAttributeListIds().split(",");
            List<String> asList = Arrays.asList(split);
            List<ProductDetailModel.Attribute.AttributeFeature> attributeFeatures = queryAttributeList(asList);
            productSku.setAttrs(attributeFeatures);

            productSkus.add(productSku);
        }

        return productSkus;

    }

    //通过sku表中的attributeListIds查询ProductAttributeList
    public List<ProductDetailModel.Attribute.AttributeFeature> queryAttributeList(List<String> ids) {
        List<ProductAttributeList> attributeLists = attributeListMapper.selectBatchIds(ids);
        List<ProductDetailModel.Attribute.AttributeFeature> attributeFeatures = new ArrayList<>();
        for (ProductAttributeList attributeList : attributeLists) {
            ProductDetailModel.Attribute.AttributeFeature feature = new ProductDetailModel.Attribute.AttributeFeature();

            feature.setFeatureId(attributeList.getId().toString());
            feature.setFeatureName(attributeList.getValue());
            feature.setFeatureNameID(attributeList.getValueId());
            feature.setFeatureNameTH(attributeList.getValueTh());
            feature.setFeatureNameZH(attributeList.getValueZh());

            attributeFeatures.add(feature);
        }
        return attributeFeatures;
    }

    //通过List<ProductDetailModel.ProductSku>查询List<ProductAttribute>
    public List<ProductDetailModel.Attribute> queryAttribute(List<ProductDetailModel.ProductSku> ProductSku) {
        List<String> list = new ArrayList<>();
        for (ProductDetailModel.ProductSku sku : ProductSku) {
            List<ProductDetailModel.Attribute.AttributeFeature> attrs = sku.getAttrs();
            for (ProductDetailModel.Attribute.AttributeFeature attributeFeature : attrs) {
                list.add(attributeFeature.getFeatureId());
            }
        }

        List<ProductAttributeList> attributeLists = attributeListMapper.selectBatchIds(list);
        List<String> arrayList = new ArrayList<>();
        for (ProductAttributeList attributeList : attributeLists) {
            arrayList.add(attributeList.getId().toString());
        }
        //查询插入list<attribute>
        List<ProductAttribute> productAttributes = attributeMapper.selectBatchIds(arrayList);
        List<ProductDetailModel.Attribute> attributes = new ArrayList<>();
        EntityWrapper<ProductAttributeList> wrapper = new EntityWrapper<>();
        for (ProductAttribute productAttribute : productAttributes) {
            ProductDetailModel.Attribute attribute = new ProductDetailModel.Attribute();

            attribute.setAttributeId(productAttribute.getId().toString());
            attribute.setAttributeName(productAttribute.getName());
            attribute.setAttributeNameID(productAttribute.getNameId());
            attribute.setAttributeNameTH(productAttribute.getNameTh());
            attribute.setAttributeNameZH(productAttribute.getNameZh());

            wrapper.eq("attribute_id", productAttribute.getId());
            List<ProductAttributeList> lists = attributeListMapper.selectList(wrapper);
            List<String> strings = new ArrayList<>();
            for (ProductAttributeList li : lists) {
                strings.add(li.getId().toString());
            }
            List<ProductDetailModel.Attribute.AttributeFeature> attributeFeatures = queryAttributeList(strings);

            attribute.setFeatures(attributeFeatures);

            attributes.add(attribute);
        }
        return attributes;
    }

    //通过商品id查询ProductProps表
    public List<ProductDetailModel.ProductProps> queryProperty(String ids) {
        ArrayList<ProductDetailModel.ProductProps> productProps = new ArrayList<>();

        String[] split = ids.split(",");
        List<String> list = Arrays.asList(split);
        List<ProductPropertyList> lists = propertyListMapper.selectBatchIds(list);

        for (ProductPropertyList propertyList : lists) {
            ProductProperty property = propertyMapper.selectById(propertyList.getPropertyId());

            ProductDetailModel.ProductProps props = new ProductDetailModel.ProductProps();
            props.setPropId(property.getPropertyId());
            props.setPropKey(property.getPropertyKey());
            props.setPropKeyZH(property.getPropertyKeyZH());
            props.setPropKeyTH(property.getPropertyKeyTH());
            props.setPropKeyID(property.getPropertyKeyID());

            ProductDetailModel.ProductProps.PropsValue propsValue = new ProductDetailModel.ProductProps.PropsValue();
            propsValue.setValueId(propertyList.getPropertyId());
            propsValue.setPropValue(propertyList.getPropertyValue());
            propsValue.setPropValueID(propertyList.getPropertyValueID());
            propsValue.setPropValueTH(propertyList.getPropertyValueTH());
            propsValue.setPropValueZH(propertyList.getPropertyValueZH());

            props.setPropValue(propsValue);
            productProps.add(props);
        }

        return productProps;
    }

}
