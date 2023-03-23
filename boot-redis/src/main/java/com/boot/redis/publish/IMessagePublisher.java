package com.boot.redis.publish;


/**
 * 发布者接口
 *
 * @author yuez
 * @version 1.0.0
 * @className IMessagePublisher
 * @date 2021/4/14 17:03
 **/
public interface IMessagePublisher {
    boolean sendMessage(String message);
}
