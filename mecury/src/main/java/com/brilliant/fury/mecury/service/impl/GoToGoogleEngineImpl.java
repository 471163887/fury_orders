package com.brilliant.fury.mecury.service.impl;

import static com.brilliant.fury.mecury.util.Constants.MERCURY_START;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.brilliant.fury.core.exception.DuplicatePayException;
import com.brilliant.fury.core.exception.OrderNoExistException;
import com.brilliant.fury.core.model.po.BizAuth;
import com.brilliant.fury.core.model.po.OrderDetail;
import com.brilliant.fury.core.model.po.OrderInfo;
import com.brilliant.fury.core.model.po.PayDetail;
import com.brilliant.fury.core.model.req.OrderDto;
import com.brilliant.fury.core.util.CollectionUtil;
import com.brilliant.fury.core.util.TimeUtil;
import com.brilliant.fury.mecury.model.enums.PayStatus;
import com.brilliant.fury.mecury.service.OrderApiService;
import com.brilliant.fury.mecury.util.TimeSequenceUtil;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import javax.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpException;
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
    @Resource
    private PayDetailServiceImpl payDetailService;

    @Resource
    private PayServiceImpl payService;

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

    /**
     * 创建支付连接前需要校验 1.订单是否已支付. 2.这个订单号是否创建过支付链接(mammon不允许相同的订单号支付).
     */
    @Override
    public String genPayUrl(String orderNo) throws OrderNoExistException, InterruptedException,
        HttpException, DuplicatePayException {

        QueryWrapper<OrderDetail> detailWrapper = new QueryWrapper<>();
        detailWrapper.eq("order_no", orderNo);
        OrderDetail orderDetail = orderDetailService.getOne(detailWrapper);
        if (null == orderDetail) {
            throw new OrderNoExistException("OrderNo no exist");
        }
        QueryWrapper<PayDetail> payWrapper = new QueryWrapper<>();
        payWrapper.eq("order_no", orderNo);
        List<PayDetail> payDetailList = payDetailService.list(payWrapper);
        if (CollectionUtil.isEmpty(payDetailList)) {
            return genFirstPayUrl(orderDetail);
        }
        for (PayDetail current : payDetailList) {
            if (PayStatus.codeOf(current.getPayStatus()).equals(PayStatus.PAID)) {
                throw new DuplicatePayException("订单已支付");
            }
        }
        return genFirstPayUrl(orderDetail);
    }

    private String genFirstPayUrl(OrderDetail orderDetail) throws InterruptedException,
        HttpException {
        BigDecimal payAmount = orderDetail.getTotal().subtract(orderDetail.getFavourableTotal());
        String payId = TimeSequenceUtil.genPayId(MERCURY_START);
        PayDetail payDetail = PayDetail.builder()
            .payTotal(payAmount)
            .payId(payId)
            .payStatus((byte) PayStatus.WAIT_PAY.ordinal())
            .createAt(TimeUtil.nowDate())
            .build();
        boolean save = payDetailService.save(payDetail);
        if (save) {
            return payService.genPayUrl(payAmount, payId, "");
        } else {
            throw  new RuntimeException("生成支付订单失败");
        }

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
