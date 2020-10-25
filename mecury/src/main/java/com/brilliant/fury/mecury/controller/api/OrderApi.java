package com.brilliant.fury.mecury.controller.api;

import com.brilliant.fury.core.base.BaseController;
import com.brilliant.fury.core.exception.DuplicatePayException;
import com.brilliant.fury.core.exception.OrderNoExistException;
import com.brilliant.fury.core.model.req.OrderDto;
import com.brilliant.fury.mecury.config.AuthInterceptor;
import com.brilliant.fury.mecury.engine.OrderApiEngine;
import com.brilliant.fury.core.model.po.BizAuth;
import com.brilliant.fury.mecury.service.OrderApiService;
import com.google.common.base.Preconditions;
import java.math.BigDecimal;
import javax.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.Mapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author by fury.
 * version 2017/11/7.
 */
@Slf4j
@RestController
@RequestMapping("/api/order")
public class OrderApi extends BaseController {

    @Resource
    private OrderApiEngine orderApiEngine;

    /**
     * 创建订单.
     *
     * @return 订单号.
     */
    @PostMapping(value = "/create")
    public Object createOrder(@RequestBody OrderDto orderRequest) {
        BigDecimal total = orderRequest.orderDetail.getTotal();
        Preconditions.checkArgument(null != total && total.compareTo(BigDecimal.ZERO) > 0,
            "订单总金额不能为空，并且应大于0");
        log.info("[FURY_MECURY_BizApi_createOrder_bizId]={}", orderRequest);
        BizAuth bizAuth = AuthInterceptor.getBizAuth();
        OrderApiService orderApiService = orderApiEngine.getApiByEngine(bizAuth.getWorkflowEngine());
        String orderNo = orderApiService.newOrder(orderRequest, bizAuth);
        log.info("[FURY_MECURY_BizApi_createOrder_gen_orderNo]={}", orderNo);
        return dataJson(orderNo);
    }

    /**
     * 查询订单.
     *
     * @return 订单 JSON 对象.
     */
    @GetMapping(value = "/query")
    public Object queryOrder(@RequestParam("order_no") String orderNo) throws Exception {

        log.info("[FURY_MECURY_BizApi_queryOrder_orderNo]={}", orderNo);
        BizAuth bizAuth = AuthInterceptor.getBizAuth();
        OrderApiService orderApiService = orderApiEngine.getApiByEngine(bizAuth.getWorkflowEngine());

        Object order = orderApiService.queryOrder(orderNo);
        log.info("[FURY_MECURY_BizApi_queryOrder_resp]={}", order);
        return dataJson(order);
    }

    /**
     * 修改订单.
     */
    @PostMapping(value = "/update")
    public Object updateOrder(@RequestBody OrderDto orderRequest) throws Exception {
        BigDecimal total = orderRequest.orderDetail.getTotal();
        Preconditions.checkArgument(null != total && total.compareTo(BigDecimal.ZERO) > 0,
            "订单总金额不能为空，并且应大于0");
        log.info("[FURY_MECURY_BizApi_updateOrder_orderDto]={}", orderRequest);
        BizAuth bizAuth = AuthInterceptor.getBizAuth();
        OrderApiService orderApiService = orderApiEngine.getApiByEngine(bizAuth.getWorkflowEngine());
        try {
            String updateRet = orderApiService.updateOrder(orderRequest, bizAuth);
            log.info("[FURY_MECURY_BizApi_updateOrder_updateRet]={}", updateRet);
            return dataJson(updateRet);
        } catch (Throwable t) {
            String message = t.getMessage();
            log.error("[MECURY_updateOrder_error]={}", message, t);
            return errorJson(message);
        }
    }

    /**
     * 删除订单.
     */
    @PostMapping(value = "/del")
    public Object delOrder(@RequestParam("order_no") String orderNo) throws Exception {
        log.info("[FURY_MECURY_BizApi_delOrder_orderNo]={}", orderNo);
        BizAuth bizAuth = AuthInterceptor.getBizAuth();
        OrderApiService orderApiService = orderApiEngine.getApiByEngine(bizAuth.getWorkflowEngine());
        try {
            String delRet = orderApiService.delOrder(orderNo);
            log.info("[FURY_MECURY_BizApi_delOrder_resp]={}", delRet);
            return dataJson(delRet);
        } catch (Throwable t) {
            String message = t.getMessage();
            log.error("[MECURY_delOrder_error]={}", message, t);
            return errorJson(message);
        }

    }

    /**
     * 生成支付链接.
     *
     * @return 支付链接.
     */
    @RequestMapping(value = "/payurl", method = RequestMethod.GET)
    public Object genOrderPayUrl(@RequestParam("orderNo") String orderNo) throws HttpException {
        log.info("[FURY_MECURY_genOrderPayUrl_req]orderNo={}", orderNo);
        BizAuth bizAuth = AuthInterceptor.getBizAuth();
        OrderApiService orderApiService = orderApiEngine.getApiByEngine(bizAuth.getWorkflowEngine());
        try {
            String payUrl = orderApiService.genPayUrl(orderNo);
            log.info("[FURY_MECURY_genOrderPayUrl_resp]payUrl={}", payUrl);
            return dataJson(payUrl);
        } catch (OrderNoExistException e) {
            log.warn("genOrderPayUrl err msg:%s", e.getMessage(), e);
            return errorJson(e.getMessage());
        } catch (InterruptedException e) {
            log.warn("genOrderPayUrl err msg:%s", e.getMessage(), e);
            return errorJson("生成支付id错误, 请稍后重试");
        } catch (DuplicatePayException e) {
            log.warn("genOrderPayUrl err msg:%s", e.getMessage(), e);
            return errorJson("订单重复支付.");
        }
    }

}
