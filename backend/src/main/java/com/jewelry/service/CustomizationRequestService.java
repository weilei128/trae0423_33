package com.jewelry.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.jewelry.entity.CustomizationRequest;

public interface CustomizationRequestService extends IService<CustomizationRequest> {
    Page<CustomizationRequest> pageRequest(int page, int size, String requestNo, Long customerId, Integer status);
    String generateRequestNo();
}
