package com.brilliant.fury.core.http;

import java.util.Map;

/**
 * Http请求的参数
 *
 * @author fury.
 * version 2017/10/2.
 */
public class HttpRequest {
    private String url;
    private Map<String, String> params;
    /**
     * 对于某些请求，传递的参数并非K=V的map。
     */
    private Object param;
    private Map<String, String> headers;

    public String getUrl() {
        return url;
    }

    public HttpRequest setUrl(String url) {
        this.url = url;
        return this;
    }

    public Map<String, String> getParams() {
        return params;
    }

    public HttpRequest setParams(Map<String, String> params) {
        this.params = params;
        return this;
    }

    public Object getParam() {
        return param;
    }

    public void setParam(Object param) {
        this.param = param;
    }

    public Map<String, String> getHeaders() {
        return headers;
    }

    public HttpRequest setHeaders(Map<String, String> headers) {
        this.headers = headers;
        return this;
    }

    @Override
    public String toString() {
        return "HttpRequest{" +
            "url='" + url + '\'' +
            ", params=" + params +
            ", headers=" + headers +
            '}';
    }
}
