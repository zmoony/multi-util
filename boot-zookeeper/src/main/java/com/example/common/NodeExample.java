package com.example.common;

import org.apache.zookeeper.*;
import org.apache.zookeeper.data.Stat;

import java.time.LocalDateTime;
import java.util.List;

/**
 * NodeExample
 *
 * @author yuez
 * @since 2023/3/31
 */
public class NodeExample implements Watcher {

    public static void main(String[] args) throws InterruptedException, KeeperException {
        NodeExample nodeExample = new NodeExample();
//        nodeExample.watchEvent("");
//        nodeExample.dataChange("/yes");
//        nodeExample.deleteNode("/yes");
//        nodeExample.nodeChildren("/");//path:/ 's children:[dubbo, zookeeper]
    }

    private ZooKeeper zooKeeper;

    public NodeExample(){
        zooKeeper = new ZkClient().initZk();
    }

    /**
     * 创建节点
     * 需要一级级 的创建，要不然会 noNodeException
     */
    private void nodeCreate(String path) throws InterruptedException, KeeperException {
        // 第三个参数ACL 表示访问控制权限
        // 第四个参数，控制创建的是持久节点，持久顺序节点，还是临时节点；临时顺序节点
        // 返回 the actual path of the created node
        // 单节点存在时，抛异常 KeeperException.NodeExists
        String node = zooKeeper.create(path + "/yes", "保存数据".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
        System.out.println("create node: " + node);

        //带生命周期的节点--测试失败
        Stat stat = new Stat();
        String ttlNode = zooKeeper.create(path + "/ttl", ("now: " + LocalDateTime.now()).getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT_WITH_TTL, stat, 1000);
        System.out.println("ttl node:" + ttlNode + " | " + stat);
        zooKeeper.exists(path + "/ttl",(e)->{
            System.out.println("ttl 节点变更: " + e);
        });
    }

    /**
     * 判断节点是否存在
     * 当节点存在时，返回Stat对象，包含一些基本信息；如果不存在，则返回null
     * 第二个参数，传入的是事件回调对象，我们的测试类NodeExmaple 实现了接口 Watcher， 所以直接传的是this
     * 注册事件监听时，需要注意这个回调只会执行一次，即触发之后就没了；后面再次修改、删除、创建节点都不会再被接收到
     * @param path
     */
    public void checkPathExist(String path) throws InterruptedException, KeeperException {
        // 节点存在，则返回stat对象； 不存在时，返回null
        // watch: true 表示给这个节点添加监听器，当节点出现创建/删除 或者 新增数据时，触发watcher回调
        Stat stat = zooKeeper.exists(path + "/no", false);
        System.out.println("NoStat: " + stat);

        Stat stat2 = zooKeeper.exists(path + "/yes", this);
        System.out.println("YesStat: " + stat2);
    }

    /**
     * 获取节点的所有子节点, 只能获取一级节点
     *
     * @param path
     */
    public void nodeChildren(String path) throws InterruptedException, KeeperException {
        // 如果获取成功，会监听 当前节点的删除，子节点的创建和删除，触发回调事件, 这个回调也只会触发一次
        List<String> children = zooKeeper.getChildren(path, this, new Stat());
        System.out.println("path:" + path + " 's children:" + children);

    }

    /**
     * 设置数据，获取数据
     * 在设置数据时，可以指定版本，当version > 0时，表示根据版本精确匹配；如果为-1时，则只要节点路径对上就成
     * @param path
     */
    public void dataChange(String path) throws InterruptedException, KeeperException {
        Stat stat = new Stat();
        byte[] data = zooKeeper.getData(path, false, stat);
        System.out.println("path: " + path + " data: " + new String(data) + " : " + stat);

        // 根据版本精确匹配; version = -1 就不需要进行版本匹配了
        Stat newStat = zooKeeper.setData(path, ("new data" + LocalDateTime.now()).getBytes(), stat.getVersion());
        System.out.println("newStat: " + stat.getVersion() + "/" + newStat.getVersion() + " data: " + new String(zooKeeper.getData(path, false, stat)));

    }

    /**
     * 节点删除
     * 据版本限定删除， -1 表示不需要管版本，path匹配就可以执行；否则需要版本匹配，不然就会抛异常
     * @param path
     */
    public void deleteNode(String path) throws InterruptedException, KeeperException {
        zooKeeper.delete(path, -1);
    }

    /**
     * 长期有效的监听
     * @param path
     */
    public void watchEvent(String path) throws InterruptedException, KeeperException {
        // 注意这个节点存在
        // 添加监听, 与 exist判断节点是否存在时添加的监听器 不同的在于，触发之后，依然有效还会被触发， 只有手动调用remove才会取消
        // 感知： 节点创建，删除，数据变更 ； 创建子节点，删除子节点
        // 无法感知： 子节点的子节点创建/删除， 子节点的数据变更
        zooKeeper.addWatch(path + "/yes", new Watcher() {
            @Override
            public void process(WatchedEvent event) {
                System.out.println("事件触发 on " + path + " event:" + event);
            }
        },AddWatchMode.PERSISTENT);

        // 注意这个节点不存在
        // 添加监听, 与 exist 不同的在于，触发之后，依然有效还会被触发， 只有手动调用remove才会取消
        // 与前面的区别在于，它的子节点的变动也会被监听到
        zooKeeper.addWatch(path + "/no", new Watcher() {
            @Override
            public void process(WatchedEvent event) {
                System.out.println("事件触发 on " + path + " event:" + event);
            }
        }, AddWatchMode.PERSISTENT_RECURSIVE);
    }

    @Override
    public void process(WatchedEvent watchedEvent) {

    }
}
