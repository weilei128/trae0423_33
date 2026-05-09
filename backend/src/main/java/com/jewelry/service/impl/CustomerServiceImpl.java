package com.jewelry.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jewelry.entity.Customer;
import com.jewelry.mapper.CustomerMapper;
import com.jewelry.service.CustomerService;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
public class CustomerServiceImpl extends ServiceImpl<CustomerMapper, Customer> implements CustomerService {
    
    @Override
    public Page<Customer> pageCustomer(int page, int size, String name, String phone) {
        Page<Customer> pageParam = new Page<>(page, size);
        LambdaQueryWrapper<Customer> wrapper = new LambdaQueryWrapper<>();
        
        if (StringUtils.hasText(name)) {
            wrapper.like(Customer::getName, name);
        }
        if (StringUtils.hasText(phone)) {
            wrapper.like(Customer::getPhone, phone);
        }
        
        wrapper.orderByDesc(Customer::getCreateTime);
        return this.page(pageParam, wrapper);
    }
}
