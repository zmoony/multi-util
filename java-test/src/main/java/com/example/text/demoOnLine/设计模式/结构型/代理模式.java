package com.example.text.demoOnLine.设计模式.结构型;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * 代理模式
 * 结构型设计模式
 * 代理模式在Java中的应用比较广泛，比如Spring的AOP实现、远程RPC调用等。代理模式可以在不修改原始接口的情况下，对目标对象进行增强或者替换
 * 代理模式的主要目的是通过代理对象来控制对原始对象的访问，并提供一些额外的功能。
 * 代理模式可以在不修改原始接口的情况下，对目标对象进行增强或者替换
 * 它为其他对象提供一个代理以控制对该对象的访问。代理是一个具有与原始对象相同的接口的对象，客户端不必知道它与原始对象交互的方式。代理可以拦截对原始对象的访问，并在某些情况下将请求传递给原始对象。
 *
 * 静态代理：在编译时就已经确定了代理类和被代理类之间的关系，通常需要为每个被代理类都编写一个对应的代理类，并实现相同的接口。静态代理的优点是简单易懂，缺点是不灵活，代码冗余。
 * 动态代理：在运行时动态生成代理对象，并根据反射机制调用被代理类的方法。动态代理可以使用Java原生API或者第三方框架来实现，如JDK Proxy、CGLIB、等。动态代理的优点是灵活高效，缺点是复杂难懂。

 * @author yuez
 * @since 2023/4/25
 */
public class 代理模式 {
    public static void main(String[] args) {
        //静态代理
        BankAccount bankAccount = new BankAccount(1000.0);
        AccountProxy accountProxy = new AccountProxy(bankAccount);
        accountProxy.deposit(500.0);
        accountProxy.withdraw(200.0);
        accountProxy.withdraw(2000.0);
        //动态代理
        BankAccount bankAccount2 = new BankAccount(1000.0);
        AccountDynamicProxy dynamicProxy = new AccountDynamicProxy(bankAccount2);
        Account proxy = (Account) Proxy.newProxyInstance(Account.class.getClassLoader(), new Class[]{Account.class}, dynamicProxy);
        // 使用代理对象进行操作
        proxy.deposit(500.0);
        proxy.withdraw(200.0);
        proxy.withdraw(2000.0);
    }

}
//********************静态代理**********************
interface Account{
    void deposit(double amount);//存款
    void withdraw(double amount);//取款
}
//实现账户类接口
class BankAccount implements Account{
    private double balance;

    public BankAccount(double balance) {
        this.balance = balance;
    }

    @Override
    public void deposit(double amount) {
        balance += amount;
        System.out.println("Deposited " + amount + ", balance is now " + balance);
    }

    @Override
    public void withdraw(double amount) {
        if(balance >= amount){
            balance -= amount;
            System.out.println("Withdrew " + amount + ", balance is now " + balance);
        }else{
            System.out.println("Sorry, insufficient balance");
        }
    }
}
//账户代理类
class AccountProxy implements Account{
    private BankAccount bankAccount;

    public AccountProxy(BankAccount bankAccount) {
        this.bankAccount = bankAccount;
    }

    @Override
    public void deposit(double amount) {
        bankAccount.deposit(amount);
    }

    @Override
    public void withdraw(double amount) {
        bankAccount.withdraw(amount);
    }
}

//********************动态代理**********************
class AccountDynamicProxy implements InvocationHandler{
    private BankAccount bankAccount;

    public AccountDynamicProxy(BankAccount bankAccount) {
        this.bankAccount = bankAccount;
    }

    //可以添加额外功能，前置后置操作
    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        if(method.getName().equals("deposit")){
            System.out.println("Before deposit");
            method.invoke(bankAccount,args);
            System.out.println("After deposit");
        } else if (method.getName().equals("withdraw")) {
            System.out.println("Before withdraw");
            method.invoke(bankAccount,args);
            System.out.println("After withdraw");
        }
        return null;
    }
}


























