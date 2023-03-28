package com.example.text.demoOnLine.重复请求过滤;

import lombok.extern.log4j.Log4j2;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.mvc.condition.RequestConditionHolder;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

/**
 * 这里我们使用过滤器的方式对进入服务器的请求进行过滤操作，实现对相同客户端请求同一个接口的过滤。
 *
 * @author yuez
 * @since 2023/2/21
 */
@Log4j2
//@Component
public class IRequestFilter extends OncePerRequestFilter {
    private static Map<String,Long> expireMap = new HashMap<>();//建议使用redis或者caffine
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws ServletException, IOException {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        String address = attributes != null? attributes.getRequest().getRemoteAddr(): UUID.randomUUID().toString();
        if(Objects.equals(request.getMethod(),"GET")){
            StringBuilder str = new StringBuilder();
            str.append(request.getRequestURI()).append("|")
                    .append(request.getRemotePort()).append("|")
                    .append(request.getLocalName()).append("|")
                    .append(address);
            String hex = DigestUtils.md5Hex(new String(str));
            log.info("请求的MD5值为：{}",hex);
            if(expireMap.containsKey(hex)){
                log.warn("请求重复，退出");
//                return;
            }
            expireMap.put(hex,10*1000L);
        }
        log.info("请求的 address:{}", address);
        chain.doFilter(request, response);
    }
}
