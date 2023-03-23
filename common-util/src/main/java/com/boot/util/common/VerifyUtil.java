package com.boot.util.common;

import com.boot.util.http.HttpClientUtil;
import org.springframework.lang.NonNull;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

/**
 * @program: kingaev
 * @description:
 * @author: Mr.Wu
 * @create: 2021-08-08 21:38
 **/
public class VerifyUtil {

    /**
     * 实体参数全局校验方法
     *
     * @param obj 校验的实体对象
     * @return 返回校验信息，若为空或null则通过校验
     */

    private final static String BIRTH_DATE_FORMAT = "yyyyMMdd"; // 身份证号码中的出生日期的格式

    private final static Date MINIMAL_BIRTH_DATE = new Date(-2209017600000L); // 身份证的最小出生日期,1900年1月1日

    private final static int NEW_CARD_NUMBER_LENGTH = 18;

    private final static int OLD_CARD_NUMBER_LENGTH = 15;

    private final static char[] VERIFY_CODE = {'1', '0', 'X', '9', '8', '7',
            '6', '5', '4', '3', '2'}; // 18位身份证中最后一位校验码

    private final static int[] VERIFY_CODE_WEIGHT = {7, 9, 10, 5, 8, 4, 2, 1,
            6, 3, 7, 9, 10, 5, 8, 4, 2};// 18位身份证中，各个数字的生成校验码时的权值


    /**
     * 如果是15位身份证号码，则自动转换为18位
     *
     * @param cardNumber
     * @return
     */
    public static boolean checkIdNumber(String cardNumber) {
        if (null != cardNumber) {
            cardNumber = cardNumber.trim();
            if (OLD_CARD_NUMBER_LENGTH == cardNumber.length()) {
                cardNumber = contertToNewCardNumber(cardNumber);
            }
            return validate(cardNumber);
        }
        return false;
    }

    public static boolean validate(String cardNumber) {
        boolean result = true;
        result = result && (null != cardNumber); // 身份证号不能为空
        result = result && NEW_CARD_NUMBER_LENGTH == cardNumber.length(); // 身份证号长度是18(新证)
        // 身份证号的前17位必须是阿拉伯数字
        for (int i = 0; result && i < NEW_CARD_NUMBER_LENGTH - 1; i++) {
            char ch = cardNumber.charAt(i);
            result = result && ch >= '0' && ch <= '9';
        }
        // 身份证号的第18位校验正确
        result = result
                && (calculateVerifyCode(cardNumber) == cardNumber
                .charAt(NEW_CARD_NUMBER_LENGTH - 1));
        // 出生日期不能晚于当前时间，并且不能早于1900年
        try {
            Date birthDate = new SimpleDateFormat(BIRTH_DATE_FORMAT)
                    .parse(getBirthDayPart(cardNumber));
            result = result && null != birthDate;
            result = result && birthDate.before(new Date());
            result = result && birthDate.after(MINIMAL_BIRTH_DATE);
            /**
             * 出生日期中的年、月、日必须正确,比如月份范围是[1,12],
             * 日期范围是[1,31]，还需要校验闰年、大月、小月的情况时，
             * 月份和日期相符合
             */
            String birthdayPart = getBirthDayPart(cardNumber);
            String realBirthdayPart = new SimpleDateFormat(BIRTH_DATE_FORMAT)
                    .format(birthDate);
            result = result && (birthdayPart.equals(realBirthdayPart));
        } catch (Exception e) {
            result = false;
        }
        return result;
    }

    public static String getBirthDayPart(String cardNumber) {
        if(cardNumber.length()>15){
            return cardNumber.substring(6, 14);
        }
        return "";
    }

