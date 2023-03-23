package com.boot.util.http;

import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.ParseException;
import org.apache.http.StatusLine;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.routing.HttpRoute;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.DefaultConnectionReuseStrategy;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultConnectionKeepAliveStrategy;
import org.apache.http.impl.client.DefaultHttpRequestRetryHandler;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.util.EntityUtils;
import org.springframework.stereotype.Component;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.nio.charset.Charset;
import java.nio.charset.UnsupportedCharsetException;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * http工具类
 *
 * @author yuez
 * @version 1.0.0
 * @className HttpUtils
 * @date 2021/3/22 9:32
 **/
@Component
@Log4j2
public class HttpUtils {
    public static PoolingHttpClientConnectionManager MyPoolingHttpClientConnectionManager;
    public static CloseableHttpClient MyCloseableHttpClient;

    static {
        //自定义sslf
        /*
            KeyStore myTrustStore = <...>
            SSLContext sslContext = SSLContexts.custom()
                    .loadTrustMaterial(myTrustStore)
                    .build();
            SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(sslContext);
        */

        /*  接受任何有效的ssl通信，默认使用 getDefaultHostnameVerifier {@link SSLConnectionSocketFactory#getSocketFactory}
        SSLContext sslContext = SSLContexts.createSystemDefault();
        SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(
                sslContext,
                NoopHostnameVerifier.INSTANCE);*/

        Registry<ConnectionSocketFactory> socketFactoryRegistry = RegistryBuilder
                .<ConnectionSocketFactory>create()
                .register("http", PlainConnectionSocketFactory.INSTANCE)
                .register("https", SSLConnectionSocketFactory.getSocketFactory())
                .build();
        //创建池化连接管理器
        MyPoolingHttpClientConnectionManager = new PoolingHttpClientConnectionManager(socketFactoryRegistry);
        MyPoolingHttpClientConnectionManager.setMaxTotal(1000);
        // 每个路由最大连接数  设置过小无法支持大并发(ConnectionPoolTimeoutException) Timeout waiting for connection from pool
        MyPoolingHttpClientConnectionManager.setDefaultMaxPerRoute(50);
        //自定义路由数
       /* HttpHost localhost = new HttpHost("locahost", 80);
        MyPoolingHttpClientConnectionManager.setMaxPerRoute(new HttpRoute(localhost), 50);*/
        //在从连接池获取连接时，连接不活跃多长时间后需要进行一次验证，默认为2s
        MyPoolingHttpClientConnectionManager.setValidateAfterInactivity(5 * 1000);

        //默认请求配置
        //设置连接超时时间，2s
        //设置等待数据超时时间，5s
        //设置从连接池获取连接的等待超时时间
        RequestConfig defaultRequestConfig = RequestConfig.custom()
                .setConnectTimeout(5 * 1000)
                .setSocketTimeout(60 * 1000)
                .setConnectionRequestTimeout(5000)
                .build();

        //需要确认是在静态方法里创建一遍（固定数量），还是每次在请求中创建
        MyCloseableHttpClient = HttpClients.custom()
                .setConnectionManager(MyPoolingHttpClientConnectionManager)
                //连接池不是共享模式
                .setConnectionManagerShared(false)
                //定期回收空闲连接
                .evictIdleConnections(60, TimeUnit.SECONDS)
                // 定期回收过期连接
                .evictExpiredConnections()
                //连接存活时间，如果不设置，则根据长连接信息决定
                .setConnectionTimeToLive(60, TimeUnit.SECONDS)
                //设置默认请求配置
                .setDefaultRequestConfig(defaultRequestConfig)
                //连接重用策略，即是否能keepAlive
                .setConnectionReuseStrategy(DefaultConnectionReuseStrategy.INSTANCE)
                //长连接配置，即获取长连接生产多长时间
                .setKeepAliveStrategy(DefaultConnectionKeepAliveStrategy.INSTANCE)
                //设置重试次数
                .setRetryHandler(new DefaultHttpRequestRetryHandler(0, false))
                .build();

        //官方同时建议我们在后台起一个定时清理无效连接的线程
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                System.out.println("=====closeIdleConnections===");
                MyPoolingHttpClientConnectionManager.closeExpiredConnections();
                MyPoolingHttpClientConnectionManager.closeIdleConnections(5, TimeUnit.SECONDS);
            }
        }, 0, 5 * 1000);

        Runtime.getRuntime().addShutdownHook(new Thread() {
            @Override
            public void run() {
                try {
                    MyPoolingHttpClientConnectionManager.close();
                } catch (Exception e) {
                    log.error("error when close httpClient:{}", e);
                }
            }
        });
    }

    //通用请求
    public static String request(HttpRequestBase request, Class<?> clazz, int succStatus) {
        try {
            CloseableHttpResponse response = MyCloseableHttpClient.execute(request);
            HttpEntity entity = response.getEntity();
            ContentType contentType = ContentType.getOrDefault(entity);
            Charset charset = contentType.getCharset();
            String s = EntityUtils.toString(entity, charset);
            return s;
        } catch (IOException e) {
            log.error("发送异常：{}", e);
        }
        return null;
    }

    public String doPost(String url, Map<String, String> mapHeader, String jsonBody, String charset) {
        HttpPost httpPost = null;
        String result = null;
        CloseableHttpResponse response = null;
        try {
            httpPost = new HttpPost(url);
            httpPost.setHeader("Content-Type", "application/json;charset=" + charset);
            if (mapHeader != null) {
                Iterator<String> headers = mapHeader.keySet().iterator();
                while (headers.hasNext()) {
                    String key = headers.next();
                    String vallue = mapHeader.get(key);
                    httpPost.setHeader(key, vallue);
                }
            }
            //设置参数
            if (StringUtils.isNotEmpty(jsonBody)) {
                httpPost.setEntity(new StringEntity(jsonBody, Charset.forName("UTF-8")));
            }

            response = MyCloseableHttpClient.execute(httpPost);
            HttpEntity resEntity = response.getEntity();
            if (resEntity != null) {
                result = EntityUtils.toString(resEntity, charset);
            }
            //确保响应体消费完成
            EntityUtils.consume(resEntity);
        } catch (Exception ex) {
            log.error("http请求出错:{}", ex);
        } finally {
            try {
                if (response != null) {
                    response.close();
                }
            } catch (IOException e) {
                log.error("响应体关闭出错:{}", e);
            }
        }

        return result;
    }


    //下载图片
    public static byte[] httpDownloadImageFromPool(String url) {
        byte[] content = null;
        try {
            HttpGet httpget = new HttpGet(url);
            CloseableHttpResponse response = MyCloseableHttpClient.execute(httpget);
            try {
                HttpEntity entity = response.getEntity();
                final InputStream is = response.getEntity().getContent();
                int len = -1;
                ByteArrayOutputStream data = new ByteArrayOutputStream();
                byte[] by = new byte[1024];
                while ((len = is.read(by)) != -1) {
                    data.write(by, 0, len);
                }
                content = data.toByteArray();
            } finally {
                response.close();
            }
        } catch (Exception ex) {
            log.error("下载图片异常：" + ex.getMessage() + " 图片路径：" + url);
        }
        return content;
    }

    /**
     * 可能会中文乱码--按照一下格式传输
     * MultipartEntityBuilder builder =MultipartEntityBuilder.create().setMode(HttpMultipartMode.RFC6532);;
     * builder.addTextBody("imgUrl",url,ContentType.APPLICATION_JSON);
     * HttpClientUtil.keyValuePostFromPool(analysisUrl,builder.build() )
     * @param url
     * @param he
     * @return
     */
    public static String keyValuePostFromPool(String url, HttpEntity he) {
        String content = null;
        try {
            HttpPost httpPost = new HttpPost(url);
            httpPost.setEntity(he);

            CloseableHttpResponse response = MyCloseableHttpClient.execute(httpPost);
            try {
                StatusLine statusLine = response.getStatusLine();  //成功返回 HTTP/1.1 200
                HttpEntity entity = response.getEntity();
                //获取
                ContentType contentType = ContentType.getOrDefault(entity);
                Charset charset = contentType.getCharset();
                content = EntityUtils.toString(entity, charset);
                //log.info("响应体中消息内容:"+content);
                return content;
            } finally {
                response.close();
            }
        } catch (IOException | UnsupportedCharsetException | ParseException ex) {
            log.error("接口失败" + ex.getMessage());

        }

        return content;
    }

    public static void main(String[] args) {
        System.out.println(new BigDecimal(-1).intValue()-new BigDecimal(-1).intValue());
    }

}
