/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.boot.util.wiscom;

import lombok.Data;
import lombok.extern.log4j.Log4j2;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

/**
 *
 * @author Administrator
 */
@Log4j2
public class UDPSendUtil {
    
    public static void send(SendBean sendBean,SendStatusBean sendStatusBean){
        try{
            DatagramSocket datagramSocket=new DatagramSocket(); 
            //String senMsg = "你好！接收方！";  
            byte[] sendbuf = sendBean.getSendMsg().getBytes();
//            InetAddress ip = InetAddress.getLocalHost();
//            int port = 5000;  
//            log.info("我的IP:"+ip.getHostAddress());

            DatagramPacket  sendPacket =new DatagramPacket (sendbuf,sendbuf.length, InetAddress.getByName(sendBean.getIp()),sendBean.getPort());
            datagramSocket.send(sendPacket);

            byte[] receivebuf = new byte[1024]; 
            DatagramPacket receivePacket = new DatagramPacket(receivebuf, receivebuf.length); 
            datagramSocket.setSoTimeout(sendBean.getSotimeout());  //java.net.SocketTimeoutException: Receive timed out
            datagramSocket.receive(receivePacket);

            
            String receiveMsg = new String(receivebuf,0,receivePacket.getLength());
            //System.out.println("收到的消息:"+receiveMsg);
            
            datagramSocket.close();
            sendStatusBean.setCode(0);
            sendStatusBean.setMessage(receiveMsg);
            
        }catch(Exception   ex ){
            //log.error(ex.getMessage());
            ex.printStackTrace();
            sendStatusBean.setCode(1);
            sendStatusBean.setMessage(ex.getMessage());
        }
        
    
    }
    
    
}
@Data
class SendBean{

    private String sendMsg ;
    private String ip;
    private int port;
    private int sotimeout=4000; //socket超时时间
}
@Data
class SendStatusBean{
    private int code=200;
    private String message;
}