package com.boot.shiro.httpservice.openfeign;

import feign.Response;
import feign.codec.ErrorDecoder;
import javassist.NotFoundException;

import java.rmi.ServerException;

/**
 * CustomErrorDecoder
 * OpenFeign 可以通过实现 ErrorDecoder 来自定义错误处理逻辑
 * 通用配置
 *
 * @author yuez
 * @since 2024/11/27
 */
public class CustomErrorDecoder implements ErrorDecoder {
    @Override
    public Exception decode(String methodKey, Response response) {
        if (response.status() == 404) {
//            return new NotFoundException("Resource not found");
            return new HystrixFallbackException("异常了404");
        } else if (response.status() == 500) {
            return new ServerException("Server error");
        } else {
            return new Exception("Generic error");
        }

    }
}
