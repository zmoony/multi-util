package com.example.text.demoOnLine.设计模式.行为型;

import java.util.ArrayList;
import java.util.List;

/**
 * 命令模式
 * 行为型模式
 * 将一个请求封装成一个对象，从而让你使用不同的请求把客户端参数化，对请 求排队或者记录请求日志，可以提供命令的撤销和恢复功能
 * 角色：
 * <ul>
 *     <li>抽象命令类（Command）角色：声明执行命令的接口，拥有执行命令的抽象方法 execute()。</li>
 *      <li>具体命令角色（Concrete    Command）角色：是抽象命令类的具体实现类，它拥有接收者对象，并通过调用接收者的功能来完成命令要执行的操作。</li>
 *      <li>实现者/接收者（Receiver）角色：执行命令功能的相关操作，是具体命令对象业务的真正实现者。</li>
 *      <li>调用者/请求者（Invoker）角色：是请求的发送者，它通常拥有很多的命令对象，并通过访问命令对象来执行相关请求，它不直接访问接收者。</li>
 * </ul>
 *
 *
 * @author yuez
 * @since 2023/4/24
 */
public class 命令模式 {
    public static void main(String[] args) {
        //命令
        HotelCommand hc = new ConcreteChiken();
        //我们发现使用：hc.order()或者new CookReceiver()也能达到相同的效果
        //而我们使用命令模式，对单个的请求，与直接调用的区别似乎不大
        //但是目的确实不一样的，命令模式加了个中间层，有WaiterInvoker去传递命令
        WaiterInvoker wi = new WaiterInvoker(hc);
        wi.callCooking();
        System.out.println("------------------");

        //当请求较多的时候，其实就比较明显了，有个排队处理的效果
        HotelCommand hc1 = new ConcreteRiceWithEgg();
        List<HotelCommand> all = new ArrayList<HotelCommand>();
        all.add(hc);
        all.add(hc1);
        WaiterInvoker wi1 = new WaiterInvoker(all);
        wi1.callCookingList();

    }
}
//接收者，真正的命令执行者
class CookReceiver {
    void actionCooking(){
        System.out.println("已完成烹饪....");
    }
}
//抽象命令类
interface HotelCommand{
    void order();//点菜
}

class ConcreteChiken implements HotelCommand{
    private CookReceiver cookReceiver;

    public ConcreteChiken() {
        this.cookReceiver = new CookReceiver();
    }

    @Override
    public void order() {
        System.out.println("宫保鸡丁");
        System.out.println("加辣");
        cookReceiver.actionCooking();
    }
}
class ConcreteRiceWithEgg implements HotelCommand{
    private CookReceiver receiver; //命令的真正执行者

    public ConcreteRiceWithEgg(){
        this.receiver = new CookReceiver();
    }
    @Override
    public void order() {
        System.out.println("蛋炒饭");
        receiver.actionCooking();
    }
}
//调用者(真正的调用者，服务员，也就是接收客户信息并把信息传递给接受者厨师的人)
class WaiterInvoker {
    private HotelCommand command;
    private List<HotelCommand> commandList;

    public WaiterInvoker(HotelCommand command) {
        this.command = command;
    }

    public WaiterInvoker(List<HotelCommand> commandList) {
        this.commandList = commandList;
    }

    public void callCooking(){
        command.order();
    }

    public void callCookingList(){
        if(commandList == null){
            return;
        }
        for (HotelCommand hc : commandList) {
            hc.order();
        }
    }
}