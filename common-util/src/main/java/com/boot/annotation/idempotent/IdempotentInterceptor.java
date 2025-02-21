package com.boot.annotation.idempotent;

import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;

/**
 * IdempotentInterceptor
 *  <pre>@Idempotent(requestId = "orderId")</pre>
 * @author yuez
 * @since 2024/12/27
 */
@Component
public class IdempotentInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
//        Method method = ((HandlerMethod) handler).getMethod();
//        Idempotent idempotent = method.getAnnotation(Idempotent.class);
        Idempotent idempotent = request.getClass().getAnnotation(Idempotent.class);
        if(idempotent !=null){
            String requestId = request.getHeader(idempotent.requestId());
//            String requestId = request.getParameter(idempotent.requestId());
            if(requestId == null){
                throw new RuntimeException("requestId is null");
            }
            Object obj = request.getSession().getAttribute(requestId);
            if(obj != null){
                throw new RuntimeException("requestId is repeat");
            }
            request.getSession().setAttribute(requestId,requestId);
            //设置过期时间
            request.getSession().setMaxInactiveInterval(idempotent.expireTime());
            return true;
        }
        return HandlerInterceptor.super.preHandle(request, response, handler);
    }
}
