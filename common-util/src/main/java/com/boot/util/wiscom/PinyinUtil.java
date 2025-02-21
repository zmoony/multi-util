package com.boot.util.wiscom;

import lombok.extern.log4j.Log4j2;
import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinCaseType;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType;
import net.sourceforge.pinyin4j.format.exception.BadHanyuPinyinOutputFormatCombination;

/**
* @Description: 中文转拼音工具类
*/
@Log4j2
public final class PinyinUtil
{

    /**
     * 获取字符串拼音的第一个字母
     * @param chinese
     * @return
     */
    public static String toFirstChar(String chinese){
        String pinyinStr = "";
        char[] newChar = chinese.toCharArray();  //转为单个字符
        HanyuPinyinOutputFormat defaultFormat = new HanyuPinyinOutputFormat();
        defaultFormat.setCaseType(HanyuPinyinCaseType.LOWERCASE);
        defaultFormat.setToneType(HanyuPinyinToneType.WITHOUT_TONE);
        for (int i = 0; i < newChar.length; i++) {
            if (newChar[i] > 128) {
                try {
                    String[] temps=PinyinHelper.toHanyuPinyinStringArray(newChar[i], defaultFormat);
                    if(temps.length>0){
                        pinyinStr += temps[0].charAt(0);
                    }else{
                        pinyinStr += newChar[i];
                    }
                } catch (BadHanyuPinyinOutputFormatCombination e) {
                    e.printStackTrace();
                    log.error("中文转换首字母异常",e);
                }
            }else{
                pinyinStr += newChar[i];
            }
        }
        return pinyinStr;
    }


    /**
     * 汉字转为拼音
     * @param chinese
     * @return
     */
    public static String toPinyin(String chinese){
        String pinyinStr = "";
        char[] newChar = chinese.toCharArray();
        HanyuPinyinOutputFormat defaultFormat = new HanyuPinyinOutputFormat();
        defaultFormat.setCaseType(HanyuPinyinCaseType.LOWERCASE);
        defaultFormat.setToneType(HanyuPinyinToneType.WITHOUT_TONE);
        for (int i = 0; i < newChar.length; i++) {
            if (newChar[i] > 128) {
                try {
                    String[] temps=PinyinHelper.toHanyuPinyinStringArray(newChar[i], defaultFormat);
                    if(temps.length>0){
                        pinyinStr += temps[0];
                    }else{
                        pinyinStr += newChar[i];
                    }
                } catch (BadHanyuPinyinOutputFormatCombination e) {
                    e.printStackTrace();
                    log.error("中文转换全拼异常",e);

                }
            }else{
                pinyinStr += newChar[i];
            }
        }
        return pinyinStr;
    }

    /**
     *  * 获取中文拼音
     *  *     * @param str
     * * @param shortPinyin 为true获取中文拼音首字母
     * * @param retain      为true保留其他字符
     * * @return String
     *
     */
//    private static String getPinyin(String str, boolean shortPinyin, boolean retain) {
//        if (StringUtil.isBlank(str)) {
//            return "";
//        }
//        StringBuffer pinyinBuffer = new StringBuffer();
//        char[] arr = str.toCharArray();
//        for (char c : arr) {
//            String[] temp = PinyinHelper.toHanyuPinyinStringArray(c);
//            if (!StringUtils.isEmpty(temp)) {
//                if (StringUtil.isNotBlank(temp[0])) {
//                    if (shortPinyin) {
//                        pinyinBuffer.append(temp[0].charAt(0));
//                    } else {
//                        pinyinBuffer.append(temp[0].replaceAll("\\d", ""));
//                    }
//                }
//            } else {
//                if (retain) {
//                    pinyinBuffer.append(c);
//                }
//            }
//        }
//        return pinyinBuffer.toString();
//    }


    public static void main(String[] args) {
        String str = "中 岁";
        System.out.println("例一：获取中文拼音首字母");
        System.out.println(str);
//        System.out.println(getPinyin(str, true, true));
        System.out.println(toFirstChar(str));
        str = "我爱 你中国！";
        System.out.println("例二：获取中文拼音");
        System.out.println(str);
        System.out.println(toPinyin(str));
    }



}
