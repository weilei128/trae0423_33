package com.jewelry.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.jewelry.entity.ProductionProcess;

import java.util.List;

public interface ProductionProcessService extends IService<ProductionProcess> {
    List<ProductionProcess> getByTrackingId(Long trackingId);
    void createProcesses(Long trackingId, List<String> processNames);
}
