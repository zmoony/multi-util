# 应用场景
https://juejin.cn/post/7322518839522000923?utm_source=gold_browser_extension
## 登录鉴权
利用redis的超时机制，将token存入redis，设置超时时间，token失效时，用户需要重新登录。get不到即为过期
```ruby
[//]: # (该函数是设置token的值为"1234567890"，并设置token的过期时间为3600秒。)
setex token:1234567890 3600 "用户ID"  

[//]: # (通过Redis存储前缀 + 手机号作为key，验证码作为value，并设置60秒过期时间。)
setex phone:18501230462 60 "验证码"
```
## 计数器
点赞数 Redis的INCR、DECR、INCRBY、DECRBY相关命令
```ruby
set article1 0 //初始化，点赞数为0

incr article1 //article1的点赞数加1
decr article1 //article1的点赞数减1
incrby article1 10 //article1的点赞数加10
decrby article1 10 //article1的点赞数减10
```
## 粉丝关注
粉丝关注场景本身还好，但涉及到计算共同粉丝，单方粉丝之类的会比较麻烦，这时就轮到Redis Set数据类型粉墨登场了。
Set还提供了求交集、求并集等一系列直接操作集合的方法，非常适合于求共同或单方好友、粉丝、爱好之类的业务场景，实现起来特别方便。
```ruby
SADD tony Mary //Mary成为了Tony的粉丝
SADD tony John //John成为了Tony的粉丝
SMEMBERS tony //查看Tony的粉丝列表
SCARD tony //查看Tony的粉丝列表数量

SADD tom Mary //Mary成为了Tom的粉丝
SADD tom eric //eric成为了Tom的粉丝
SMEMBERS tom //查看Tom的粉丝列表

SINTER tony tom //查看共有的粉丝列表
SUNION tony tom //查看所有粉丝列表
SDIFF tony tom //Tony的粉丝，但不是Tom的粉丝
SDIFF tom tony //Tom的粉丝，但不是Tony的粉丝
```
## 排行榜
Zset（SortedSet），是Set的可排序版，是通过增加一个排序属性score来实现的
```ruby
ZADD 家品全类 5.5 海尔         //添加了海尔电器和5.5亿销售额
ZADD 家电全品类 4.5 美的       //添加了美的电器和4.5亿销售额
ZADD 家电全品类 3.2 小米       //添加了小米电器和3.2亿销售额
ZADD 家电全品类 2.7 格力       //添加了格力电器和2.7亿销售额

ZCARD 家电全品类 //家电全品类的数量
ZSCORE 家电全品类 格力     //获取格力的销售额
ZREVRANGE 家电全品类 0 -1 WITHSCORES     //家电全品类的倒序输出
ZRANGE 家电全品类 0 -1 WITHSCORES      //家电全品类的正序输出
ZINCRBY 家电全品类 2.2 格力            //为格力增加2.2亿销售额
ZREVRANGE 家电全品类 0 -1 WITHSCORES   //增加销售额后的排行榜变化
```
## 防刷/限流
防刷：用户在极短时间内，频繁发起请求去调用系统中的某个接口，该情况下我们需要对其进行限制。
限流：用户在一定时间内，只能发起一次请求去调用系统中的某个接口，该情况下我们需要对其进行限制。
setex key 100 value  //只有不存在的时候才能设置值，如果存在则返回nil
```ruby
redis> set createorder|userid|1234 “” EX 1 NX    //userid为1234的用户第一次下单成功，设置一秒钟过期时间 
"OK"
redis> set createorder|userid|1234 “” EX 1 NX    //userid为1234的用户一秒钟内第二次下单，结果不成功
(nil)
redis> set createorder|userid|1234 “” EX 1 NX    //userid为1234的用户超过一秒钟再次下单，结果成功
"OK"

redis> setex key 100 value  //key的值为100，100秒后过期
"OK"
```
## 消息对列
list数据结构实现消息队列的功能(右边拿)
```ruby
redis> lpush mybooks java    //往mybooks list中填充java，实现生产者功能
(integer) 1
redis> lpush mybooks mysql    //往mybooks list中填充mysql，实现生产者功能
(integer) 2
redis> lpush mybooks redis    //往mybooks list中填充redis，实现生产者功能
(integer) 3
redis> rpop mybooks    //往mybooks list中取出java，实现消费者功能 
"java"
redis> rpop mybooks    //往mybooks list中取出mysql，实现消费者功能 
"mysql"
redis> rpop mybooks    //往mybooks list中取出redis，实现消费者功能 
"redis"

```
## 浏览器的历史记录
我们可以通过Redis list来实现栈功能，进而实现浏览器历史记录场景。（左边拿）
```ruby
redis> lpush mybrowser sohu    //浏览sohu
(integer) 1
redis> lpush mybrowser sina    //浏览sina
(integer) 2
redis> lpush mybrowser baidu    //浏览baidu
(integer) 3
redis> lpop mybrowser    //后退
"baidu"
redis> lpop mybrowser    //后退
"sina"
redis> lpop mybrowser    //后退
"sohu"

```
## 分布式锁
目前主流的分布式锁解决方案是通过Redisson来实现的，相比于上述方案，Redisson解决了锁的可重入和续期问题。
```ruby
redis> set mytasklock “tony” ex 10 nx    //获取分布式锁成功，加锁人为tony，过期时间为10秒
"OK"
redis> set mytasklock “tom” ex 10 nx    //获取分布式锁失败，加锁人为tom
(nil)
redis> del mytasklock    //释放分布式锁
(integer) 1              //该步骤需要通过lua脚本实现原子性操作——“如果加锁人为tony，则释放锁”

```
## 用户签到
用户签到、用户出勤、当天活跃用户等场景，虽然我们用Redis Set数据结构也可以实现，但用户量级庞大的情况下，会极大占用内存空间。
非常适合Redis BitMap数据结构，通过其bit位来进行状态存储
SETBIT key offset value
```ruby
setbit userid:1234:202312 0 1 //用户1234在20231201日签到(偏移量从0开始，所以减1)
setbit userid:1234:202312 1 1 //用户1234在20231202日签到
setbit userid:1234:202312 3 1 //用户1234在20231204日签到

getbit userid:1234:202312 0 //查看用户1234在20231201日是否签到
getbit userid:1234:202312 1 //查看用户1234在20231202日是否签到

bitcount userid:1234:202312 //查看用户1234在20231201日到20231204日的签到天数
```
## 网站UV统计
假设如下场景，某大型网站需要统计每个网页每天的UV（Unique Visitor）数据，与PV（Page View）的不同点在于，UV需要进行去重操作，同一个用户一天内的多次访问一个网页，只能计数一次。
如果我们通过Redis Set存储用户ID的方式进行解决，非常耗费内存空间。这时，我们可以使用HyperLogLog。
Redis HyperLogLog 提供不精确的去重计数方案，标准误差是 0.81%，但仅仅占用12k的内存空间，非常适用于大型网站UV统计这种空间消耗巨大，但数据不需要特别精确的业务场景。
```ruby
pfadd page1 user1 //user1访问page1，uv计数+1
pfadd page1 user1    //user1再次访问page1，uv不计数
pfadd page1 user2    //user2访问page1，uv计数+1
pfadd page1 user3    //user3访问page1，uv计数+1
pfadd page1 user4    //user4访问page1，uv计数+1

pfcount page1    //获取page1的uv

pfadd page2 user1    //user1访问page2，uv计数+1
pfadd page2 user5    //user5访问page2，uv计数+1
pfadd page2 user6    //user6访问page2，uv计数+1

pfcount page2    //获取page2的uv

pfmerge page1and2 page1 page2    //将page1和page2merge成一个
pfcount page1and2    //获取page1and2的uv

```






















