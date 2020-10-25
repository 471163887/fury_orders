package com.brilliant.fury.mecury.service;

import com.brilliant.fury.core.exception.DuplicatePayException;
import com.brilliant.fury.core.exception.OrderNoExistException;
import com.brilliant.fury.core.model.req.OrderDto;
import com.brilliant.fury.core.model.po.BizAuth;
import org.apache.http.HttpException;

/**
 * @author by fury.
 * version 2017/11/8.
 */
public interface OrderApiService {

    /**
     * 创建订单
     *
     * @return 订单号
     */
    String newOrder(OrderDto orderDto, BizAuth bizAuth);

    /**
     * 查询订单
     *
     * @return 订单Json对象
     */
    Object queryOrder(String orderNo);

    /**
     * 更新订单
     */
    String updateOrder(OrderDto orderRequest, BizAuth bizAuth);

    /**
     * 删除订单
     */
    String delOrder(String orderNo);

    /**
     * 生成支付链接
     */
    String genPayUrl(String orderNo) throws OrderNoExistException, InterruptedException,
        HttpException, DuplicatePayException;
}
