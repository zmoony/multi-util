package com.example.text.demoOnLine.负载均衡;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

/**
 * 随机算法非常简单，该算法的核心是通过随机函数随机获取一个服务器进行访问。
 * 假设我们现在有四台服务器，192.168.1.1~ 192.168.1.4
 *
 * 当样本较小时，算法可能分布不均匀，但根据概率论，样本越大，负载会越均匀，
 * 而负载均衡算法本来就是为应对高并发场景而设计的。该算法的另一个缺点是所有机器都有相同的访问概率, 如果服务器性能不同，负载将不平衡。
 * @author yuez
 * @since 2023/2/16
 */
public class 随机算法 {
    private static final List<String> servers = Arrays.asList("192.168.1.1", "192.168.1.2", "192.168.1.3", "192.168.1.4");

    public static String getServer(){
        Random random = new Random();
        int index = random.nextInt(servers.size());
        return servers.get(index);
    }

    public static void main(String[] args) {
        /*for (int i = 0; i < 10; i++) {
            String server = getServer();
            System.out.println("select server: "+server);
        }*/
        System.out.println(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyMMddHHmmss")));
        System.out.println(new Random().nextInt(10));
        System.out.println("321084199210285520".length());
        System.out.println("321084199210285520".charAt(18-2));
        System.out.println("2022-01-12 00:00:00".replaceAll("-","").replaceAll(":","").replaceAll(" ",""));
    }


}
