package com.boot.util.wiscom.存储服务.bean;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * FileStoreConfig
 *
 * @author yuez
 * @since 2025/2/18
 */
@Data
@AllArgsConstructor
public class FileStoreConfig {
    private String fileStoreUrl;
    private String thriftHost;
    private int thriftPort;
    private int thriftTimeout = 3000;
}
