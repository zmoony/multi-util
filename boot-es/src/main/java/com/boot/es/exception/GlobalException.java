package com.boot.es.exception;

import com.boot.es.common.ResultCodeEnum;
import lombok.Data;

/**
 * 自定义全局异常
 * 自定一个异常类，捕获针对项目或业务的异常;
 * 异常的对象信息补充到统一结果枚举中；
 * Created by zy on 2020/4/20.
 */
@Data
public class GlobalException extends Exception {
    private Integer code;

    public GlobalException(Integer code,String message){
        super(message);
        this.code = code;
    }

    public GlobalException(ResultCodeEnum resultCodeEnum){
        super(resultCodeEnum.getMessage());
        this.code = resultCodeEnum.getCode();
    }

    @Override
    public String toString() {
        return "GlobalException{" +
                "code=" + code +
                ", message=" + this.getMessage() +
                '}';
    }
}
