package com.example.text.java.function;

@FunctionalInterface
public interface BranchHandle {
    /**
     * 分支操作
     * @param trueHandle 为true时操作
     * @param falseHandle 为false时操作
     */
    void trueOrFalseHandle(Runnable trueHandle,Runnable falseHandle);


}
