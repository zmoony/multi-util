package com.example.text.java;

import org.testng.annotations.Test;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.temporal.TemporalField;

/**
 * DateTest
 *
 * @author yuez
 * @since 2023/4/19
 */
public class DateTest {

    @Test
    public void Instant(){
        ZonedDateTime zonedDateTime = Instant.now().atZone(ZoneId.systemDefault());
        System.out.println(zonedDateTime.getHour());

//        Instant.ofEpochMilli()
    }
}
