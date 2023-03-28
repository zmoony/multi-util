package com.example.text.java;

import java.util.function.Predicate;

/**
 * Predicate
 *
 * @author yuez
 * @since 2022/7/13
 */
public class PredicateTest {
    static Predicate<String> emptyPredicate = x-> x == null ||x.equalsIgnoreCase("")||x.equalsIgnoreCase("null");

    public static void main(String[] args) {
        System.out.println(emptyPredicate.test(null));
    }

}
