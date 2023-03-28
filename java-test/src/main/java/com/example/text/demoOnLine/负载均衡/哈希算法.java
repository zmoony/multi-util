package com.example.text.demoOnLine.负载均衡;

import java.util.Arrays;
import java.util.List;

/**
 * 哈希算法，顾名思义，就是利用哈希表根据 计算出请求的路由hashcode%N。这里hashcode代表哈希值，N代表服务器数量。该算法的优点是实现起来非常简单
 *
 * @author yuez
 * @since 2023/2/17
 */
public class 哈希算法 {
    private static final List<String> servers = Arrays.asList("192.168.1.1", "192.168.1.2", "192.168.1.3", "192.168.1.4");

    public static String getServer(String key){
        int hash = key.hashCode();
        int index = hash%servers.size();
        return servers.get(index);
    }

    public static void main(String[] args) {
        for (int i = 0; i < 10; i++) {
            String server = getServer(String.valueOf(i));
            System.out.println("select server: "+server);
        }
    }
}
