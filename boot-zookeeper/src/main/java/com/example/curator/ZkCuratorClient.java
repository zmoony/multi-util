package com.example.curator;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;

import java.io.IOException;

/**
 * ZkCuratorClient
 *
 * @author yuez
 * @since 2023/3/31
 */
public class ZkCuratorClient {
    private static final String address = "172.18.12.144:2181";
    /**
     * session 超时时间
     */
    private int timeOut = 60_000;
    /**
     * zkclient重试间隔时间
     */
    private int baseSleepTimeMs = 5_000;
    /**
     * zkclient重试次数
     */
    private int retryCount = 5;

    public CuratorFramework initZk(String path)  {
        try {
            return CuratorFrameworkFactory.builder()
                    .connectString(address)
                    .sessionTimeoutMs(timeOut)
                    .retryPolicy(new ExponentialBackoffRetry(baseSleepTimeMs,retryCount))
                    .namespace(path)
                    .build();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public void closeConnection(CuratorFramework client){
        if (client != null) {
            client.close();
        }
    }
}
