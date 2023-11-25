package com.example.favoriteschoolmeal.global.common.util;

import com.example.favoriteschoolmeal.global.common.util.exception.DateTimeException;
import com.example.favoriteschoolmeal.global.common.util.exception.DateTimeExceptionType;
import java.time.LocalDateTime;
import java.time.Year;
import java.time.format.DateTimeFormatter;

/**
 * 날짜와 시간을 다루는 유틸리티 클래스입니다. 이 클래스는 특정 패턴에 따라 날짜와 시간을 포맷팅하고 파싱하는 메소드를 제공합니다.
 */
public class DateTimeUtil {

    private static final DateTimeFormatter SINGLE_FORMAT = DateTimeFormatter.ofPattern(
            "MM/dd HH:mm");
    private static final DateTimeFormatter RANGE_FORMAT = DateTimeFormatter.ofPattern(
            "MM/dd HH:mm ~ HH:mm");

    /**
     * LocalDateTime 객체를 문자열로 포맷팅합니다.
     *
     * @param dateTime 포맷팅할 LocalDateTime 객체.
     * @return "MM/dd HH:mm" 패턴의 날짜와 시간을 나타내는 문자열.
     */
    public static String formatSingleDateTime(LocalDateTime dateTime) {
        return dateTime.format(SINGLE_FORMAT);
    }

    /**
     * 두 LocalDateTime 객체의 범위를 하나의 문자열로 포맷팅합니다. 시작과 종료 날짜-시간이 범위 표현으로 결합됩니다.
     *
     * @param startDateTime 범위의 시작 LocalDateTime 객체.
     * @param endDateTime   범위의 종료 LocalDateTime 객체.
     * @return "MM/dd HH:mm ~ HH:mm" 패턴의 날짜와 시간 범위를 나타내는 문자열.
     */
    public static String formatDateTimeRange(LocalDateTime startDateTime,
            LocalDateTime endDateTime) {
        return startDateTime.format(SINGLE_FORMAT) + " ~ " + endDateTime.format(
                DateTimeFormatter.ofPattern("HH:mm"));
    }

    /**
     * 문자열을 LocalDateTime 객체로 파싱합니다. 문자열은 "MM/dd HH:mm" 형식이어야 합니다. 현재 연도가 가정됩니다.
     *
     * @param dateTime 날짜와 시간을 나타내는 문자열.
     * @return LocalDateTime 객체.
     * @throws DateTimeException 문자열을 파싱할 수 없는 경우 발생.
     */
    public static LocalDateTime parseSingleDateTime(String dateTime) {
        try {
            return LocalDateTime.parse(dateTime + " " + Year.now().getValue(),
                    DateTimeFormatter.ofPattern("MM/dd HH:mm yyyy"));
        } catch (Exception e) {
            throw new DateTimeException(DateTimeExceptionType.PARSING_ERROR);
        }
    }

    /**
     * 날짜와 시간 범위를 나타내는 문자열을 LocalDateTime 객체 배열로 파싱하고, 그 유효성을 검증합니다.
     *
     * @param dateTimeRange 날짜와 시간 범위를 나타내는 문자열.
     * @return 범위의 시작과 끝을 나타내는 LocalDateTime 객체 배열.
     * @throws DateTimeException 문자열 파싱 또는 범위 유효성 검증 중 오류 발생시.
     */
    public static LocalDateTime[] parseDateTimeRange(String dateTimeRange) {
        try {
            String[] parts = dateTimeRange.split(" ~ ");
            LocalDateTime start = LocalDateTime.parse(parts[0] + " " + Year.now().getValue(),
                    DateTimeFormatter.ofPattern("MM/dd HH:mm yyyy"));
            LocalDateTime end = LocalDateTime.parse(
                    parts[0].substring(0, 6) + parts[1] + " " + Year.now().getValue(),
                    DateTimeFormatter.ofPattern("MM/dd HH:mm yyyy"));

            validateDateTimeRange(start, end);

            return new LocalDateTime[]{start, end};
        } catch (Exception e) {
            throw new DateTimeException(DateTimeExceptionType.PARSING_ERROR);
        }
    }

    /**
     * 두 LocalDateTime 객체가 유효한 시간 범위인지 검증합니다. startDateTime은 endDateTime 이전이어야 하며, 둘 다 현재 시간보다 미래여야
     * 합니다.
     *
     * @param startDateTime 시작 시간
     * @param endDateTime   종료 시간
     * @throws DateTimeException 시작 시간 또는 종료 시간이 유효하지 않은 경우 발생
     */
    public static void validateDateTimeRange(LocalDateTime startDateTime,
            LocalDateTime endDateTime) {
        LocalDateTime now = LocalDateTime.now();
        if (startDateTime.isBefore(now) || endDateTime.isBefore(now)) {
            throw new DateTimeException(DateTimeExceptionType.INVALID_DATE_TIME_RANGE);
        }
        if (startDateTime.isAfter(endDateTime)) {
            throw new DateTimeException(DateTimeExceptionType.INVALID_DATE_TIME_RANGE);
        }
    }
}