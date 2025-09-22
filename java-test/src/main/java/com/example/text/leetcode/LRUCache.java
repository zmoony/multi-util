package com.example.text.leetcode;

import java.util.HashMap;

public class LRUCache {

    /**
     * 双向链表
     */
    class DListNode {
        int key, value;
        DListNode next;
        DListNode prev;

        DListNode(int _key, int _value) {
            this.key = _key;
            this.value = _value;
        }

        DListNode() {
        }

        DListNode(int _key) {
            this.key = _key;
        }


    }

    private HashMap<Integer, DListNode> map = new HashMap<>();
    private int capacity = 0;
    private DListNode head,tail;
    private int size = 0;

    public LRUCache(int capacity) {
        this.capacity = capacity;
        this.size = 0;
        this.head = new DListNode();
        this.tail = new DListNode();
        head.next = tail;
        tail.prev = head;
    }

    public Integer get(int key) {
        DListNode dListNode = map.get(key);
        if (dListNode == null) {
            return -1;
        }
        moveToHead(dListNode);
        return dListNode.value;
    }

    public void put(int key, int value) {
        DListNode dListNode = map.get(key);
        if(dListNode != null){
            dListNode.value = value;
            moveToHead(dListNode);
        }else{
            DListNode dListNode1 = new DListNode(key, value);
            map.put(key,dListNode1);
            addToHead(dListNode1);
            ++size;
            if(size > capacity){
                DListNode removedTail = removeTail();
                map.remove(removedTail.key);
                --size;
            }
        }


    }


    public void addToHead(DListNode node){
        node.next = head.next;
        node.prev = head;
        head.next.prev = node;
        head.next = node;
    }
    public void removeNode(DListNode node){
        node.prev.next = node.next;
        node.next.prev = node.prev;
    }
    public void moveToHead(DListNode node){
        removeNode(node);
        addToHead(node);
    }

    public DListNode removeTail(){
        DListNode res = tail.prev;
        removeNode(res);
        return res;
    }





}
