package com.example.common;

import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;

import javax.annotation.PostConstruct;
import java.io.IOException;

/**
 * ZkClient 初始化连接
 *
 * @author yuez
 * @since 2023/3/31
 */
public class ZkClient implements Watcher {
    private static final String address = "172.18.12.144:2181";

    public ZooKeeper initZk()  {
        try {
            return new ZooKeeper(address,500_000,this);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void process(WatchedEvent watchedEvent) {

    }

}
