package com.brilliant.fury.client;

import com.brilliant.fury.core.base.JsonUtil;
import com.brilliant.fury.core.http.HttpClient;
import com.brilliant.fury.core.http.HttpResponse;
import com.brilliant.fury.core.model.req.OrderDto;
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

    private static final String MECURY_URL = "http://localhost:9102/";
    private static String MECURY_TOKEN;

    /**
     * 在调用Mecury前需要先初始化系统参数
     *
     * @param mecuryToken 注册时生成的唯一授权码
     */
    public static void init(String mecuryToken) {
        MECURY_TOKEN = mecuryToken;
    }

    public static String createOrder(OrderDto orderDto) throws Exception {
        Map<String, String> headers = new HashMap<>();
        headers.put("Authorization", MECURY_TOKEN);
        String json = JsonUtil.toJson(orderDto);
        log.info("req:{}", json);
        HttpResponse resp = HttpClient.postJson(MECURY_URL + "api/order/create", json, headers);
        log.info("resp:{}", resp.toString());
        return resp.getResult();
    }

}
