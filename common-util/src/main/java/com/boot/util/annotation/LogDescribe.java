package com.boot.util.annotation;

import lombok.extern.log4j.Log4j2;

import java.lang.annotation.*;

@Target({ElementType.METHOD,ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface LogDescribe {
    String description() default "";
}
