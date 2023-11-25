package com.example.favoriteschoolmeal.global.common.util;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class DateTimeUtilTest {

    @Test
    @DisplayName("LocalDateTime 객체를 'MM/dd HH:mm' 형식의 문자열로 포맷")
    void testFormatSingleDateTime() {
        LocalDateTime dateTime = LocalDateTime.of(2023, 11, 8, 15, 30);
        String formatted = DateTimeUtil.formatSingleDateTime(dateTime);
        assertEquals("11/08 15:30", formatted);
    }

    @Test
    @DisplayName("두 LocalDateTime 객체를 'MM/dd HH:mm ~ HH:mm' 형식의 문자열로 포맷")
    void testFormatDateTimeRange() {
        LocalDateTime start = LocalDateTime.of(2023, 11, 8, 15, 30);
        LocalDateTime end = LocalDateTime.of(2023, 11, 8, 17, 30);
        String formattedRange = DateTimeUtil.formatDateTimeRange(start, end);
        assertEquals("11/08 15:30 ~ 17:30", formattedRange);
    }

    @Test
    @DisplayName("'MM/dd HH:mm' 형식의 문자열을 LocalDateTime 객체로 파싱")
    void testParseSingleDateTime() {
        String dateTimeStr = "11/08 15:30";
        LocalDateTime dateTime = DateTimeUtil.parseSingleDateTime(dateTimeStr);
        LocalDateTime expected = LocalDateTime.now().withMonth(11).withDayOfMonth(8).withHour(15).withMinute(30).withSecond(0).withNano(0);
        assertEquals(expected, dateTime);
    }

    @Test
    @DisplayName("'MM/dd HH:mm ~ HH:mm' 형식의 문자열을 두 LocalDateTime 객체로 파싱")
    void testParseDateTimeRange() {
        String dateTimeRangeStr = "12/08 15:30 ~ 17:30";
        LocalDateTime[] dateTimes = DateTimeUtil.parseDateTimeRange(dateTimeRangeStr);
        LocalDateTime expectedStart = LocalDateTime.now().withMonth(12).withDayOfMonth(8).withHour(15).withMinute(30).withSecond(0).withNano(0);
        LocalDateTime expectedEnd = LocalDateTime.now().withMonth(12).withDayOfMonth(8).withHour(17).withMinute(30).withSecond(0).withNano(0);
        assertEquals(expectedStart, dateTimes[0]);
        assertEquals(expectedEnd, dateTimes[1]);
    }
}