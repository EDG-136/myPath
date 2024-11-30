package com.tecksupport.utils;

import org.junit.jupiter.api.Test;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoField;

public class TestArea {
    @Test
    public void testTimeParser() {
        String time = "10:30:00";
        LocalTime localTime = LocalTime.parse(time);
        System.out.println(localTime.get(ChronoField.MINUTE_OF_DAY));
    }
}
