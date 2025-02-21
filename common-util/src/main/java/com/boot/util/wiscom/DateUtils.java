package com.boot.util.wiscom;

import lombok.extern.log4j.Log4j2;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;

/**
 * 日期操作类
 */
@Log4j2
public class DateUtils {


    public static Date getBeginDayOfLastWeek() {
        Date date = new Date();
        if (date == null) {
            return null;
        }
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        int dayofweek = cal.get(Calendar.DAY_OF_WEEK);
        if (dayofweek == 1) {
            dayofweek += 7;
        }
        cal.add(Calendar.DATE, 2 - dayofweek - 7);
        return getDayStartTime(cal.getTime());
    }

    // 获取上周的结束时间
    public static Date getEndDayOfLastWeek() {
        Calendar cal = Calendar.getInstance();
        cal.setTime(getBeginDayOfLastWeek());
        cal.add(Calendar.DAY_OF_WEEK, 6);
        Date weekEndSta = cal.getTime();
        return getDayEndTime(weekEndSta);
    }
    // 获取某个日期的开始时间
    public static Date getDayStartTime(Date d) {
        Calendar calendar = Calendar.getInstance();
        if (null != d)
            calendar.setTime(d);
        calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH), 0, 0, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTime();
    }

    // 获取某个日期的结束时间
    public static Date getDayEndTime(Date d) {
        Calendar calendar = Calendar.getInstance();
        if (null != d)
            calendar.setTime(d);
        calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH), 23, 59, 59);
        calendar.set(Calendar.MILLISECOND, 999);
        return calendar.getTime();
    }

    // 获取某个时间精确到当前小时20200519090000
    public static Date getLastHour(Date d) {
        Calendar calendar = Calendar.getInstance();
        if (null != d)
            calendar.setTime(d);
        calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH), calendar.get(Calendar.HOUR_OF_DAY), 0, 0);
        calendar.set(Calendar.MILLISECOND, 999);
        return calendar.getTime();
    }

    //日期增加天数 结果小时数为000000为一天的开始
    public static Date getAfterDayOfStart(Date d,int count) {
        Calendar calendar = Calendar.getInstance();
        if (null != d)
            calendar.setTime(d);
        calendar.add(Calendar.DAY_OF_MONTH, count);
        return getDayStartTime(calendar.getTime());
    }

    // 日期增加天数 结果小时数为235959为一天的结束
    public static Date getAfterDayOfEnd(Date d,int count) {
        Calendar calendar = Calendar.getInstance();
        if (null != d)
            calendar.setTime(d);
        calendar.add(Calendar.DAY_OF_YEAR, count);
        return getDayEndTime(calendar.getTime());
    }

    /**
     * yyyy-MM-dd HH:mm
     * 异常返回null  yyyyMMddHHmm
     * @param stringdate
     * @return
     */
    public static Date getDateToMinute(String stringdate){
        SimpleDateFormat sdf =   new SimpleDateFormat( "yyyy-MM-dd HH:mm");
        Date date1=null;
        try {
            date1=sdf.parse(stringdate);
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            log.error("字符串时间格式错误，正确格式为yyyyMMddHHmm");
        }
        sdf=null;
        return date1;
    }
    /**
     * yyyy-MM-dd
     * @param stringdate
     * @return
     */
    public static Date getDateToDay(String stringdate){
        SimpleDateFormat sdf =   new SimpleDateFormat( "yyyy-MM-dd");
        Date date1=null;
        try {
            date1=sdf.parse(stringdate);
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            log.error("字符串时间格式错误，正确格式为yyyy-MM-dd");
        }
        return date1;
    }

    /**
     * yyyy-MM
     * @param stringdate
     * @return
     */
    public static Date getDateToMonth(String stringdate){
        SimpleDateFormat sdf =   new SimpleDateFormat( "yyyy-MM");
        Date date1=null;
        try {
            date1=sdf.parse(stringdate);
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            log.error("字符串时间格式错误，正确格式为yyyy-MM");
        }
        return date1;
    }

    /**
     * yyyy-MM-dd
     * @param stringdate
     * @return
     */
    public static Date getDateToHour(String stringdate){
        SimpleDateFormat sdf =   new SimpleDateFormat( "yyyy-MM-dd HH");
        Date date1=null;
        try {
            date1=sdf.parse(stringdate);
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            log.error("字符串时间格式错误，正确格式为yyyy-MM");
        }
        return date1;
    }

    /**
     * yyyyMMddHHmmss
     * @param stringdate
     * @return
     */
    public static Date getDateToSecond(String stringdate){
        SimpleDateFormat sdf =   new SimpleDateFormat( "yyyyMMddHHmmss");
        Date date1=null;
        try {
            date1=sdf.parse(stringdate);
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            log.error("字符串时间格式错误，正确格式为yyyyMMddHHmmss");
        }
        return date1;
    }



    public static String getStringDate(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        String str = "";
        try {
            str = sdf.format(date);
        } catch (Exception e) {
            log.error("时间转换错误");
        }
        return str;
    }

    public static String getStringDate2(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
        String str = "";
        try {
            str = sdf.format(date);
        } catch (Exception e) {
            log.error("时间转换错误yyyyMMddHHmmss");
        }
        return str;
    }

    public static String getStringDate3(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        String str = "";
        try {
            str = sdf.format(date);
        } catch (Exception e) {
            log.error("时间转换错误yyyyMMdd");
        }
        return str;
    }

    /**
     * 日期增加分钟
     * @param date
     * @return
     */
    public static Date addMinute(Date date,int minutes){
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.MINUTE,minutes);
        return cal.getTime();
    }

    /**
     * 日期增加天
     * @param date
     * @return
     */
    public static Date addDay(Date date,int days){
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.DAY_OF_MONTH,days);
        return cal.getTime();
    }

    //获取某个日期的结束
    public static Date getDayEnd(Date d) {
        Calendar calendar = Calendar.getInstance();
        if (null != d)
            calendar.setTime(d);
        calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                calendar.getActualMaximum(Calendar.DAY_OF_MONTH), 23, 59);
        return calendar.getTime();
    }



    public static boolean compareNowDate(String time){
        try {
            Date date = new Date();
            SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
            String nowDate = format.format(date);
            String pass_date = time.substring(0,8);
            if(!pass_date.equals(nowDate)){
                return false;
            }else{
                return true;
            }
        }catch (Exception e){
            log.error("非当日时间判断异常："+time+",error:"+e);
            return false;
        }

    }


    /**
     *
     * @param time_start
     * @param time_end
     * @param pattern 时间格式 yyyyMMddHHmmss yyyy-MM-dd HH:mm:ss
     * @param unit 计算时间差值的单位 day hour minute
     * @return 返回值保留两位小时，小数保留规则：四舍五入
     */
    public static double timeInterval(String time_start, String time_end, String pattern, String unit) {
        LocalDateTime date_start = LocalDateTime.parse(time_start, DateTimeFormatter.ofPattern(pattern));
        LocalDateTime date_end = LocalDateTime.parse(time_end, DateTimeFormatter.ofPattern(pattern));
        Instant instant_start = date_start.toInstant(ZoneOffset.UTC);
        Instant instant_end = date_end.toInstant(ZoneOffset.UTC);
        long sencond_start = instant_start.getEpochSecond();
        long sencond_end = instant_end.getEpochSecond();
        double diff = getDateDiff(sencond_start, sencond_end, unit);
        return diff;
    }


    /**
     * @param start_Date
     * @param end_Date
     * @param unit day hour minute
     * @return
     */
    private static double getDateDiff(long start_Date, long end_Date, String unit) {
        double diff = 0;
        long diff_sencond = end_Date - start_Date;
        switch (unit) {
            case "day":
                diff = (double) (diff_sencond) / 60 / 60 / 24;
                break;
            case "hour":
                diff = (double) (diff_sencond) / 60 / 60;
                break;
            case "minute":
                diff = (double) (diff_sencond) / 60;
                break;
        }
        diff = new BigDecimal(diff).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
        return diff;
    }

}
