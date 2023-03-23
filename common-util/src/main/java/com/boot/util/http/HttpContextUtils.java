package com.boot.util.http;

import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

/**
 * httpcontext工具类
 *
 * @author yuez
 * @version 1.0.0
 * @className HttpContextUtils
 * @date 2021/3/15 10:58
 **/
public class HttpContextUtils {
    /**
    　　获取httpservletRequest
    　　 @param
    　　 @return javax.servlet.http.HttpServletRequest
    　　 @throws
    　　 @author yuez
    　　 @date 2021/3/15 10:59
    　　*/
    public static HttpServletRequest getHttpServletRequest(){
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();
        return request;
    }


}