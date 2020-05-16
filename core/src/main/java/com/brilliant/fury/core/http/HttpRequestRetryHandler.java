package com.brilliant.fury.core.http;

import java.io.IOException;
import java.io.InterruptedIOException;
import java.net.UnknownHostException;
import javax.net.ssl.SSLException;
import javax.net.ssl.SSLHandshakeException;
import org.apache.http.HttpEntityEnclosingRequest;
import org.apache.http.HttpRequest;
import org.apache.http.NoHttpResponseException;
import org.apache.http.RequestLine;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.protocol.HttpContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Http客户端的重试策略
 *
 * @author fury.
 * version 2017/10/2.
 */
public class HttpRequestRetryHandler implements org.apache.http.client.HttpRequestRetryHandler {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    //设置的重试总次数
    private int retryCount;

    public HttpRequestRetryHandler() {
    }

    public HttpRequestRetryHandler(int retryCount) {
        this.retryCount = retryCount;
    }

    @Override
    public boolean retryRequest(IOException exception, int executionCount, HttpContext context) {
        // 超过重试次数，就放弃
        if (executionCount > retryCount) {
            RequestLine requestLine = null;
            if (context instanceof HttpClientContext) {
                requestLine = ((HttpClientContext) context).getRequest().getRequestLine();
            }

            logger.info("[ARCH_COMMON_exceed_retryCount]超出最大重试次数！！！requestLine={},retryCount={}",
                requestLine, retryCount);
            return false;
        }
        // 服务器丢掉了连接，重试
        if (exception instanceof NoHttpResponseException) {
            return true;
        }
        // SSL握手异常，不重试
        if (exception instanceof SSLHandshakeException) {
            return false;
        }
        // 超时，重试
        if (exception instanceof InterruptedIOException) {
            return true;
        }
        // 目标服务器不可达，不重试
        if (exception instanceof UnknownHostException) {
            return false;
        }
        // 连接被拒绝，不重试
        if (exception instanceof ConnectTimeoutException) {
            return false;
        }
        // ssl握手异常，不重试
        if (exception instanceof SSLException) {
            return false;
        }
        HttpClientContext clientContext = HttpClientContext.adapt(context);
        HttpRequest request = clientContext.getRequest();
        // 如果请求是幂等的，就再次尝试
        if (!(request instanceof HttpEntityEnclosingRequest)) {
            return true;
        }

        return false;
    }
}
