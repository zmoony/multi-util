package com.boot.template.web;


import lombok.Data;

import java.io.Serializable;

/**
 * ResultResponse
 *
 * @author yuez
 * @since 2024/2/6
 */
@Data
public class ResultResponse <T> implements Serializable {
    /**
     * 接口响应状态码
     */
    private Integer code;
    /**
     * 接口响应信息
     */
    private String msg;
    /**
     * 接口响应的数据
     */
    private T data;
}
