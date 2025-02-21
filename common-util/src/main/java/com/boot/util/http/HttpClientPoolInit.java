package com.boot.util.http;

import com.boot.util.wiscom.GlobalObject;
import lombok.extern.log4j.Log4j2;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.DefaultHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLContexts;
import org.apache.http.impl.DefaultConnectionReuseStrategy;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultConnectionKeepAliveStrategy;
import org.apache.http.impl.client.DefaultHttpRequestRetryHandler;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import java.io.File;
import java.io.FileInputStream;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.util.concurrent.TimeUnit;

/**
 * 连接池管理：使用连接池管理的 CloseableHttpClient 时，不需要每次使用后都关闭客户端。
 * 释放连接：在每次请求完成后，通过关闭 CloseableHttpResponse 来释放连接，而不是关闭 CloseableHttpClient。
 * 配置：合理配置连接池参数，如最大连接数、每个路由的最大连接数、连接超时时间等，以优化性能和资源利用。
 * <pre>
 *          try (CloseableHttpResponse response = this.httpClient.execute(httpGet)) {
 *             // 判断状态码是否为200
 *             if (response.getStatusLine().getStatusCode() == 200) {
 *                 // 返回响应体的内容
 *                 return EntityUtils.toString(response.getEntity(), "UTF-8");
 *             }
 *          }
 * </pre>
 */
@Configuration
@Log4j2
public class HttpClientPoolInit {

    public void customSSlf() throws Exception {
        // 加载自定义的 CA 证书
        KeyStore trustStore = KeyStore.getInstance(KeyStore.getDefaultType());
        try (FileInputStream fis = new FileInputStream("path/to/truststore.jks")) {
            trustStore.load(fis, "truststore-password".toCharArray());
        }

        // 创建 SSLContext
        SSLContext sslContext = SSLContexts.custom()
                .loadTrustMaterial(trustStore, null) // 使用自定义的信任库
                .build();

        // 创建 HostnameVerifier
        HostnameVerifier hostnameVerifier = new DefaultHostnameVerifier();

        // 创建 SSLConnectionSocketFactory
        SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(sslContext, hostnameVerifier);
    }

    @Bean
    public CloseableHttpClient init(){
        Registry<ConnectionSocketFactory> socketFactoryRegistry = RegistryBuilder
                        .<ConnectionSocketFactory>create()
                        .register("http", PlainConnectionSocketFactory.INSTANCE)
                        .register("https", SSLConnectionSocketFactory.getSocketFactory())
                        .build();
        GlobalObject.MyPoolingHttpClientConnectionManager  = new PoolingHttpClientConnectionManager(socketFactoryRegistry);  //创建池化连接管理器
        GlobalObject.MyPoolingHttpClientConnectionManager.setMaxTotal(1000);
        GlobalObject.MyPoolingHttpClientConnectionManager.setDefaultMaxPerRoute(50); // 每个路由最大连接数  设置过小无法支持大并发(ConnectionPoolTimeoutException) Timeout waiting for connection from pool
        GlobalObject.MyPoolingHttpClientConnectionManager.setValidateAfterInactivity(5 * 1000);  //在从连接池获取连接时，连接不活跃多长时间后需要进行一次验证，默认为2s


        //默认请求配置
        //设置连接超时时间，2s
        //设置等待数据超时时间，30s
        //设置从连接池获取连接的等待超时时间
        RequestConfig defaultRequestConfig = RequestConfig.custom()
                    .setConnectTimeout(5 * 1000)
                    .setSocketTimeout(30 * 1000)
                    .setConnectionRequestTimeout(5000)
                    .build();

        return HttpClients.custom()
                    .setConnectionManager(GlobalObject.MyPoolingHttpClientConnectionManager)
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





    }

    static {
        Runtime.getRuntime().addShutdownHook(new Thread() {
            @Override
            public void run() {

                try {
                    GlobalObject.MyPoolingHttpClientConnectionManager.close();

                } catch (Exception e) {
                    log.error("error when close httpClient:{}", e);
                }
            }
        });
    }

}
