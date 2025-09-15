package com.example.demo.service.ServiceImpl;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.dto.FilterRequest;
import com.example.demo.entity.Product;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.mapper.ProductMapper;
import com.example.demo.service.ProductService;
import com.example.demo.utils.RedisUtil;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class ProductServiceImpl implements ProductService {

    @Autowired
    private RedisUtil redisUtil;

    @Autowired
    private ProductMapper productMapper;

    // 商品缓存时间（分钟）
    private static final long PRODUCT_CACHE_EXPIRE = 30;
    // 商品缓存键前缀
    private static final String PRODUCT_CACHE_KEY_PREFIX = "product:";

    @Override
    public Product getProduct(Long id) {
        String cacheKey = PRODUCT_CACHE_KEY_PREFIX + id;

        // 1. 先从缓存中获取
        Product product = redisUtil.get(cacheKey, Product.class);
        if (product != null) {
            log.info("缓存命中，获取商品: {}", id);
            return product;
        }

        // 2. 缓存中没有，从数据库查询
        log.info("缓存未命中，从数据库查询商品: {}", id);
        product = productMapper.getProduct(id);

        if (product != null) {
            // 3. 将查询结果放入缓存
            redisUtil.set(cacheKey, product, PRODUCT_CACHE_EXPIRE, TimeUnit.MINUTES);
            log.info("将商品存入缓存: {}", id);
        }

        return product;
    }

    @Override
    public ArrayList<Product> getProductsList() {
        String cacheKey = PRODUCT_CACHE_KEY_PREFIX + "list:all";

        // 1. 先从缓存中获取
        @SuppressWarnings("unchecked")
        ArrayList<Product> products = (ArrayList<Product>) redisUtil.get(cacheKey);
        if (products != null) {
            log.info("从缓存中获取商品列表");
            return products;
        }

        // 2. 缓存中没有，从数据库查询
        log.info("缓存未命中，从数据库查询商品列表");
        products = productMapper.getProductsList();

        if (products != null && !products.isEmpty()) {
            // 3. 将查询结果放入缓存
            redisUtil.set(cacheKey, products, PRODUCT_CACHE_EXPIRE, TimeUnit.MINUTES);
            log.info("将商品列表存入缓存");
        }

        return products;
    }

    @Override
    @Transactional
    public Product addProduct(Product product) {
        // 设置创建时间和更新时间
        product.setPublishTime(LocalDateTime.now());
        product.setUpdateTime(LocalDateTime.now());
        product.setStatus("未售出");
        // 插入产品并返回带有ID的产品对象
        productMapper.addProduct(product);

        clearProductListCaches();
        log.info("添加商品后清除相关缓存");

        return product;
    }

    @Override
    public Product updateProduct(Product product) {
        // 设置更新时间
        product.setUpdateTime(LocalDateTime.now());

        // 更新数据库
        int affectedRows = productMapper.updateProduct(product);
        if (affectedRows == 0) {
            throw new ResourceNotFoundException("商品不存在，ID: " + product.getId());
        }

        // 更新缓存 - 先获取最新数据
        Product updatedProduct = productMapper.getProduct(product.getId());
        String cacheKey = PRODUCT_CACHE_KEY_PREFIX + product.getId();
        redisUtil.set(cacheKey, updatedProduct, PRODUCT_CACHE_EXPIRE, TimeUnit.MINUTES);

        // 清除列表缓存
        clearProductListCaches();
        log.info("更新商品后更新单个商品缓存并清除列表缓存");

        return updatedProduct;
    }

    @Override
    @Transactional
    public boolean deleteProduct(Long id) {
        // 删除数据库记录
        int affectedRows = productMapper.deleteProduct(id);

        if (affectedRows > 0) {
            // 删除缓存
            String cacheKey = PRODUCT_CACHE_KEY_PREFIX + id;
            redisUtil.delete(cacheKey);

            // 清除列表缓存
            clearProductListCaches();
            log.info("删除商品后删除单个商品缓存并清除列表缓存");
            return true;
        }
        throw new ResourceNotFoundException("商品不存在，ID: " + id);
    }

    // 在ProductService中添加方法
    @Override
    public List<Product> getProductsByFilters(FilterRequest filterRequest) {
        String type = filterRequest.getType();
        Double minPrice = filterRequest.getMinPrice();
        Double maxPrice = filterRequest.getMaxPrice();
        Integer hours = filterRequest.getHours();
        String status = filterRequest.getStatus();
        String sortField = filterRequest.getSortField();
        String sortDirection = filterRequest.getSortDirection();

        return productMapper.selectByFilters(type, minPrice, maxPrice, hours, status, sortField, sortDirection);
    }

    /**
     * 清除商品列表缓存
     */
    private void clearProductListCaches() {
        // 清除所有商品列表相关的缓存
        // 这里使用简单的模式匹配，实际生产中可能需要更复杂的管理
        Set<String> keys = redisUtil.keys(PRODUCT_CACHE_KEY_PREFIX + "list:*");
        if (keys != null && !keys.isEmpty()) {
            for (String key : keys) {
                redisUtil.delete(key);
            }
        }
    }
}
