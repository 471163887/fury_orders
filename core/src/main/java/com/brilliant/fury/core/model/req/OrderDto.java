package com.brilliant.fury.core.model.req;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author by fury.
 * version 2020/5/15.
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OrderDto {

    /**
     * 订单详情，总金额必须设置。
     */
    public OrderDetail orderDetail;

    /**
     * 买家地址，可以为空。
     */
    public BuyerAddr buyerAddr;

}
