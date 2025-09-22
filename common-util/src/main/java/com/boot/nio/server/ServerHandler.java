package com.boot.nio.server;

import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.core.session.IoSession;

/**
 * 处理器是 MINA 应用的核心，通过重写不同的方法来处理各种事件：
 * sessionCreated：连接建立时调用
 * messageReceived：收到消息时调用
 * messageSent：消息发送成功后调用
 * sessionIdle：连接空闲时调用
 * exceptionCaught：发生异常时调用
 * sessionClosed：连接关闭时调用
 */
public class ServerHandler extends IoHandlerAdapter {

    @Override
    public void sessionCreated(IoSession session) {
        // 新会话创建时调用
        System.out.println("新客户端连接: " + session.getRemoteAddress());
    }

    @Override
    public void messageReceived(IoSession session, Object message) {
        // 收到消息时调用
        String str = message.toString();
        System.out.println("收到消息: " + str);

        // 向客户端发送响应
        session.write("服务器已收到: " + str);

        // 如果收到"quit"，则关闭连接
        if (str.trim().equalsIgnoreCase("quit")) {
            session.closeNow();
        }
    }

    @Override
    public void messageSent(IoSession session, Object message) {
        // 消息发送成功后调用
        System.out.println("消息已发送: " + message);
    }

    @Override
    public void sessionIdle(IoSession session, IdleStatus status) {
        // 会话空闲时调用
        System.out.println("会话空闲: " + session.getIdleCount(status));
    }

    @Override
    public void exceptionCaught(IoSession session, Throwable cause) {
        // 发生异常时调用
        cause.printStackTrace();
        session.closeNow();
    }

    @Override
    public void sessionClosed(IoSession session) {
        // 会话关闭时调用
        System.out.println("客户端断开连接: " + session.getRemoteAddress());
    }
}
