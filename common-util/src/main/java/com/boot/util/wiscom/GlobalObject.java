package com.boot.util.wiscom;

import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.net.Socket;
import java.util.List;
import java.util.Properties;

/**
 *
 *
 * @author yuez
 * @version 1.0.0
 * @className GlobalObject
 * @date 2021/5/18 22:31
 **/
@Component
public class GlobalObject {
    public static PoolingHttpClientConnectionManager MyPoolingHttpClientConnectionManager; //图片http下载
    public static CloseableHttpClient MyCloseableHttpClient;//图片http下载

    public static Socket socket;

    public static Properties properties_business_my;


    @Value("${redis.host}")
    public String redisHost;

    @Value("${redis.port}")
    public int redisPort;

    @Value("${redis.maxIdle}")
    public int maxIdle;

    @Value("${redis.maxActive}")
    public int maxActive;

    @Value("${redis.maxWait}")
    public int maxWait;

    @Value("${redis.testOnBorrow}")
    public boolean testOnBorrow;

    @Value("${redis.minEvictableIdleTimeMillis}")
    public int minEvictableIdleTimeMillis;

    @Value("${redis.numTestsPerEvictionRun}")
    public int numTestsPerEvictionRun;

    @Value("${redis.timeBetweenEvictionRunsMillis}")
    public int timeBetweenEvictionRunsMillis;

    @Value("${redis.softMinEvictableIdleTimeMillis}")
    public int softMinEvictableIdleTimeMillis;

    @Value("${redis.blockWhenExhausted}")
    public boolean blockWhenExhausted;

    @Value("${redis.timeout}")
    public int timeout;

    @Value("${redis.cluster.nodes}")
    public String clusterNodes;

    @Value("${redis.password}")
    public String password;

    @Value("${redis.client.way}")
    public int redisClientWay;

    @Value("${redis.database}")
    public int redisDataBase;



    public static String sendData(List<?> list){
        RequestBean requestBean=new RequestBean();
        requestBean.setAuthnom(GlobalObject.properties_business_my.getProperty("vehicle.alltokafka.authnom"));
        requestBean.setType(Integer.parseInt(GlobalObject.properties_business_my.getProperty("vehicle.alltokafka.type").trim()));
        requestBean.setContent(list);
        String json=ObjectToJsonUtil.objectToJson(requestBean);
        return HttpClientPoolUtil.jsonPostFromPool(GlobalObject.properties_business_my.getProperty("vehicle.alltokafka.address").trim(),json);
    }
}
