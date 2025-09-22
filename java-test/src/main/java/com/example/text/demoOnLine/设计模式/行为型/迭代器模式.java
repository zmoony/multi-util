package com.example.text.demoOnLine.设计模式.行为型;

import java.util.ArrayList;
import java.util.List;

/**
 * 迭代器模式
 * 行为设计模式
 * 它提供一种统一的方法来遍历一个容器中的所有元素，而不用暴露容器的内部结构。
 *
 * Iterator（迭代器）这个接口负责定义按顺序遍历元素的接口。其中hasNext负责判断是否还存在下一个元素，next方法用于获取该元素并将元素指针后移。
 * ConcreteIterator（具体的迭代器） 该类负责实现Iterator所定义的接口，同时包含遍历集合所必要的信息。在此示例中，BookShelf类的实例保存至bookShelf中，指向当前书的下标保存在index中。
 * Aggregate（集合）实现该接口的类都需要实现获取Iterator的方法
 * ConcreteAggregate（具体的集合）该类负责具体实现Aggregate所定义的方法。在该示例中由BookShelf负责承担此功能。
 *
 * @author yuez
 * @since 2023/4/26
 */
public class 迭代器模式 {
    public static void main(String[] args) {
        /*BookShelf bookShelf = new BookShelf();
        bookShelf.appendBook(new Book("文城", 55.0,"余华"));
        bookShelf.appendBook(new Book("机器学习", 63.0,"周志华"));
        bookShelf.appendBook(new Book("统计学习方法", 82.0,"李航"));

        Iterator<Book> iterator = bookShelf.getIterator();
        while (iterator.hasNext()){
            Book book = iterator.next();
            System.out.println("book = " + book);
        }*/
        NameList nameList = new NameListImpl();
        nameList.addName("Alice");
        nameList.addName("Bob");
        nameList.addName("Charlie");

        java.util.Iterator<String> iterator = nameList.iterator();
        while (iterator.hasNext()){
            System.out.println(iterator.next());
        }


        //测试 index++(先赋值 后计算)  --index（先计算，后赋值） 方法传参：传的是赋值给一个方法临时变量，所以前后方向很关键
        int index = 0;
        System.out.println(index++);//0
        System.out.println(index++);//1
//        System.out.println(index--);//2
        System.out.println(--index);//1


    }
}
//**************简单模式***************
interface NameList{
    void addName(String name);
    java.util.Iterator<String> iterator();
}
class NameListImpl implements NameList{
    private List<String> names = new ArrayList<>();

    @Override
    public void addName(String name) {
        names.add(name);
    }

    @Override
    public java.util.Iterator<String> iterator() {
        return new NameIterator();
    }

    private class NameIterator implements java.util.Iterator<String>{
        private int index = 0;
        @Override
        public boolean hasNext() {
            return index < names.size();
        }

        @Override
        public String next() {
            return names.get(index++);
        }


        public void remove(){
            names.remove(--index);
        }
    }
}



//Iterator 实现
interface Iterator<T>{
    public boolean hasNext();
    public T next();
}
//BookShelfIterator实现
class BookShelfIterator implements Iterator<Book>{
    private BookShelf bookShelf;
    private int index = 0;
    public BookShelfIterator(BookShelf bookShelf) {
        this.bookShelf = bookShelf;
    }
    @Override
    public boolean hasNext() {
        return bookShelf.getLength() > index;
    }

    @Override
    public Book next() {
        Book book = bookShelf.getBookAt(index);
        index++;
        return book;
    }
}
interface Aggregate<T> {
    /**
     * 获取迭代器
     * @return Iterator
     */
    public Iterator<T> getIterator();
}
class BookShelf implements Aggregate<Book>{
    List<Book> bookList = new ArrayList<>();
    @Override
    public Iterator<Book> getIterator() {
        return new BookShelfIterator(this);
    }
    public Integer getLength(){
        return this.bookList.size();
    }

    public void appendBook(Book book){
        bookList.add(book);
    }

    public Book getBookAt(Integer position){
        return this.bookList.get(position);
    }
}


class Book {
    private String bookName;
    private Double price;
    private String author;

    public Book() {
    }

    public Book(String bookName, Double price, String author) {
        this.bookName = bookName;
        this.price = price;
        this.author = author;
    }

    @Override
    public String toString() {
        return "Book{" +
                "bookName='" + bookName + '\'' +
                ", price=" + price +
                ", author='" + author + '\'' +
                '}';
    }
}