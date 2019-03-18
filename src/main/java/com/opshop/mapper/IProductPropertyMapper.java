package com.opshop.mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.opshop.bean.ProductProperty;
import org.apache.ibatis.annotations.Param;

/**
 * Created by zhouxj6112 on 2019/1/5.
 */
public interface IProductPropertyMapper extends BaseMapper<ProductProperty> {

    ProductProperty selPropertyByName(@Param("propKey") String propKey,
                                      @Param("from") String from);
    // 插入一条常规属性
    int insertSelective(ProductProperty property);

    ProductProperty selPropertyById(@Param("propertyId") Integer propertyId);

}
