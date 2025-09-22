package com.example.text.demoOnLine.日志处理.AOP注解;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

/**
 * LoggingAspect
 *
 * @author yuez
 * @since 2024/1/10
 */
@Aspect
@Component
public class LoggingAspect {

    @Around("@annotation(loggable)")
    public Object logExecutionTime(ProceedingJoinPoint pjp,Loggable loggable) throws Throwable {
        long startTime = System.currentTimeMillis();
        //执行方法
        Object result = pjp.proceed();
        long endTime = System.currentTimeMillis();
        System.out.println(pjp.getSignature()+"方法执行时间：" + (endTime - startTime) + "ms");
        return result;
    }

}
