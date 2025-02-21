package com.boot.bean;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.ArrayList;
import java.util.List;

/**
 * 统一结果类
 * 允许链式编程
 * Created by zy on 2020/4/20.
 */
@Data
@Accessors(chain = true)
public class R {
    private Boolean success;
    private Integer code;
    private String message;
    private Integer total;
    private Long took;
//    private Map<String, Object> data = new HashMap<>();
    private List<?> data = new ArrayList<>();
    //构造方法私有化
    private R(){}
    //通用的返回成功
    public static R ok(){
        return new R().setSuccess(ResultCodeEnum.SUCCESS.isSuccess())
                .setCode(ResultCodeEnum.SUCCESS.getCode())
                .setMessage(ResultCodeEnum.SUCCESS.getMessage());
    }
    //通用返回失败，未知错误
    public static R error(){
        return new R().setSuccess(ResultCodeEnum.UNKNOWN_ERROR.isSuccess())
                .setCode(ResultCodeEnum.UNKNOWN_ERROR.getCode())
                .setMessage(ResultCodeEnum.UNKNOWN_ERROR.getMessage());
    }
    //通用返回失败，超时
    public static R timeOut(){
        return new R().setSuccess(ResultCodeEnum.TIME_OUT.isSuccess())
                .setCode(ResultCodeEnum.TIME_OUT.getCode())
                .setMessage(ResultCodeEnum.TIME_OUT.getMessage());
    }

    //根据结果枚举自定义返回
    public static R setResult(ResultCodeEnum codeEnum){
        return new R().setSuccess(codeEnum.isSuccess())
                .setCode(codeEnum.getCode())
                .setMessage(codeEnum.getMessage());
    }

    // 自定义返回数据
    public R data(List<?> list) {
        this.setData(list);
        this.setTotal(list==null?0:list.size());
        return this;
    }

    // 自定义状态信息
    public R message(String message) {
        this.setMessage(message);
        return this;
    }

    // 自定义状态码
    public R code(Integer code) {
        this.setCode(code);
        return this;
    }

    // 自定义返回结果
    public R success(Boolean success) {
        this.setSuccess(success);
        return this;
    }
}

