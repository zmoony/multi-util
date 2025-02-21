package com.boot.shiro.httpservice.openfeign;

import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.stereotype.Component;

/**
 * OpenFeignFallback
 *
 * ErrorDecoder 会在 Feign 处理 HTTP 响应时先于 fallback 执行。
 * 如果 ErrorDecoder 处理了异常并抛出了新的异常，那么 fallback 将不会被触发。
 * Hystrix 默认只会处理 RuntimeException 和其子类
 *
 * @author yuez
 * @since 2024/11/27
 */
@Component
public class OpenFeignFallback implements FallbackFactory<OpenFeignService> {
    @Override
    public OpenFeignService create(Throwable cause) {
        return new OpenFeignService() {
            @Override
            public String lockAdd() {
                // 提供备用的处理逻辑
                return "Fallback response: " + cause.getMessage();
            }
        };
    }
}
