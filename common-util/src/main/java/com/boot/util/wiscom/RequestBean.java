package com.boot.util.wiscom;

import lombok.Data;

import java.util.List;

@Data
public class RequestBean {
    private String authnom;
    private  int type;
    private List<?> content;
}
