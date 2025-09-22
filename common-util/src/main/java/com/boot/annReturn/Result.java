package com.boot.annReturn;

import lombok.Data;

import java.io.Serializable;

/**
 * Result
 *
 * @author yuez
 * @since 2025/8/7
 */
@Data
public class Result implements Serializable {
    private static final long serialVersionUID = 1L;

    private Integer code;
    private String message;
    private Object data;

    public Result(){}

    public Result(Integer code, String message, Object data){
        this.code = code;
        this.message = message;
        this.data = data;
    }

    public static Result success() {
        Result result = new Result();
        result.setCode(ResultCode.SUCCESS.code());
        result.setMessage(ResultCode.SUCCESS.message());
        return result;
    }

    public static Result success(Object data) {
        Result result = new Result();
        result.setCode(ResultCode.SUCCESS.code());
        result.setMessage(ResultCode.SUCCESS.message());
        result.setData(data);
        return result;
    }

    public static Result success(String message, Object data) {
        Result result = new Result();
        result.setCode(ResultCode.SUCCESS.code());
        result.setMessage(message);
        result.setData(data);
        return result;
    }

   //失败
    public static Result fail(String message) {
        Result result = new Result();
        result.setCode(ResultCode.FAILED.code());
        result.setMessage(message);
        return result;
    }

    public static Result fail(ResultCode resultCode) {
        Result result = new Result();
        result.setCode(resultCode.code());
        result.setMessage(resultCode.message());
        return result;
    }

    public static Result fail(ResultCode resultCode, String message) {
        Result result = new Result();
        result.setCode(resultCode.code());
        result.setMessage(message);
        return result;
    }

    public static Result fail(ResultCode resultCode, Object data) {
        Result result = new Result();
        result.setCode(resultCode.code());
        result.setMessage(resultCode.message());
        result.setData(data);
        return result;
    }

    public static Result fail(Object data) {
        return fail(ResultCode.FAILED, data);
    }

}
