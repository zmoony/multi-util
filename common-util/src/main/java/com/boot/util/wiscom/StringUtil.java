package com.boot.util.wiscom;

import lombok.extern.log4j.Log4j2;

import java.text.SimpleDateFormat;
import java.util.Date;

@Log4j2
/**
 * 字符串工具类
 * @author hwang
 */
public final class StringUtil {

    /**
     * 判断字符串是否为空
     *
     * @param parameter 输入字符串
     * @return boolean
     */
    public static boolean isBlank(String parameter) {
        return (null == parameter || parameter.trim().isEmpty() || "null".equalsIgnoreCase(parameter));
    }
    /**
     * 判断字符串是否非空
     *
     * @param parameter 输入字符串
     * @return boolean
     */
    public static boolean isNotBlank(String parameter) {
        return !isBlank(parameter);
    }

    /**
     * 判断是否车牌
     *
     * @param plateNo 车牌号
     * @return boolean true:是 false:否
     * 1.常规车牌号：仅允许以汉字开头，后面可录入六个字符，由大写英文字母和阿拉伯数字组成。如：粤B12345；
     * 2.挂车车牌，后面可录入六个字符，前五位字符，由大写英文字母和阿拉伯数字组成，而最后一个字符为汉字“挂”。
     */
    public static boolean isPlateNo(String plateNo) {
        String Regex = "^[京津沪渝冀豫云辽黑湘皖鲁新苏浙赣鄂桂甘晋蒙陕吉闽贵粤青藏川宁琼使领]{1}[A-Z]{1}[A-Z0-9]{4}[A-Z0-9挂]{1}$";
        if (isBlank(plateNo)) {
            return false;
        } else {
            return plateNo.matches(Regex);
        }
    }

    /**
     * 判断是否挂车车牌
     *
     * @param plateNo 车牌号
     * @return boolean true:是 false:否
     * 1.常规车牌号：仅允许以汉字开头，后面可录入六个字符，由大写英文字母和阿拉伯数字组成。如：粤B12345；
     * 2.挂车车牌，后面可录入六个字符，前五位字符，由大写英文字母和阿拉伯数字组成，而最后一个字符为汉字“挂”。
     */
    public static boolean isTrailerPlateNo(String plateNo) {
        String Regex = "^[京津沪渝冀豫云辽黑湘皖鲁新苏浙赣鄂桂甘晋蒙陕吉闽贵粤青藏川宁琼使领]{1}[A-Z]{1}[A-Z0-9]{4}[挂]{1}$";
        if (isBlank(plateNo)) {
            return false;
        } else {
            return plateNo.matches(Regex);
        }
    }

    /**
     * 判断时间是否在时间段内
     *
     * @param time      需要判断的时间 格式：HHmmss
     * @param startTime 开始时间 格式：HH:mm:ss
     * @param endTime   结束时间 格式：HH:mm:ss
     */
    public static boolean timeCompare(String time, String startTime, String endTime) {

        try {
            String format = "HH:mm:ss";
            Date startDate = new SimpleDateFormat(format).parse(startTime);
            Date endDate = new SimpleDateFormat(format).parse(endTime);
            String format2 = "HHmmss";
            Date date = new SimpleDateFormat(format2).parse(time);
            long begin = startDate.getTime();
            long end = endDate.getTime();
            long cd = date.getTime();
            return (cd >= begin && cd <= end);

        } catch (Exception e) {
            log.error("时间比较出错：" + e);
            return false;
        }
    }

    /**
     * 获取字符串尺寸长度
     *
     * @param param     文字内容
     * @param limitSize 限制大小
     * @return int 文字个数
     */
    public static int size(String param, int limitSize) {
        double size = 0;
        char[] chs = param.toCharArray();
        for (char ch : chs) {
            if (isChineseChar(ch)) {
                size += 63;
            } else if (ChineseAndEnglishUtils.isChinesePunctuation(ch)) {
                size += 63;
            } else {
                size += 38;
            }
        }

        double count = Math.ceil(size / limitSize);
        return Double.valueOf(count).intValue();
    }

    /**
     * 获取字符串尺寸长度
     *
     * @param param     文字内容
     * @param limitSize 限制大小
     * @return int 文字个数
     */
    public static int findIndex(String param, int limitSize) {
        int size = 0;
        char[] chs = param.toCharArray();
        for (int i = 0; i < chs.length; i++) {
            if (isChineseChar(chs[i])) {
                size += 63;
            } else if (ChineseAndEnglishUtils.isChinesePunctuation(chs[i])) {
                size += 63;
            } else {
                size += 38;
            }
            if (size > limitSize) {
                return i;
            }
        }
        return param.length();
    }

    /**
     * 判断一个字符是否是汉字
     * PS：中文汉字的编码范围：[\u4e00-\u9fa5]
     *
     * @param c 需要判断的字符
     * @return 是汉字(true), 不是汉字(false)
     */
    private static boolean isChineseChar(char c) {
        return String.valueOf(c).matches("[\u4e00-\u9fa5]");
    }

    /**
     * 字符串匹配 ? 和 *
     * @param s 待匹配的字符串
     * @param p 匹配规则
     * @return Boolean
     */
    public static Boolean isMatch(String s, String p) {

        if (isBlank(p)) {
            p = "*";
        }
        //s的索引位置
        int i = 0;
        //p的索引位置
        int j = 0;
        //通配符时回溯的位置
        int ii = -1;
        int jj = -1;
        while (i < s.length()) {
            if (j < p.length() && p.charAt(j) == '*') {
                //遇到通配符了,记录下位置,规则字符串+1,定位到非通配字符串
                ii = i;
                jj = j;
                j++;
            } else if (j < p.length() && (s.charAt(i) == p.charAt(j) || p.charAt(j) == '?')) {
                //匹配到了
                i++;
                j++;
            } else {
                //匹配失败,需要判断s 是否被p的*号匹配着
                if (jj == -1) {
                    //前面没有通配符
                    return false;
                }
                //回到之前记录通配符的位置
                j = jj;
                //带匹配字符串也回到记录的位置,并后移一位
                i = ii + 1;
            }

        }
        //当s的每一个字段都匹配成功以后,判断p剩下的串,是*则放行
        while (j < p.length() && p.charAt(j) == '*') {
            j++;
        }
        //检测到最后就匹配成功
        return j == p.length();
    }

    public static void main(String[] args) {
        boolean bFlag = isPlateNo("豫NSU51挂");
    }
}
