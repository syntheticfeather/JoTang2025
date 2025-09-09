package com.example.demo.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.Entity.Result;
import com.example.demo.Entity.Product;
import com.example.demo.Service.ServiceImpl.ProductServiceImpl;

@RestController
@RequestMapping("/product")
public class ProductController {
    @Autowired
    private ProductServiceImpl productService;

    @GetMapping()
    public Result getProduct(long id) {
        Product p = productService.getProduct(id);
        if (p == null) {
            return Result.Fail("产品不存在");
        } else {
            return Result.Success(p);
        }
    }

    @PostMapping
    public Result addProduct(@RequestBody Product product) {
        productService.addProduct(product);
        return Result.Success(null);
    }

    @PutMapping
    public Result updateProduct(@RequestBody Product product) {
        productService.updateProduct(product);
        return Result.Success(null);
    }

    @DeleteMapping
    public Result deleteProduct(long id) {
        productService.deleteProduct(id);
        return Result.Success(null);    
    }
}