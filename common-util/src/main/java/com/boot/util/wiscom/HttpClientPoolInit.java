package com.boot.util.wiscom;

import lombok.extern.log4j.Log4j2;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.DefaultConnectionReuseStrategy;
import org.apache.http.impl.client.DefaultConnectionKeepAliveStrategy;
import org.apache.http.impl.client.DefaultHttpRequestRetryHandler;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Component
@Log4j2
public class HttpClientPoolInit {
    public void init(){
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
        
        GlobalObject.MyCloseableHttpClient = HttpClients.custom()
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
