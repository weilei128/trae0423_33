package com.jewelry.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.jewelry.entity.Product;

public interface ProductService extends IService<Product> {
    Page<Product> pageProduct(int page, int size, String name, String category, Integer status);
}
