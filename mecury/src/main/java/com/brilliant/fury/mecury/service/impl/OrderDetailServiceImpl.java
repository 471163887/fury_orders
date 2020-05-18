package com.brilliant.fury.mecury.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.brilliant.fury.core.model.po.OrderInfo;
import com.brilliant.fury.mecury.mapper.OrderDetailMapper;
import com.brilliant.fury.core.model.po.OrderDetail;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * @author by fury.
 * version 2020/5/16.
 */
@Slf4j
@Service
public class OrderDetailServiceImpl extends ServiceImpl<OrderDetailMapper,
    OrderDetail> implements IService<OrderDetail> {

    public void delByOrderNo(String orderNo) {
        QueryWrapper<OrderDetail> detailWrapper = new QueryWrapper<>();
        detailWrapper.eq("order_no", orderNo);
        try {
            remove(detailWrapper);
        } catch (Throwable t) {
            log.error("[FURY_MECURY_OrderDetailService_delByOrderNo_error]={}", t.getMessage(), t);
            throw t;
        }
    }

}
