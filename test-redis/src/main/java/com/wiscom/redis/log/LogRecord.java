package com.wiscom.redis.log;

import java.lang.annotation.*;

/**
 * 可以获取参数，进行具体的日志记录，每个日志模板不一样
 * 采用sqel表达式进行参数解析
 * @author yuez
 */
@Target({ElementType.TYPE,ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
public @interface LogRecord {
    String success();
    String fail() default "";
    String operator() default ""; //业务操作场景人
    String type();//业务场景，模板范围

    String subType() default "";//业务子场景，主要是模板下的功能范围
    String bizNo();//业务编号
    String extra() default "";//扩展操作
    String actionType();//业务操作类型，比如编辑，新增，删除


}
