package com.boot.websocket.vo;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;

/**
 * Mutlipart
 *
 * @author yuez
 * @since 2024/11/4
 */
@Data
public class Params {
    private String name;
    private String age;
    private String pwd;
    private String fileName;
    //多媒体文件
    private MultipartFile file;
}
