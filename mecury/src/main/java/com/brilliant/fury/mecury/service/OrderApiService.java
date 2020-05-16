package com.brilliant.fury.mecury.service;

import com.brilliant.fury.core.model.req.OrderDto;
import com.brilliant.fury.mecury.model.po.BizAuth;

/**
 * @author by fury.
 * version 2017/11/8.
 */
public interface OrderApiService {

  /**
   * 创建订单
   * @return 订单号
   */
  String newOrder(OrderDto orderDto, BizAuth bizAuth);

  /**
   * 查询订单
   * @return 订单Json对象
   */
  Object queryOrder(String orderNo);

}
