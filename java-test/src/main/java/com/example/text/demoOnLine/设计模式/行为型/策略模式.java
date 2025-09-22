package com.example.text.demoOnLine.设计模式.行为型;

/**
 * 策略模式
 * 行为型设计模式
 * 它定义了一系列的算法，将每个算法都封装起来，并使它们可以互相替换，从而使算法的变化不会影响到使用算法的客户端。这种模式可以使算法的变化更加灵活和可控，同时也可以提高代码的可读性和可维护性。
 *
 * @author yuez
 * @since 2023/4/21
 */
public class 策略模式 {
    public static void main(String[] args) {
        Orders orders = new Orders(100, new DiscountPromotionStrategy(0.1));
        System.out.println(orders.calculatePrice());

        orders = new Orders(200,new FullReductionPromotionStrategy(150,50));
        System.out.println(orders.calculatePrice());

        orders = new Orders(300, new DirectReductionPromotionStrategy(50));
        System.out.println(orders.calculatePrice());
    }
}
class Orders{
    double price;
    PromotionStrategy promotionStrategy;

    public Orders(double price, PromotionStrategy promotionStrategy) {
        this.price = price;
        this.promotionStrategy = promotionStrategy;
    }

    //包含策略的统一方法，直接调用
    public double calculatePrice(){
        return promotionStrategy.calculatePrice(price);
    };
}

/**
 * 假设有一个电商网站，它需要根据不同的促销策略来计算订单的价格。促销策略包括打折、满减、直降等等。
 */
interface PromotionStrategy {
    double calculatePrice(double price);
}

/**
 * 打折
 */
class DiscountPromotionStrategy implements PromotionStrategy {
    double discount;

    public DiscountPromotionStrategy(double discount) {
        this.discount = discount;
    }

    @Override
    public double calculatePrice(double price) {
        return price * (1 - discount);
    }
}

/**
 * 满减
 */
class FullReductionPromotionStrategy implements PromotionStrategy {
    double threshold;
    double reduction;

    public FullReductionPromotionStrategy(double threshold, double reduction) {
        this.threshold = threshold;
        this.reduction = reduction;
    }

    @Override
    public double calculatePrice(double price) {
        return price > threshold ? price - reduction : price;
    }
}

/**
 * 直降
 */
class DirectReductionPromotionStrategy implements PromotionStrategy{
    double reduction;

    public DirectReductionPromotionStrategy(double reduction) {
        this.reduction = reduction;
    }

    @Override
    public double calculatePrice(double price) {
        return Math.max(0,price -reduction);
    }
}