package com.boot.shiro.httpservice.restTemplate;

import com.boot.util.wiscom.GlobalObject;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.DefaultConnectionReuseStrategy;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultConnectionKeepAliveStrategy;
import org.apache.http.impl.client.DefaultHttpRequestRetryHandler;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.ssl.SSLContexts;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.util.concurrent.TimeUnit;

/**
 * RestTemplateConfig
 *
 * @author yuez
 * @since 2024/11/27
 */
@Configuration
public class RestTemplateConfig {
    @Bean
//    @LoadBalanced  ribbon 负载均衡
    public RestTemplate restTemplate() throws NoSuchAlgorithmException, KeyStoreException, KeyManagementException {
//        //创建SSLContext.这里使用了一个信任所有证书的策略（loadTrustMaterial(null, (chain, authType) -> true)）。在生产环境中，建议使用正式的证书管理策略。
//        SSLContext sslContext = SSLContexts.custom().loadTrustMaterial(null, (chain, authType)->true).build();
//        //创建SSLConnectionSocketFactory,并设置主机名验证器为 NoopHostnameVerifier，表示不验证主机名。
//        HostnameVerifier hostNameVerifier = NoopHostnameVerifier.INSTANCE;
//        SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(sslContext, hostNameVerifier);
//        // 创建 Registry.注册 HTTP 和 HTTPS 的 ConnectionSocketFactory
//        Registry<ConnectionSocketFactory> socketFactoryRegistry = RegistryBuilder.<ConnectionSocketFactory>create()
//                .register("http", PlainConnectionSocketFactory.getSocketFactory())
//                .register("https", sslsf)
//                .build();

        Registry<ConnectionSocketFactory> socketFactoryRegistry = RegistryBuilder
                .<ConnectionSocketFactory>create()
                .register("http", PlainConnectionSocketFactory.INSTANCE)
                .register("https", SSLConnectionSocketFactory.getSocketFactory())
                .build();


        PoolingHttpClientConnectionManager cm = new PoolingHttpClientConnectionManager(socketFactoryRegistry);
        cm.setMaxTotal(100);
        cm.setDefaultMaxPerRoute(10);

        RequestConfig defaultRequestConfig = RequestConfig.custom()
                .setConnectTimeout(5 * 1000)
                .setSocketTimeout(30 * 1000)
                .setConnectionRequestTimeout(5000)
                .build();
        CloseableHttpClient httpClient = HttpClients.custom()
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

        HttpComponentsClientHttpRequestFactory factory = new HttpComponentsClientHttpRequestFactory(httpClient);
        RestTemplate restTemplate = new RestTemplate(factory);
        restTemplate.setErrorHandler(new CustomErrorHandler());
        return restTemplate;
    }
}
