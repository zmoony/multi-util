package com.example.text.demoOnLine.设计模式.创建型;

/**
 * 抽象工厂模式(选择产品族的实现--多个工厂组合成一个产品)
 *  创建型设计模式，它提供了一种创建相关或依赖对象的接口，而无需指定它们的具体类。它的基本思想是：定义一个用于创建一系列相关或相互依赖对象的接口，而不需要指定他们的具体类。
 *
 * AbstractFactory：抽象工厂，声明了创建产品对象的方法。
 * ConcreteFactory：具体工厂，实现了创建产品对象的方法。
 * AbstractProduct：抽象产品，声明了产品对象的共性接口。
 * Product：具体产品，实现了抽象产品中的抽象方法，构成产品族。
 * Client：客户端，通过调用工厂类的方法创建产品对象。
 *
 * 抽象工厂和工厂模式都是创建对象的设计模式，它们的主要区别什么呢?
 * 目的不同：工厂模式用于创建一类产品对象的实例，而抽象工厂模式用于创建一组相关的产品对象实例。
 * 实现方式不同：工厂模式中只有一个工厂类，该类负责创建所有的产品对象；而抽象工厂模式中有多个工厂类，每个工厂类负责创建一组相关的产品对象。
 * 范围不同：工厂模式通常用于创建单个对象，而抽象工厂模式通常用于创建一组相关的对象
 *
 *
 * @author yuez
 * @since 2023/4/19
 */
public class 抽象工厂模式 {
    public static void main(String[] args) {
        //工厂1
        PaymentFactory paymentFactory =new VisaPaymentFactory();
        CreditCardPayment creditCardPayment = paymentFactory.createCreditCardPayment();
        OnlineBankingPayment onlineBankingPayment = paymentFactory.createOnlineBankingPayment();
        creditCardPayment.pay(100);
        onlineBankingPayment.pay(200);

        //工厂2
        MasterCardPaymentFactory masterCardPaymentFactory = new MasterCardPaymentFactory();
        CreditCardPayment creditCardPayment1 = masterCardPaymentFactory.createCreditCardPayment();
        OnlineBankingPayment onlineBankingPayment1 = masterCardPaymentFactory.createOnlineBankingPayment();
        creditCardPayment1.pay(100);
        onlineBankingPayment1.pay(200);

    }

}
// 抽象支付工厂
abstract class PaymentFactory {
    public abstract CreditCardPayment createCreditCardPayment();
    public abstract OnlineBankingPayment createOnlineBankingPayment();
}

// Visa 信用卡支付工厂（组合）
class VisaPaymentFactory extends PaymentFactory {
    @Override
    public CreditCardPayment createCreditCardPayment() {
        return new VisaCreditCardPayment();
    }

    @Override
    public OnlineBankingPayment createOnlineBankingPayment() {
        return new AlipayOnlineBankingPayment();
    }
}

// MasterCard 信用卡支付工厂
class MasterCardPaymentFactory extends PaymentFactory {
    @Override
    public CreditCardPayment createCreditCardPayment() {
        return new MasterCardCreditCardPayment();
    }

    @Override
    public OnlineBankingPayment createOnlineBankingPayment() {
        return new WeChatOnlineBankingPayment();
    }
}


// 信用卡支付接口
interface CreditCardPayment {
    void pay(double amount);
}

// 网银支付接口
interface OnlineBankingPayment {
    void pay(double amount);
}
// Visa 信用卡支付
class VisaCreditCardPayment implements CreditCardPayment {
    @Override
    public void pay(double amount) {
        System.out.println("Visa credit card payment: $" + amount);
    }
}

// MasterCard 信用卡支付
class MasterCardCreditCardPayment implements CreditCardPayment {
    @Override
    public void pay(double amount) {
        System.out.println("MasterCard credit card payment: $" + amount);
    }
}

// 支付宝网银支付
class AlipayOnlineBankingPayment implements OnlineBankingPayment {
    @Override
    public void pay(double amount) {
        System.out.println("Alipay online banking payment: ¥" + amount);
    }
}

// 微信网银支付
class WeChatOnlineBankingPayment implements OnlineBankingPayment {
    @Override
    public void pay(double amount) {
        System.out.println("WeChat online banking payment: ¥" + amount);
    }
}
