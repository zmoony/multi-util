# 说明
> Disruptor是一个开源框架，研发的初衷是为了解决高并发下队列锁的问题<br>
> 目前，包括Apache Storm、Camel、Log4j2在内的很多知名项目都应用了Disruptor以获取高性能<br>
[官方文档](https://link.zhihu.com/?target=http%3A//lmax-exchange.github.io/disruptor/)

# java内置对列保证线程安全的方式
~~~
1. ArrayBlockingQueue：基于数组形式的队列，通过加锁的方式，来保证多线程情况下数据的安全；
2. LinkedBlockingQueue：基于链表形式的队列，也通过加锁的方式，来保证多线程情况下数据的安全；
3. ConcurrentLinkedQueue：基于链表形式的队列，通过CAS的方式
~~~
Disruptor是一个高性能的异步处理框架，一个轻量级的JMS，和JDK中的BlockingQueue有相似处，但是它的处理速度非常快
## 【核心】RingBuffer
Disruptor底层数据结构实现，核心类，是线程间交换数据的中转地；
RingBuffer，环形缓冲区，在disruptor中扮演着非常重要的角色，理解RingBuffer的结构有利于我们理解disruptor为什么这么快、无锁的实现方式、生产者/消费者模式的实现细节。
## 数组
这个类似于轮胎的东西实际上就是一个数组，使用数组的好处当然是由于预加载的缘故使得访问比链表要快的多。
## 序号管理器 Sequencer
序号管理器，生产同步的实现者，负责消费者/生产者各自序号、序号栅栏的管理和协调,Sequencer有单生产者,多生产者两种不同的模式,里面实现了各种同步的算法；
## 序号 
RingBuffer中元素拥有序号的概念，并且序号是一直增长的
序号，声明一个序号，用于跟踪ringbuffer中任务的变化和消费者的消费情况，disruptor里面大部分的并发代码都是通过对Sequence的值同步修改实现的,而非锁,这是disruptor高性能的一个主要原因；
## SequenceBarrier 
序号栅栏，管理和协调生产者的游标序号和各个消费者的序号，确保生产者不会覆盖消费者未来得及处理的消息，确保存在依赖的消费者之间能够按照正确的顺序处理
## EventProcessor
事件处理器，监听RingBuffer的事件，并消费可用事件，从RingBuffer读取的事件会交由实际的生产者实现类来消费；它会一直侦听下一个可用的序号，直到该序号对应的事件已经准备好。
## EventHandler
业务处理器，是实际消费者的接口，完成具体的业务逻辑实现，第三方实现该接口；代表着消费者。
## Producer
生产者接口，第三方线程充当该角色，producer向RingBuffer写入事件。
## Wait Strategy
Wait Strategy决定了一个消费者怎么等待生产者将事件（Event）放入Disruptor中。
## 等待策略
### BlockingWaitStrategy
Disruptor的默认策略是BlockingWaitStrategy。在BlockingWaitStrategy内部是使用锁和condition来控制线程的唤醒。BlockingWaitStrategy是最低效的策略，但其对CPU的消耗最小并且在各种不同部署环境中能提供更加一致的性能表现。
### SleepingWaitStrategy
SleepingWaitStrategy 的性能表现跟 BlockingWaitStrategy 差不多，对 CPU 的消耗也类似，但其对生产者线程的影响最小，通过使用LockSupport.parkNanos(1)来实现循环等待。
### YieldingWaitStrategy
YieldingWaitStrategy是可以使用在低延迟系统的策略之一。YieldingWaitStrategy将自旋以等待序列增加到适当的值。在循环体内，将调用Thread.yield()以允许其他排队的线程运行。在要求极高性能且事件处理线数小于 CPU 逻辑核心数的场景中，推荐使用此策略；例如，CPU开启超线程的特性。
### BusySpinWaitStrategy
性能最好，适合用于低延迟的系统。在要求极高性能且事件处理线程数小于CPU逻辑核心数的场景中，推荐使用此策略；例如，CPU开启超线程的特性。
### PhasedBackoffWaitStrategy
自旋 + yield + 自定义策略，CPU资源紧缺，吞吐量和延迟并不重要的场景。


## 无锁的机制

