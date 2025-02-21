package com.boot.util.http;


import lombok.extern.log4j.Log4j2;
import org.apache.http.HttpEntity;
import org.apache.http.ParseException;
import org.apache.http.StatusLine;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.DefaultConnectionReuseStrategy;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultConnectionKeepAliveStrategy;
import org.apache.http.impl.client.DefaultHttpRequestRetryHandler;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.nio.charset.UnsupportedCharsetException;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * @author yuez
 * @date 2022/6/08
 */
@Log4j2
public class HttpUtil {

    /**
     * 是否需要使用http连接池
     */
    private final boolean isNeedHttpClientPool;

    /**
     * http连接池
     */
    private CloseableHttpClient closeableHttpClient;

    /**
     * 空参构造函数，默认不使用http连接池
     */
    public HttpUtil() {
        this(false);
    }

    /**
     * 构造函数
     *
     * @param isNeedHttpClientPool 是否需要使用连接池，如果为true，则使用默认的配置
     */
    public HttpUtil(boolean isNeedHttpClientPool) {
        this(isNeedHttpClientPool, null);
    }

    /**
     * 构造函数
     *
     * @param isNeedHttpClientPool 是否需要使用连接池；如果为true，则需要传入自定义的closeablehttpclient,否则使用默认的配置
     * @param closeableHttpClient  自定义的closeablehttpclient
     */
    public HttpUtil(boolean isNeedHttpClientPool, CloseableHttpClient closeableHttpClient) {
        this.isNeedHttpClientPool = isNeedHttpClientPool;
        if (isNeedHttpClientPool) {
            this.closeableHttpClient = Objects.isNull(closeableHttpClient) ? getDefaultCloseHttpClient() : closeableHttpClient;
        }
    }

