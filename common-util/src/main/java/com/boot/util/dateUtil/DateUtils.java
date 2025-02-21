package com.boot.util.dateUtil;

import lombok.extern.log4j.Log4j2;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 * Created by zy on 2017/12/8.
 */
@Log4j2
public class DateUtils {
    public static final String START_TIME = "start_time";
    public static final String END_TIME = "end_time";

    public static String[] getRouting(Date begin, Date end) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        List<String> result = new ArrayList<>();
        Calendar tempStart = Calendar.getInstance();
        tempStart.setTime(begin);

        while (begin.getTime() <= end.getTime()) {
            String date = sdf.format(tempStart.getTime());
            result.add(date);
            Date parse = null;
            try {
                parse = sdf.parse(date);
                tempStart.setTime(parse);
                tempStart.add(Calendar.DAY_OF_YEAR, 1);
                begin = tempStart.getTime();
            } catch (ParseException e) {
                log.error("时间路由错误:{}", e.getMessage());
            }
        }
        log.info("路由:" + result);
        return result.toArray(new String[result.size()]);
    }
    public static String[] getRouting1(Date begin, Date end) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        List<String> result = new ArrayList<>();
        Calendar tempStart = Calendar.getInstance();
        tempStart.setTime(begin);

        while (begin.getTime() <= end.getTime()) {
            String date = sdf.format(tempStart.getTime());
            result.add(date);
            Date parse = null;
            try {
                parse = sdf.parse(date);
                tempStart.setTime(parse);
                tempStart.add(Calendar.DAY_OF_YEAR, 1);
                begin = tempStart.getTime();
            } catch (ParseException e) {
                log.error("时间路由错误:{}", e.getMessage());
            }
        }
        log.info("路由:" + result);
        return result.toArray(new String[result.size()]);
    }
    
    

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
        if (null != d) {
            calendar.setTime(d);
        }
        calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH), 0, 0, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTime();
    }

    // 获取某个日期的结束时间
    public static Date getDayEndTime(Date d) {
        Calendar calendar = Calendar.getInstance();
        if (null != d) {
            calendar.setTime(d);
        }
        calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH), 23, 59, 59);
        calendar.set(Calendar.MILLISECOND, 999);
        return calendar.getTime();
    }

    // 获取某个时间的前一小时
    public static Date getLastHour(Date d) {
        Calendar calendar = Calendar.getInstance();
        if (null != d) {
            calendar.setTime(d);
        }
        calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH), calendar.get(Calendar.HOUR_OF_DAY), 0, 0);
        calendar.set(Calendar.MILLISECOND, 999);
        return calendar.getTime();
    }

    // 获取某个时间的前几天
    public static Date getLastDayBeginTime(Date d, int count) {
        Calendar calendar = Calendar.getInstance();
        if (null != d) {
            calendar.setTime(d);
        }
        calendar.add(Calendar.DAY_OF_MONTH, count);
        return getDayStartTime(calendar.getTime());
    }

    // 获取某个时间的前几天
    public static Date getLastDayEndTime(Date d, int count) {
        Calendar calendar = Calendar.getInstance();
        if (null != d) {
            calendar.setTime(d);
        }
        calendar.add(Calendar.DAY_OF_YEAR, count);
        return getDayEndTime(calendar.getTime());
    }

    //获取今天的时间段
    public static Map<String, Date> getTodayPeriod() {
        Map<String, Date> result = new HashMap<>();
        final Date date = new Date();
        final Date dayStartTime = getDayStartTime(date);
        final Date dayEndTime = getDayEndTime(date);
        result.put(START_TIME, dayStartTime);
        result.put(END_TIME, dayEndTime);
        return result;
    }

    //获取昨天的时间段
    public static Map<String, Date> getLastDayPeriod() {
        Map<String, Date> result = new HashMap<>();
        final Date date = new Date();
        final Date dayStartTime = getLastDayBeginTime(date, -1);
        final Date dayEndTime = getLastDayEndTime(date, -1);
        result.put(START_TIME, dayStartTime);
        result.put(END_TIME, dayEndTime);
        return result;
    }

    //获取7天的时间段
    public static Map<String, Date> getLast7Period() {
        Map<String, Date> result = new HashMap<>();
        final Date date = new Date();
        final Date dayEndTime = getDayEndTime(date);
        final Date dayStartTime = getLastDayBeginTime(dayEndTime, -6);
        result.put(START_TIME, dayStartTime);
        result.put(END_TIME, dayEndTime);
        return result;
    }

    public static Map<String, Date> getDates(Date begin, Date end, String yyyyMMdd) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        SimpleDateFormat sdf1 = new SimpleDateFormat(yyyyMMdd);
        Map<String, Date> result = new HashMap<>();
        Calendar tempStart = Calendar.getInstance();
        tempStart.setTime(begin);

        while (begin.getTime() <= end.getTime()) {
            String date = sdf1.format(tempStart.getTime());
            String format = sdf.format(tempStart.getTime());
            result.put(date, tempStart.getTime());
            Date parse = null;
            try {
                parse = sdf.parse(format);
                tempStart.setTime(parse);
                tempStart.add(Calendar.DAY_OF_YEAR, 1);
                begin = tempStart.getTime();
            } catch (Exception e) {
                log.error("时间路由错误:{}", e.getMessage());
            }
        }
        log.info("路由:" + result);
        return result;
    }


    /**
     * 异常返回null  yyyyMMddHHmm
     *
     * @param stringdate
     * @return
     */
    public static Date getDateToMinute(String stringdate) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        Date date1 = null;
        try {
            date1 = sdf.parse(stringdate);
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            log.error("字符串时间格式错误，正确格式为yyyyMMddHHmm");
        }
        sdf = null;
        return date1;
    }
    public static Date getDateToSecond(String stringdate) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date1 = null;
        try {
            date1 = sdf.parse(stringdate);
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            log.error("字符串时间格式错误，正确格式为yyyy-MM-dd HH:mm:ss");
        }
        sdf = null;
        return date1;
    }

    /**
     * yyyy-MM-dd
     *
     * @param stringdate
     * @return
     */
    public static Date getDateToDay(String stringdate) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date date1 = null;
        try {
            date1 = sdf.parse(stringdate);
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            log.error("字符串时间格式错误，正确格式为yyyy-MM-dd");
        }
        return date1;
    }

    /**
     * yyyy-MM-dd
     *
     * @param stringdate
     * @return
     */
    public static Date getDateToMonth(String stringdate) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
        Date date1 = null;
        try {
            date1 = sdf.parse(stringdate);
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            log.error("字符串时间格式错误，正确格式为yyyy-MM");
        }
        return date1;
    }

    /**
     * yyyy-MM-dd
     *
     * @param stringdate
     * @return
     */
    public static Date getDateToHour(String stringdate) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH");
        Date date1 = null;
        try {
            date1 = sdf.parse(stringdate);
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            log.error("字符串时间格式错误，正确格式为yyyy-MM");
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

    /**
     * 日期增加分钟
     *
     * @param date
     * @return
     */
    public static Date addMinute(Date date, int minutes) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.MINUTE, minutes);
        return cal.getTime();
    }

    //获取某个日期的结束
    public static Date getDayEnd(Date d) {
        Calendar calendar = Calendar.getInstance();
        if (null != d) {
            calendar.setTime(d);
        }
        calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                calendar.getActualMaximum(Calendar.DAY_OF_MONTH), 23, 59);
        return calendar.getTime();
    }

    private final static DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    /**
     * 获取当月的开始时间，结束时间
     * @return
     */
    public static List<String> getCurrentMonth() {
        LocalDateTime now = LocalDateTime.now();
        String endTime = now.format(dateTimeFormatter);
        String startTime = now.withDayOfMonth(1).withHour(0).withMinute(0).withSecond(0).format(dateTimeFormatter);
        return Arrays.asList(startTime, endTime);
    }

    /**
     * 获取本年度
     * @return
     */
    public static List<String> getCurrentYear() {
        LocalDateTime now = LocalDateTime.now();
        String endTime = now.format(dateTimeFormatter);
        String startTime = now.withDayOfYear(1).withHour(0).withMinute(0).withSecond(0).format(dateTimeFormatter);
        return Arrays.asList(startTime, endTime);
    }

    /**
     * 获取最近30天
     * @return
     */
    public static List<String> getLastThirtyDays() {
        LocalDateTime now = LocalDateTime.now();
        String endTime = now.format(dateTimeFormatter);
        String startTime = now.minusDays(30).format(dateTimeFormatter);
        return Arrays.asList(startTime, endTime);
    }

    /**
     * 获取两个时间之间的月份
     * @param timeStart
     * @param timeEnd
     * @return
     */
    public static List<String> getMonth(String timeStart,String timeEnd){
        LocalDateTime start = LocalDateTime.parse(timeStart, dateTimeFormatter);
        LocalDateTime end = LocalDateTime.parse(timeEnd, dateTimeFormatter);
        List<String> list = new ArrayList<>();
        while (start.isBefore(end)) {
            list.add(String.format("%02d",start.getMonthValue()));
            start = start.plusMonths(1);
        }
        return list;
    }

    /**
     * 获取指定时间的上个月的范围
     * @param timeStart
     * @return
     */
    public static List<String> getLastMonth(String timeStart){
        LocalDateTime start = LocalDateTime.parse(timeStart, dateTimeFormatter);
        List<String> list = new ArrayList<>();
        LocalDateTime firstDayOfLastMonth = start.minusMonths(1).withDayOfMonth(1).withHour(0).withMinute(0).withSecond(0);
        LocalDateTime lastDayOfLastMonth = start.withDayOfMonth(1).withHour(0).withMinute(0).withSecond(0).minusDays(1);
        return Arrays.asList(firstDayOfLastMonth.format(dateTimeFormatter), lastDayOfLastMonth.format(dateTimeFormatter));
    }



}
