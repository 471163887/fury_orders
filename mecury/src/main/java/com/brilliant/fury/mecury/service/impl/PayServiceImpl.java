package com.brilliant.fury.mecury.service.impl;

import static com.brilliant.fury.core.util.GuavaUtil.EMPTY_STRING;

import com.brilliant.fury.core.http.HttpClient;
import com.google.common.collect.Maps;
import java.math.BigDecimal;
import java.util.Map;
import org.apache.http.HttpException;
import org.springframework.stereotype.Service;

/**
 * @author by fury. version 2017/11/13.
 */
@Service
public class PayServiceImpl {

  private static String MARS_HOST = "localhost:9103/";
  private static String CREATE_PAY_URI = "create/pay";
  private static String CREATE_PAY_URL = MARS_HOST + CREATE_PAY_URI;


  public String genPayUrl(BigDecimal amount, String orderId,
      String desc) throws HttpException {
    /*Map<String, String> params = Maps.newHashMap();
    params.put("amount", amount.toString());
    params.put("product_detail", desc);
    params.put("order_id", orderId);
    params.put("sign", AuthUtil.genMd5Sign(params, appSecret));
    String result = HttpClient.post(CREATE_PAY_URL);
    return StringUtil.isNullOrEmpty(result) ? EMPTY_STRING : MARS_HOST + result;*/
    return "";
  }

}
