package com.jewelry.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("production_process")
public class ProductionProcess {
    @TableId(type = IdType.AUTO)
    private Long id;
    
    private Long trackingId;
    private String processName;
    private Integer processOrder;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private Integer status;
    private String operator;
    private String remark;
    
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;
}
