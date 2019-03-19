package com.opshop.mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.opshop.bean.Product;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.sql.Timestamp;
import java.util.List;
import java.util.Map;

/**
 * Created by zhouxj6112 on 2018/12/3.
 */
@Mapper
public interface IProductMapper extends BaseMapper<Product> {

    /***
     * 添加商品到数据库,spu级别的
     * @param pid
     * @param pname
     * @param pprice
     * @param pfactory
     * @param pdate
     * @param purl
     * @param pnickname
     */
    void addProduct(@Param("pid") String pid,
                    @Param("pname") String pname,
                    @Param("pprice") String pprice,
                    @Param("pfactory") String pfactory,
                    @Param("pdate") Timestamp pdate,
                    @Param("purl") String purl,
                    @Param("pnickname") String pnickname);

    /**
     * 获取商品详情
     *
     * @param spuId
     * @return
     */
    Map<String, Object> getProductInfo(@Param("pid") String spuId);

    /**
     * 检测属性值是否存在
     *
     * @param attrname
     * @param level    层次
     * @return 存在就返回这条属性的id, 不存在就返回0
     */
    Long isProductAttributeExist(@Param("attrname") String attrname,
                                 @Param("level") Integer level);

    /**
     * 添加属性值到属性表
     *
     * @param parentid
     * @param attrname
     */
    void addProductAttribute(@Param("parentid") Integer parentid,
                             @Param("attrname") String attrname);

    // 查表部分
    Map<String, Object> getOneAttribute(@Param("id") String attrId);

    Map<String, Object> getAttributeName(@Param("id") String attrNameId);

    List<Map<String, Object>> getProductSkuList(@Param("id") String spuId);

    // 后台获取商品列表
    List<Map<String, Object>> getProductList(@Param("from") String from,
                                             @Param("status") String status,
                                             @Param("pageNo") Integer pageNo,
                                             @Param("pageSize") Integer pageSize,
                                             @Param("categoryIds") String categoryIds);
    Long countProductList(@Param("from") String from,
                          @Param("status") String status,
                          @Param("categoryIds") String categoryIds);

    // 上下架操作
    void changeProductStatus(@Param("spuId") Long spuId,
                             @Param("status") String status);

}
