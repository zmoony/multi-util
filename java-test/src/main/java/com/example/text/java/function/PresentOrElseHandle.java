package com.example.text.java.function;

import java.util.function.Consumer;

/**
 * PresentOrElseHandle
 *
 * @author yuez
 * @since 2023/4/12
 */
@FunctionalInterface
public interface PresentOrElseHandle<T extends Object> {

    /**
     * 不为空的时候进行消费操作
     * 为空的时候就行其他操作
     * @param action 不为空
     * @param emptyAction 为空
     */
    void presentOrEleseHandle(Consumer<? super T> action,Runnable emptyAction);
}
