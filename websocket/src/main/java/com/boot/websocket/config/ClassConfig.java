package com.boot.websocket.config;

import com.boot.websocket.util.CommonCryptoUtil;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * ClassConfig
 *
 * @author yuez
 * @since 2024/11/26
 */
@Configuration
public class ClassConfig {

    @Bean
    public CommonCryptoUtil commonCryptoUtil() {
        return new CommonCryptoUtil();
    }
}
