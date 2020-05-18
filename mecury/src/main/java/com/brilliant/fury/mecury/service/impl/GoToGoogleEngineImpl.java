package com.brilliant.fury.mecury.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.brilliant.fury.core.model.po.BizAuth;
import com.brilliant.fury.core.model.po.BuyerAddr;
import com.brilliant.fury.core.model.po.OrderDetail;
import com.brilliant.fury.core.model.po.OrderInfo;
import com.brilliant.fury.core.model.req.OrderDto;
import com.brilliant.fury.core.util.TimeUtil;
import com.brilliant.fury.mecury.service.OrderApiService;
import java.util.Date;
import javax.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
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
        OrderDetail orderDetail = orderDto.orderDetail;
        String orderNo = orderDetail.getOrderNo();
        // 订单初始状态
        orderInfoService.createByOrderNo(orderNo);
        // 订单详情
        orderDetailService.save(orderDetail);
        // 买家信息
        if (null != orderDto.buyerAddr) {
            buyerAddrService.save(orderDto.buyerAddr);
        }
    }

    @Override
    public Object queryOrder(String orderNo) {
        QueryWrapper<OrderDetail> detailWrapper = new QueryWrapper<>();
        detailWrapper.eq("order_no", orderNo);
        OrderDetail orderDetail = orderDetailService.getOne(detailWrapper);

        QueryWrapper<OrderInfo> infoWrapper = new QueryWrapper<>();
        infoWrapper.eq("order_no", orderNo);
        OrderInfo orderInfo = orderInfoService.getOne(infoWrapper);

        OrderDto orderDto = new OrderDto();
        orderDto.orderDetail = orderDetail;
        orderDto.orderInfo = orderInfo;
        return orderDto;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public String updateOrder(OrderDto orderDto, BizAuth bizAuth) {
        if (null != orderDto.orderInfo) {
            orderInfoService.updateById(orderDto.orderInfo);
        }
        // 订单详情
        if (null != orderDto.orderDetail) {
            orderDetailService.updateById(orderDto.orderDetail);
        }
        // 买家信息
        if (null != orderDto.buyerAddr) {
            buyerAddrService.updateById(orderDto.buyerAddr);
        }
        return "ok";
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public String delOrder(String orderNo) {
        orderInfoService.delByOrderNo(orderNo);
        orderDetailService.delByOrderNo(orderNo);
        return "ok";
    }

    private void paddingOrderNo(OrderDto orderDto, String orderNo, BizAuth bizAuth) {
        Date nowDate = TimeUtil.nowDate();

        OrderDetail orderDetail = orderDto.getOrderDetail();
        orderDetail.setOrderNo(orderNo);
        orderDetail.setCreateAt(nowDate);
        orderDetail.setBizId(bizAuth.getId());

        if (null != orderDto.buyerAddr) {
            orderDto.buyerAddr.setCreateAt(nowDate);
        }
    }
}
