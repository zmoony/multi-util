package com.boot.kafka.common;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;
import java.util.Map;

/**
 * kafka接收参数
 *
 * @author yuez
 * @version 1.0.0
 * @className RequestData
 * @date 2021/3/29 13:57
 **/
@Data
@ApiModel(value = "kafka接收参数")
public class RequestData<T> {
    @ApiModelProperty(value = "生产者的目标主题",example = "wiscom.test")
    private String topic;
    @ApiModelProperty(value = "消息体",example = "[{\"name\":\"test\"}]")
    private List<T> data;
//    private List<Map<String, Object>> data;
}
