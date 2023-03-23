package com.boot.redis.common;

import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * 配置实体类
 *
 * @author yuez
 * @version 1.0.0
 * @className GlobalObject
 * @date 2021/4/14 15:08
 **/
@Log4j2
@Component
public class GlobalObject {
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

    @Value("${redis.model}")
    public String model;


    public static final String MODEL_SINGLE = "single";
    public static final String MODEL_CLUSTER = "cluster";

}
