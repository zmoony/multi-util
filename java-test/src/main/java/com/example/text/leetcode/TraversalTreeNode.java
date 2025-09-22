package com.example.text.leetcode;

import java.util.*;

/**
 * TraversalTreeNode
 *
 * @author yuez
 * @since 2024/2/29
 */
public class TraversalTreeNode {
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

    /**
     * 深度遍历
     * @param root
     */
    void dfs(TreeNode root) {
        if (root == null) {
            return;
        }
        dfs(root.left);
        dfs(root.right);
    }

    /**
     * 广度遍历/层序遍历
     * @param root
     */
    void bfs(TreeNode root) {
        Queue<TreeNode> queue = new ArrayDeque<>();
        queue.add(root);
        while (!queue.isEmpty()) {
            TreeNode node = queue.poll(); // Java 的 pop 写作 poll()
            if (node.left != null) {
                queue.add(node.left);
            }
            if (node.right != null) {
                queue.add(node.right);
            }
        }
    }


    //输入：root = [3,9,20,null,null,15,7] 输出：[[3],[9,20],[15,7]]
    public List<List<Integer>> levelOrder(com.example.text.leetcode.TreeNode root) {
        List<List<Integer>> res = new ArrayList<>();
        //使用BFS
        Deque<com.example.text.leetcode.TreeNode> deque = new ArrayDeque<>();
        if (root != null) {
            deque.add(root);
        }
        while (!deque.isEmpty()) {
            int size = deque.size();
            List<Integer> level = new ArrayList<>();
            for (int i = 0; i < size; i++) {
                com.example.text.leetcode.TreeNode treeNode = deque.poll();
                level.add(treeNode.val);
                if (treeNode.left != null) {
                    deque.add(treeNode.left);
                }
                if (treeNode.right != null) {
                    deque.add(treeNode.right);
                }
            }
            res.add(level);
        }
        return res;
    }


}
