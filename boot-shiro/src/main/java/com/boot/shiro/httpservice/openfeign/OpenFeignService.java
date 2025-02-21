package com.boot.shiro.httpservice.openfeign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;

/**
 * OpenFeignService
 * 依赖
 * <pre>
 *     <dependency>
 *     <groupId>org.apache.httpcomponents</groupId>
 *     <artifactId>httpclient</artifactId>
 *     <version>4.5.13</version> <!-- 选择合适的版本 -->
 * </dependency>
 *
 * <dependency>
 *     <groupId>org.springframework.cloud</groupId>
 *     <artifactId>spring-cloud-starter-openfeign</artifactId>
 * </dependency>
 * </pre>
 *
 * 失败情况的备选逻辑
 * @Fallback 可以指定一个备选类，当请求失败时会返回该类的一个实例。
 * @FallbackFactory 类似于 @Fallback，但它允许基于异常类型返回不同的备选实现。
 *
 * @author yuez
 * @since 2024/11/27
 */
@FeignClient(
        name = "openFeignService",
        url = "http://localhost:9099",
        configuration = OpenFeignConfig.class,
        fallbackFactory = OpenFeignFallback.class
)
public interface  OpenFeignService {

    @PostMapping(value = "/lock/add")
    public String lockAdd();

}
