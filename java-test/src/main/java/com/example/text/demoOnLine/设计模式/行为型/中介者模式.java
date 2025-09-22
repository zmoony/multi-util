package com.example.text.demoOnLine.设计模式.行为型;

import lombok.Data;

import java.util.Hashtable;

/**
 * 中介者模式
 * 行为型模式
 * 一个中介对象来封装一系列的对象交互，中介者使各对象不需要显式地相互引用，从而使其耦合松散，而且可以独立地改变它们之间的交互。中介者模式又称为调停者模式
 * 对象：
 * Mediator: 抽象中介者
 * ConcreteMediator: 具体中介者
 * Colleague: 抽象同事类
 * ConcreteColleague: 具体同事类
 *
 * @author yuez
 * @since 2023/5/4
 */
public class 中介者模式 {
    public static void main(String[] args) {
        AbstractChatroom happyChat = new ChatGroup();
        Member m1,m2,m3,m4,m5;
        m1 = new DiamondMember("张三");
        m2 = new DiamondMember("李四");
        m3 = new CommonMember("王五");
        m4 = new CommonMember("小芳");
        m5 = new CommonMember("小红");
        happyChat.register(m1);
        happyChat.register(m2);
        happyChat.register(m3);
        happyChat.register(m4);
        happyChat.register(m5);
        m1.sendText("李四", "李四，你好");
        m2.sendText("张三", "张三，你好");
        m1.sendText("李四", "今天天气不错，有日");
        m2.sendImage("张三", "一个很大很大的太阳");
        m2.sendImage("张三", "太阳");
        m3.sendText("小芳", "还有问题吗？");
        m3.sendText("小红", "还有问题吗？");
        m4.sendText("王五", "没有了，谢谢");
        m5.sendText("王五", "我也没有了");
        m5.sendImage("王五", "谢谢");
    }
}

//抽象中介者
abstract class AbstractChatroom {
    abstract void register(Member member);
    abstract void sendText(String from,String to,String message);
    abstract void sendImage(String from,String to,String image);
}
//抽象同事类
@Data
abstract class Member {
    AbstractChatroom chatroom;
    String name;
    public Member(String name){
        this.name = name;
    }
    abstract void sendText(String to,String message);
    abstract void sendImage(String to,String image);

    public void receiveText(String from,String message){
        System.out.println(from + "发送文本给" + this.name + ",内容为：" + message);
    }

    public void receiveImage(String from,String image){
        System.out.println(from + "发送图片给" + this.name + ",内容为：" + image);
    }

}
//具体中介者
class ChatGroup extends AbstractChatroom{
    private Hashtable members =new Hashtable();

    @Override
    void register(Member member) {
        if(!members.contains(member)){
            members.put(member.getName(),member);
            member.setChatroom(this);
        }
    }

    @Override
    void sendText(String from, String to, String message) {
        Member memberTo = (Member) members.get(to);
        String newMessage = message.replaceAll("日", "");
        memberTo.receiveText(from,newMessage);
    }

    @Override
    void sendImage(String from, String to, String image) {
        Member memberTo = (Member) members.get(to);
        if(image.length()>5){
            System.out.println( "图片太大，发送失败");
        }
        memberTo.receiveImage(from, image);
    }
}
//具体的同事类
class CommonMember extends Member{
    public CommonMember(String name) {
        super(name);
    }

    @Override
    void sendText(String to, String message) {
        System.out.println("普通会员发送消息:");
        chatroom.sendText(name, to ,message);
    }

    @Override
    void sendImage(String to, String image) {
        System.out.println("普通会员不能发送图片");
    }
}
//砖石会员
class DiamondMember extends Member{
    public DiamondMember(String name){
        super(name);
    }
    public void sendText(String to, String message){
        System.out.println("砖石会员发送消息:");
        chatroom.sendText(name, to ,message);
    }
    public void sendImage(String to, String image){
        System.out.println("砖石会员发送图片:");
        chatroom.sendText(name, to ,image);
    }
}






















