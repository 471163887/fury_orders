package com.brilliant.fury.core.http;

/**
 * Http请求的配置选项
 *
 * @author fury.
 * version 2017/10/2.
 */
public class HttpOptions {

    public static final HttpOptions DEFAULT_HTTP_OPTION = new HttpOptions();

    /**
     * 重试次数(不包括第一次)，默认null表示不重试。
     */
    private Integer retryCount;
    /**
     * 建立连接的超时时间，毫秒。
     */
    private Integer connectTimeoutMs;
    /**
     * 每次请求的超时时间，毫秒。
     */
    private Integer timeoutMs;

    public Integer getRetryCount() {
        return retryCount;
    }

    public HttpOptions setRetryCount(Integer retryCount) {
        this.retryCount = retryCount;
        return this;
    }

    public Integer getConnectTimeoutMs() {
        return connectTimeoutMs;
    }

    public HttpOptions setConnectTimeoutMs(Integer connectTimeoutMs) {
        this.connectTimeoutMs = connectTimeoutMs;
        return this;
    }

    public Integer getTimeoutMs() {
        return timeoutMs;
    }

    public HttpOptions setTimeoutMs(Integer timeoutMs) {
        this.timeoutMs = timeoutMs;
        return this;
    }

    @Override
    public String toString() {
        return "HttpOptions{" +
            "retryCount=" + retryCount +
            ", connectTimeoutMs=" + connectTimeoutMs +
            ", timeoutMs=" + timeoutMs +
            '}';
    }
}
