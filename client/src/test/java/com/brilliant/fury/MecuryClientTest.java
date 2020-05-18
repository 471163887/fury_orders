package com.brilliant.fury;

import com.alibaba.fastjson.JSONObject;
import com.brilliant.fury.client.MecuryClient;
import com.brilliant.fury.core.model.po.OrderDetail;
import com.brilliant.fury.core.model.req.OrderDto;
import java.math.BigDecimal;
import java.util.UUID;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MecuryClientTest {

    private static final Logger log = LoggerFactory.getLogger(MecuryClientTest.class);

    static {
        MecuryClient.init("3f9f0876f3b14fec8b75479e0793442b");
    }

    @Test
    public void createTest() {
        OrderDetail orderDetailReq = new OrderDetail();
        orderDetailReq.setTotal(new BigDecimal(0.01));
        OrderDto orderDto = OrderDto.builder()
            .orderDetail(orderDetailReq)
            .build();
        try {
            String order = MecuryClient.createOrder(orderDto);
            log.info("[MecuryClient.createOrder]resp={}", order);
        } catch (Throwable t) {
            log.error("[errMsg]={}", t.getMessage(), t);
        }
    }

    @Test
    public void queryTest() {
        try {
            MecuryClient.queryOrder("447087744104140800");
        } catch (Throwable t) {
            log.error("[errMsg]={}", t.getMessage(), t);
        }
    }

    @Test
    public void updateTest() {
        try {
            String orderJson = MecuryClient.queryOrder("447087744104140800");
            JSONObject parse = (JSONObject)JSONObject.parse(orderJson);
            OrderDto orderDto = parse.getObject("resp", OrderDto.class);
            orderDto.orderDetail.setIsVirtual(true);
            orderDto.orderDetail.setRemark("im remark");
            MecuryClient.updateOrder(orderDto);
        } catch (Throwable t) {
            log.error("[errMsg]={}", t.getMessage(), t);
        }
    }

    @Test
    public void delTest() {
        try {
            MecuryClient.delOrder("447087744104140800");
        } catch (Throwable t) {
            log.error("[errMsg]={}", t.getMessage(), t);
        }
    }

    @Test
    public void getUUID() {
        UUID uuid = UUID.randomUUID();
        String cleanUUID = uuid.toString().replace("-", "");
        log.info("[uuid]={}", cleanUUID);
    }

}
