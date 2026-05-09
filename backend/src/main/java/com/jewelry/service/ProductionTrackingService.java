package com.jewelry.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.jewelry.entity.ProductionTracking;

public interface ProductionTrackingService extends IService<ProductionTracking> {
    Page<ProductionTracking> pageTracking(int page, int size, Long requestId, Integer status);
    void startProcess(Long trackingId, Long processId);
    void completeProcess(Long trackingId, Long processId, String operator);
}
