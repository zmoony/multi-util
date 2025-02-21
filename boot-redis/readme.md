# redis快速入门
## 一 基本规范
### 1. key命名规范
为防止冲突，一般为<code>业务名:表名:id</code>
<br><font color='red'>注意:</font>不得包含空格，换行，单双引号以及其他转义字符
### 2. value设计
> 1. **拒绝bigkey**
> <br>String类型控制在10kb，hash/list/set/zset元素最好不要超过5000；如果出现bigkey不要使用del进行删除，使用hscan/sscan/zscan进行渐进式删除，同时注意过期机制的问题，会触发del操作，且不会记录进慢日志
> 2. **选择合适的数据类型**
> <br>合理运用实体对象类型进行操作
> ~~~
> set user:1 name tom
> set user:1 age 18
> set user:1 work 1
> 转为
> hmset user:1 name tom age 18 work 1
> ~~~
> 3. redis使用过期机制，防止内存溢出

### 3. 命令的注意事项
1. 命名关注命令数量，建议使用hscan,sscan,zscan渐进式查询
2. 线上禁止使用keys,flushall,flushdb,通过redis的rename进行禁用
###4. 客户端使用
1. 避免多个业务使用同一个redis实例
2. 使用连接池，本项目使用Lettuce进行common-pool的连接池设置
~~~
使用jedis连接时
Jedis jedis = null
try{
    jedis = jedisPool.getResource();
    //具体的命令
    jedis.executeCommond();
}catch(...){

}finally{
    //JedisPool的模式下不是关闭连接，是将其回归资源池
    if(jedis != null) jedis.close();
}
~~~
3. 淘汰策略
   * volatile-lru（默认）：超过最大内存后，在过期键中使用lru算法进行key的剔除，保证只删除过期的数据，但是会导致OOM的问题
   * allkeys-lru：根据lru算法删除key,不管有没有设置超时，直至腾出空间
   * allkeys-random：随机删除所有key,直至腾出空间
   * volatile-random：随机删除过期key,直至腾出空间
   * volatile-ttl：根据对象的ttl的属性，删除最近将要过期的数据，如果没有，回到noeviction 策略
   * noeviction：不会剔除任何数据，禁止写入，报OOM错误，只能进行读操作
### 4. 相关工具
1. 数据同步：redis-port
2. bigkey搜索：redis大key搜索工具
3. 热点key寻找：内部monitor,短时间使用redis-faina
###5. 删除bigkey
> 所有操作可以使用pipline加速<br>
> redis 4.0已经支持异步的key删除

