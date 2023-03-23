package com.boot.util.ftp;

import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.pool2.impl.GenericObjectPool;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;

/**
 * ftp连接池
 *
 * @author yuez
 * @since 2022/6/14
 */
public class FtpClientPool {
    private static FtpClientPool instance = new FtpClientPool();
    private FtpClientPool(){
        GenericObjectPoolConfig genericObjectPoolConfig = new GenericObjectPoolConfig();
        genericObjectPoolConfig.setMaxTotal(50);
//        genericObjectPoolConfig.setMaxIdle(10);
        genericObjectPoolConfig.setMaxIdle(100);
        genericObjectPoolConfig.setTestOnBorrow(true);
        genericObjectPoolConfig.setTestOnCreate(true);
        genericObjectPoolConfig.setTestOnReturn(true);
        genericObjectPoolConfig.setTestWhileIdle(true);
        ftpClientPool = new GenericObjectPool<>(new FtpClientFactory(),genericObjectPoolConfig);
    }

    public GenericObjectPool<FTPClient> ftpClientPool;

    public static FtpClientPool getInstance() {
        return instance;
    }



}
