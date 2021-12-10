package cn.sysu.service.impl;

import cn.sysu.mapper.ProductMapper;
import cn.sysu.pojo.Product;
import cn.sysu.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class ProductServiceImpl implements ProductService {

    @Autowired(required = false)
    private ProductMapper productMapper;

    @Override
    public Product findById(int id) {
        return productMapper.selectByPrimaryKey(id);
    }
}
