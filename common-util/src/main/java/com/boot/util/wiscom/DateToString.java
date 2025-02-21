package com.boot.util.wiscom;

import lombok.extern.log4j.Log4j2;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * DateToString Class
 * 日期转换为字符串
 * @author hwang
 * @date 2020-10-08
 */
@Log4j2
public class DateToString {

    private static String date_formatter_str = "yyyyMMddHHmmss";

    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern(date_formatter_str);

    private static String day_formatter_str = "yyyyMMdd";

    private static final DateTimeFormatter DAY_FORMATTER = DateTimeFormatter.ofPattern(day_formatter_str);

    /**获取当前时间 格式：yyyyMMddHHmmss*/
    public static String getCurrentDateTime() {
        return DATE_TIME_FORMATTER.format(LocalDateTime.now());
    }
    /**获取当前时间
     * @param strFormat
     *      字符串中的日期格式，例如: yyyyMMddHHmmss 或 yyyy-MM-dd HH:mm:ss 或其他
     */
    public static String getCurrentDateTimeByFormat(String strFormat) {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(strFormat);
        return dateTimeFormatter.format(LocalDateTime.now());
    }

    /**获取当前日期 格式：yyyyMMdd*/
    public static String getCurrentDay() {
        return DAY_FORMATTER.format(LocalDateTime.now());
    }

    /**
     * @param strFormat
     *      字符串中的日期格式，例如: yyyyMMddHHmmss 或 yyyy-MM-dd HH:mm:ss 或其他
     * */
    public static String getCurrentDateTime(String strFormat) {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(strFormat);
        return dateTimeFormatter.format(LocalDateTime.now());
    }
    /**
     * @param time
     *      输入时间
     *      strFormat
     *      字符串中的日期格式，例如: yyyyMMddHHmmss 或 yyyy-MM-dd HH:mm:ss 或其他
     * @return String
     *      输出字符串
     * */
    public static String getDate(Timestamp time, String strFormat){
        SimpleDateFormat sdf =   new SimpleDateFormat( strFormat);
        String str=null;
        try {
            str=sdf.format(time);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            log.error("字符串时间格式错误，正确格式为{}",strFormat);
        }
        return str;
    }
}
