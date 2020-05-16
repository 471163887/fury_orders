package com.brilliant.fury;

import com.brilliant.fury.client.MecuryClient;
import com.brilliant.fury.core.model.req.OrderDetail;
import com.brilliant.fury.core.model.req.OrderDto;
import java.math.BigDecimal;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AppTest {

    private static final Logger log = LoggerFactory.getLogger(AppTest.class);

    @Test
    public void MecuryClientTest() {
        MecuryClient.init("aa");
        OrderDetail orderDetail = new OrderDetail();
        orderDetail.setTotal(new BigDecimal(1.0));
        OrderDto orderDto = OrderDto.builder()
            .orderDetail(orderDetail)
            .build();
        try {
            String order = MecuryClient.createOrder(orderDto);
            log.info("[MecuryClient.createOrder]resp={}", order);
        } catch (Throwable t) {
            log.error("[errMsg]={}", t.getMessage(), t);
        }
    }



}
