package com.example.demo.service.ServiceImpl;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.entity.Product;
import com.example.demo.mapper.ProductMapper;
import com.example.demo.service.ProductService;

@Service
public class ProductServiceImpl implements ProductService {

    @Autowired
    private ProductMapper productMapper;

    @Override
    public Product getProduct(Long id) {
        return productMapper.getProduct(id);
    }

    @Override
    public ArrayList<Product> getProductsList() {
        return productMapper.getProductsList();
    }

    @Override
    public Product addProduct(Product product) {
        // 设置创建时间和更新时间
        product.setPublishTime(LocalDateTime.now());
        product.setUpdateTime(LocalDateTime.now());
        product.setStatus("未售出");
        // 插入产品并返回带有ID的产品对象
        productMapper.addProduct(product);
        return product;
    }

    @Override
    public Product updateProduct(Product product) {
        // 设置更新时间
        product.setUpdateTime(LocalDateTime.now());

        // 执行更新
        int affectedRows = productMapper.updateProduct(product);
        if (affectedRows == 0) {
            return null; // 表示更新失败（产品不存在）
        }

        // 返回更新后的产品信息
        return productMapper.getProduct(product.getId());
    }

    @Override
    public boolean deleteProduct(Long id) {
        int affectedRows = productMapper.deleteProduct(id);
        return affectedRows > 0; // 返回是否成功删除
    }

    // 在ProductService中添加方法
    @Override
    public List<Product> getProductsByFilters(String type, Double minPrice, Double maxPrice, Integer hours) {
        return productMapper.selectByFilters(type, minPrice, maxPrice, hours);
    }
}
