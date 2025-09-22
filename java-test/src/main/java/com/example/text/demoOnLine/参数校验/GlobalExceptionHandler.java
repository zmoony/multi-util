package com.example.text.demoOnLine.参数校验;

import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

/**
 * 全局异常处理
 *
 * @author yuez
 * @since 2023/2/21
 */
@Log4j2
@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler({MethodArgumentNotValidException.class, BindException.class})
    @ResponseBody
    public ResponseEntity<Object> exception(MethodArgumentNotValidException e,BindException e2, HttpServletRequest request){
        Map<String,String> result = new HashMap<>();
        BindingResult bindingResult = e.getBindingResult();
        if(e2 == null){
            bindingResult = e2.getBindingResult();
        }
        log.error("请求[{}]{}的参数校验发生错误",request.getMethod(),request.getRequestURL());
        for (ObjectError allError : bindingResult.getAllErrors()) {
            FieldError fieldError = (FieldError) allError;
            log.error("参数{}={}校验错误:{}",fieldError.getField(),fieldError.getRejectedValue(),fieldError.getDefaultMessage());
            result.put(fieldError.getField(),fieldError.getDefaultMessage());
        }
        // 一般项目都会有自己定义的公共返回实体类，这里直接使用现成的 ResponseEntity 进行返回，同时设置 Http 状态码为 400
        return ResponseEntity.badRequest().body(result);
    }


}
