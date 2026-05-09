package com.jewelry.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jewelry.common.Result;
import com.jewelry.entity.CustomizationRequest;
import com.jewelry.entity.Designer;
import com.jewelry.service.CustomizationRequestService;
import com.jewelry.service.DesignerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/customizations")
public class CustomizationRequestController {

    @Autowired
    private CustomizationRequestService customizationRequestService;

    @Autowired
    private DesignerService designerService;

    @GetMapping
    public Result<Page<CustomizationRequest>> list(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String requestNo,
            @RequestParam(required = false) Long customerId,
            @RequestParam(required = false) Integer status) {
        return Result.success(customizationRequestService.pageRequest(page, size, requestNo, customerId, status));
    }

    @GetMapping("/{id}")
    public Result<CustomizationRequest> getById(@PathVariable Long id) {
        return Result.success(customizationRequestService.getById(id));
    }

    @PostMapping
    public Result<Boolean> save(@RequestBody CustomizationRequest request) {
        if (request.getRequestNo() == null || request.getRequestNo().isEmpty()) {
            request.setRequestNo(customizationRequestService.generateRequestNo());
        }
        if (request.getStatus() == null) {
            request.setStatus(0);
        }
        return Result.success(customizationRequestService.save(request));
    }

    @PutMapping
    public Result<Boolean> update(@RequestBody CustomizationRequest request) {
        return Result.success(customizationRequestService.updateById(request));
    }

    @DeleteMapping("/{id}")
    public Result<Boolean> delete(@PathVariable Long id) {
        return Result.success(customizationRequestService.removeById(id));
    }

    @PutMapping("/{id}/accept")
    public Result<Boolean> accept(@PathVariable Long id, @RequestParam Long designerId) {
        CustomizationRequest request = customizationRequestService.getById(id);
        if (request != null) {
            request.setStatus(1);
            request.setDesignerId(designerId);
            return Result.success(customizationRequestService.updateById(request));
        }
        return Result.error("需求不存在");
    }

    @GetMapping("/designers")
    public Result<List<Designer>> listDesigners() {
        return Result.success(designerService.list());
    }
}
