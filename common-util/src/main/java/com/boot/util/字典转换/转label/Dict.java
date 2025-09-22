package com.boot.util.字典转换.转label;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.FIELD, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface Dict {
    String type();        // 字典类型
    String target() default ""; // 转换后写到哪个字段，空则覆盖原值
}
