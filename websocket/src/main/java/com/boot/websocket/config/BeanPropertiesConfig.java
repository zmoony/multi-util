package com.boot.websocket.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import java.util.HashMap;

/**
 * BeanConfig
 *
 * @author yuez
 * @since 2024/11/26
 */
@Data
@Configuration
@EnableConfigurationProperties(BeanPropertiesConfig.class)
@ConfigurationProperties(prefix = "bean")
//@PropertySource("classpath:bean.yml")
@PropertySource("classpath:bean.properties")
public class BeanPropertiesConfig {
    private String name;
    private String[] list1;
    private String[] list2;
    private HashMap<String, Object> map;
    private HashMap<String, Object> map2;
}
