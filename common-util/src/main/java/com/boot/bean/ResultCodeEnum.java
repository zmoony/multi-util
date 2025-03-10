package com.boot.bean;

import lombok.Getter;

/**
 * 结果枚举类
 * Created by zy on 2020/4/20.
 */
@Getter
public enum ResultCodeEnum {
    SUCCESS(true,20000,"成功"),
    FAILEDSHARDS(false,20001,"分片处理失败"),
    UNKNOWN_ERROR(false,50001,"未知错误"),
    PARAM_ERROR(false,50002,"参数错误"),
    HTTP_CLIENT_ERROR(false,50003,"通信异常"),
    NULL_POINT(false,50004,"空指针异常"),
    TIME_OUT(false,50005,"超时");

    private boolean success;//响应是否成功
    private Integer code;//响应状态码
    private String message;//响应信息

    ResultCodeEnum(boolean success, Integer code, String message) {
        this.success = success;
        this.code = code;
        this.message = message;
    }
}
