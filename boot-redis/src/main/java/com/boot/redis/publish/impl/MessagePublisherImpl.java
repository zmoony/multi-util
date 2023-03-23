package com.boot.redis.publish.impl;

import com.boot.redis.publish.IMessagePublisher;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.stereotype.Repository;

/**
 * 发布信息实现
 *
 * @author yuez
 * @version 1.0.0
 * @className MessagePublisherImpl
 * @date 2021/4/14 17:04
 **/
@Repository(value = "messagePublisher")
public class MessagePublisherImpl implements IMessagePublisher {
    /**
     * 可以同时向多个频道发送数据
     */
    private String[] channels;

    public void setChannels(String[] channels) {
        this.channels = channels;
    }

    @Override
    public boolean sendMessage(String message) {
        /*RedisProperties.Jedis jedis = null;
        try {

        }*/
        return false;
    }
}
