package com.jewelry.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jewelry.entity.Designer;
import com.jewelry.mapper.DesignerMapper;
import com.jewelry.service.DesignerService;
import org.springframework.stereotype.Service;

@Service
public class DesignerServiceImpl extends ServiceImpl<DesignerMapper, Designer> implements DesignerService {
}