    public static Integer getAge(String cardNumber) throws Exception {
        if (null != cardNumber) {
            cardNumber = cardNumber.trim();
            if (OLD_CARD_NUMBER_LENGTH == cardNumber.length()) {
                cardNumber = contertToNewCardNumber(cardNumber);
            }

            final Date birthDay = new SimpleDateFormat(BIRTH_DATE_FORMAT).parse(getBirthDayPart(cardNumber));
            Calendar cal = Calendar.getInstance();
            if (cal.before(birthDay)) { //出生日期晚于当前时间，无法计算
                throw new IllegalArgumentException(
                        "The birthDay is before Now.It's unbelievable!");
            }
            int yearNow = cal.get(Calendar.YEAR);  //当前年份
            int monthNow = cal.get(Calendar.MONTH);  //当前月份
            int dayOfMonthNow = cal.get(Calendar.DAY_OF_MONTH); //当前日期
            cal.setTime(birthDay);
            int yearBirth = cal.get(Calendar.YEAR);
            int monthBirth = cal.get(Calendar.MONTH);
            int dayOfMonthBirth = cal.get(Calendar.DAY_OF_MONTH);
            int age = yearNow - yearBirth;   //计算整岁数
            if (monthNow <= monthBirth) {
                if (monthNow == monthBirth) {
                    if (dayOfMonthNow < dayOfMonthBirth) {
                        age--;//当前日期在生日之前，年龄减一
                    }
                } else {
                    age--;//当前月份在生日之前，年龄减一
                }
            }
            return age;
        }
        return null;
    }

    /**
     * 1:男；2：女
     */
    public static Integer getSex(String cardNumber) {
        if (null != cardNumber) {
            cardNumber = cardNumber.trim();
            if (OLD_CARD_NUMBER_LENGTH == cardNumber.length()) {
                cardNumber = contertToNewCardNumber(cardNumber);
            }
            final String substring = cardNumber.substring(16, 17);
            return Integer.parseInt(substring) % 2 == 0 ? 2 : 1;
        }
        return null;
    }

    /**
     * 校验码（第十八位数）：
     * <p>
     * 十七位数字本体码加权求和公式 S = Sum(Ai * Wi), i = 0...16 ，先对前17位数字的权求和；
     * Ai:表示第i位置上的身份证号码数字值 Wi:表示第i位置上的加权因子 Wi: 7 9 10 5 8 4 2 1 6 3 7 9 10 5 8 4
     * 2; 计算模 Y = mod(S, 11)< 通过模得到对应的校验码 Y: 0 1 2 3 4 5 6 7 8 9 10 校验码: 1 0 X 9
     * 8 7 6 5 4 3 2
     *
     * @param cardNumber
     * @return
     */
    private static char calculateVerifyCode(CharSequence cardNumber) {
        int sum = 0;
        for (int i = 0; i < NEW_CARD_NUMBER_LENGTH - 1; i++) {
            char ch = cardNumber.charAt(i);
            sum += ((int) (ch - '0')) * VERIFY_CODE_WEIGHT[i];
        }
        return VERIFY_CODE[sum % 11];
    }

    /**
     * 把15位身份证号码转换到18位身份证号码<br>
     * 15位身份证号码与18位身份证号码的区别为：<br>
     * 1、15位身份证号码中，"出生年份"字段是2位，转换时需要补入"19"，表示20世纪<br>
     * 2、15位身份证无最后一位校验码。18位身份证中，校验码根据根据前17位生成
     *
     * @param oldCardNumber
     * @return
     */
    private static String contertToNewCardNumber(String oldCardNumber) {
        StringBuilder buf = new StringBuilder(NEW_CARD_NUMBER_LENGTH);
        buf.append(oldCardNumber.substring(0, 6));
        buf.append("19");
        buf.append(oldCardNumber.substring(6));
        buf.append(VerifyUtil.calculateVerifyCode(buf));
        return buf.toString();
    }

    public static void main(String[] args) throws ExecutionException, InterruptedException, TimeoutException {
        List<Map<String, Object>> list_kafka = Collections.singletonList(null);
        list_kafka.clear();
    }

}
