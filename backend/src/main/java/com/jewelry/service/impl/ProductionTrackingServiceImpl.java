package com.jewelry.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jewelry.entity.ProductionProcess;
import com.jewelry.entity.ProductionTracking;
import com.jewelry.mapper.ProductionProcessMapper;
import com.jewelry.mapper.ProductionTrackingMapper;
import com.jewelry.service.ProductionTrackingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ProductionTrackingServiceImpl extends ServiceImpl<ProductionTrackingMapper, ProductionTracking> implements ProductionTrackingService {

    @Autowired
    private ProductionProcessMapper processMapper;

    @Override
    public Page<ProductionTracking> pageTracking(int page, int size, Long requestId, Integer status) {
        Page<ProductionTracking> pageParam = new Page<>(page, size);
        LambdaQueryWrapper<ProductionTracking> wrapper = new LambdaQueryWrapper<>();
        
        if (requestId != null) {
            wrapper.eq(ProductionTracking::getRequestId, requestId);
        }
        if (status != null) {
            wrapper.eq(ProductionTracking::getStatus, status);
        }
        
        wrapper.orderByDesc(ProductionTracking::getCreateTime);
        return this.page(pageParam, wrapper);
    }

    @Override
    @Transactional
    public void startProcess(Long trackingId, Long processId) {
        ProductionProcess process = processMapper.selectById(processId);
        if (process != null && process.getTrackingId().equals(trackingId)) {
            process.setStatus(1);
            process.setStartTime(LocalDateTime.now());
            processMapper.updateById(process);
            
            updateProgress(trackingId);
        }
    }

    @Override
    @Transactional
    public void completeProcess(Long trackingId, Long processId, String operator) {
        ProductionProcess process = processMapper.selectById(processId);
        if (process != null && process.getTrackingId().equals(trackingId)) {
            process.setStatus(2);
            process.setEndTime(LocalDateTime.now());
            process.setOperator(operator);
            processMapper.updateById(process);
            
            updateProgress(trackingId);
        }
    }

    private void updateProgress(Long trackingId) {
        List<ProductionProcess> processes = processMapper.selectByTrackingId(trackingId);
        if (processes.isEmpty()) return;
        
        int total = processes.size();
        int completed = (int) processes.stream().filter(p -> p.getStatus() == 2).count();
        int inProgress = (int) processes.stream().filter(p -> p.getStatus() == 1).count();
        
        int progress = (int) ((completed * 100.0) / total);
        int status = inProgress > 0 ? 1 : (completed == total ? 2 : 0);
        
        ProductionTracking tracking = this.getById(trackingId);
        if (tracking != null) {
            tracking.setProgress(progress);
            tracking.setStatus(status);
            if (completed == total) {
                tracking.setActualCompleteDate(java.time.LocalDate.now());
            }
            this.updateById(tracking);
        }
    }
}
