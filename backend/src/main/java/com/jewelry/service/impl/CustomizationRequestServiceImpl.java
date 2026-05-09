package com.jewelry.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jewelry.entity.CustomizationRequest;
import com.jewelry.mapper.CustomizationRequestMapper;
import com.jewelry.service.CustomizationRequestService;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.atomic.AtomicInteger;

@Service
public class CustomizationRequestServiceImpl extends ServiceImpl<CustomizationRequestMapper, CustomizationRequest> implements CustomizationRequestService {

    private static final AtomicInteger sequence = new AtomicInteger(0);

    @Override
    public Page<CustomizationRequest> pageRequest(int page, int size, String requestNo, Long customerId, Integer status) {
        Page<CustomizationRequest> pageParam = new Page<>(page, size);
        LambdaQueryWrapper<CustomizationRequest> wrapper = new LambdaQueryWrapper<>();
        
        if (StringUtils.hasText(requestNo)) {
            wrapper.like(CustomizationRequest::getRequestNo, requestNo);
        }
        if (customerId != null) {
            wrapper.eq(CustomizationRequest::getCustomerId, customerId);
        }
        if (status != null) {
            wrapper.eq(CustomizationRequest::getStatus, status);
        }
        
        wrapper.orderByDesc(CustomizationRequest::getCreateTime);
        return this.page(pageParam, wrapper);
    }

    @Override
    public String generateRequestNo() {
        String dateStr = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        int seq = sequence.incrementAndGet() % 1000;
        return String.format("CR%s%03d", dateStr, seq);
    }
}
