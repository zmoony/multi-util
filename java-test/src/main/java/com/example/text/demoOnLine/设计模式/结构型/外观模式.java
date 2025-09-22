package com.example.text.demoOnLine.设计模式.结构型;

/**
 * 外观模式<br>
 * 结构型设计模式
 * 它提供了一个简单的接口，隐藏了一组复杂的子系统的复杂性，使得客户端可以更容易地使用这个子系统。
 * (将多个接口封装成一个接口  作为门面给外部重复调用)
 * @author yuez
 * @since 2023/4/25
 */
public class 外观模式 {
    public static void main(String[] args) {
        Computer computer = new Computer();
        computer.start();
    }
}
class Computer{
    private CPU cpu;
    private Memory memory;
    private HardDrive hardDrive;

    public Computer() {
        cpu = new CPU();
        memory = new Memory();
        hardDrive = new HardDrive();
    }

    //对外提供一个门面接口
    public void start(){
        System.out.println("computer is starting....");
        cpu.start();
        memory.start();
        hardDrive.start();
        System.out.println("computer has started....");
    }
}


class CPU{
    public void start(){
        System.out.println("CPU START....");
    }
}
class Memory{
    public void start(){
        System.out.println("Memory start ...");
    }
}
class HardDrive{
    public void start(){
        System.out.println("HardDrive start...");
    }
}