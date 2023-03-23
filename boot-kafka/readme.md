# kafka快速入门
## 生产模式
### 幂等性
1. 幂等消息保证producer在一次会话内写入**同一个partition**内的消息具有幂等性,也就是说消息不会重复。
2. Kafka的幂等性其实就是将原来需要在下游系统中进行的去重操作放在了数据**上游kafka** 中。
>即使 Producer 端重复发送了相同的消息，Broker 端也能做到自动去重。在下游 Consumer 看来，消息依然只有一条，而这就是我们今天要介绍的幂等性。
><br>  无法保证跨分区和跨会话的会话</font>
3. 使用方法
>producer默认不是幂等的，需要进行设置*props.put(ProducerConfig.ENABLE_IDEMPOTENCE_CONFIG， true)*
4. 原理
>1. 空间换取时间，Broker端多保留一些信息，当producer发送相同信息时，broker自动知晓，并进行去除操作
>2. Kafka为此引入了producer id（以下简称PID）和序列号（sequence number）这两个概念。每个新的生产者实例在初始化的时候都会被分配一个PID，这个PID对用户而言是完全透明的。
>3. 对于每个PID，消息发送到的每一个分区都有对应的序列号，这些序列号从0开始单调递增。生产者每发送一条消息就会将对应的序列号的值加1。
    broker端会在内存中为每一对维护一个序列号。对于收到的每一条消息，只有当它的序列号的值（SN_new）比broker端中维护的对应的序列号的值（SN_old）大1（即SN_new = SN_old + 1）时，broker才会接收它。
5. 问题
>Kafka的幂等只能保证单个生产者会话（session）中单分区的幂等。幂等性不能跨多个分区运作，而事务可以弥补这个缺陷。
###事务模式
1. 事务可以保证对**多个分区**写入操作的原子性。操作的原子性是指多个操作要么全部成功，要么全部失败，不存在部分成功、部分失败的可能。
2. 使用方法
>- 提供transactional.id显性设置，需要保证幂等性的开启`enable.idempotence:true`,如果设置成false,则会抛出**ConfigException**异常
>- transactionalId与PID一一对应，两者之间所不同的是transactionalId由用户显式设置，而PID是由Kafka内部分配的。
>- 为了保证新的生产者启动后具有相同transactionalId的旧生产者能够立即失效，每个生产者通过transactionalId获取PID的同时，还会获取一个单调递增的producer epoch。如果使用同一个transactionalId开启两个生产者，那么前一个开启的生产者会报错。
>- <font color='red'>消费端配合：isolation.level，“read_uncommitted（默认）”：消费端应用可以看到（消费到）未提交的事务；“read_committed”：消费端应用不可以看到尚未提交的事务内的消息。</font>
>- 设置重试次数，否则会得到如下错误：ConfigException: Must set retries to non-zero when using the idempotent producer.
~~~
producer.initTransactions();
try {
      producer.beginTransaction();
      producer.send(record1);
      producer.send(record2);
      producer.commitTransaction();
} catch (KafkaException e) {
      producer.abortTransaction();
}
---------------------------------------------
KafkaProducer提供了5个与事务相关的方法：
//初始化事务
void initTransactions();
//开启事务
void beginTransaction() throws ProducerFencedException;
//为消费者提供在事务内的位移提交的操作
void sendOffsetsToTransaction(Map<TopicPartition, OffsetAndMetadata> offsets,String consumerGroupId) throws ProducerFencedException;
//提交事务
void commitTransaction() throws ProducerFencedException;
//中止事务，类似于事务回滚。
void abortTransaction() throws ProducerFencedException;
~~~
3. 分析<br>
   - 从生产者的角度分析，通过事务，Kafka可以保证跨生产者会话的消息幂等发送，以及跨生产者会话的事务恢复。<br>
   - 前者表示具有相同transactionalId的新生产者实例被创建且工作的时候，旧的且拥有相同transactionalId的生产者实例将不再工作。<br>
   - 后者指当某个生产者实例宕机后，新的生产者实例可以保证任何未完成的旧事务要么被提交（Commit），要么被中止（Abort），如此可以使新的生产者实例从一个正常的状态开始工作。

## 源码部分分析
### ProducerRecord
1. 内部结构
~~~
-- Topic （名字）
-- PartitionID ( 可选)
-- Key[( 可选 )
-- Value
~~~
2. 发送逻辑
* 若指定Partition ID,则PR被发送至指定Partition
* 若未指定Partition ID,但指定了Key, PR会按照hasy(key)发送至对应Partition
* 若既未指定Partition ID也没指定Key，PR会按照round-robin模式发送到每个Partition
* 若同时指定了Partition ID和Key, PR只会发送到指定的Partition (Key不起作用，代码逻辑决定)
