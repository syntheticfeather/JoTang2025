package com.example.demo.Service.ServiceImpl;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.Entity.Product;
import com.example.demo.Mapper.ProductMapper;
import com.example.demo.Service.ProductService;

@Service
public class ProductServiceImpl implements ProductService {

    @Autowired
    private ProductMapper productMapper;

    @Override
    public void addProduct(Product product) {
        LocalDateTime now = LocalDateTime.now();
        product.setPublishTime(now);
        product.setUpdateTime(now);
        if (product.getStatus() == null) {
            product.setStatus("未售出");
        }
        productMapper.addProduct(product);
    }

    @Override
    public void deleteProduct(Long id) {
        productMapper.deleteProduct(id);
    }

    @Override
    public Product getProduct(Long id) {
        return productMapper.getProduct(id);
    }

    @Override
    public void updateProduct(Product product) {
        product.setUpdateTime(LocalDateTime.now());
        productMapper.updateProduct(product);
    }
}
