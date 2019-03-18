package com.opshop.service.impl;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.opshop.bean.Product;
import com.opshop.mapper.IProductMapper;
import com.opshop.service.ProductService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(rollbackFor = Exception.class)
public class ProductServiceImpl extends ServiceImpl<IProductMapper, Product> implements ProductService {
}
