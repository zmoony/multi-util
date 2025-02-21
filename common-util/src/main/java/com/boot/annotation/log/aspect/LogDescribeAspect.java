package com.boot.annotation.log.aspect;

import com.boot.annotation.log.LogDescribe;
import com.boot.bean.R;
import com.boot.util.http.HttpContextUtils;
import lombok.extern.log4j.Log4j2;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;

/**
 * 打印详细日志
 *  <p>可以自定义入库或者其他操作</p>
 *  <p>//返回指定的注解 getAnnotation //判断当前元素是否被指定注解修饰 isAnnotationPresent //返回所有的注解 getAnnotations
 * </p>
 * @author yuez
 * @version 1.0.0
 * @className LogDescribeAspect
 * @date 2021/3/15 10:35
 **/
@Aspect
@Component
@Log4j2
public class LogDescribeAspect {

    /**
     * 只要用到了com.boot.util.annotation.LogDescribe这个注解的，就是目标类
     */
    @Pointcut("@annotation(com.boot.es.annotation.LogDescribe)")
    private void myValid() {
    }

    @Around("myValid()")
    public R around(ProceedingJoinPoint joinPoint) throws Throwable {
        long beginTime = System.currentTimeMillis();
        R r = (R)joinPoint.proceed();
        long time = System.currentTimeMillis() - beginTime;
        log.info("花费时长：{}ms",time);
        return r;
    }

    @Before("myValid()")
    public void before(JoinPoint joinPoint) {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        LogDescribe logDescribe = signature.getMethod().getAnnotation(LogDescribe.class);
        if(logDescribe !=null){
            log.info(logDescribe.description());
        }
        /*String className = joinPoint.getTarget().getClass().getName();
        String methodName = signature.getMethod().getName();*/

        HttpServletRequest request = HttpContextUtils.getHttpServletRequest();
        // 记录下请求内容
        String url = request.getRequestURL().toString();
        log.info("URL : " + url);
        log.info("HTTP_METHOD : " + request.getMethod());
        log.info("IP : " + request.getRemoteAddr());
        log.info("CLASS_METHOD : " + joinPoint.getSignature().getDeclaringTypeName() + "." + joinPoint.getSignature().getName());
        log.info("ARGS : " + Arrays.toString(joinPoint.getArgs()));

    }
}
