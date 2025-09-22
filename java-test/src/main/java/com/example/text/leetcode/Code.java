package com.example.text.leetcode;

import java.util.*;

/**
 * Code
 *
 * @author yuez
 * @since 2024/2/22
 */
public class Code {
    public static void main(String[] args) {

//        System.out.println(new Solution().isValid("234Adas"));

    }
}

class Solution {
    /**
     * I 1
     * V 5
     * X 10
     * L 50
     * C 100
     * D 500
     * M 1000
     * <p>
     * I 可以放在 V (5) 和 X (10) 的左边，来表示 4 和 9。
     * X 可以放在 L (50) 和 C (100) 的左边，来表示 40 和 90。
     * C 可以放在 D (500) 和 M (1000) 的左边，来表示 400 和 900。
     * <p>
     * 题目数据保证 s 是一个有效的罗马数字，且表示整数在范围 [1, 3999] 内
     * 题目所给测试用例皆符合罗马数字书写规则，不会出现跨位等情况。
     * IL 和 IM 这样的例子并不符合题目要求，49 应该写作 XLIX，999 应该写作 CMXCIX 。
     *
     * @param s
     * @return
     */
    public int romanToInt(String s) {
        char[] charArray = s.toCharArray();
        int length = s.length();
        int sum = 0;
        int index = length -1;
        while (index >= 0) {
            if (index > 0 && switchRoman(charArray[index]) > switchRoman(charArray[index - 1])) {
                sum += switchRoman(charArray[index]) - switchRoman(charArray[index - 1]);
                index -= 2;
            } else {
                sum += switchRoman(charArray[index]);
                index--;
            }
        }
        return sum;
    }
    

    private int switchRoman(char c) {
        switch (c) {
            case 'I':
                return 1;
            case 'V':
                return 5;
            case 'X':
                return 10;
            case 'L':
                return 50;
            case 'C':
                return 100;
            case 'D':
                return 500;
            case 'M':
                return 1000;
            default:
                return 0;
        }
    }


}

/**
 * 给你一个整数 x ，如果 x 是一个回文整数，返回 true ；否则，返回 false 。
 * <p>
 * 回文数是指正序（从左向右）和倒序（从右向左）读都是一样的整数。
 * <p>
 * 例如，121 是回文，而 123 不是。
 */
class RangeFreqQuery {
    private Map<Integer, List<Integer>> map;

    public RangeFreqQuery(int[] arr) {
        map = new HashMap<Integer, List<Integer>>();
        for (int i = 0; i < arr.length; i++) {
            map.computeIfAbsent(arr[i], k -> new ArrayList<>()).add(i);
        }
    }

    public int query(int left, int right, int value) {
        List<Integer> integers = map.get(value);
        if (integers == null || integers.isEmpty()) {
            return 0;
        }
        int count = 0;
        for (int i = 0; i < integers.size(); i++) {
            if (integers.get(i) >= left && integers.get(i) <= right) {
                count++;
            }
        }
        return count;
    }


}

/**
 * 123
 * 1   null 1 2
 * 2  1  2
 */
//先入先出
class MyQueue {
    //后入先出
    private Stack<Integer> stack1;
    private Stack<Integer> stack2;

    public MyQueue() {
        stack1 = new Stack<>();
        stack2 = new Stack<>();
    }

    public void push(int x) {
        stack2.push(x);
        while (!stack2.isEmpty()) {
            stack1.push(stack2.pop());
        }
    }

    public int pop() {
        return stack1.pop();
    }

    public int peek() {
        return stack1.peek();
    }

    public boolean empty() {
        return stack1.isEmpty();
    }
}

//模拟栈  后进先出
class MyStack {

    Queue<Integer> queue1;
    Queue<Integer> queue2;


    public MyStack() {
        queue1 = new LinkedList<Integer>();
        queue2 = new LinkedList<Integer>();
    }

    public void push(int x) {
        queue2.offer(x);
        while (!queue1.isEmpty()) {
            queue2.offer(queue1.poll());
        }
        Queue<Integer> temp = queue1;
        queue1 = queue2;
        queue2 = temp;
    }

    public int pop() {
        return queue1.poll();
    }

    public int top() {
        return queue1.peek();
    }

    public boolean empty() {
        return queue1.isEmpty();
    }
}


class Solution2 {
    public int trainWays(int num) {
        int a = 1, b = 1, sum;
        for (int i = 0; i < num; i++) {
            sum = (a + b) % 1000000007;
            a = b;
            b = sum;
        }
        return a;
    }
}

