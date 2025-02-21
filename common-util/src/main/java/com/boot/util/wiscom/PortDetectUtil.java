/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.boot.util.wiscom;

import lombok.extern.log4j.Log4j2;

import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;

/**
 *
 * @author Administrator
 */
@Log4j2
public class PortDetectUtil {
    /**
     * 检测端口是否被占用，false表示被占用，true表示未使用
     * "127.0.0.1"以及"0.0.0.0" 也是表示本机
     * @param host
     * @param port
     * @return 
     */
    public static boolean isPortUsing(String host,int port) {  
        boolean flag = true;  
        try {  
            InetAddress theAddress = InetAddress.getByName(host);  
            try (Socket socket = new Socket(theAddress,port)) {
                flag = false;
            }
        }catch (IOException e) {  
            log.error("端口未被占用");
        }  
        return flag;  
    }  
    
}
