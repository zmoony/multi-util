/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.boot.util.wiscom.存储服务.bean;

import lombok.Data;

/**
 * 存储服务参数对象
 * @author 13900
 */
@Data
public class FileStoreBean {
    private String pass_time;
    private byte[] content;
    private String file_suffix;
    private String head_path;
    private String bottom_path;

}
