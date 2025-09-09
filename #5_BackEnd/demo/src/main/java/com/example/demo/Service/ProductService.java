package com.example.demo.Service;

import com.example.demo.Entity.Product;

public interface ProductService {
    public void addProduct(Product product);

    public void deleteProduct(Long id);

    public Product getProduct(Long id);

    public void updateProduct(Product product);
}
