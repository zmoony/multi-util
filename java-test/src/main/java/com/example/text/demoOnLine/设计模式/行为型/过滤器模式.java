package com.example.text.demoOnLine.设计模式.行为型;


import com.example.text.demoOnLine.设计模式.User;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * 过滤器模式
 * 结构型设计模式
 * 它允许你使用不同的标准来过滤一组对象，从而去除其中不需要的元素。
 *
 * @author yuez
 * @since 2023/4/24
 */
public class 过滤器模式 {
    public static void main(String[] args) {
        List<User> users = new ArrayList<>();
        users.add(new User.Builder().setName("aili").setAge(20).build());
        users.add(new User.Builder().setName("aaa").setAge(20).build());
        users.add(new User.Builder().setName("aili").setAge(10).build());

        Filter nameFilter = new NameFilter();
        Filter ageFilter = new AgeFilter();
        new FilterChain(Arrays.asList(nameFilter, ageFilter)).filter(users).forEach(System.out::println);
    }
}
interface Filter{
    List<User> filter(List<User> users);
}

class FilterChain implements Filter{
    private List<Filter> filters;

    public FilterChain(List<Filter> filters) {
        this.filters = filters;
    }

    @Override
    public List<User> filter(List<User> users) {
        List<User> userList = users;
        for (Filter filter : filters) {
            userList = filter.filter(userList);
        }
        return userList;
    }
}

class NameFilter implements Filter{

    @Override
    public List<User> filter(List<User> users) {
        return users.stream().filter(user -> user.name.startsWith("l")).collect(Collectors.toList());
    }
}

class AgeFilter implements Filter{
    @Override
    public List<User> filter(List<User> users) {
        return users.stream().filter(user -> user.age>18).collect(Collectors.toList());
    }
}

/**
 * 使用多个stream的filter进行过滤的代码结构优化
 */
class FilterChain3  {
    public static void main(String[] args) {
        List<User> users = new ArrayList<>();
        users.add(new User.Builder().setName("aili").setAge(20).build());
        users.add(new User.Builder().setName("aaa").setAge(20).build());
        users.stream()
                .filter(new Fileter1())
                .filter(new Fileter2())
                .forEach(System.out::println);
    }
}

class Fileter1 implements Predicate<User>{
    @Override
    public boolean test(User o) {
        return o.age>18;
    }
}
class Fileter2 implements Predicate<User>{
    @Override
    public boolean test(User o) {
        return o.email.contains("@");
    }
}
