package com.jewelry.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.jewelry.entity.Customer;

public interface CustomerService extends IService<Customer> {
    Page<Customer> pageCustomer(int page, int size, String name, String phone);
}
