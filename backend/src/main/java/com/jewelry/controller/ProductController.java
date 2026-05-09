package com.jewelry.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jewelry.common.Result;
import com.jewelry.entity.Product;
import com.jewelry.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    @Autowired
    private ProductService productService;

    @GetMapping
    public Result<Page<Product>> list(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String category,
            @RequestParam(required = false) Integer status) {
        return Result.success(productService.pageProduct(page, size, name, category, status));
    }

    @GetMapping("/{id}")
    public Result<Product> getById(@PathVariable Long id) {
        return Result.success(productService.getById(id));
    }

    @PostMapping
    public Result<Boolean> save(@RequestBody Product product) {
        return Result.success(productService.save(product));
    }

    @PutMapping
    public Result<Boolean> update(@RequestBody Product product) {
        return Result.success(productService.updateById(product));
    }

    @DeleteMapping("/{id}")
    public Result<Boolean> delete(@PathVariable Long id) {
        return Result.success(productService.removeById(id));
    }
}
