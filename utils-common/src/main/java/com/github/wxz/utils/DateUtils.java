package com.github.wxz.utils;

import org.apache.commons.lang3.StringUtils;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

/**
 * jdk8 线程安全操作时间类
 *
 * @author xianzhi.wang
 * @date 2017/12/22 -16:53
 */
public class DateUtils {
    /**
     * LocalDateTime to str
     *
     * @param datePattern
     * @param localDateTime
     * @return
     */
    public static String localDateTimeToString(String datePattern, LocalDateTime localDateTime) {
        if (StringUtils.isEmpty(datePattern)) {
            datePattern = "yyyy-MM-dd HH:mm:ss";
        }
        DateTimeFormatter df = DateTimeFormatter.ofPattern(datePattern);
        return df.format(localDateTime);
    }

    /**
     * LocalDateTime to str
     *
     * @param localDateTime
     * @return
     */
    public static String localDateTimeToString(LocalDateTime localDateTime) {
        return localDateTimeToString(null, localDateTime);
    }

    /**
     * str to LocalDateTime
     *
     * @param datePattern
     * @param localDateTimeStr
     * @return
     */
    public static LocalDateTime parseLocalDateTime(String datePattern, String localDateTimeStr) {
        if (StringUtils.isEmpty(datePattern)) {
            datePattern = "yyyy-MM-dd HH:mm:ss";
        }
        DateTimeFormatter df = DateTimeFormatter.ofPattern(datePattern);
        return LocalDateTime.parse(localDateTimeStr, df);
    }

    /**
     * str to LocalDateTime
     *
     * @param localDateTimeStr
     * @return
     */
    public static LocalDateTime parseLocalDateTime(String localDateTimeStr) {
        return parseLocalDateTime(null, localDateTimeStr);
    }

    /**
     * 毫秒转LocalDateTime
     *
     * @param time
     * @return
     */
    public static LocalDateTime parseLongToLocalDateTime(long time) {
        return LocalDateTime.ofInstant(
                Instant.ofEpochMilli(time), ZoneId.systemDefault());
    }
}
