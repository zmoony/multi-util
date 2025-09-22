package com.wiscom.redis.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

/**
 * RedisConfig
 *
 * @author yuez
 * @since 2023/11/22
 */
@Component
@Data
@PropertySource({"classpath:application-redis.properties"})
public class RedisConfig {
    @Value("${redis.host.test}")
    private String host;
    @Value("${redis.port.test}")
    private Integer port;
    @Value("${redis.password.test}")
    private String password;
    @Value("${redis.database.test}")
    private Integer database;
}
