package com.boot.annReturn.intercept;

import com.boot.annReturn.annotation.ResponseResult;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;

/**
 * ResponseResultIntercept
 *
 * @author yuez
 * @since 2025/8/7
 */
@Log4j2
@Component
public class ResponseResultIntercept implements HandlerInterceptor {

    private static final String RESPONSE_RESULT_ANN = "RESPONSE_RESULT_ANN";

    /**
     * ResourceHttpRequestHandler：处理静态资源请求
     * HttpRequestHandler：处理HTTP请求的简单处理器
     * ControllerClassNameHandlerMapping相关的处理器
     * 默认的servlet处理器等
     * @param request current HTTP request
     * @param response current HTTP response
     * @param handler chosen handler to execute, for type and/or instance evaluation
     * @return
     * @throws Exception
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if(handler instanceof HandlerMethod){
            final HandlerMethod handlerMethod = (HandlerMethod) handler;
            final Class<?> clazz = handlerMethod.getBeanType();
            final Method method = handlerMethod.getMethod();
            if(clazz.isAnnotationPresent(ResponseResult.class) || method.isAnnotationPresent(ResponseResult.class)){
                request.setAttribute(RESPONSE_RESULT_ANN, clazz.isAnnotationPresent(ResponseResult.class) ? clazz.getAnnotation(ResponseResult.class) : method.getAnnotation(ResponseResult.class));
            }
        }
        return true;
    }
}
