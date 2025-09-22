package com.wiscom.redis.log;

import com.wiscom.util.HttpContextUtils;
import lombok.extern.log4j.Log4j2;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.Arrays;

/**
 * LoggingAspect
 *
 * @author yuez
 * @since 2024/1/11
 */
@Aspect
@Component
@Log4j2
public class LoggingAspect {

    @Pointcut("@annotation(com.wiscom.redis.log.Loggable)")
    public void myPoint() {
    }

    @Around(value = "myPoint()")
    public Object around(ProceedingJoinPoint pjp) throws Throwable {
        long beginTime = System.currentTimeMillis();
        //当前的请求对象
        Object[] args = pjp.getArgs();
        Object result = pjp.proceed();
        //方法连接点信息
        MethodSignature methodSignature = (MethodSignature) pjp.getSignature();
        String name = methodSignature.getName();
        Method method = methodSignature.getMethod();
        Loggable annotation = method.getAnnotation(Loggable.class);

        long time = System.currentTimeMillis() - beginTime;
        log.info("注解里的值：{}", annotation.value());
        log.info("方法名：{}", name);
        log.info("参数：{}", args);
        log.info("花费时长：{}ms", time);

        // 记录下请求内容
        HttpServletRequest request = HttpContextUtils.getHttpServletRequest();

        String url = request.getRequestURL().toString();
        log.info("URL : " + url);
        log.info("HTTP_METHOD : " + request.getMethod());
        log.info("IP : " + request.getRemoteAddr());

        return result;
    }
}
