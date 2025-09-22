package com.example.text.demoOnLine.设计模式.行为型;

import java.util.HashSet;
import java.util.Set;

/**
 * 解释器模式
 * 行为型设计模式
 * 用编译语言的方式来分析应用中的实例
 * 它定义了一种语言语法，以及解释器，可以解释这种语法。解释器模式通常用于编译器、解析器、表达式计算器等领域。例如，正则表达式引擎就是一个常见的使用解释器模式的例子。
 * 优点：扩展性好，利用继承机制扩展文法
 * 缺点：效率低，调用麻烦，容易引起类膨胀，这个模式很少能用到
 *
 * 抽象表达式（Abstract Expression）：定义解释器的接口，约定解释器的解释操作，主要包含解释方法 interpret()。
 * 终结符表达式（Terminal Expression）：是抽象表达式的子类，用来实现文法中与终结符相关的操作，文法中的每一个终结符都有一个具体终结表达式与之相对应。
 * 非终结符表达式（Nonterminal Expression）：也是抽象表达式的子类，用来实现文法中与非终结符相关的操作，文法中的每条规则都对应于一个非终结符表达式。
 * 上下文（Context）：通常包含各个解释器需要的数据或是公共的功能，一般用来传递被所有解释器共享的数据，后面的解释器可以从这里获取这些值。
 *
 * @author yuez
 * @since 2023/4/26
 */
public class 解释器模式 {
    public static void main(String[] args) {
        Context bus = new Context();
        System.out.println("公交乘车开始刷卡");
        bus.freeRide("北京的老人");
        bus.freeRide("太原的孕妇");
        bus.freeRide("太原的儿童");
        bus.freeRide("上海的老人");
    }
}
//抽象表达式
interface Expression{
    boolean interpret(String info);
}
//终结符表达式
class TerminalExpression implements Expression{
    private Set<String> set = new HashSet<>();

    public TerminalExpression(String[] data) {
        for (int i = 0; i < data.length; i++) {
            set.add(data[i]);
        }
    }

    @Override
    public boolean interpret(String info) {
        if(set.contains(info)){
            return true;
        }
        return false;
    }
}

//非终结符表达式
class AndExpression implements Expression{
    //左半部分
    private Expression left;
    //右半部分
    private Expression right;
    //切分关键字
    public static final String FLFX = "的";

    public AndExpression(Expression left, Expression right) {
        this.left = left;
        this.right = right;
    }

    @Override
    public boolean interpret(String info) {
        String[] s = info.split(FLFX);
        return left.interpret(s[0]) && right.interpret(s[1]);
    }
}

class Context{
    private String[] citys = {"北京", "太原"};
    private String[] persons = {"老人", "孕妇"};
    private Expression expression;
    public static final String FLFX = "的";

    public Context() {
        TerminalExpression city = new TerminalExpression(citys);
        TerminalExpression persons = new TerminalExpression(this.persons);
        expression = new AndExpression(city,persons);
    }

    public void freeRide(String info){
        //提示语，可自行设置
        String s1 = "哔！"+info.split(FLFX)[1] + "卡，欢迎乘车，您本次乘车免费！";
        String s2 = "哔！欢迎乘车，您本次乘车扣费2元！";
        System.out.println(expression.interpret(info) ? s1 : s2);
    }

}




