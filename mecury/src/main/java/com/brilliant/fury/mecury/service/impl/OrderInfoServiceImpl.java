package com.brilliant.fury.mecury.service.impl;

import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.brilliant.fury.core.util.TimeUtil;
import com.brilliant.fury.mecury.mapper.OrderInfoMapper;
import com.brilliant.fury.mecury.model.po.OrderInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * @author by fury.
 * version 2020/5/16.
 */
@Slf4j
@Service
public class OrderInfoServiceImpl extends ServiceImpl<OrderInfoMapper,
    OrderInfo> implements IService<OrderInfo> {

    public boolean createByOrderNo(String orderNode) {
        OrderInfo orderInfo = new OrderInfo();
        orderInfo.setOrderNo(orderNode);
        orderInfo.setCancelledAt(TimeUtil.nowDate());
        return save(orderInfo);
    }

}
