package com.jewelry.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jewelry.common.Result;
import com.jewelry.entity.Customer;
import com.jewelry.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/customers")
public class CustomerController {

    @Autowired
    private CustomerService customerService;

    @GetMapping
    public Result<Page<Customer>> list(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String phone) {
        return Result.success(customerService.pageCustomer(page, size, name, phone));
    }

    @GetMapping("/{id}")
    public Result<Customer> getById(@PathVariable Long id) {
        return Result.success(customerService.getById(id));
    }

    @PostMapping
    public Result<Boolean> save(@RequestBody Customer customer) {
        return Result.success(customerService.save(customer));
    }

    @PutMapping
    public Result<Boolean> update(@RequestBody Customer customer) {
        return Result.success(customerService.updateById(customer));
    }

    @DeleteMapping("/{id}")
    public Result<Boolean> delete(@PathVariable Long id) {
        return Result.success(customerService.removeById(id));
    }
}
