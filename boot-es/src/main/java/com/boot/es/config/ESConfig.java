package com.boot.es.config;

import lombok.Data;
import lombok.extern.log4j.Log4j2;
import org.apache.http.HttpHost;
import org.apache.http.client.config.RequestConfig;
import org.elasticsearch.action.admin.cluster.health.ClusterHealthRequest;
import org.elasticsearch.action.admin.cluster.health.ClusterHealthResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestClientBuilder;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import javax.validation.constraints.NotNull;
import java.io.IOException;
import java.util.Arrays;
import java.util.Objects;

/**
 * 在Spring容器中定义 RestClient 对象
 * @author yuez
 * @version 1.0.0
 * @className ESConfig
 * @date 2021/3/13 11:13
 **/
@Log4j2
@ConfigurationProperties(prefix="wiscom.elasticsearch",ignoreUnknownFields = true)
@Configuration
@Data
@Component
public class ESConfig {
    private String ccsCluster;
    @NotNull
    private String[] servers;
    @NotNull
    private boolean ccs;

    @Bean(name = "restClientBuilder")
    public RestClientBuilder restClientBuilder() {
        HttpHost[] hosts = Arrays.stream(servers)
                .map(this::makeHttpHost)
                .filter(Objects::nonNull)
                .toArray(HttpHost[]::new);
        log.debug("common-hosts:{}", Arrays.toString(hosts));
        return RestClient.builder(hosts)
                .setRequestConfigCallback(new RestClientBuilder.RequestConfigCallback(){
        //java.net.SocketTimeoutException: 30,000 milliseconds timeout on connection http-outgoing-46 [ACTIVE]
            @Override
            public RequestConfig.Builder customizeRequestConfig(RequestConfig.Builder builder) {
                return builder
                        .setConnectTimeout(5000 * 1000)//连接超时（默认1秒）
                        .setSocketTimeout(6000 * 1000);// 套接字超时（默认为30秒）//更改客户端的超时限制默认30秒现在改为100*1000分钟
            }// 调整最大重试超时时间（默认为30秒）.setMaxRetryTimeoutMillis(60000);
        });
    }
    /**
    　　高级客户端
    　　 @param restClientBuilder
    　　 @return org.elasticsearch.client.RestHighLevelClient
    　　 @throws
    　　 @author yuez
    　　 @date  2021/3/13 10:56
    　　*/
    @Bean(name = "restHighLevelClient")
    public RestHighLevelClient highLevelClient(@Autowired RestClientBuilder restClientBuilder) {
        //7.x版本取消
        //restClientBuilder.setMaxRetryTimeoutMillis(60000);
        return new RestHighLevelClient(restClientBuilder);
    }

    /**
    　　获取集群状态
    　　 @param restHighLevelClient
    　　 @return org.elasticsearch.action.admin.cluster.health.ClusterHealthResponse
    　　 @throws
    　　 @author yuez
    　　 @date 2021/3/13 11:45
    　　*/
    @Bean(name = "clusterHealthResponse")
    public ClusterHealthResponse testHealth(@Autowired RestHighLevelClient restHighLevelClient){
        ClusterHealthResponse health=null;
        try {
            health = restHighLevelClient.cluster()
                    .health(new ClusterHealthRequest(), RequestOptions.DEFAULT);
            log.info("通用-集群状态:{}",health.toString());
        } catch (IOException e) {
            log.error("通用-获取客户端状态错误:{}",e);
        }
        return health;
    }

    /**
    　* @description: 项目主要使用 RestHighLevelClient，对于低级的客户端暂时不用
    　* @param [restClientBuilder]
    　* @return org.elasticsearch.client.RestClient
    　* @throws
    　* @author yuez
    　* @date 2021/3/13 10:57
    　*/
    public RestClient restClient(@Autowired RestClientBuilder restClientBuilder){
//        restClientBuilder.setMaxRetryTimeoutMillis(60000);
        return restClientBuilder.build();
    }

    //生成 HttpHost
    private HttpHost makeHttpHost(String s) {
        String[] address = s.split(":");
        if (address.length == 2) {
            String ip = address[0];
            int port = Integer.parseInt(address[1]);
            return new HttpHost(ip, port, "http");
        } else {
            return null;
        }
    }
}
