package com.example.text.demoOnLine.负载均衡;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

/**
 * Round-Robin轮询算法是另一种经典的负载均衡算法。请求以循环的方式分发到集群中的所有服务器。同理，对于上述四台服务器，假设客户端向集群发送10个请求，则请求分布将如下图所示：
 * 1.1-》1，5，9   1.2-》2，6，10  1.3-》3，7  1.3-》4，8
 * Round-Robin轮询算法是另一种经典的负载均衡算法。请求以循环的方式分发到集群中的所有服务器。同理，对于上述四台服务器，假设客户端向集群发送10个请求，则请求分布将如下图所示：
 * @author yuez
 * @since 2023/2/16
 */
public class 轮询算法 {
    private static final List<String> servers = Arrays.asList("192.168.1.1", "192.168.1.2", "192.168.1.3", "192.168.1.4");

    public static String getServer(int i){
        int index = i%servers.size();
        return servers.get(index);
    }

    public static void main(String[] args) {
        for (int i = 0; i < 10; i++) {
            String server = getServer(i);
            System.out.println("select server: "+server+"="+i);
        }
    }
}
