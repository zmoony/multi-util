package com.boot.annotation.idempotent;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * WebConfig
 *
 * @author yuez
 * @since 2024/12/27
 */
@Configuration
public class WebConfig implements WebMvcConfigurer {
    @Autowired
    private IdempotentInterceptor idempotentInterceptor;
    @Override
    public void addInterceptors(org.springframework.web.servlet.config.annotation.InterceptorRegistry registry) {
        registry.addInterceptor(idempotentInterceptor).addPathPatterns("/api/v1/idempotent/**");
    }
}
