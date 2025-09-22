package com.wiscom.redis.pool;

import com.wiscom.redis.config.RedisConfig;
import io.lettuce.core.RedisClient;
import io.lettuce.core.RedisURI;
import io.lettuce.core.api.StatefulRedisConnection;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * RedisPool
 *
 * @author yuez
 * @since 2023/11/22
 */
@Log4j2
@Component
public class RedisPool {

    /**
     * <5>：关闭连接一般在应用程序停止之前操作，一个应用程序中的一个Redis驱动实例不需要太多的连接（一般情况下只需要一个连接实例就可以，如果有多个连接的需要可以考虑使用连接池，其实Redis目前处理命令的模块是单线程，在客户端多个连接多线程调用理论上没有效果）。
     * <6>：关闭客户端一般应用程序停止之前操作，如果条件允许的话，基于后开先闭原则，客户端关闭应该在连接关闭之后操作。
     */
    public static RedisClient redisClient;
    public static StatefulRedisConnection statefulRedisConnectionR;
    public static StatefulRedisConnection statefulRedisConnectionW;
    private final RedisConfig redisConfig;

    public RedisPool(RedisConfig redisConfig) {
        this.redisConfig = redisConfig;
    }


    @PostConstruct
    public void init(){
        RedisURI uri = RedisURI.Builder.redis(redisConfig.getHost())
                .withPassword(redisConfig.getPassword())
                .withDatabase(redisConfig.getDatabase())
                .withPort(redisConfig.getPort())
                .build();
        redisClient = RedisClient.create();
        statefulRedisConnectionR = redisClient.connect(uri);
        statefulRedisConnectionW = redisClient.connect(uri);
        log.info("redis 连接初始化成功");
    }


}
