package com.example.text.demoOnLine.负载均衡;

import lombok.Getter;
import lombok.Setter;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

/**
 * Weighted Round-Robin加权轮询算法是在round-robin算法的基础上根据服务器的性能分配权重。
 * 服务器能支持的请求越多，权重就越高，分配的请求也就越多。对于同样的10个请求，使用加权轮询算法的请求分布会如下图所示：
 * requestId    weight  receive_request_server
 * 5            1       192.168.1.1
 * 3,8          2       192.168.1.2
 * 2,6,9        3       192.168.1.3
 * 1,4,7,10     4       192.168.1.4
 *
 * 可以看到192.168.1.4权重最大，分配的请求数最多。
 * @author yuez
 * @since 2023/2/17
 */
public class 加权轮询算法 {
    List<Node> servers = Arrays.asList(
            new Node("192.168.1.1",1),
            new Node("192.168.1.2",2),
            new Node("192.168.1.3",3),
            new Node("192.168.1.4",4)
    );
    private Integer totalWeight;

    public 加权轮询算法() {
        this.totalWeight = servers.stream().mapToInt(Node::getWeight).reduce((a,b)->a+b).getAsInt();
    }

    /**
     * 该算法的核心是的动态计算currentWeight。
     * 每个服务器被选中后，currentWeight需要减去所有服务器的权重之和，这样可以避免权重高的服务器一直被选中。
     * 权重高的服务器有更多的分配请求，请求可以平均分配给所有服务器。
     * @return
     */
    public String getServer(){
        Node node = servers.stream().max(Comparator.comparingInt(Node::getCurrentWeight)).get();
        node.setCurrentWeight(node.getCurrentWeight() - totalWeight);
        servers.forEach(server->server.setCurrentWeight(server.getCurrentWeight()+server.getWeight()));
        System.out.println(node.getCurrentWeight());
        return node.getIp();
    }

    public static void main(String[] args) {
        加权轮询算法 roundRobinTest = new 加权轮询算法();
        for (int i = 0; i < 10; i++) {
            String server = roundRobinTest.getServer();
            System.out.println("select server: "+server+"="+i);

        }
    }
}
@Getter
@Setter
class Node{
    private String ip;
    private Integer weight;
    private Integer currentWeight;

    public Node(String ip, Integer weight) {
        this.ip = ip;
        this.weight = weight;
        this.currentWeight = weight;
    }
}