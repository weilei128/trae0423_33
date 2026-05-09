package com.jewelry.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jewelry.common.Result;
import com.jewelry.entity.ProductionProcess;
import com.jewelry.entity.ProductionTracking;
import com.jewelry.service.ProductionProcessService;
import com.jewelry.service.ProductionTrackingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/production")
public class ProductionTrackingController {

    @Autowired
    private ProductionTrackingService trackingService;

    @Autowired
    private ProductionProcessService processService;

    @GetMapping
    public Result<Page<ProductionTracking>> list(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) Long requestId,
            @RequestParam(required = false) Integer status) {
        return Result.success(trackingService.pageTracking(page, size, requestId, status));
    }

    @GetMapping("/{id}")
    public Result<ProductionTracking> getById(@PathVariable Long id) {
        return Result.success(trackingService.getById(id));
    }

    @PostMapping
    public Result<Boolean> save(@RequestBody ProductionTracking tracking) {
        if (tracking.getStatus() == null) {
            tracking.setStatus(0);
        }
        if (tracking.getProgress() == null) {
            tracking.setProgress(0);
        }
        if (tracking.getDelayWarning() == null) {
            tracking.setDelayWarning(0);
        }
        boolean success = trackingService.save(tracking);
        
        if (success && tracking.getId() != null) {
            List<String> defaultProcesses = Arrays.asList(
                "设计确认",
                "原材料准备",
                "加工制作",
                "质量检测",
                "成品包装"
            );
            processService.createProcesses(tracking.getId(), defaultProcesses);
        }
        
        return Result.success(success);
    }

    @PutMapping
    public Result<Boolean> update(@RequestBody ProductionTracking tracking) {
        return Result.success(trackingService.updateById(tracking));
    }

    @DeleteMapping("/{id}")
    public Result<Boolean> delete(@PathVariable Long id) {
        return Result.success(trackingService.removeById(id));
    }

    @GetMapping("/{id}/processes")
    public Result<List<ProductionProcess>> getProcesses(@PathVariable Long id) {
        return Result.success(processService.getByTrackingId(id));
    }

    @PostMapping("/{id}/processes")
    public Result<Boolean> addProcess(@PathVariable Long id, @RequestBody Map<String, Object> body) {
        String processName = (String) body.get("processName");
        if (processName != null && !processName.isEmpty()) {
            List<ProductionProcess> existing = processService.getByTrackingId(id);
            int order = existing.size() + 1;
            
            ProductionProcess process = new ProductionProcess();
            process.setTrackingId(id);
            process.setProcessName(processName);
            process.setProcessOrder(order);
            process.setStatus(0);
            processService.save(process);
            return Result.success(true);
        }
        return Result.error("工序名称不能为空");
    }

    @DeleteMapping("/{trackingId}/processes/{processId}")
    public Result<Boolean> deleteProcess(@PathVariable Long trackingId, @PathVariable Long processId) {
        ProductionProcess process = processService.getById(processId);
        if (process != null && process.getTrackingId().equals(trackingId)) {
            return Result.success(processService.removeById(processId));
        }
        return Result.error("工序不存在");
    }

    @PutMapping("/{trackingId}/processes/{processId}/start")
    public Result<Boolean> startProcess(@PathVariable Long trackingId, @PathVariable Long processId) {
        trackingService.startProcess(trackingId, processId);
        return Result.success(true);
    }

    @PutMapping("/{trackingId}/processes/{processId}/complete")
    public Result<Boolean> completeProcess(
            @PathVariable Long trackingId, 
            @PathVariable Long processId,
            @RequestParam(defaultValue = "管理员") String operator) {
        trackingService.completeProcess(trackingId, processId, operator);
        return Result.success(true);
    }
}
