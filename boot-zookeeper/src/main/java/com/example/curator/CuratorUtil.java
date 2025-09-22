package com.example.curator;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.cache.*;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.data.Stat;

import java.util.List;

/**
 * CuratorUtil
 *
 * @author yuez
 * @since 2023/3/31
 */
public class CuratorUtil {

    public static void main(String[] args) throws Exception {
        CuratorUtil curatorUtil = new CuratorUtil("");
        String path = "/yes";
        curatorUtil.getNodeChildren("/");
//        curatorUtil.addWatcherWithTreeCache("/");
//        curatorUtil.createNode(path,"172.18.12.144:2181");
//        curatorUtil.getNodeData(path);
//        curatorUtil.setNodeData(path,"172.17.112.104");
//        curatorUtil.getNodeData(path);
    }

    private CuratorFramework client;

    public CuratorUtil(String path) {
        client = new ZkCuratorClient().initZk(path);
        client.start();
    }

    /**
     * 创建节点
     * *__SEQUENTIAL:返回节点的名字（会加上序列号） 后续操作要注意
     * EPHEMERAL:程序运行结束就没有了
     *
     * @param path
     * @param value
     * @return
     */
    public void createNode(String path, String value) throws Exception {
        String node = client.create()
                .creatingParentsIfNeeded()
                .withMode(CreateMode.PERSISTENT)
                .forPath(path, value.getBytes());
        getNodeData(path);
        System.out.println(node);
    }

    /**
     * 删除节点
     *
     * @param path
     * @throws Exception
     */
    public void deleteNode(String path) throws Exception {
        Void unused = client.delete()
                .deletingChildrenIfNeeded()
                .forPath(path);
    }

    /**
     * 获取子节点
     *
     * @param path
     * @throws Exception
     */
    public void getNodeChildren(String path) throws Exception {
        List<String> nodes = client.getChildren()
                .forPath(path);
        System.out.println(nodes);
    }

    /**
     * 获取节点内容
     *
     * @param path
     * @return
     * @throws Exception
     */
    public String getNodeData(String path) throws Exception {
        Stat stat = new Stat();
        byte[] bytes = client.getData().storingStatIn(stat).forPath(path);
        System.out.println(new String(bytes));
        return new String(bytes);
    }

    /**
     * 设置节点内容
     *
     * @param path
     * @param value
     * @throws Exception
     */
    public void setNodeData(String path, String value) throws Exception {
        Stat stat = client.checkExists().forPath(path);
        if (null == stat) {
            throw new RuntimeException(String.format("{} Znode is not exists", path));
        }
        getNodeData(path);
        client.setData().withVersion(stat.getVersion()).forPath(path, value.getBytes());
        getNodeData(path);
    }

    /**
     * 节点是否存在
     *
     * @param path
     * @return
     */
    public boolean existNode(String path) throws Exception {
        Stat stat = client.checkExists().forPath(path);
        return stat == null ? false : true;
    }

    /**
     * 本节点监听（不包括删除）
     *
     * @param path
     * @throws Exception
     */
    public void addWatcherWithNodeCache(String path) throws Exception {
        NodeCache nodeCache = new NodeCache(client, path, false);
        NodeCacheListener listener = () -> {
            ChildData currentData = nodeCache.getCurrentData();
            System.out.println("currentDate====>" + currentData);
        };
        nodeCache.getListenable().addListener(listener);
        nodeCache.start();
    }

    public void addWatcherWithNodeCache(String path,NodeCacheListener listener) throws Exception {
        NodeCache nodeCache = new NodeCache(client, path, false);
        nodeCache.getListenable().addListener(listener);
        nodeCache.start();
    }

    /**
     * 获取子节点的更新
     *
     * @param path
     * @throws Exception
     */
    public void addWatcherWithChildCache(String path) throws Exception {
        PathChildrenCache childrenCache = new PathChildrenCache(client, path, false);
        PathChildrenCacheListener listener = (client, event) -> {
            System.out.println("evenr path :" + event.getData().getPath() + "event type is" + event.getType());
        };
        childrenCache.getListenable().addListener(listener);
        childrenCache.start(PathChildrenCache.StartMode.NORMAL);
    }

    /**
     * 当前节点和子节点的监听
     *
     * @param path
     * @throws Exception
     */
    public void addWatcherWithTreeCache(String path) throws Exception {
        TreeCache treeCache = new TreeCache(client, path);
        TreeCacheListener listener = (client, event) -> {
            System.out.println("节点路径 --" + event.getData().getPath() + " ,节点事件类型: " + event.getType() + " , 节点值为: " + event.getData() + "");
        };
        treeCache.getListenable().addListener(listener);
        treeCache.start();
    }


}
