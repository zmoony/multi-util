package com.boot.util.common;

import org.springframework.util.CollectionUtils;

import java.text.DecimalFormat;
import java.util.Base64;
import java.util.Collections;
import java.util.List;
import java.util.Set;

/**
 * @author yuez
 * @title: StringUtils
 * @projectName zy_wiscom
 * @description: String的工具类
 * @date 2020/9/23 11:25
 */
public class StringUtils {
    public static final String sizeTypeB = "B";
    public static final String sizeTypeMB = "MB";
    public static final String sizeTypeKB = "KB";
    public static final String sizeTypeGB = "GB";

    /***
     　　* @description: 转换文件大小
     　　* @param [str, sizeType]
     　　* @return int
     　　* @throws
     　　* @author yuez
     　　* @date 2020/9/23 11:31
     　　*/
    public static String getSize(String str, String sizeType) {
        if (isEmpty(str)) return "0";
        DecimalFormat df2 = new DecimalFormat("0.000");
        byte[] bytes = str.getBytes();
        switch (sizeType) {
            case sizeTypeB:
                return (double) bytes.length + "";
            case sizeTypeKB:
                return df2.format(bytes.length / 1024.00f);
            case sizeTypeMB:
                return df2.format((double) bytes.length / (1024 * 1024.00f));
            case sizeTypeGB:
                return df2.format((double) bytes.length / (1024 * 1024 * 1024.00f));
            default:
                return (double) bytes.length + "";
        }
    }

    /**
     * 　　* @description: 判断字符是否为空
     * 　　* @param [str]
     * 　　* @return boolean
     * 　　* @throws
     * 　　* @author yuez
     * 　　* @date 2020/9/23 11:27
     *
     */
    public static boolean isEmpty(String str) {
        if (str == null || str.length() == 0) {
            return true;
        }
        return false;
    }

    //将 sda,sds 转换为 'sda','sds'
    public static String dealIds(String ids) {
        return ids.replaceAll("\\w+", "'$0'");
    }
    //将url转为uri
    public static String UrlToUri (String url) {
        return url.replaceAll("\\w+:\\/\\/\\d+\\.\\d+\\.\\d+\\.\\d+:\\d+","");
    }

    /**
     * 占位符夫人对比
     * @param str
     * @param fuzzyList ？代表一位 * 代表多位
     * @return
     */
    public static boolean fuzzyMatching(String str, List<String> fuzzyList){
        for (String fuzzyStr : fuzzyList) {
            if(str.length() < fuzzyStr.length()){
                break;
            }
            fuzzyStr = fuzzyStr.replaceAll("\\?", ".{1}");
            fuzzyStr = fuzzyStr.replaceAll("\\*",".*");
            System.out.println(fuzzyStr);
           return str.matches(fuzzyStr);
        }
        return false;
    }

    public static String encodeString(String str){
        String s = Base64.getEncoder().encodeToString(str.getBytes());
        System.out.println(s);
        byte[] decode = Base64.getDecoder().decode(s);
        System.out.println(new String(decode));
        return "";
    }

    public static void main(String[] args) {
       /* String str = "苏A12345";
        List<String> singleton = Collections.singletonList("苏A?345");
        System.out.println(str+"比对"+singleton+":"+fuzzyMatching(str,singleton));*/
        encodeString("s63723186y3917239@苏A12345");
    }
}
