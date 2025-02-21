package com.boot.template.web;

/**
 * ResultUtil
 * 封装成功响应的方法
 *
 * @author yuez
 * @since 2024/2/6
 */
public class ResultUtil {
    /**
     * 封装成功响应的方法
     *
     * @param data 响应数据
     * @param <T>  响应数据类型
     * @return reponse
     */
    public static <T> ResultResponse<T> success(T data) {
        ResultResponse<T> response = new ResultResponse<>();
        response.setData(data);
        response.setCode(StatusEnum.SUCCESS.code);
        return response;
    }

    /**
     * 封装error的响应
     *
     * @param statusEnum error响应的状态值
     * @param <T>
     * @return
     */
    public static <T> ResultResponse<T> error(StatusEnum statusEnum) {
        return error(statusEnum, statusEnum.message);
    }

    /**
     * 封装error的响应    可自定义错误信息
     *
     * @param statusEnum error响应的状态值
     * @param <T>
     * @return
     */
    public static <T> ResultResponse<T> error(StatusEnum statusEnum, String errorMsg) {
        ResultResponse<T> response = new ResultResponse<>();
        response.setCode(statusEnum.code);
        response.setMsg(errorMsg);
        return response;
    }

}

