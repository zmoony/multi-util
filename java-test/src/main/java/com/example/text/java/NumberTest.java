package com.example.text.java;

import org.apache.commons.codec.binary.Base64;
import org.springframework.util.StopWatch;
import org.springframework.util.StringUtils;
import org.testng.annotations.Test;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.NumberFormat;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Numberes
 *
 * @author yuez
 * @since 2022/7/14
 */
public class NumberTest {

    @Test
    public void DoubleTest() {
        System.out.println(Double.valueOf(85)/100+"");
        System.out.println(Double.valueOf("NULL"));
    }

    @Test
    public void BigdecimalTest(){
        System.out.println("浮点类型的坑");
        BigDecimal bigDecimal = new BigDecimal(0.01);//会存在精度丢失的问题，因为传入之前，0.01已经是浮点数了，丢失了
        BigDecimal bigDecimal1 = BigDecimal.valueOf(0.01);//不会存在丢失问题，因为valueOf()首先会转成字符串，保持精度
        System.out.println(bigDecimal);//0.01000000000000000020816681711721685132943093776702880859375
        System.out.println(bigDecimal1);//0.01

        System.out.println("浮点精度的坑");
        BigDecimal a = new BigDecimal("0.01");//-----------使用较多
        BigDecimal b = new BigDecimal("0.010");
        System.out.println(a.equals(b));//false   严格的精度对比
        System.out.println(a.compareTo(b));//0 -1（小于），0（等于），1（大于）  直接比较两个值的大小--------------使用较多

        System.out.println("设置精度的坑");
        BigDecimal a1 = new BigDecimal("1.0");
        BigDecimal b1 = new BigDecimal("3.0");
        //如果结果是无限小数，那么回报一下的异常，需要设置精度(0.33333....)
        //a1.divide(b1);  error:java.lang.ArithmeticException: Non-terminating decimal expansion; no exact representable decimal result.
        System.out.println(a1.divide(b1, RoundingMode.UP));//0.4
        System.out.println(a1.divide(b1, RoundingMode.DOWN));//0.3
        System.out.println(a1.divide(b1, RoundingMode.CEILING));//0.4
        System.out.println(a1.divide(b1, RoundingMode.FLOOR));//0.3
        System.out.println(a1.divide(b1, RoundingMode.HALF_UP));//0.3--------------------四舍五入
        System.out.println(a1.divide(b1, RoundingMode.HALF_DOWN));//0.3
        System.out.println(a1.divide(b1, RoundingMode.HALF_EVEN));//0.3
//        System.out.println(a1.divide(b1, RoundingMode.UNNECESSARY));//java.lang.ArithmeticException: Rounding necessary

        System.out.println("三种字符串输出的坑");
        BigDecimal a3 = BigDecimal.valueOf(35634535255456719.22345634534124578902);
        System.out.println(a3.toString());//3.563453525545672E+16
        System.out.println(a3.toPlainString());//35634535255456720-----------这个使用较多
        System.out.println(a3.toEngineeringString());//35.63453525545672E+15

        System.out.println("结合NumberFormat");
        NumberFormat currency = NumberFormat.getCurrencyInstance(); //建立货币格式化引用
        NumberFormat percent = NumberFormat.getPercentInstance();  //建立百分比格式化引用
        percent.setMaximumFractionDigits(8);//百分比小数点后面3位
        BigDecimal loanAmount = new BigDecimal("15000.48"); //金额
        BigDecimal interestRate = new BigDecimal("0.008123434"); //利率
        BigDecimal interest = loanAmount.multiply(interestRate); //相乘

        System.out.println("金额:\t" + currency.format(loanAmount));
        System.out.println("利率:\t" + percent.format(interestRate));
        System.out.println("利息:\t" + currency.format(interest));




    }

    /**
     * 结果里要轮询多遍的话直接用for循环一遍解决，防止耗时
     */
    @Test
    public void StreamTest() {
        StopWatch stopWatch = new StopWatch();
        Map<String, Long> map2 = new HashMap<>();
        Map<String, Long> map3 = new HashMap<>();
        Map<String, Long> map = new HashMap<>();
        map.put("A", 5L);
        map.put("B", 1L);
        map.put("C", 2L);
        map.put("D", 3L);
        String plate_no = "C,D";
        stopWatch.start("stream...");
        List<String> plateList = Arrays.asList(plate_no.split(","));
       /* long sum = map.entrySet().stream().collect(Collectors.summarizingLong(Map.Entry::getValue)).getSum();
        map2.put("sum", sum);
        if (!StringUtils.isEmpty(plate_no)) {
            long filteredSum = map.entrySet().stream().filter(x -> plateList.contains(x.getKey())).collect(Collectors.summarizingLong(Map.Entry::getValue)).getSum();
            long otherSum = sum - filteredSum;
            map2.put("filteredSum", filteredSum);
            map2.put("otherSum", otherSum);
        }*/
        /*Map<Boolean, List<Map.Entry<String, Long>>> collect = map.entrySet().stream().collect(Collectors.groupingBy(m -> plateList.contains(m.getKey())));
        long filteredSum = collect.get(true).stream().mapToLong(Map.Entry::getValue).sum();
        long otherSum = collect.get(false).stream().mapToLong(Map.Entry::getValue).sum();*/

      /*  map2.put("sum", filteredSum+otherSum);
        map2.put("filteredSum", filteredSum);
        map2.put("otherSum", otherSum);*/

        stopWatch.stop();
        System.out.println(map2);

        stopWatch.start("for...");
        long sum2 = 0L;
        long filteredSum2 = 0L;
        long otherSum2 = 0L;
//        List<String> plateList = Arrays.asList(plate_no.split(","));
        for (Map.Entry<String, Long> entry : map.entrySet()) {
            if (plateList.contains(entry.getKey())) {
                filteredSum2+=entry.getValue();
            } else {
                otherSum2+=entry.getValue();
            }
            sum2+=entry.getValue();
        }
        map3.put("sum", sum2);
        map3.put("filteredSum", filteredSum2);
        map3.put("otherSum", otherSum2);
        stopWatch.stop();
        System.out.println(map3);
        System.out.println(stopWatch.prettyPrint());
        System.out.println("总耗时：" + stopWatch.getTotalTimeMillis());
        System.out.println("任务数：" + stopWatch.getTaskCount());
        System.out.println("所有任务详情：" + stopWatch.getTaskInfo().toString());
    }

    @Test
    public void LongTest(){
        String similarity = "76.97256";
        double v = Double.parseDouble(String.format("%.2f", Double.valueOf(similarity.replaceAll("%",""))));
        System.out.println(v);
    }

    @Test
    public void TimeNumberTest(){
        long second = Instant.now().toEpochMilli();
        System.out.println(System.currentTimeMillis());
        System.out.println(second);
    }

    @Test
    public void arrayTest(){
        List<? extends Number> numbers = Arrays.asList(-1.0f, 2, 4, -2.3);
        System.out.println(numbers.contains(-1.0f));
        System.out.println(numbers.contains(2));
    }

    @Test
    public void randDomTest(){
        System.out.println((int)(Math.random()*10));
        Random random = new Random();
        int i = random.nextInt(5)+5;
        System.out.println(i);
    }


}
