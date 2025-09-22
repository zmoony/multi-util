package com.example.text.demoOnLine.设计模式.行为型;



import java.util.ArrayList;
import java.util.List;

/**
 * 责任链模式
 * 行为型设计模式，它允许多个对象来处理请求，并且将这些对象连成一条链。当请求到来时，它会依次经过链上的对象，直到有一个对象能够处理请求为止。
 * 过滤器是过滤，责任链会传递给下一个对象
 * 实现要点：具体filter里的chain都是同一个对象；每次调用就会自动调用加入的下一个filter
 *
 * @author yuez
 * @since 2023/4/26
 */
public class 责任链模式 {
    public static void main(String[] args) {
        Request request = new Request();
        Response response = new Response();
        FilterChain2 filterChain = new FilterChain2();
        filterChain.addFilter(new FilterA());
        filterChain.addFilter(new FilterB());
        filterChain.doFilter(request,response);//调用第一个A
    }
}

interface Filter2 {
    void doFilter(Request request, Response response, FilterChain2 chain);
}
class FilterA implements Filter2{

    @Override
    public void doFilter(Request request, Response response, FilterChain2 chain) {
        System.out.println("A 过滤...");
        chain.doFilter(request,response);//继续调用下一个filterB
    }
}
class FilterB implements Filter2{

    @Override
    public void doFilter(Request request, Response response, FilterChain2 chain) {
        System.out.println("B 过滤...");
        chain.doFilter(request,response);//继续调用下一个filter
    }
}

class Request{}
class Response{}
class FilterChain2{
    private List<Filter2> filter2s = new ArrayList<>();
    private int index = 0;
    public void addFilter(Filter2 filter2){
        filter2s.add(filter2);
    }
    public void doFilter(Request request,Response response){
        if(index == filter2s.size()){
            return;
        }
        Filter2 filter2 = filter2s.get(index);
        index++;
        filter2.doFilter(request,response,this);

    }

}
