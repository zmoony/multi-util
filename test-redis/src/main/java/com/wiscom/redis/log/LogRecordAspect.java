package com.wiscom.redis.log;

import lombok.extern.log4j.Log4j2;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.expression.EvaluationContext;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.lang.reflect.Method;

/**
 * LogRecordAspect
 *
 * @author yuez
 * @since 2024/1/11
 */
@Aspect
@Component
@Log4j2
public class LogRecordAspect {
    @Deprecated
    @Pointcut("@annotation(com.wiscom.redis.log.LogRecord)")
    public void myPoint() {
    }
//    @Around(value = "execution(* com.rq.aop.controller..*.*(..))")

    @Around("@annotation(logRecord)")
    public Object around(ProceedingJoinPoint pjp, LogRecord logRecord) throws Throwable {
        MethodSignature methodSignature = (MethodSignature) pjp.getSignature();
        Method method = methodSignature.getMethod();
        Object[] args = pjp.getArgs();
        Object result = pjp.proceed();

        EvaluationContext context = SpELParserUtils.initContext(method, args);

        if (StringUtils.hasLength(logRecord.success())) {
            String success = SpELParserUtils.parse(context ,logRecord.success(), String.class);
            System.out.println("success:" + success);
        }

        if (StringUtils.hasLength(logRecord.fail())) {
            String fail = SpELParserUtils.parse(context ,logRecord.fail(), String.class);
            System.out.println("fail:" + fail);
        }

        if (StringUtils.hasLength(logRecord.subType())) {
            String subType = SpELParserUtils.parse(context, logRecord.subType(), String.class);
            System.out.println("subType:" + subType);
        }

        return result;
    }

    @AfterReturning(value = "myPoint()", returning = "returnObject")
    public void log(JoinPoint jp, Object returnObject) {
        //正常记录日志
    }

    @AfterThrowing(value = "myPoint()", throwing = "error")
    public void errorLog(JoinPoint jp, Error error) {

    }

}
