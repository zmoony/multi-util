package com.example.text.netty;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpRequestDecoder;
import io.netty.handler.codec.http.HttpRequestEncoder;

public class HttpServer {
    private final int port;
//    SslContext sslContext;
    public HttpServer(int port) {
        this.port = port;
        /*//https的ssl证书
        String keyStoreFilePath = "/root/.ssl/test.pkcs12";
        String keyStorePassword = "Password@123";
        try {
            KeyStore keyStore = KeyStore.getInstance("PKCS12");
            keyStore.load(new FileInputStream(keyStoreFilePath),keyStorePassword.toCharArray());

            KeyManagerFactory keyManagerFactory = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
            keyManagerFactory.init(keyStore,keyStorePassword.toCharArray());
            sslContext = SslContextBuilder.forServer(keyManagerFactory).build();
        }catch (Exception e){
            throw new RuntimeException(e);
        }*/

    }

    public static void main(String[] args) throws InterruptedException {
        new HttpServer(6666).start();
    }

    public void start() throws InterruptedException {
        //创建两个线程组 bossGroup、workerGroup
        NioEventLoopGroup bossGroup = new NioEventLoopGroup();
        NioEventLoopGroup workerGroup = new NioEventLoopGroup();
        try {
            ////创建服务端的启动对象，设置参数
            ServerBootstrap serverBootstrap = new ServerBootstrap();
            serverBootstrap.group(bossGroup,workerGroup)
                    //设置服务端通道实现类型
                    .channel(NioServerSocketChannel.class)
                    //使用匿名内部类的形式初始化通道对象
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            System.out.println("给pipeline管道设置处理器==initChannel ch:" + ch);
                            ch.pipeline()
                               /*     .addLast("decoder", new HttpRequestDecoder())
                                    .addLast("encoder", new HttpRequestEncoder())*/
                                    /**
                                     * 消息聚合器（重要）。
                                     * 为什么能有FullHttpRequest这个东西，就是因为有他，HttpObjectAggregator，
                                     * 如果没有他，就不会有那个消息是FullHttpRequest的那段Channel，同样也不会有FullHttpResponse。
                                     * 如果我们将HttpObjectAggregator(512 * 1024)的参数含义是消息合并的数据大小，如此代表聚合的消息内容长度不超过512kb。
                                     */
                                    .addLast("aggregator", new HttpObjectAggregator(512 * 1024))
                                    .addLast("hander", new HttpServerHandler());
                        }
                    })
                    //设置线程队列得到连接个数
                    .option(ChannelOption.SO_BACKLOG, 128)// determining the number of connections queued
                    //设置保持活动连接状态
                    .childOption(ChannelOption.SO_KEEPALIVE, true);
            System.out.println("java技术爱好者的服务端已经准备就绪...");
            //绑定端口号，启动服务端
            ChannelFuture channelFuture = serverBootstrap.bind(port).sync();
            //对关闭通道进行监听
            channelFuture.channel().closeFuture().sync();
        } finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }
}