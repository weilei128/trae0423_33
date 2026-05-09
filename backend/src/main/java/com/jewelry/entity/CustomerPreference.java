package com.jewelry.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("customer_preference")
public class CustomerPreference {
    @TableId(type = IdType.AUTO)
    private Long id;
    
    private Long customerId;
    private String preferenceType;
    private String preferenceValue;
    private String description;
    
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;
    
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;
    
    @TableLogic
    private Integer deleted;
}
