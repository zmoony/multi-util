package com.example.text.leetcode;

import java.util.HashMap;

/**
 * 滑动窗口
 *
 * @author yuez
 * @since 2024/5/28
 */
public class 滑动窗口 {
    //abcbabcabc ，pwwkew
    public int lengthOfLongestSubstring(String s) {
        if (s.length() == 0) {
            return 0;
        }
        HashMap<Character, Integer> map = new HashMap<>();
        int left = 0,max = 0;
        for (int i = 0; i < s.length(); i++) {
            if(map.containsKey(s.charAt(i))){
                left = Math.max(left, map.get(s.charAt(i)) + 1);
            }
            map.put(s.charAt(i), i);
            max = Math.max(max, i - left + 1);
        }
        return max;
    }
}
