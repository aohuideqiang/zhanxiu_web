package com.example.demo.controller;

import cn.dev33.satoken.util.SaResult;
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

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/products")
@CrossOrigin(origins = "*")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping
    public SaResult getAllProducts() {
        return SaResult.data(productService.list());
    }

    @GetMapping("/{id}")
    public SaResult getProductById(@PathVariable Long id) {
        Product product = productService.getById(id);
        return product == null ? SaResult.error("产品不存在").setCode(404) : SaResult.data(product);
    }

    @PostMapping
    public SaResult createProduct(@Valid @RequestBody Product product) {
        return productService.save(product) ? SaResult.ok("创建产品成功") : SaResult.error("创建产品失败");
    }

    @PutMapping("/{id}")
    public SaResult updateProduct(@PathVariable Long id, @Valid @RequestBody Product updatedProduct) {
        updatedProduct.setId(id);
        return productService.saveOrUpdate(updatedProduct) ? SaResult.ok("更新产品成功") : SaResult.error("更新产品失败");
    }

    @DeleteMapping("/{id}")
    public SaResult deleteProduct(@PathVariable Long id) {
        return productService.removeById(id) ? SaResult.ok("删除产品成功") : SaResult.error("产品不存在或已删除").setCode(404);
    }
}
