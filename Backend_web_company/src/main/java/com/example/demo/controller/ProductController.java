package com.example.demo.controller;

import java.util.List;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.entity.Product;
import com.example.demo.service.ProductService;

@RestController
@RequestMapping("/api/products")
@CrossOrigin(origins = "*")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping
    public List<Product> getAllProducts() {
        return productService.list();
    }

    @GetMapping("/{id}")
    public Product getProductById(@PathVariable Long id) {
        return productService.getById(id);
    }

    @PostMapping
    public boolean createProduct(@RequestBody Product product) {
        return productService.save(product);
    }

    @PutMapping("/{id}")
    public boolean updateProduct(@PathVariable Long id, @RequestBody Product updatedProduct) {
        updatedProduct.setId(id);
        return productService.saveOrUpdate(updatedProduct);
    }

    @DeleteMapping("/{id}")
    public boolean deleteProduct(@PathVariable Long id) {
        return productService.removeById(id);
    }
}