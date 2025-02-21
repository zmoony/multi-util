package com.boot.annotation.idempotent;

import java.lang.annotation.*;

@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Idempotent {
    /**
     * 请求标识符的参数名称，默认为"requestId"
     */
    String requestId() default "requestId";

    /**
     * 幂等有效时长（单位：秒）
     */
    int expireTime() default 60;
}
