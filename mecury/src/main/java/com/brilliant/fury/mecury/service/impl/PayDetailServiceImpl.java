package com.brilliant.fury.mecury.service.impl;

import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.brilliant.fury.core.model.po.PayDetail;
import com.brilliant.fury.mecury.mapper.PayDetailMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * @author by fury.
 * version 2020/5/24.
 */
@Slf4j
@Service
public class PayDetailServiceImpl extends ServiceImpl<PayDetailMapper,
    PayDetail> implements IService<PayDetail> {

}
