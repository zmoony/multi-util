package com.boot.bean;

import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class ServiceOperationBean {
    private String status="error"; //状态代码
    private String message;
    private int statusCode;
    private String stdOut; //标准输出
    private List<String> stdOuts;//标砖输出
    private List<Map<String,Object>> stdOutsmap;
}
