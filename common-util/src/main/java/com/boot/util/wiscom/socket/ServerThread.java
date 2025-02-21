package com.boot.util.wiscom.socket;

import lombok.extern.log4j.Log4j2;

import java.io.DataInputStream;
import java.net.Socket;

@Log4j2
public class ServerThread extends Thread {

    private Socket socket;
    private boolean isOnline = true;

    public ServerThread(Socket socket) {
        this.socket = socket;
    }

    public void run() {
        try {
            while (true) {
                DataInputStream dis = new DataInputStream(socket.getInputStream());
                byte[] bytes = new byte[1024];
                log.info("收到数据：" + new String(bytes, 0, dis.read(bytes)));
            }
        } catch (Exception e) {
            //e.printStackTrace();
            log.error("读取客户端信息报错：" + e);
        }


    }

}
