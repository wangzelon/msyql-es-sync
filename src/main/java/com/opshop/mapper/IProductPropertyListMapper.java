package com.opshop.mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.opshop.bean.ProductPropertyList;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Created by zhouxj6112 on 2019/1/5.
 */
public interface IProductPropertyListMapper extends BaseMapper<ProductPropertyList> {

    ProductPropertyList selPropertyValueByName(@Param("propValue") String propValue);

    int insertSelective(ProductPropertyList property);

    List<ProductPropertyList> selPropertyValueBySpuId(@Param("spuId") String spuId);

}
