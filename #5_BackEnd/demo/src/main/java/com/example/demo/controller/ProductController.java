package com.example.demo.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.dto.FilterRequest;
import com.example.demo.entity.ApiResponse;
import com.example.demo.entity.Product;
import com.example.demo.service.ProductService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/products")
public class ProductController {

    @Autowired
    private ProductService productService;

    @GetMapping()
    public ResponseEntity<ApiResponse<Product>> getProduct(@RequestParam long id) {
        Product p = productService.getProduct(id);
        if (p == null) {
            return ResponseEntity.ok(ApiResponse.error(404, "产品不存在"));
        } else {
            return ResponseEntity.ok(ApiResponse.success(p));
        }
    }

    @GetMapping("/list")
    public ResponseEntity<ApiResponse<List<Product>>> getProductList() {
        List<Product> products = productService.getProductsList();
        return ResponseEntity.ok(ApiResponse.success(products));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<Product>> addProduct(@RequestBody Product product) {
        Product createdProduct = productService.addProduct(product);
        return ResponseEntity.ok(ApiResponse.success(createdProduct, "产品添加成功"));
    }

    @PutMapping
    public ResponseEntity<ApiResponse<Product>> updateProduct(@RequestBody Product product) {
        Product updatedProduct = productService.updateProduct(product);
        if (updatedProduct == null) {
            return ResponseEntity.ok(ApiResponse.error(404, "产品不存在，更新失败"));
        }
        return ResponseEntity.ok(ApiResponse.success(updatedProduct, "产品更新成功"));
    }

    @DeleteMapping
    public ResponseEntity<ApiResponse<Void>> deleteProduct(@RequestParam long id) {
        boolean deleted = productService.deleteProduct(id);
        if (deleted) {
            return ResponseEntity.ok(ApiResponse.success(null, "产品删除成功"));
        } else {
            return ResponseEntity.ok(ApiResponse.error(404, "产品不存在，删除失败"));
        }
    }

    // 在ProductController中添加筛选接口
    @GetMapping("/filter")
    public ResponseEntity<ApiResponse<List<Product>>> filter(
            @Valid @RequestBody FilterRequest filterRequest) {

        List<Product> products = productService.getProductsByFilters(filterRequest);
        return ResponseEntity.ok(ApiResponse.success(products));
    }

}
