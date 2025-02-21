package com.boot.websocket.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * CommonConfig
 *
 * @author yuez
 * @since 2024/11/26
 */
@Data
@Configuration
@ConfigurationProperties
public class CommonConfig {
    private String name;
}
