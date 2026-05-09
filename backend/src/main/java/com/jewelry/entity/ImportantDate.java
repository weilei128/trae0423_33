package com.jewelry.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@TableName("important_date")
public class ImportantDate {
    @TableId(type = IdType.AUTO)
    private Long id;
    
    private Long customerId;
    private String dateType;
    private LocalDate dateDate;
    private Integer reminderDays;
    private String description;
    
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;
    
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;
    
    @TableLogic
    private Integer deleted;
}
