package com.boot.nio.client;

import org.apache.mina.core.future.ConnectFuture;
import org.apache.mina.core.service.IoConnector;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.filter.codec.textline.TextLineCodecFactory;
import org.apache.mina.transport.socket.nio.NioSocketConnector;

import java.net.InetSocketAddress;
import java.nio.charset.Charset;
import java.util.Scanner;

/**
 * 创建NioSocketConnector实例作为客户端连接器
 * 配置与服务器相同的编解码器，确保双方能正确解析数据
 * 设置ClientHandler处理客户端的 IO 事件
 * 连接到服务器并获取会话
 * 通过会话发送消息到服务器
 */
public class MinaClient {
    private static final String HOST = "localhost";
    private static final int PORT = 8080;

    public static void main(String[] args) {
        // 创建NIO连接器
        IoConnector connector = new NioSocketConnector();

        // 添加编码过滤器
        connector.getFilterChain().addLast("codec",
            new ProtocolCodecFilter(new TextLineCodecFactory(Charset.forName("UTF-8"))));

        // 设置IO处理器
        connector.setHandler(new ClientHandler());

        // 连接服务器
        ConnectFuture future = connector.connect(new InetSocketAddress(HOST, PORT));
        // 等待连接完成
        future.awaitUninterruptibly();

        // 获取会话并发送消息
        IoSession session = future.getSession();

        // 从控制台读取输入并发送
        Scanner scanner = new Scanner(System.in);
        while (true) {
            String input = scanner.nextLine();
            session.write(input);
            if ("quit".equalsIgnoreCase(input)) {
                break;
            }
        }

        // 等待会话关闭
        session.getCloseFuture().awaitUninterruptibly();
        // 释放资源
        connector.dispose();
        scanner.close();
    }
}
