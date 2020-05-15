package com.brilliant.fury.core.http;


import static com.brilliant.fury.core.http.HttpOptions.DEFAULT_HTTP_OPTION;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import javax.net.ssl.SSLContext;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.ContentBody;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 带有连接池的Http客户端工具类。具有如下特点：
 * 基于apache的高性能Http客户端
 * 连接池的最大连接数默认是20，可通过{@link #init(int, int)}、或者系统变量-Dfury.common.http.max.total=200指定；
 * 连接池的每个路由的最大连接数默认是2，可通过{@link #init(int, int)}、或者系统变量-Dfury.common.http.max.per.route=10指定；
 * 可设置超时，重试次数，通过{@link HttpOptions}进行设置；
 *
 * @author fury.
 * version 2017/10/2.
 */
public class HttpClient {

    private static final Logger logger = LoggerFactory.getLogger(HttpClient.class);

    /**
     * 系统变量，控制HttpClientUtil中连接池的最大连接数
     */
    public static final String SYSTEM_PROPERTY_KEY_HTTP_MAX_TOTAL = "fury.common.http.max.total";

    /**
     * 系统变量，控制HttpClientUtil中连接池的每个路由的最大连接数
     */
    public static final String SYSTEM_PROPERTY_KEY_HTTP_MAX_PER_ROUTE =
        "fury.common.http.max.per.route";

    /**
     * HttpClient 连接池
     */
    private static AbstractLazyInitializer<PoolingHttpClientConnectionManager>
        LAZY_CONNECTION_MANAGER =
        new AbstractLazyInitializer<PoolingHttpClientConnectionManager>() {
            @Override
            protected PoolingHttpClientConnectionManager initialize() {
                return buildPoolingHttpClientConnectionManager(null, null);
            }
        };

    /**
     * @param maxTotal    连接池的最大连接数,默认为20。
     * @param maxPerRoute 连接池的每个路由的最大连接数，默认为2。
     */
    public static void init(int maxTotal, int maxPerRoute) {
        LAZY_CONNECTION_MANAGER = new AbstractLazyInitializer<PoolingHttpClientConnectionManager>
            () {
            @Override
            protected PoolingHttpClientConnectionManager initialize() {
                return buildPoolingHttpClientConnectionManager(maxTotal, maxPerRoute);
            }
        };
    }

    public static String get(String url) throws Exception {
        return httpGet(url, null, null, null);
    }

    public static String get(String url, HttpOptions httpOptions) throws Exception {
        return httpGet(url, null, null, httpOptions);
    }

    public static String get(String url, Map<String, String> params) throws Exception {
        return httpGet(url, null, params, null);
    }

    public static String get(String url, Map<String, String> params,
        HttpOptions httpOptions) throws Exception {
        return httpGet(url, null, params, httpOptions);
    }

    public static String get(String url, Map<String, String> headers,
        Map<String, String> params) throws Exception {
        return httpGet(url, headers, params, null);
    }

    public static String httpGet(String url, Map<String, String> headers,
        Map<String, String> params, HttpOptions httpOptions) throws Exception {
        HttpRequest httpRequest = new HttpRequest().setUrl(url).setHeaders(headers)
            .setParams(params);

        return httpGet(httpRequest, httpOptions).getResult();
    }

    public static HttpResponse get(HttpRequest httpRequest) throws Exception {
        return httpGet(httpRequest, null);
    }

    /**
     * 发送 HTTP GET请求
     *
     * @param httpRequest 请求参数，如url，header等。
     * @param httpOptions 配置参数，如重试次数、超时时间等。
     */
    public static HttpResponse httpGet(HttpRequest httpRequest,
        HttpOptions httpOptions) throws Exception {
        // 装载请求地址和参数
        URIBuilder ub = new URIBuilder(httpRequest.getUrl());

        // 转换请求参数
        List<NameValuePair> pairs = convertParams2NVPS(httpRequest.getParams());
        if (!pairs.isEmpty()) {
            ub.setParameters(pairs);
        }
        HttpGet httpGet = new HttpGet(ub.build());

        // 设置请求头
        if (Objects.nonNull(httpRequest.getHeaders())) {
            for (Map.Entry<String, String> header : httpRequest.getHeaders().entrySet()) {
                httpGet.addHeader(header.getKey(), String.valueOf(header.getValue()));
            }
        }

        return doHttp(httpGet, httpOptions);
    }

    public static String post(String url, Map<String, String> params) throws Exception {
        return httpPost(url, null, params, null);
    }

    public static String post(String url, Map<String, String> params,
        HttpOptions httpOptions) throws Exception {
        return httpPost(url, null, params, httpOptions);
    }

    public static String post(String url, Map<String, String> headers,
        Map<String, String> params) throws Exception {
        return httpPost(url, headers, params, null);
    }

    public static String httpPost(String url, Map<String, String> headers,
        Map<String, String> params, HttpOptions httpOptions) throws Exception {
        HttpRequest httpRequest = new HttpRequest().setUrl(url).setHeaders(headers).setParams(
            params);

        return httpPost(httpRequest, httpOptions).getResult();
    }

    public static HttpResponse httpPost(HttpRequest httpRequest) throws Exception {
        return httpPost(httpRequest, null);
    }

    /**
     * 发送 HTTP POST请求
     *
     * @param httpRequest 请求参数
     * @param httpOptions 配置参数
     */
    public static HttpResponse httpPost(HttpRequest httpRequest,
        HttpOptions httpOptions) throws Exception {
        HttpPost httpPost = new HttpPost(httpRequest.getUrl());

        // 转换请求参数
        List<NameValuePair> pairs = convertParams2NVPS(httpRequest.getParams());
        if (!pairs.isEmpty()) {
            httpPost.setEntity(new UrlEncodedFormEntity(pairs, StandardCharsets.UTF_8.name()));
        }

        // 设置请求头
        if (Objects.nonNull(httpRequest.getHeaders())) {
            for (Map.Entry<String, String> header : httpRequest.getHeaders().entrySet()) {
                httpPost.addHeader(header.getKey(), String.valueOf(header.getValue()));
            }
        }

        return doHttp(httpPost, httpOptions);
    }

    /**
     * postJson  使用默认的配置.
     */
    public static HttpResponse postJson(String url, String json) throws Exception {
        HttpPost httpPost = new HttpPost(url);
        // 设置请求头
        httpPost.addHeader("Content-Type", "application/json; charset=UTF-8");
        // 设置请求参数
        httpPost.setEntity(new StringEntity(json, StandardCharsets.UTF_8.name()));
        return doHttp(httpPost, DEFAULT_HTTP_OPTION);
    }

    /**
     * 支持自定义Header
     */
    public static HttpResponse postJson(String url, String json,
        Map<String, String> headers) throws Exception {
        HttpPost httpPost = new HttpPost(url);
        // 设置请求头
        httpPost.addHeader("Content-Type", "application/json; charset=UTF-8");

        Set<Map.Entry<String, String>> entries = headers.entrySet();
        for (Map.Entry<String, String> entry : entries) {
            httpPost.addHeader(entry.getKey(), entry.getValue());
        }
        // 设置请求参数
        httpPost.setEntity(new StringEntity(json, StandardCharsets.UTF_8.name()));
        return doHttp(httpPost, DEFAULT_HTTP_OPTION);
    }

    /**
     * postJson 指定 HttpOptions
     */
    public static HttpResponse postJson(String url, String json,
        HttpOptions httpOptions) throws Exception {
        HttpPost httpPost = new HttpPost(url);
        httpPost.addHeader("Content-Type", "application/json; charset=UTF-8");
        httpPost.setEntity(new StringEntity(json, StandardCharsets.UTF_8.name()));
        return doHttp(httpPost, httpOptions);
    }

    /**
     * 发送 HTTP POST请求，参数通过{@link HttpRequest#setParam(Object)}来设置，且必须为byte[]类型。
     */
    public static HttpResponse postJsonByteArray(HttpRequest httpRequest,
        HttpOptions httpOptions) throws Exception {
        //检查参数必须是byte[]
        if (!(httpRequest.getParam() instanceof byte[])) {
            logger.error("[ARCH_COMMON_param_not_byte[]]httpRequest={},httpOptions={}", httpRequest,
                httpOptions);
            return HttpResponse.HTTP_RESPONSE_PARAM_ERROR;
        }
        byte[] param = (byte[]) httpRequest.getParam();
        HttpPost httpPost = new HttpPost(httpRequest.getUrl());

        // 设置请求头
        httpPost.addHeader("Content-Type", "application/json; charset=UTF-8");
        if (httpRequest.getHeaders() != null) {
            for (Map.Entry<String, String> entry : httpRequest.getHeaders().entrySet()) {
                httpPost.addHeader(entry.getKey(), entry.getValue());
            }
        }

        // 设置请求参数
        httpPost.setEntity(new ByteArrayEntity(param));

        return doHttp(httpPost, httpOptions);
    }

    /**
     * 发送 HTTP POST请求，参数格式XML
     * <p>请求参数是XML格式，数据编码是UTF-8</p>
     */
    public static HttpResponse postXml(String url, String param,
        HttpOptions httpOptions) throws Exception {
        HttpPost httpPost = new HttpPost(url);

        // 设置请求头
        httpPost.addHeader("Content-Type", "application/xml; charset=UTF-8");

        // 设置请求参数
        httpPost.setEntity(new StringEntity(param, StandardCharsets.UTF_8.name()));

        return doHttp(httpPost, httpOptions);
    }

    /**
     * 通过post发送multipart信息。
     */
    public static HttpResponse postMultipart(String url, Map<String, ContentBody> multiparts,
        HttpOptions httpOptions) throws Exception {
        HttpPost httpPost = new HttpPost(url);

        // 设置Multipart
        if (Objects.nonNull(multiparts)) {
            MultipartEntityBuilder multipartEntityBuilder = MultipartEntityBuilder.create();
            for (Map.Entry<String, ContentBody> multipartEntry : multiparts.entrySet()) {
                multipartEntityBuilder.addPart(multipartEntry.getKey(), multipartEntry.getValue());
            }

            httpPost.setEntity(multipartEntityBuilder.build());
        }

        return doHttp(httpPost, httpOptions);
    }

    /**
     * 转换请求参数，将Map键值对拼接成QueryString字符串
     */
    public static String convertParams2QueryStr(Map<String, ?> params) {
        List<NameValuePair> pairs = convertParams2NVPS(params);

        return URLEncodedUtils.format(pairs, StandardCharsets.UTF_8.name());
    }

    /**
     * 转换请求参数
     */
    public static List<NameValuePair> convertParams2NVPS(Map<String, ?> params) {
        if (Objects.isNull(params)) {
            return new ArrayList<>();
        }

        return params.entrySet().stream().map(param -> new BasicNameValuePair(param.getKey(),
            String.valueOf(param.getValue()))).collect(Collectors.toList());
    }

    /**
     * 发送 HTTP 请求
     */
    private static HttpResponse doHttp(HttpRequestBase request,
        HttpOptions httpOptions) throws Exception {
        //若为空，则用默认的。
        if (Objects.isNull(httpOptions)) {
            httpOptions = DEFAULT_HTTP_OPTION;
        }

        RequestConfig.Builder requestConfigBuilder = RequestConfig.custom();
        // 设置建立连接的超时时间
        if (Objects.nonNull(httpOptions.getConnectTimeoutMs())) {
            requestConfigBuilder.setConnectTimeout(httpOptions.getConnectTimeoutMs());
        }

        // 设置请求的超时时间
        if (Objects.nonNull(httpOptions.getTimeoutMs())) {
            requestConfigBuilder.setSocketTimeout(httpOptions.getTimeoutMs());
        }
        request.setConfig(requestConfigBuilder.build());

        //设置重试策略
        HttpRequestRetryHandler httpRequestRetryHandler = null;
        if (Objects.nonNull(httpOptions.getRetryCount())) {
            httpRequestRetryHandler = new HttpRequestRetryHandler(httpOptions.getRetryCount());
        }

        // 通过连接池获取连接对象
        CloseableHttpClient httpClient = HttpClients.custom().setConnectionManager(
            LAZY_CONNECTION_MANAGER.get()).setRetryHandler(httpRequestRetryHandler).build();
        return doRequest(httpClient, request);

    }

    /**
     * 处理Http/Https请求，并返回请求结果
     * <p>注：默认请求编码方式 UTF-8</p>
     */
    private static HttpResponse doRequest(CloseableHttpClient httpClient,
        HttpRequestBase request) throws Exception {
        HttpResponse res = new HttpResponse();
        CloseableHttpResponse response = null;
        long start = System.currentTimeMillis();

        try {
            // 获取请求结果
            response = httpClient.execute(request);
            // 解析请求结果
            HttpEntity entity = response.getEntity();
            // 转换结果
            String result = EntityUtils.toString(entity, StandardCharsets.UTF_8.name());
            // 关闭IO流
            EntityUtils.consume(entity);
            //解析返回header
            Map<String, String> headers = new HashMap<>(response.getAllHeaders().length);
            for (Header header : response.getAllHeaders()) {
                headers.put(header.getName(), header.getValue());
            }
            res.setStatusCode(response.getStatusLine().getStatusCode()).setResult(
                result).setHeaders(headers);
        } finally {
            if (Objects.nonNull(response)) {
                response.close();
            }
        }

        long elapsed = System.currentTimeMillis() - start;
        logger.debug("op=end_doRequest,request={},res={},elapsed={}", request, res, elapsed);
        return res;
    }

    /**
     * 初始化连接池
     */
    private static PoolingHttpClientConnectionManager buildPoolingHttpClientConnectionManager(
        Integer maxTotal, Integer maxPerRoute) {
        // 初始化连接池，可用于请求HTTP/HTTPS（信任所有证书）
        Registry<ConnectionSocketFactory> registry = getRegistry();
        PoolingHttpClientConnectionManager connectionManager =
            new PoolingHttpClientConnectionManager(registry);

        // 整个连接池的最大连接数
        String maxTotalProperty = null;
        //首先看有没有在参数中显式指定
        if (Objects.nonNull(maxTotal)) {
            connectionManager.setMaxTotal(maxTotal);
        } else { //如果没有在参数中显式指定，则再看有没有在系统变量中指定
            maxTotalProperty = System.getProperty(SYSTEM_PROPERTY_KEY_HTTP_MAX_TOTAL);
            if (Objects.nonNull(maxTotalProperty)) {
                connectionManager.setMaxTotal(Integer.valueOf(maxTotalProperty));
            }
        }

        // 每个路由的最大连接数
        String maxPerRouteProperty = null;
        //首先看有没有在参数中显式指定
        if (Objects.nonNull(maxPerRoute)) {
            connectionManager.setDefaultMaxPerRoute(maxPerRoute);
        } else { //如果没有在参数中显式指定，则再看有没有在系统变量中指定
            maxPerRouteProperty = System.getProperty(
                SYSTEM_PROPERTY_KEY_HTTP_MAX_PER_ROUTE);
            if (Objects.nonNull(maxPerRouteProperty)) {
                connectionManager.setDefaultMaxPerRoute(Integer.valueOf(maxPerRouteProperty));
            }
        }

        logger.info(
            "[ARCH_COMMON_SUCCESS_buildPoolingHttpClientConnectionManager]maxTotal={}," +
                "maxPerRoute={},maxTotalProperty={},maxPerRouteProperty={}",
            maxTotal, maxPerRoute, maxTotalProperty, maxPerRouteProperty);
        return connectionManager;
    }

    /**
     * 获取 HTTPClient注册器
     */
    private static Registry<ConnectionSocketFactory> getRegistry() {
        try {
            return RegistryBuilder.<ConnectionSocketFactory>create().register("http",
                new PlainConnectionSocketFactory()).register("https",
                new SSLConnectionSocketFactory(SSLContext.getDefault())).build();
        } catch (Exception e) {
            logger.error("[ERROR_getRegistry]", e);
        }

        return null;
    }
}
