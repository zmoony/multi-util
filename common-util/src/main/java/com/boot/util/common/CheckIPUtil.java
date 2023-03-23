/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.boot.util.common;

import lombok.extern.log4j.Log4j2;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.*;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author Administrator
 */
@Log4j2
public class CheckIPUtil {
    /**
     * 检测网络是否可用  采用ping方式
     * @param host
     * @param count
     * @return 
     */
    public static boolean isHostReachablebyping(String host, int count) {
        
        Runtime runtime = Runtime.getRuntime();  
        StringBuilder sb = new StringBuilder();
        try {
            Process process = runtime.exec("ping " +host +" -n "+count);  
            String line;
            BufferedReader	bufferedReader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            while ((line = bufferedReader.readLine()) != null) {
                    sb.append(line);
            }

            if(sb.toString().contains("TTL") || sb.toString().contains("ttl") ){
                return true;
            }
        } catch (IOException ex) {
            log.error(ex.getMessage());
        }
        return false;
    }
    /**
     * 检测网络是否可用 采用InetAddress.getByName(host).isReachable(timeOut)  单位毫秒
     * @param host
     * @param timeOut
     * @return 
     */
    public static boolean isHostReachable(String host, Integer timeOut) {
        try {
            return InetAddress.getByName(host).isReachable(timeOut);
        } catch (UnknownHostException e) {
            log.error("网络检测异常"+e.getMessage());
        } catch (IOException e) {
            log.error("网络检测异常"+e.getMessage());
        }
        return false;
    }
    /**
     * 展示结果
     * @param host
     * @param count
     * @return 
     */
    public static String displayPingResult(String host, int count) {
        String result=null;
        String line = null;
        Runtime runtime = Runtime.getRuntime();  
        StringBuilder sb = new StringBuilder();
        try {
            Process process = runtime.exec("ping " +host +" -n "+count);  
		BufferedReader	bufferedReader = new BufferedReader(new InputStreamReader(process.getInputStream()));
                while ((line = bufferedReader.readLine()) != null) {
                        sb.append(line).append("\n");
                }

            result=sb.toString();
        } catch (IOException ex) {
            log.error(ex.getMessage());
        }
        return result;
    }
    /**
     * 获取本机IP列表  [127.0.0.1, 10.1.25.174, 172.17.112.2]
     * @return 
     */
    public static Map<String,Integer> getLocalIPMap() {
        Map<String,Integer> map= new HashMap<>();
        try {
            Enumeration<NetworkInterface> networkInterfaces = NetworkInterface.getNetworkInterfaces();
            NetworkInterface networkInterface;
            Enumeration<InetAddress> inetAddresses;
            InetAddress inetAddress;
            String ip;
            while (networkInterfaces.hasMoreElements()) {
                networkInterface = networkInterfaces.nextElement();
                inetAddresses = networkInterface.getInetAddresses();
                while (inetAddresses.hasMoreElements()) {
                    inetAddress = inetAddresses.nextElement();
                    // IPV4
                    if (inetAddress != null && inetAddress instanceof Inet4Address) {
                        ip = inetAddress.getHostAddress();
                        map.put(ip, Integer.SIZE);
                    }
                }
            }
        } catch (SocketException e) {
            log.error(e.getMessage());
        }
        return map;
    }
    

    
}
