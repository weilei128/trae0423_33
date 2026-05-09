package com.jewelry.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@TableName("production_tracking")
public class ProductionTracking {
    @TableId(type = IdType.AUTO)
    private Long id;
    
    private Long requestId;
    private String processName;
    private Integer progress;
    private Integer status;
    private LocalDate estimatedCompleteDate;
    private LocalDate actualCompleteDate;
    private Integer delayWarning;
    private String remark;
    
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;
    
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;
    
    @TableLogic
    private Integer deleted;
}