    private CloseableHttpClient getDefaultCloseHttpClient() {
        PoolingHttpClientConnectionManager httpClientPoolManager;
        CloseableHttpClient closeableHttpClient;
        //创建连接池管理器
        httpClientPoolManager = new PoolingHttpClientConnectionManager(12, TimeUnit.MINUTES);
        //连接池最大连接数量，所有路由并发数之和不超过该数字
        httpClientPoolManager.setMaxTotal(500);
        // 每个路由最大连接数  设置过小无法支持大并发(ConnectionPoolTimeoutException) Timeout waiting for connection from pool
        httpClientPoolManager.setDefaultMaxPerRoute(500);
        //在从连接池获取连接时，连接不活跃多长时间后需要进行一次验证，默认为2s
        httpClientPoolManager.setValidateAfterInactivity(5 * 1000);


        //默认请求配置
        RequestConfig defaultRequestConfig = RequestConfig.custom()
                //建立连接最大时长
                .setConnectTimeout(2000)
                //响应接收最大时间
                .setSocketTimeout(500000)
                //从连接池获取连接超时时间
                .setConnectionRequestTimeout(5000)
                .build();

        closeableHttpClient = HttpClients.custom()
                .setConnectionManager(httpClientPoolManager)
                //连接池不是共享模式
                .setConnectionManagerShared(false)
                //定期回收空闲连接
                .evictIdleConnections(60, TimeUnit.SECONDS)
                // 定期回收过期连接
                .evictExpiredConnections()
                //连接存活时间，如果不设置，则根据长连接信息决定
                .setConnectionTimeToLive(15, TimeUnit.MINUTES)
                //设置默认请求配置
                .setDefaultRequestConfig(defaultRequestConfig)
                //连接重用策略，即是否能keepAlive
                .setConnectionReuseStrategy(DefaultConnectionReuseStrategy.INSTANCE)
                //长连接配置，即获取长连接生产多长时间
                .setKeepAliveStrategy(DefaultConnectionKeepAliveStrategy.INSTANCE)
                //设置重试次数
                .setRetryHandler(new DefaultHttpRequestRetryHandler(0, false))
                .build();


        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            try {
                httpClientPoolManager.close();
            } catch (Exception e) {
                log.error("error when close httpClient:{}" + e.getMessage());
            }
        }));

        return closeableHttpClient;
    }

    public CloseableHttpClient getCloseHttpClient() {
        return this.closeableHttpClient;
    }

    /**
     * http-get请求
     *
     * @param url            请求地址
     * @param requestHeaders 自定义请求头
     * @return 返回值
     * @see HttpClientRequestHeader 自定义请求头
     * @see HttpClientResponse 通用返回值
     */
    public HttpClientResponse httpGet(String url, HttpClientRequestHeader... requestHeaders) {
        if (StringUtil.isEmpty(url)) {
            return HttpClientResponse.error("请求地址不能为空");
        }

        try {
            HttpGet httpget = new HttpGet(url);
            setHttpHeader(httpget, requestHeaders);

            return realSendRequestWithResponseString(httpget, "httpGet");
        } catch (Exception ex) {
            log.error("项目错误日志：{}#httpGet\nget请求，请求失败；\n请求地址为：{}\n异常信息为：{}", this.getClass().getName(), url, ex.getMessage());
            return HttpClientResponse.error("请求失败，请求地址为：" + url + "，" + ex.getMessage());
        }
    }

    /**
     * http-get请求
     *
     * @param url            请求地址
     * @param param          请求参数，自动拼接到url后面提交
     * @param requestHeaders 自定义请求头
     * @return 返回值
     * @see #httpGet(String, HttpClientRequestHeader...)
     * @see HttpClientRequestHeader 自定义请求头
     * @see HttpClientResponse 通用返回值
     */
    public HttpClientResponse httpGet(String url, Map<String, Object> param, HttpClientRequestHeader... requestHeaders) {
        if (StringUtil.isEmpty(url)) {
            return HttpClientResponse.error("请求地址不能为空");
        }
        return httpGet(StringUtil.urlGetParamGenerator(url, param), requestHeaders);
    }

    /**
     * http-post请求，表单方式提交
     *
     * @param url            请求地址
     * @param param          请请求参数
     * @param requestHeaders 自定义请求头
     * @return 返回值
     * @see HttpClientRequestHeader 自定义请求头
     * @see HttpClientResponse 通用返回值
     */
    public HttpClientResponse httpPostForm(String url, Map<String, String> param, HttpClientRequestHeader... requestHeaders) {
        if (StringUtil.isEmpty(url)) {
            return HttpClientResponse.error("请求地址不能为空");
        }
        try {
            HttpPost httpPost = new HttpPost(url);
            setHttpHeader(httpPost, null, Collections.singletonList(new HttpClientRequestHeader("Content-Type", "*")), requestHeaders);

            if (!(Objects.isNull(param) || param.isEmpty())) {
                UrlEncodedFormEntity urlEncodedFormEntity = new UrlEncodedFormEntity(param.entrySet().stream().map(entry -> new BasicNameValuePair(entry.getKey(), entry.getValue())).collect(Collectors.toList()));
                httpPost.setEntity(urlEncodedFormEntity);
            }

            return realSendRequestWithResponseString(httpPost, "httpPostForm");
        } catch (Exception ex) {
            log.error("项目错误日志：{}#httpPostForm\npost表单方式请求，请求失败；\n请求地址为：{}\n请求参数为：{}\n异常信息为：{}", this.getClass().getName(), url, param, ex.getMessage());
            return HttpClientResponse.error("请求失败，请求地址为：" + url + "，" + ex.getMessage());
        }
    }

    /**
     * http-post请求，json字符串方式提交
     *
     * @param url            请求地址
     * @param param          请求参数
     * @param requestHeaders 自定义请求头
     */
    public HttpClientResponse httpPostJson(String url, Object param, HttpClientRequestHeader... requestHeaders) {
        if (StringUtil.isEmpty(url)) {
            return HttpClientResponse.error("请求地址不能为空");
        }
        try {
            HttpPost httpPost = new HttpPost(url);
            setHttpHeader(httpPost, Collections.singletonList(new HttpClientRequestHeader("Content-Type", "application/json;charset=utf-8")), null, requestHeaders);

            String paramJsonString = param instanceof String ? (String) param : StringUtil.objectToJson(param);

            if (StringUtil.isNotEmpty(paramJsonString)) {
                StringEntity stringEntity = new StringEntity(paramJsonString, "utf-8");
                stringEntity.setContentType("application/json;charset=utf-8");
                httpPost.setEntity(stringEntity);
            }

            return realSendRequestWithResponseString(httpPost, "httpPostJson");
        } catch (Exception ex) {
            log.error("项目错误日志：{}#httpPostJson\npost-json方式请求，请求失败；\n请求地址为：{}\n请求参数为：{}\n异常信息为：{}", this.getClass().getName(), url, param, ex.getMessage());
            return HttpClientResponse.error("请求失败，请求地址为：" + url + "，" + ex.getMessage());
        }
    }

    /**
     * 下载图片的字节数组
     *
     * @param url 图片地址
     * @return 图片字节数组
     */
    public HttpClientResponse downLoadImg(String url) {
        if (StringUtil.isEmpty(url)) {
            return HttpClientResponse.error("请求地址不能为空");
        }
        if (StringUtil.isEmpty(url)) {
            return HttpClientResponse.error("请求地址不能为空");
        }
        try {
            HttpGet httpget = new HttpGet(url);

            return realSendRequestWithResponseByteArray(httpget, "downLoadImg");
        } catch (Exception ex) {
            log.error("项目错误日志：{}#httpGet\nget请求，请求失败；\n请求地址为：{}\n异常信息为：{}", this.getClass().getName(), url, ex.getMessage());
            return HttpClientResponse.error("请求失败，请求地址为：" + url + "，" + ex.getMessage());
        }
    }

    /**
     * 下载图片的base64
     *
     * @param url 图片地址
     * @return 图片base64字符串
     */
    public HttpClientResponse downLoadImgToBase64(String url) {
        if (StringUtil.isEmpty(url)) {
            return HttpClientResponse.error("请求地址不能为空");
        }
        HttpClientResponse httpClientResponse = downLoadImg(url);
        if (HttpClientResponseStatus.success == httpClientResponse.getStatus()) {
            httpClientResponse.setData(Base64.getEncoder().encodeToString((byte[]) httpClientResponse.getData()));
        }
        return httpClientResponse;
    }

    private void setHttpHeader(HttpRequestBase httpRequest, HttpClientRequestHeader... requestHeaders) {
        if ((!Objects.isNull(httpRequest)) && (requestHeaders.length > 0)) {
            for (HttpClientRequestHeader requestHeader : requestHeaders) {
                httpRequest.setHeader(requestHeader.getKey(), requestHeader.getValue());
            }
        }
    }

    private void setHttpHeader(HttpRequestBase httpRequest, List<HttpClientRequestHeader> includeRequestHeader, List<HttpClientRequestHeader> excludeRequestHeader, HttpClientRequestHeader... requestHeaders) {
        if ((!Objects.isNull(httpRequest)) && (requestHeaders.length > 0)) {
            List<HttpClientRequestHeader> httpClientRequestHeaders = new ArrayList<>(Arrays.asList(requestHeaders));
            if (includeRequestHeader != null) {
                httpClientRequestHeaders.addAll(includeRequestHeader);
            }
            if (excludeRequestHeader != null) {
                httpClientRequestHeaders.removeAll(excludeRequestHeader);
            }
            setHttpHeader(httpRequest, httpClientRequestHeaders.toArray(new HttpClientRequestHeader[0]));
        }
    }

    private HttpClientResponse realSendRequestWithResponseString(HttpRequestBase httpRequest, String... methodName) {
        CloseableHttpClient httpclient = isNeedHttpClientPool ? this.closeableHttpClient : HttpClients.createDefault();
        if (Objects.isNull(httpclient)) {
            return HttpClientResponse.error("未找到请求客户端");
        }
        try (CloseableHttpResponse response = httpclient.execute(httpRequest)) {
            HttpEntity entity = response.getEntity();
            String content = EntityUtils.toString(entity, ContentType.getOrDefault(entity).getCharset());
            return 200 == response.getStatusLine().getStatusCode() ? HttpClientResponse.success(content) : HttpClientResponse.error(content);
        } catch (Exception ex) {
            log.error("\n项目错误日志：{}#{}\n响应失败；\n请求地址为：{}\n异常信息为：{}", this.getClass().getName(), (methodName.length > 0 ? methodName : "realSendRequest"), httpRequest.getURI().toString(), ex.getMessage());
            return HttpClientResponse.error("响应失败，请求方法为：" + this.getClass().getName() + (methodName.length > 0 ? methodName : "realSendRequest") + "，请求地址为：" + httpRequest.getURI().toString() + "，" + ex.getMessage());
        } finally {
            boolean noNeedHttpClientPool = !isNeedHttpClientPool;
            boolean needHttpClientPoolAndHttpClientPoolIsNull = (isNeedHttpClientPool && null == this.closeableHttpClient);
            if (noNeedHttpClientPool || needHttpClientPoolAndHttpClientPoolIsNull) {
                try {
                    httpclient.close();
                } catch (Exception e) {
                    log.error("httpClient关闭异常！");
                }
            }
        }
    }

    private HttpClientResponse realSendRequestWithResponseByteArray(HttpRequestBase httpRequest, String... methodName) {
        CloseableHttpClient httpclient = isNeedHttpClientPool ? this.closeableHttpClient : HttpClients.createDefault();
        if (Objects.isNull(httpclient)) {
            return HttpClientResponse.error("未找到请求客户端");
        }
        try (CloseableHttpResponse response = httpclient.execute(httpRequest)) {
            HttpEntity entity = response.getEntity();
            byte[] content = EntityUtils.toByteArray(entity);
            //确保响应体消费完成
            EntityUtils.consume(entity);
            return 200 == response.getStatusLine().getStatusCode() ? HttpClientResponse.success(content) : HttpClientResponse.error(new String(content));
        } catch (Exception ex) {
            log.error("项目错误日志：{}#{}\n响应失败；\n请求地址为：{}\n异常信息为：{}", this.getClass().getName(), (methodName.length > 0 ? methodName : "realSendRequest"), httpRequest.getURI().toString(), ex.getMessage());
            return HttpClientResponse.error("响应失败，请求方法为：" + this.getClass().getName() + (methodName.length > 0 ? methodName : "realSendRequest") + "，请求地址为：" + httpRequest.getURI().toString() + "，" + ex.getMessage());
        } finally {
            boolean noNeedHttpClientPool = !isNeedHttpClientPool;
            boolean needHttpClientPoolAndHttpClientPoolIsNull = (isNeedHttpClientPool && null == this.closeableHttpClient);
            if (noNeedHttpClientPool || needHttpClientPoolAndHttpClientPoolIsNull) {
                try {
                    httpclient.close();
                } catch (Exception e) {
                    log.error("httpClient关闭异常！");
                }
            }
        }
    }


    public byte[] httpDownloadImage(String url) {
        byte[] content = null;
        CloseableHttpClient httpclient = HttpClients.createDefault();
        try {
            HttpGet httpget = new HttpGet(url);
            CloseableHttpResponse response = httpclient.execute(httpget);
            try {
                //成功返回 HTTP/1.1 200
                StatusLine statusLine = response.getStatusLine();
                HttpEntity entity = response.getEntity();
                content = EntityUtils.toByteArray(entity);
                //确保响应体消费完成
                EntityUtils.consume(entity);
            } finally {
                response.close();
            }
        } catch (UnsupportedCharsetException | ParseException | IOException ex) {
            log.error(ex);
        } finally {
            try {
                httpclient.close();
            } catch (IOException ex) {
                log.error(ex.getMessage());
            }
        }
        return content;

    }
}
