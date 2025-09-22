package com.boot.nio.signal;

import com.boot.nio.signal.bean.GlobalConfig;
import com.boot.nio.signal.bean.GlobalUtil;
import lombok.extern.log4j.Log4j2;
import org.apache.mina.core.filterchain.DefaultIoFilterChainBuilder;
import org.apache.mina.core.filterchain.IoFilter;
import org.apache.mina.core.service.IoHandler;
import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.filter.executor.ExecutorFilter;
import org.apache.mina.transport.socket.SocketSessionConfig;
import org.apache.mina.transport.socket.nio.NioSocketAcceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Log4j2
@Component
public class LesMinaServer {
    public static final int minaCorePoolSize = 5;
    public static final int minaMaximumPoolSize = 30;
    public static final int minaKeepAliveTime = 120;
    public static final int MINA_RECEIVE_BUFFER_SIZE = 20 * 1024 * 1024;
    public static final int MINA_SEND_BUFFER_SIZE = 20 * 1024 * 1024;
    private NioSocketAcceptor acceptor;

    @Autowired
    private LesMessageHandler handler;

    @Autowired
	private GlobalConfig globalConfig;

    public void init() throws IOException {
        acceptor = new NioSocketAcceptor();
        IoHandler ioHandler = (IoHandler)new ServerHandler();
        acceptor.setHandler(ioHandler);
        DefaultIoFilterChainBuilder defaultIoFilterChainBuilder = acceptor.getFilterChain();
        LesProtocolCoder protocolCodec = new LesProtocolCoder();
        defaultIoFilterChainBuilder.addLast("codec", (IoFilter)new ProtocolCodecFilter(protocolCodec, protocolCodec));
        ExecutorFilter executorFilter = new ExecutorFilter(minaCorePoolSize, minaMaximumPoolSize, minaKeepAliveTime, TimeUnit.SECONDS);
        defaultIoFilterChainBuilder.addLast("threadPool", (IoFilter)executorFilter);
        SocketSessionConfig socketSessionConfig = acceptor.getSessionConfig();
        socketSessionConfig.setReceiveBufferSize(MINA_RECEIVE_BUFFER_SIZE);
        socketSessionConfig.setSendBufferSize(MINA_SEND_BUFFER_SIZE);

        start();
    }

    public void start() throws IOException {
        acceptor.setReuseAddress(true);
       /* acceptor.bind(new InetSocketAddress(globalConfig.getServerIp(), globalConfig.getLesServerPort()));
        log.info("莱斯 server started !!!!! port:" + globalConfig.getLesServerPort());*/
    }

    public boolean isActive() {
    	if(null == acceptor) {
    		return false;
    	}
    	return acceptor.isActive();
    }

    public void shutdown() {
        acceptor.unbind();
        acceptor.dispose(false);
    }

    public class ServerHandler extends IoHandlerAdapter {
        @Override
        public void sessionOpened(final IoSession session) throws Exception {
            log.info("sessionOpened---" + session.getRemoteAddress());
            if(!session.getRemoteAddress().toString().contains(globalConfig.getLesServerIp().trim())) {
				log.info("非莱斯客户端连接，主动断开");
				sessionClosed(session);
			}
//            LesCacheUtil.OPEN = true;
        }
        @Override
        public void sessionCreated(final IoSession session) throws Exception {
            SocketSessionConfig cfg = (SocketSessionConfig) session.getConfig();
            cfg.setSoLinger(0);
            cfg.setKeepAlive(true);
        }
        @Override
        public void sessionClosed(final IoSession session) throws Exception {
            try {
                log.info("sessionClosed---" + session.getRemoteAddress());
                if (session.getRemoteAddress().toString().contains(globalConfig.getLesServerIp().trim())) {
                    log.info("！！！！莱斯客户端连接断开！！！！");
//                    LesCacheUtil.OPEN = false;
                    Map<String, String> connectMap = new HashMap<>();
                    connectMap.put(GlobalUtil.RUNNINGTYPE_KEY, GlobalUtil.RUNNINGTYPE_CONNECTION);
                    connectMap.put("sys_id", GlobalUtil.LES);
                    connectMap.put("operation", "disconnect");
//                    GlobalUtil.queue.put(connectMap);
                }
            } catch (Exception e) {
                log.info("莱斯连接断开：",e);
            }
        }
        @Override
        public void messageReceived(final IoSession session, final Object message) throws Exception {
            log.info("messageReceived:" + message);
            handler.handleMessage(session, message);
        }

        @Override
        public void messageSent(final IoSession session, final Object message) throws Exception {
            log.info("messageSent:" + message);
        }
    }
}