/**
 * Your MinStack object will be instantiated and called as such:
 * MinStack obj = new MinStack();
 * obj.push(val);
 * obj.pop();
 * int param_3 = obj.top();
 * int param_4 = obj.getMin();
 */

//先入先出
//class MyQueue {
//    private Stack<Integer> stack1;
//    private Stack<Integer> stack2;
//
//    public MyQueue() {
//        stack1 = new Stack<>();
//        stack2 = new Stack<>();
//    }
//
//    public void push(int x) {
//        stack2.push(stack1.pop());
//        stack1.push(x);
//    }
//
//    // 出队
//    public int pop() {
//        return stack2.pop();
//    }
//
//    // 获取队首元素
//    public int peek() {
//        return stack2.peek();
//    }
//
//    public boolean empty() {
//        return stack1.isEmpty();
//    }
//}

/**
 * Your MyQueue object will be instantiated and called as such:
 * MyQueue obj = new MyQueue();
 * obj.push(x);
 * int param_2 = obj.pop();
 * int param_3 = obj.peek();
 * boolean param_4 = obj.empty();
 */


class TreeNode {
    int val;
    TreeNode left;
    TreeNode right;

    TreeNode() {
    }

    TreeNode(int val) {
        this.val = val;
    }

    TreeNode(int val, TreeNode left, TreeNode right) {
        this.val = val;
        this.left = left;
        this.right = right;
    }
}

class ListNode {
    int val;
    ListNode next;

    ListNode() {
    }

    ListNode(int val) {
        this.val = val;
        this.next = null;
    }

    ListNode(int val, ListNode next) {
        this.val = val;
        this.next = next;
    }
}

class Solution1 {
    /**
     * 动态规划
     * 获取每一天的最大利润，持有/不持有,
     * 动态规划，分解最佳子问题
     *
     * @param prices
     * @return
     */
    public int maxProfit(int[] prices) {
        int length = prices.length;
        int[][] dp = new int[length][2];
        dp[0][0] = 0;
        dp[0][1] = -prices[0];
        for (int i = 1; i < length; i++) {
            dp[i][0] = Math.max(dp[i - 1][0], dp[i - 1][1] + prices[i]);
            dp[i][1] = Math.max(dp[i - 1][1], dp[i - 1][0] - prices[i]);
        }
        return dp[length - 1][0];
    }


    /**
     * 异或运算支持交换律和结合律，所以可以进行异或运算
     *
     * @param nums
     * @return
     */
    public int singleNumber(int[] nums) {
        int res = 0;
        for (int i = 0; i < nums.length; i++) {
            res ^= nums[i];
        }
        return res;
    }


    /**
     * 反转单链表
     *
     * @param head
     * @return
     */
    private ListNode reserveListNode(ListNode head) {
        ListNode pre = null;
        ListNode next;
        while (head != null) {
            next = head.next;//暂存当前节点的下一个节点 3
            head.next = pre;//将下一个节点变成之前的节点 null   2 null
            pre = head;//当前节点给之前的节点
            head = next;//下一个节点给当前节点
        }
        return pre;
    }

    public int 深度搜索(TreeNode root) {
        if (root == null) {
            return 0;
        }
        if (root.left == null && root.right == null) {
            return 1;
        }
        int minDepth = Integer.MAX_VALUE;
        if (root.left != null) {
            minDepth = Math.min(minDepth, 深度搜索(root.left));
        }
        if (root.right != null) {
            minDepth = Math.min(minDepth, 深度搜索(root.right));
        }
        return minDepth + 1;
    }

    class QueueNode {
        TreeNode node;
        int depth;

        public QueueNode(TreeNode node, int depth) {
            this.node = node;
            this.depth = depth;
        }
    }

    public int 广度搜索(TreeNode root) {
        if (root == null) {
            return 0;
        }
        Queue<QueueNode> queue = new LinkedList<>();
        queue.add(new QueueNode(root, 1));
        while (!queue.isEmpty()) {
            QueueNode node = queue.poll();
            if (node.node.left == null && node.node.right == null) {
                return node.depth;
            }
            if (node.node.left != null) {
                queue.add(new QueueNode(node.node.left, 1 + node.depth));
            }
            if (node.node.right != null) {
                queue.add(new QueueNode(node.node.right, 1 + node.depth));
            }
        }
        return 0;

    }
}

