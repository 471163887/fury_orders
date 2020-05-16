package com.brilliant.fury.mecury.service.impl;

import com.brilliant.fury.core.model.req.BuyerAddr;
import com.brilliant.fury.core.model.req.OrderDetail;
import com.brilliant.fury.core.model.req.OrderDto;
import com.brilliant.fury.core.util.TimeUtil;
import com.brilliant.fury.mecury.model.po.BizAuth;
import com.brilliant.fury.mecury.service.OrderApiService;
import java.util.Date;
import javax.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author by fury.
 * version 2020/5/16.
 */
@Slf4j
@Service
public class GoToGoogleEngineImpl implements OrderApiService {

    @Resource
    private OrderNoPoolImpl orderNoPool;
    @Resource
    private OrderInfoServiceImpl orderInfoService;
    @Resource
    private OrderDetailServiceImpl orderDetailService;
    @Resource
    private BuyerAddrServiceImpl buyerAddrService;

    @Override
    public String newOrder(OrderDto orderDto, BizAuth bizAuth) {
        String orderNo = orderNoPool.getOrderNo();
        paddingOrderNo(orderDto, orderNo, bizAuth);
        try {
            saveOrder(orderDto);
            return orderNo;
        } catch (Throwable t) {
            log.error("[FURY_MECURY_GoToGoogleEngine_newOrder_errmsg]={}", t.getMessage(), t);
            throw t;
        }
    }

    @Transactional(rollbackFor = Exception.class)
    public void saveOrder(OrderDto orderDto) {

        OrderDetail orderDetail = orderDto.getOrderDetail();
        String orderNo = orderDetail.getOrderNo();
        // 订单初始状态
        orderInfoService.createByOrderNo(orderNo);
        // 订单详情
        com.brilliant.fury.mecury.model.po.OrderDetail detail = new com.brilliant.fury
            .mecury.model.po.OrderDetail();
        BeanUtils.copyProperties(orderDetail, detail);
        orderDetailService.save(detail);
        // 买家信息
        BuyerAddr buyerAddr = orderDto.getBuyerAddr();
        if (null != buyerAddr) {
            com.brilliant.fury.mecury.model.po.BuyerAddr buyerAddrPo = new com.brilliant.fury
                .mecury.model.po.BuyerAddr();
            BeanUtils.copyProperties(buyerAddr, buyerAddrPo);
            buyerAddrService.save(buyerAddrPo);
        }
    }

    @Override
    public Object queryOrder(String orderNo) {
        return null;
    }

    private void paddingOrderNo(OrderDto orderDto, String orderNo, BizAuth bizAuth) {
        Date nowDate = TimeUtil.nowDate();

        OrderDetail orderDetail = orderDto.getOrderDetail();
        orderDetail.setOrderNo(orderNo);
        orderDetail.setCreateAt(nowDate);
        orderDetail.setBizId(bizAuth.getId());

        BuyerAddr buyerAddr = orderDto.getBuyerAddr();
        if (null != buyerAddr) {
            buyerAddr.setCreateAt(nowDate);
        }
    }
}
