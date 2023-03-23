#quartz
1. @DisallowConcurrentExecution @PersistJobDataAfterExecution
>1. 注解@DisallowConcurrentExecution和@PersistJobDataAfterExecution都是用在org.quartz.Job的实现类上的
>2. 都是针对<font color='red'>JobDetail</font>实例的，而不是Job实现类的。
>3. 说明：<br>
>@PersistJobDataAfterExecution：告诉Quartz在成功执行了Job实现类的execute方法后（没有发生任何异常），更新JobDetail中JobDataMap的数据，使得该JobDetail实例在下一次执行的时候，JobDataMap中是更新后的数据，而不是更新前的旧数据
>@DisallowConcurrentExecution：告诉Quartz不要并发地执行同一个JobDetail实例。

>**总结**
>1. 当某一个JobDetail实例到点运行之后，在其运行结束之前，不会再发起一次该JobDetail实例的调用，即使设置的该JobDetail实例的定时执行时间到了。
>2. JobDetail实例之间互不影响