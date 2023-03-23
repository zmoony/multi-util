package com.boot.kafka.common;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * 通用发送格式
 *
 * @author yuez
 * @version 1.0.0
 * @className RequestBean
 * @date 2021/3/17 14:28
 **/
@Data
@Accessors(chain = true)
public class RequestBean {
    private String authnom;
    private String type;
    private List<?> content;
}
