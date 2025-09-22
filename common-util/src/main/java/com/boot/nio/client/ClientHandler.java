package com.boot.nio.client;

import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.core.session.IoSession;

public class ClientHandler extends IoHandlerAdapter {

    @Override
    public void sessionCreated(IoSession session) {
        System.out.println("连接到服务器: " + session.getRemoteAddress());
    }

    @Override
    public void messageReceived(IoSession session, Object message) {
        // 收到服务器响应
        String response = message.toString();
        System.out.println("服务器响应: " + response);
    }

    @Override
    public void exceptionCaught(IoSession session, Throwable cause) {
        cause.printStackTrace();
        session.closeNow();
    }

    @Override
    public void sessionClosed(IoSession session) {
        System.out.println("与服务器断开连接");
    }
}
