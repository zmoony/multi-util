package com.boot.shiro.bean;

import io.swagger.annotations.ApiModelProperty;
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
    @ApiModelProperty(required = true,notes = "接口调用标志",example = "true")
    private Boolean success;
    @ApiModelProperty(required = true,notes = "结果码",example = "200")
    private Integer code;
    @ApiModelProperty(required = true,notes = "返回信息",example = "SUCCESS")
    private String message;
//    private Map<String, Object> data = new HashMap<>();
    @ApiModelProperty(required = true,notes = "返回数据",example = "[{\"name\":\"blues\"}]")
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

