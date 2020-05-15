package com.brilliant.fury.core.http;

import java.util.Map;

/**
 * Http请求的返回结果
 *
 * @author fury.
 * version 2017/10/2.
 */
public class HttpResponse {

    private static final int PARAM_ERROR_CODE = -1;

    /**
     * 参数错误
     */
    public static final HttpResponse HTTP_RESPONSE_PARAM_ERROR =
        new HttpResponse().setStatusCode(PARAM_ERROR_CODE);

    /**
     * http状态码
     */
    private int statusCode;

    /**
     * 返回结果
     */
    private String result;

    /**
     * 返回的header
     */
    private Map<String, String> headers;

    public int getStatusCode() {
        return statusCode;
    }

    public HttpResponse setStatusCode(int statusCode) {
        this.statusCode = statusCode;
        return this;
    }

    public String getResult() {
        return result;
    }

    public HttpResponse setResult(String result) {
        this.result = result;
        return this;
    }

    public Map<String, String> getHeaders() {
        return headers;
    }

    public HttpResponse setHeaders(Map<String, String> headers) {
        this.headers = headers;
        return this;
    }

    @Override
    public String toString() {
        return "HttpResponse{" +
            "statusCode=" + statusCode +
            ", result='" + result + '\'' +
            ", headers=" + headers +
            '}';
    }
}