1.hash删除:hscan+hdel
~~~
public void delBigHash(String host, int port, String password, String bigHashKey) {
    Jedis jedis = new Jedis(host, port);
    if (password != null && !"".equals(password)) {
        jedis.auth(password);
    }
    ScanParams scanParams = new ScanParams().count(100);
    String cursor = "0";
    do {
        ScanResult<Entry<String, String>> scanResult = jedis.hscan(bigHashKey, cursor, scanParams);
        List<Entry<String, String>> entryList = scanResult.getResult();
        if (entryList != null && !entryList.isEmpty()) {
            for (Entry<String, String> entry : entryList) {
                jedis.hdel(bigHashKey, entry.getKey());
            }
        }
        cursor = scanResult.getStringCursor();
    } while (!"0".equals(cursor));
    //删除bigkey
    jedis.del(bigHashKey);
}
~~~
2. List 删除: ltrim
~~~
public void delBigList(String host, int port, String password, String bigListKey) {
   Jedis jedis = new Jedis(host, port);
   if (password != null && !"".equals(password)) {
      jedis.auth(password);
   }
   long llen = jedis.llen(bigListKey);
   int counter = 0;
   int left = 100;
   while (counter < llen) {
      //每次从左侧截掉100个
      jedis.ltrim(bigListKey, left, llen);
      counter += left;
   }
   //最终删除key
   jedis.del(bigListKey);
}
~~~
3. Set 删除: sscan + srem
~~~
public  void  delBigSet(String  host, int port,  String  password,  String  bigSetKey) {
   Jedis jedis =  new  Jedis(host, port);
   if  (password !=  null  && !"".equals(password)) {
      jedis.auth(password);
   }
   ScanParams scanParams =  new  ScanParams().count(100);
   String  cursor =  "0";
   do  {
      ScanResult<String> scanResult = jedis.sscan(bigSetKey, cursor, scanParams);
      List<String> memberList = scanResult.getResult();
      if  (memberList !=  null  && !memberList.isEmpty()) {
         for  (String  member : memberList) {
            jedis.srem(bigSetKey, member);
         }
      }
      cursor = scanResult.getStringCursor();
   }  while  (!"0".equals(cursor));
   //删除bigkey
   jedis.del(bigSetKey);
}
~~~
4. SortedSet 删除: zscan + zrem
~~~
public  void  delBigZset(String host,  int  port, String password, String bigZsetKey)  {
   Jedis jedis =  new  Jedis(host, port);
   if  (password !=  null  && !"".equals(password)) {
      jedis.auth(password);
   }
   ScanParams scanParams =  new  ScanParams().count(100);
   String cursor =  "0";
   do  {
      ScanResult<Tuple> scanResult = jedis.zscan(bigZsetKey, cursor, scanParams);
      List<Tuple> tupleList = scanResult.getResult();
      if  (tupleList !=  null  && !tupleList.isEmpty()) {
         for  (Tuple tuple : tupleList) {
            jedis.zrem(bigZsetKey, tuple.getElement());
         }
      }
      cursor = scanResult.getStringCursor();
   }  while  (!"0".equals(cursor));
   //删除bigkey
   jedis.del(bigZsetKey);
}
~~~
## 二 简要介绍
* 支持持久化，多类型数据存储，支持数据备份，采用主从模式
* 性能高，原子操作（所有操作都是原子性的，多个操作通过multi和exec包裹起来也支持事务）
* 支持public/subscribe,通知，key过期
* 一个字符串类型的值的最大存储范围为512M
1. 数据类型
> string,hash,list,set,zset
2. 持久化机制
> 1. RDB:某个时间点将数据写入临时文件，替换上一个临时文件
> * 只有一个dump.rdb,方便持久化
> * 容灾性好，一个文件可以保存到安全的磁盘
> * 性能最大化，fork一个子进程来完成写的擦偶作，主进程不受影响
> * 数据集较大时，比AOF的启动效率高
> 2. AOF:所有的命令行的写入
> * 数据安全，可以每一个命令都aof一下
> * 通过 append 模式写文件，即使中途服务器宕机，可以通过 redis-check-aof工具解决数据一致性问题
> * AOF 机制的 rewrite 模式。AOF 文件没被 rewrite 之前（文件过大时会对命令进行合并重写），可以删除其中的某些命令
> * 缺点：AOF 文件比 RDB 文件大，且恢复速度慢；数据集大的时候，比 rdb 启动效率低。
##三 重点介绍
###队列模式
消息消费完即被删除，属于点对点模式，通过key队列方式实现，阻塞线程；
BRPOP和BLPOP实现阻塞时消息队列时，当获取到数据后会自动断开，如果你想要持续获取就需要自己在程序设计时添加监听，
###订阅/通知模式
支持多个客户端获取同一频道发部的消息；
subscribe和publish自己本身就会时刻监听发送端。
> publish:发布一条消息到指定频道
> ~~~
> public Long publish(final String channel, final String message)
> ************
> jedis = redisConnection.getJedis();
> jedis.publish(channel, message);
> ~~~
> subscribe:订阅指定频道的消息（可以是多个）
> ~~~
> public void subscribe(final JedisPubSub jedisPubSub, final String... channels)
> ***************
> Jedis jedis = redisConnection.getJedis();
> jedis.subscribe(new JedisPubSub() {
>     @Override
>     public void onMessage(String channel, String message) {
>        //处理消息逻辑代码
>     }
> }, channels);
> ~~~
> psubscribe:订阅符合给定模式频道的消息<br>
> unsubscribe:停止订阅指定频道的消息<br>
> punsubscribe:停止订阅指定模式频道的消息<br>
> pubsub:待研究

###pipline模式
