package com.example.text.netty;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.http.HttpHeaderValues;
import io.netty.util.AsciiString;
import io.netty.util.CharsetUtil;

import java.util.concurrent.TimeUnit;

public class HttpServerHandler extends ChannelInboundHandlerAdapter {//1 只有msg为FullHttpRequest的消息才能进来。
    private AsciiString contentType = HttpHeaderValues.TEXT_PLAIN;
    //获取客户端发送过来的消息
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        ByteBuf byteBuf = (ByteBuf) msg;
        System.out.println("收到客户端" + ctx.channel().remoteAddress() + "发送的消息：" + byteBuf.toString(CharsetUtil.UTF_8));
        System.out.println("class:" + msg.getClass().getName());

        //TODO 测试运行时记得删除

        //taskQueue任务队列
        // 如果Handler处理器有一些长时间的业务处理，可以交给taskQueue异步处理
        ctx.channel().eventLoop().execute(()->{
            try {
                //长时间操作，不至于长时间的业务操作导致Handler阻塞
                Thread.sleep(1000);
                System.out.println("长时间的业务处理");
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        });

        //scheduleTaskQueue延时任务队列
        ctx.channel().eventLoop().schedule(()->{
            try {
                //长时间操作，不至于长时间的业务操作导致Handler阻塞
                Thread.sleep(1000);
                System.out.println("长时间的业务处理");
            } catch (Exception e) {
                e.printStackTrace();
            }
        },5, TimeUnit.SECONDS);//5秒后执行





    }

    //发送消息给客户端
    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        ctx.writeAndFlush(Unpooled.copiedBuffer("服务端已收到消息，并给你发送一个问号?", CharsetUtil.UTF_8));
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        System.out.println("exceptionCaught");
        if(null != cause) {
            cause.printStackTrace();
        }
        if(null != ctx) {
            ctx.close();
        }
    }
}