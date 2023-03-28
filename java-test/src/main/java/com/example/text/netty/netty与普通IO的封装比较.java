package com.example.text.netty;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.channel.oio.OioEventLoopGroup;
import io.netty.channel.socket.oio.OioServerSocketChannel;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.util.Iterator;
import java.util.Set;

/**
 * 1. 阻塞I/O
 * 2. 非阻塞IO{
 *     NIO的类库和API繁杂，学习成本高，你需要熟练掌握Selector、ServerSocketChannel、SocketChannel、ByteBuffer等。
 *      需要熟悉Java多线程编程。这是因为NIO编程涉及到Reactor模式，你必须对多线程和网络编程非常熟悉，才能写出高质量的NIO程序。
 *      臭名昭著的epoll bug。它会导致Selector空轮询，最终导致CPU 100%。直到JDK1.7版本依然没得到根本性的解决。
 * }
 * 3. Netty:并发高，传输快，封装好{
 *     API使用简单，学习成本低。
 *      功能强大，内置了多种解码编码器，支持多种协议。
 *      性能高，对比其他主流的NIO框架，Netty的性能最优。
 *      社区活跃，发现BUG会及时修复，迭代版本周期短，不断加入新的功能。
 *      Dubbo、Elasticsearch都采用了Netty，质量得到验证。
 * }
 *
 * @author yuez
 * @since 2023/3/27
 */
public class netty与普通IO的封装比较 {

}
//阻塞I/O
class PlainBioServer{
    public void serve(int port) throws IOException {
        ServerSocket socket = new ServerSocket(port);
        try {
            for (;;){
                Socket clientSocket = socket.accept();
                System.out.println("accept connect from :"+clientSocket);
                new Thread(()->{
                    try (OutputStream out = clientSocket.getOutputStream();){
                        out.write("Hi!\r\n".getBytes(Charset.forName("UTF-8")));
                        out.flush();
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }).start();
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
//非阻塞IO
class PlainNioServer{
    public void serve(int port) throws IOException {
        ServerSocketChannel socketChannel = ServerSocketChannel.open();
        socketChannel.configureBlocking(false);
        ServerSocket ss = socketChannel.socket();
        InetSocketAddress address = new InetSocketAddress(port);
        ss.bind(address);//1
        Selector selector = Selector.open();//2
        socketChannel.register(selector, SelectionKey.OP_ACCEPT);//3
        ByteBuffer msg = ByteBuffer.wrap("Hi\r\n".getBytes());
        for (;;){
            selector.select();//4
            Set<SelectionKey> readyKeys = selector.selectedKeys();//5
            Iterator<SelectionKey> iterator = readyKeys.iterator();
            while(iterator.hasNext()){
                SelectionKey key = iterator.next();
                if (key.isAcceptable()) {//6
                    ServerSocketChannel server = (ServerSocketChannel) key.channel();
                    SocketChannel client = server.accept();
                    client.configureBlocking(false);
                    client.register(selector,SelectionKey.OP_WRITE | SelectionKey.OP_READ,msg.duplicate());//7
                    System.out.println("Accepted connection from " + client);
                }
                if (key.isWritable()) {//8
                    SocketChannel  channel = (SocketChannel) key.channel();
                    ByteBuffer attachment = (ByteBuffer) key.attachment();
                    while (attachment.hasRemaining()){
                        if (channel.write(attachment) == 0) {//9
                            break;
                        }
                    }
                    channel.close();
                }
                key.cancel();
                key.channel().close();
            }
        }
    }
}
//netty
class NettyOioServer{
    public void serve(int port) throws InterruptedException {
        ByteBuf buf = Unpooled.unreleasableBuffer(Unpooled.copiedBuffer("hi /r/n", Charset.forName("utf-8")));
        OioEventLoopGroup group = new OioEventLoopGroup();
        ServerBootstrap b = new ServerBootstrap();
        b.group(group)
                .channel(OioServerSocketChannel.class)
                .localAddress(new InetSocketAddress(port))
                .childHandler(new ChannelInitializer<io.netty.channel.socket.SocketChannel>() {

                    @Override
                    protected void initChannel(io.netty.channel.socket.SocketChannel ch) throws Exception {
                        ch.pipeline().addLast(new ChannelInboundHandlerAdapter(){
                            @Override
                            public void channelActive(ChannelHandlerContext ctx) throws Exception {
                                ctx.writeAndFlush(buf.duplicate())
                                        .addListener(ChannelFutureListener.CLOSE);
                            }
                        });
                    }
                });
        ChannelFuture f = b.bind().sync();
        f.channel().closeFuture().sync();
        group.shutdownGracefully().sync();
    }
}


