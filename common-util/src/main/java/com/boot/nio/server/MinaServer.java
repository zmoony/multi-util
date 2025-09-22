package com.boot.nio.server;

import org.apache.mina.core.service.IoAcceptor;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.filter.codec.textline.TextLineCodecFactory;
import org.apache.mina.transport.socket.nio.NioSocketAcceptor;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.charset.Charset;

/**
 * 创建NioSocketAcceptor实例，这是 MINA 的 NIO 服务器实现
 * 配置过滤器链，这里使用TextLineCodecFactory处理文本数据的编码和解码
 * 设置自定义的ServerHandler处理各种 IO 事件
 * 配置会话参数（缓冲区大小、空闲时间等）
 * 绑定端口并启动服务器
 *
 *
 * 自定义编解码器：对于复杂协议，可以实现ProtocolEncoder和ProtocolDecoder接口
 * SSL/TLS 支持：添加SslFilter可以实现加密通信
 * 线程池配置：可以通过setExecutor方法配置自定义线程池
 * 监控与统计：使用 MINA 提供的监控功能跟踪连接数、消息量等指标
 */
public class MinaServer {
    private static final int PORT = 8080;

    public static void main(String[] args) throws IOException {
        // 创建NIO acceptor
        IoAcceptor acceptor = new NioSocketAcceptor();

        // 添加编码过滤器，使用文本行编解码器
        acceptor.getFilterChain().addLast("codec",
            new ProtocolCodecFilter(new TextLineCodecFactory(Charset.forName("UTF-8"))));

        // 设置IO处理器
        acceptor.setHandler(new ServerHandler());

        // 配置会话参数
        acceptor.getSessionConfig().setReadBufferSize(2048);
        acceptor.getSessionConfig().setIdleTime(IdleStatus.BOTH_IDLE, 10);

        // 绑定端口并启动服务器
        acceptor.bind(new InetSocketAddress(PORT));
        System.out.println("服务器已启动，监听端口: " + PORT);
    }
}
