package com.example.text.java;

import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.StringUtils;
import org.testng.annotations.Test;

import java.sql.Timestamp;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * 时间测试类
 * 一天的秒数：86400
 * @author yuez
 * @since 2022/8/17
 */
@Log4j2
public class TimeTest {
    DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
    @Test
    public void timestempMinuteTest(){
        Long minute = 27678649L;
        Instant instant = Instant.ofEpochSecond(minute * 60);
        LocalDateTime localDateTime = LocalDateTime.ofInstant(instant, ZoneId.systemDefault());
        System.out.println(localDateTime);

        Instant now = Instant.now();
        System.out.println("基于分钟的时间戳："+now.getEpochSecond()/60);
        localDateTime = LocalDateTime.ofInstant(now, ZoneId.systemDefault());
        System.out.println(localDateTime);

        Timestamp timestamp = Timestamp.valueOf(localDateTime);
        System.out.println(timestamp.getTime()/1000/60);
    }

    @Test
    public void dayTimeTest(){
//        long minute=83999;//秒
        long minute=83940;//秒
        LocalTime localTime = LocalTime.ofSecondOfDay(minute);
        String format = localTime.format(DateTimeFormatter.ofPattern("HH:mm"));
        System.out.println(format);

        Long hHmmsecond = getHHmmsecond(format);
        System.out.println(hHmmsecond);
    }



    public static Long getHHmmsecond(String time){
        int zong=0;
        try {
            String[] my =time.split(":");
            int hour =Integer.parseInt(my[0]);
            int min =Integer.parseInt(my[1]);
            zong = hour*60*60+min*60;
        } catch (Exception e) {
            // TODO Auto-generated catch block
            log.error("字符串时间格式错误，正确格式为HH:mm");
        }
        return (long) zong;
    }

    @Test
    public void filterTest(){
        List<Map<String,Object>> list = new ArrayList<>();
        /*list.add(new HashMap<String,Object>(){{
            put("pass_time","20220922150840");
        }});
        list.add(new HashMap<String,Object>(){{
            put("pass_time","20220922150600");
        }});*/
        list.add(new HashMap<String,Object>(){{
            put("pass_time","20220922151500");
        }});
        List<Map<String, Object>> maps = filterList(list, "15");
        System.out.println(maps);
    }

    private List<Map<String,Object>> filterList(List<Map<String,Object>> list, String filterTime){
        if(StringUtils.isNotBlank(filterTime)){
            log.info("*********开启时间过滤，时间：{}秒***************",filterTime);
            List<Map<String, Object>> listFilter = list.stream().filter(map -> {
                String pass_time = map.get("pass_time") + "";
                LocalDateTime passTime = LocalDateTime.parse(pass_time, timeFormatter);
                long passMills = passTime.toEpochSecond(ZoneOffset.ofHours(8)) * 1000;
                if (System.currentTimeMillis() - passMills >= Integer.parseInt(filterTime) * 1000) {
                    log.warn("当前时间间隔超过指定时间，过车时间：{}，当前时间：{}", passTime, LocalDateTime.now());
                    return false;
                }
                return true;
            }).collect(Collectors.toList());
            log.info("*********开启时间过滤结束，源数据：{}，过滤后：{}***************",list.size(),listFilter.size());
            return listFilter;
        }
        return list;
    }

    @Test
    public void test(){
        LocalDateTime startdt = LocalDateTime.parse("2022-11-16 15:04:00", DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        ZoneOffset offset = ZoneOffset.of("+08:00");
        OffsetDateTime startdate = OffsetDateTime.of(startdt, offset);
        String start_time = startdate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSXXX"));
        System.out.println(start_time);
    }
    @Test
    public void test2(){
        String time="2022-11-16 15:04:00";
        LocalDateTime startdt = LocalDateTime.parse(time, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        LocalDateTime localDateTime = startdt.minusDays(180);
        System.out.println(localDateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));

        System.out.println(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss")));
    }

    /**
     * 共 193天；产假158天，哺乳假折算30天，法定节假日5天（端午1天，中秋节1天 ，国庆节3天）
     * 开始日期 2023-5-4  结束日期 2023-11-13
     */
    @Test
    public void test3(){
        int day = 188;//产假158天，哺乳假折算30天
        LocalDateTime startTime = LocalDateTime.of(2023, 5, 4, 0, 0);
        LocalDateTime endTime = startTime.plusDays(day);
        System.out.println(endTime);//2023-11-8
        //法定节假日 端午1天  中秋节1天   国庆节3天 共5天
        LocalDateTime lastTime = endTime.plusDays(5);
        System.out.println(lastTime);//2023/11/13

    }

    @Test
    public  void breakpointTestLog() {
        ThreadLocalRandom random = ThreadLocalRandom.current();
        int count = 0;
        for (int i = 0; i < 5; i++) {
            if (isInterested(random.nextInt(10))) {
                count++;
            }
        }
        System.out.printf("Found %d interested values%n", count);
    }

    private static boolean isInterested(int i) {
        return i % 2 == 0;
    }


    @Test
    public  void breakpointTestStream() {
        Object[] res = Stream.of(1,2,3,4,5,6,7,8).filter(i -> i%2 == 0).filter(i -> i>3).toArray();
        System.out.println(Arrays.toString(res));
    }


    @Test
    public  void breakpointTestExcetion() {
        try {
            for (int i = 0; i < 20; i++) {
                if (i == 10) {
                    throw new RuntimeException("test");
                }
            }
        } catch (RuntimeException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void zoneTimeTest(){
        LocalDateTime utcDateTime = LocalDateTime.of(2025, 02, 19, 05, 47, 28);
        ZonedDateTime utcZonedDateTime = utcDateTime.atZone(ZoneId.of("UTC"));
        ZonedDateTime localZonedDateTime = utcZonedDateTime.withZoneSameInstant(ZoneId.systemDefault());
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
        String pass_time = localZonedDateTime.format(formatter);
        System.out.println(pass_time);
    }

    @Test
    public void zoneTimeLocalTest(){
        LocalDateTime utcDateTime = LocalDateTime.of(2025, 02, 19, 05, 47, 28);
        ZonedDateTime utcZonedDateTime = utcDateTime.atZone(ZoneId.of("UTC"));
        ZonedDateTime localZonedDateTime = utcZonedDateTime.withZoneSameLocal(ZoneId.systemDefault());
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
        String pass_time = localZonedDateTime.format(formatter);
        System.out.println(pass_time);
    }

    @Test
    public void formatTest(){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd H:mm:ss");
        DateTimeFormatter formatterNormal = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime localDateTime = LocalDateTime.parse("2022-09-22 15:04:00", formatter);
        LocalDateTime localDateTimeNormal = LocalDateTime.parse("2022-09-22 15:04:00", formatterNormal);
        System.out.println(localDateTime);
        System.out.println(localDateTimeNormal);
        LocalDateTime now = LocalDateTime.now().plusHours(8);
        System.out.println(now.format(formatter));
        System.out.println(now.format(formatterNormal));
    }

}
