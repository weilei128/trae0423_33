package com.jewelry.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@TableName("customization_request")
public class CustomizationRequest {
    @TableId(type = IdType.AUTO)
    private Long id;
    
    private Long customerId;
    private String requestNo;
    private String styleDesc;
    private String material;
    private String size;
    private BigDecimal budget;
    private String description;
    private String referenceImages;
    private Integer status;
    private Long designerId;
    
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;
    
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;
    
    @TableLogic
    private Integer deleted;
}
