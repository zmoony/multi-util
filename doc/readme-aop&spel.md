## AOP
### 概念
- @Aspect: 切面
- @Poincut: 切点
- JoinPoint: 普通连接点
- ProceedingJoinPoint: 环绕连接点
### 切入时间
- before: 目标方法开始执行之前
- after: 目标方法开始执行之后
- afterReturning: 目标方法返回之后
- afterThrowing： 目标方法抛出异常之后
- around: 环绕目标方法, ****_最为特殊_****
### 表达式
- execution切入点表达式
根据我们所指定的方法的描述信息来匹配切入点方法，这种方式也是最为常用的一种方式
如果我们要匹配的切入点方法的方法名不规则，或者有一些比较特殊的需求，通过execution切入点表达式描述比较繁琐
- annotation 切入点表达式
基于注解的方式来匹配切入点方法。这种方式虽然多一步操作，我们需要自定义一个注解，但是相对来比较灵活。我们需要匹配哪个方法，就在方法上加上对应的注解就可以了

### 代码展示
```java
try{
    try{
        @around
        @before
        method.invoke();
        @around
    }catch(){
        throw new Exception();
    }finally{
        @after
    }
    @afterReturning
}catch(){
    @afterThrowing
}

```
> 其中的around是最为特殊的切入时机, 它的切入点也必须为ProceedingJoinPoint, 其它均为JoinPoint<br>
> 我们需要手动调用ProceedingJoinPoint的proceed方法, 它会去执行目标方法的业务逻辑<br>
> 在around中, 我们可以对目标方法的业务逻辑进行增强, 如日志, 事务, 权限校验等<br>
> around最麻烦, 却也是最强的<br>

## SpEL
### 基本概念
- SpEL: Spring表达式语言
- SpEL表达式: 一个字符串, 它可以被解析为一个对象
### 语法
字符串、数字类型（int、long、float、double）、布尔类型、null类型。
```java
@Test
public void test2() {
    ExpressionParser parser = new SpelExpressionParser();

    String str1 = parser.parseExpression("'Hello World!'").getValue(String.class);
    int int1 = parser.parseExpression("1").getValue(Integer.class);
    long long1 = parser.parseExpression("-1L").getValue(long.class);
    float float1 = parser.parseExpression("1.1").getValue(Float.class);
    double double1 = parser.parseExpression("1.1E+2").getValue(double.class);
    int hex1 = parser.parseExpression("0xa").getValue(Integer.class);
    long hex2 = parser.parseExpression("0xaL").getValue(long.class);
    boolean true1 = parser.parseExpression("true").getValue(boolean.class);
    boolean false1 = parser.parseExpression("false").getValue(boolean.class);
    Object null1 = parser.parseExpression("null").getValue(Object.class);

    System.out.println("str1=" + str1);
    System.out.println("int1=" + int1);
    System.out.println("long1=" + long1);
    System.out.println("float1=" + float1);
    System.out.println("double1=" + double1);
    System.out.println("hex1=" + hex1);
    System.out.println("hex2=" + hex2);
    System.out.println("true1=" + true1);
    System.out.println("false1=" + false1);
    System.out.println("null1=" + null1);
}
```
#### 算数运算表达式
SpEL支持加(+)、减(-)、乘(*)、除(/)、求余（%）、幂（^）运算。

#### 关系表达式
SpEL支持等于(==)、不等于(!=)、小于(<)、小于等于(<=)、大于(>)、大于等于(>=)、相等(===)、不相等(!==)、，区间（between）运算、
空检查(empty)、非空检查(non-empty)、匹配(matches)、不匹配(does not match)、包含(in)、
不包含(not in)、空集合检查(empty collection)、非空集合检查(non-empty collection)、空字符串检查(empty string)、非空字符串检查(non-empty string)
- SpEL同样提供了等价的“EQ” 、“NE”、 “GT”、“GE”、 “LT” 、“LE”来表示等于、不等于、大于、大于等于、小于、小于等于，不区分大小写。
```java
@Test
public void test3() {
    ExpressionParser parser = new SpelExpressionParser();
    boolean v1 = parser.parseExpression("1>2").getValue(boolean.class);
    boolean between1 = parser.parseExpression("1 between {1,2}").getValue(boolean.class);
    System.out.println("v1=" + v1);
    System.out.println("between1=" + between1);
}
```
#### 字符串连接及截取表达式
使用“+”进行字符串连接，使用“'String'[0] [index]”来截取一个字符，目前只支持截取一个，如“'Hello ' + 'World!'”得到“Hello World!”；而“'Hello World!'[0]”将返回“H”。

#### 三目运算
三目运算符 **“表达式1?表达式2:表达式3”**用于构造三目运算表达式，如“2>1?true:false”将返回true；

#### Elivis运算符
Elivis运算符**“表达式1?:表达式2”**从Groovy语言引入用于简化三目运算符的，当表达式1为非null时则返回表达式1，当表达式1为null时则返回表达式2，简化了三目运算符方式“表达式1? 表达式1:表达式2”，如“null?:false”将返回false，而“true?:false”将返回true；

#### 正则表达式
使用“str matches regex，如“'123' matches '\d{3}'”将返回true；

#### 括号优先级表达式
使用“(表达式)”构造，括号里的具有高优先级。

#### 类相关表达式
#### 类类型表达式
使用“T(类名)”来获取类类型，如“T(java.lang.String)”将返回String的Class对象；
#### 实例表达式