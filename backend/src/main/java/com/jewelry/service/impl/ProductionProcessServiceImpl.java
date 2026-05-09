package com.jewelry.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jewelry.entity.ProductionProcess;
import com.jewelry.mapper.ProductionProcessMapper;
import com.jewelry.service.ProductionProcessService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ProductionProcessServiceImpl extends ServiceImpl<ProductionProcessMapper, ProductionProcess> implements ProductionProcessService {

    @Override
    public List<ProductionProcess> getByTrackingId(Long trackingId) {
        return baseMapper.selectByTrackingId(trackingId);
    }

    @Override
    @Transactional
    public void createProcesses(Long trackingId, List<String> processNames) {
        int order = 1;
        for (String processName : processNames) {
            ProductionProcess process = new ProductionProcess();
            process.setTrackingId(trackingId);
            process.setProcessName(processName);
            process.setProcessOrder(order++);
            process.setStatus(0);
            baseMapper.insert(process);
        }
    }
}
