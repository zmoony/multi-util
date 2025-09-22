package org.scaffold.difyapi.exception;


import freemarker.core.UnexpectedTypeException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.scaffold.difyapi.bean.ResultResponse;
import org.scaffold.difyapi.bean.ResultUtil;
import org.scaffold.difyapi.bean.StatusEnum;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.ObjectError;
import org.springframework.web.HttpMediaTypeException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.validation.ConstraintViolationException;
import java.util.List;
import java.util.stream.Collectors;

/**
 * ExceptionAdvice
 *
 * @author yuez
 * @since 2024/2/6
 */
@Slf4j
@ControllerAdvice
public class ExceptionAdvice {
    /**
     * 处理ServiceException
     *
     * @param serviceException ServiceException
     * @param request          请求参数
     * @return 接口响应
     */
    @ExceptionHandler(ServiceException.class)
    @ResponseBody
    public ResultResponse<Void> handleServiceException(ServiceException serviceException, HttpServletRequest request) {
        log.warn("request  {}  throw  ServiceException  \n", request, serviceException);
        return ResultUtil.error(serviceException.getStatus(), serviceException.getMessage());
    }

    /**
     * 其他异常拦截
     *
     * @param ex      异常
     * @param request 请求参数
     * @return 接口响应
     */
    @ExceptionHandler(Exception.class)
    @ResponseBody
    public ResultResponse<Void> handleException(Exception ex, HttpServletRequest request) {
        log.error("request  {}  throw  unExpectException  \n", request, ex);
        return ResultUtil.error(StatusEnum.SERVICE_ERROR);
    }


    /**
     * 参数非法校验
     *
     * @param ex
     * @return
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseBody
    public ResultResponse<Void> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
        try {
            List<ObjectError> errors = ex.getBindingResult().getAllErrors();
            String message = errors.stream().map(ObjectError::getDefaultMessage).collect(Collectors.joining(","));
            log.error("param  illegal:  {}", message);
            return ResultUtil.error(StatusEnum.PARAM_INVALID, message);
        } catch (Exception e) {
            return ResultUtil.error(StatusEnum.SERVICE_ERROR);
        }
    }

    @ExceptionHandler(UnexpectedTypeException.class)
    @ResponseBody
    public ResultResponse<Void> handleUnexpectedTypeException(UnexpectedTypeException ex,HttpServletRequest request) {
        log.error("catch  UnexpectedTypeException,  errorMessage:  \n", ex);
        return ResultUtil.error(StatusEnum.PARAM_INVALID, ex.getMessage());
    }


    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseBody
    public  ResultResponse<Void>  handlerConstraintViolationException(ConstraintViolationException  ex,  HttpServletRequest  request)  {
        log.error("request  {}  throw  ConstraintViolationException  \n",  request,  ex);
        return  ResultUtil.error(StatusEnum.PARAM_INVALID,  ex.getMessage());
    }

    @ResponseBody
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public  ResultResponse<Void>  handleHttpMessageNotReadableException(HttpMessageNotReadableException  ex,
                                                                      HttpServletRequest  request)  {
        log.error("request  {}  throw  ucManagerException  \n",  request,  ex);
        return  ResultUtil.error(StatusEnum.SERVICE_ERROR);
    }

    @ExceptionHandler({HttpRequestMethodNotSupportedException.class,  HttpMediaTypeException.class})
    @ResponseBody
    public  ResultResponse<Void>  handleMethodNotSupportedException(Exception  ex)  {
        log.error("HttpRequestMethodNotSupportedException  \n",  ex);
        return  ResultUtil.error(StatusEnum.HTTP_METHOD_NOT_SUPPORT);
    }


}
