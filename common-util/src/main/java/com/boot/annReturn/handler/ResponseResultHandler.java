package com.boot.annReturn.handler;

import com.boot.annReturn.Result;
import com.boot.annReturn.ResultCode;
import com.boot.annReturn.annotation.ResponseResult;
import lombok.extern.log4j.Log4j2;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import javax.servlet.http.HttpServletRequest;

/**
 * ResponseResultHandler
 *
 * @author yuez
 * @since 2025/8/7
 */
@Log4j2
@ControllerAdvice
public class ResponseResultHandler implements ResponseBodyAdvice {
    private static final String RESPONSE_RESULT_ANN = "RESPONSE_RESULT_ANN";
    @Override
    public boolean supports(MethodParameter returnType, Class converterType) {
        ServletRequestAttributes sra = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = sra.getRequest();
        ResponseResult responseResult = (ResponseResult) request.getAttribute(RESPONSE_RESULT_ANN);
        return responseResult != null;
    }

    @Override
    public Object beforeBodyWrite(Object body, MethodParameter returnType, MediaType selectedContentType, Class selectedConverterType, ServerHttpRequest request, ServerHttpResponse response) {
        log.info("进入 返回体 重写格式 处理中");

        // 如果body已经是Result类型，直接返回，避免重复包装
        if (body instanceof Result) {
            return body;
        }

        // 处理各种异常类型
        if (body instanceof Exception) {
            Exception exception = (Exception) body;
            log.error("接口返回异常: ", exception);

            // 可以根据不同的异常类型返回不同的错误码和信息
            if (exception instanceof IllegalArgumentException) {
                return Result.fail(ResultCode.FAILED, exception.getMessage());
            } else if (exception instanceof RuntimeException) {
                return Result.fail(ResultCode.FAILED, "系统内部错误");
            } else {
                return Result.fail(ResultCode.FAILED, exception.getMessage());
            }
        }

        // 处理ErrorCodeResponseException
//        if (body instanceof ErrorCodeResponseException) {
//            ErrorCodeResponseException errorCodeException = (ErrorCodeResponseException) body;
//            return Result.fail(ResultCode.FAILED, errorCodeException.getMessage());
//        }

        // 处理null值
        if (body == null) {
            return Result.success(null);
        }

        // 正常情况返回成功结果
        return Result.success(body);
    }
}
