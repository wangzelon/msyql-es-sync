package com.opshop.mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.opshop.bean.ProductAttribute;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ProductAttributeMapper extends BaseMapper<ProductAttribute> {

    int deleteByPrimaryKey(Integer id);

//    int insert(ProductAttribute record);

    int insertSelective(ProductAttribute record);

    List<ProductAttribute> selectAll();

    ProductAttribute selectByName(@Param("name") String name,
                                  @Param("from") String from);

    ProductAttribute selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(ProductAttribute record);

    int updateByPrimaryKey(ProductAttribute record);
}