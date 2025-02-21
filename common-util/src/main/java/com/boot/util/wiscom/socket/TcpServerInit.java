package com.boot.util.wiscom.socket;

import com.boot.util.wiscom.GlobalObject;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.ServerSocket;

@Log4j2
@Service
public class TcpServerInit {

    private ServerSocket serverSocket;

    @Value("${tcp.port}")
    private String tcpPort;

    public void init(){
        try {
            int port = Integer.parseInt(tcpPort);
            serverSocket = new ServerSocket(port);
            while (true) {
                GlobalObject.socket = serverSocket.accept();
                // 每接受一个线程，就随机生成一个一个新用户
                log.info(GlobalObject.socket.getInetAddress() + "登录。。。");
                // 创建一个新的线程，接收信息并转发
                ServerThread thread = new ServerThread(GlobalObject.socket);
                thread.start();
            }
        } catch (IOException e) {
            e.printStackTrace();
            log.error("服务端初始化出错："+e);
        }finally {
            try {
                serverSocket.close();
            }catch (Exception e){
                e.printStackTrace();
                log.error("服务端关闭报错："+e);
            }
        }


    }


}
