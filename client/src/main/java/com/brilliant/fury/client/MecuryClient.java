package com.brilliant.fury.client;

import com.brilliant.fury.core.base.JsonUtil;
import com.brilliant.fury.core.http.HttpClient;
import com.brilliant.fury.core.http.HttpResponse;
import com.brilliant.fury.core.model.po.OrderDetail;
import com.brilliant.fury.core.model.req.OrderDto;
import com.google.common.base.Preconditions;
import java.util.HashMap;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author by fury.
 * version 2020/5/14.
 */
public class MecuryClient {

    private static final Logger log = LoggerFactory.getLogger(MecuryClient.class);

    private static final String MECURY_URL = "http://localhost:9102/api/";
    private static final String ORDER_CREATE = "order/create";
    private static final String ORDER_QUERY = "order/query";
    private static final String ORDER_UPDATE = "order/update";
    private static final String ORDER_DEL = "order/del";

    private static String MECURY_TOKEN;
    private static Map<String, String> HEADERS = new HashMap<>();

    /**
     * 在调用Mecury前需要先初始化系统参数
     *
     * @param mecuryToken 注册时生成的唯一授权码
     */
    public static void init(String mecuryToken) {
        MECURY_TOKEN = mecuryToken;
        HEADERS.put("Authorization", MECURY_TOKEN);
    }

    /**
     * 创建订单
     */
    public static String createOrder(OrderDto orderDto) throws Exception {
        OrderDetail orderDetailReq = orderDto.orderDetail;
        Preconditions.checkArgument(null != orderDetailReq, "订单详情不能为空");
        String json = JsonUtil.toJson(orderDto);
        log.info("[MecuryClient_createOrder_req]={}", json);
        HttpResponse resp = HttpClient.postJson(MECURY_URL + ORDER_CREATE, json, HEADERS);
        log.info("[MecuryClient_createOrder_resp]={}", resp.toString());
        return resp.getResult();
    }

    /**
     * 查询订单
     */
    public static String queryOrder(String orderNo) throws Exception {
        HashMap<String, String> params = new HashMap<>(1);
        params.put("order_no", orderNo);
        String resp = HttpClient.get(MECURY_URL + ORDER_QUERY, HEADERS, params);
        log.info("[MecuryClient_queryOrder_resp]={}", resp);
        return resp;
    }

    /**
     * 修改订单
     */
    public static String updateOrder(OrderDto orderDto) throws Exception {
        String json = JsonUtil.toJson(orderDto);
        log.info("[MecuryClient_updateOrder_req]={}", json);
        HttpResponse resp = HttpClient.postJson(MECURY_URL + ORDER_UPDATE, json, HEADERS);
        log.info("[MecuryClient_updateOrder_resp]={}", resp.toString());
        return resp.getResult();
    }

    /**
     * 删除订单
     */
    public static String delOrder(String orderNo) throws Exception {
        HttpResponse httpResponse = HttpClient.postJson(
            MECURY_URL + ORDER_DEL + "?order_no=" + orderNo, "", HEADERS);
        String result = httpResponse.getResult();
        log.info("[MecuryClient_queryOrder_resp]={}", result);
        return result;
    }

}
