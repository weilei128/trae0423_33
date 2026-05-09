package com.jewelry.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.jewelry.entity.ProductionProcess;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface ProductionProcessMapper extends BaseMapper<ProductionProcess> {
    default List<ProductionProcess> selectByTrackingId(Long trackingId) {
        return selectList(new LambdaQueryWrapper<ProductionProcess>()
                .eq(ProductionProcess::getTrackingId, trackingId)
                .orderByAsc(ProductionProcess::getProcessOrder));
    }
}
