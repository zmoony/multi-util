package com.boot.annReturn.annotation;

import java.lang.annotation.*;

/**
 * @ResponseResult 响应结果注解
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.METHOD, ElementType.TYPE})
@Documented
public @interface ResponseResult {
}
