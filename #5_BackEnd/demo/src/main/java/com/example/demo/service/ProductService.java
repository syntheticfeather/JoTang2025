package com.example.demo.service;

import java.util.ArrayList;
import java.util.List;

import com.example.demo.dto.FilterRequest;
import com.example.demo.entity.Product;

public interface ProductService {

    public Product addProduct(Product product);

    public boolean deleteProduct(Long id);

    public Product getProduct(Long id);

    public Product updateProduct(Product product);

    public ArrayList<Product> getProductsList();

    public List<Product> getProductsByFilters(FilterRequest filterRequest);
}
