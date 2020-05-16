package com.brilliant.fury.mecury.controller.api;

import static com.google.common.base.Preconditions.checkArgument;

import com.brilliant.fury.core.base.BaseController;
import com.brilliant.fury.core.model.req.OrderDto;
import com.brilliant.fury.mecury.config.AuthInterceptor;
import javax.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpException;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author by fury. version 2017/11/7.
 */
@Slf4j
@RestController("/api/order")
public class BizApi extends BaseController {

  @Resource
  private OrderApiService orderApiService;

  /**
   * 创建订单.
   *
   * @return OrderNo.
   */
  @RequestMapping(value = "", method = RequestMethod.POST)
  public Object createOrder(@RequestBody OrderDto orderRequest) {
    Long bizId = AuthInterceptor.getBizId();
    log.info("[FURY_MECURY_BizApi_createOrder_bizId]={}", bizId);
    String orderNo = orderApiService.newOrder(request);
    log.info("[FURY_MECURY_BizApi_createOrder_gen_orderNo]={}", orderNo);
    return dataJson(orderNo);
  }

  /**
   * 查询订单.
   *
   * @return OrderNo.
   */
  @RequestMapping(value = "", method = RequestMethod.GET)
  public Object queryOrder(@RequestParam("orderNo") String orderNo) throws Exception {
    LOG.info("welcome to queryOrder interface. orderNo is:%s", orderNo);
    checkArgument(StringUtil.notNullOrEmpty(orderNo), "订单号不能为空.");
    OrderView orderView = orderApiService.queryOrder(orderNo);
    LOG.info("gen orderView:%s", orderView.toString());
    return dataJson(orderView);
  }

  /**
   * 生成支付链接.
   *
   * @return 支付链接.
   */
  @RequestMapping(value = "/payurl", method = RequestMethod.GET)
  public Object genOrderPayUrl(@RequestParam("orderNo") String orderNo) throws HttpException {
    LOG.info("welcome to genOrderPayUrl interface. orderNo:%s", orderNo);
    try {
      String payUrl = orderApiService.genPayUrl(orderNo);
      LOG.info("gen payUrl:%s", payUrl);
      return dataJson(payUrl);
    } catch (NoExistException e) {
      LOG.warn("genOrderPayUrl err msg:%s", e.getMessage(), e);
      return errorJson(e.getMessage());
    } catch (InterruptedException e) {
      LOG.warn("genOrderPayUrl err msg:%s", e.getMessage(), e);
      return errorJson("生成支付id错误, 请稍后重试");
    } catch (DuplicatePayException e) {
      LOG.warn("genOrderPayUrl err msg:%s", e.getMessage(), e);
      return errorJson("订单重复支付.");
    }
  }

}
