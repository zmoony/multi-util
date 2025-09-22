package com.example.text.leetcode;

/**
 * BinarySearch
 * 数组必须是有序的
 * @author yuez
 * @since 2024/3/5
 */
public class BinarySearch {
    /**
     * 循环版本的二分查找
     * @param array
     * @param target
     * @return
     */
    public static int binarySearch(int[] array,int target){
        int left = 0;
        int right = array.length-1;
        while(left <= right){
            int mid = left + (right - left)/2;
            if(array[mid] == target){
                return mid;
            }else if(array[mid] < target){
                left = mid + 1;
            }else{
                right = mid - 1;
            }
        }
        return -1;
    }

    /**
     * 递归版本的二分查找
     * @param array
     * @param target
     * @return
     */
    public static int binarySearchRecursive(int[] array,int target){
        return binarySearchRecursive(array,target,0,array.length-1);
    }

    public static int binarySearchRecursive(int[] array,int target,int left,int right){
        if(left > right){
            return -1;
        }
        int mid = left + (right - left)/2;
        if(array[mid] == target){
            return mid;
        }else if(array[mid] < target){
            return binarySearchRecursive(array,target,mid+1,right);
        }else{
            return binarySearchRecursive(array,target,left,mid-1);
        }
    }
}
