package com.example.boot3.jdk17;

/**
 * 密封类用关键字 sealed修饰，并且在声明末尾用 permits表示要开放给哪些类型。
 * 继承类也要加上密封限制。比如这个例子中是用的 non-sealed，表示不限制，任何类都可以继承，还可以是 sealed，或者 final。
 * 不被允许的类继承，会报org.jdk17.SealedPlayer（因为它未列在其 'permits' 子句中）
 * @author yuez
 * @since 2023/2/15
 */
public class SealedMain{
    public static void main(String[] args) {
        SealedTest sealedTest = new ATest();
        sealedTest.play();
    }
}
sealed class SealedTest permits ATest{
    public void play() {
        System.out.println("玩儿吧");
    }
}
non-sealed class ATest extends SealedTest{
    @Override
    public void play() {
        System.out.println("新的");
    }
}

/*
non-sealed class BTest extends SealedTest{
    @Override
    public void play() {
        System.out.println("bbb新的");
    }
}*/
