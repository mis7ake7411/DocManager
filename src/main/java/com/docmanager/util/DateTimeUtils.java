package com.docmanager.util;

import com.docmanager.constants.DateTimeFormattersEnum;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAccessor;
import java.util.Date;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

@Slf4j
public class DateTimeUtils {
    public static final String DATE_TIME_WITH_DASH = DateTimeFormattersEnum.DATE_TIME_WITH_DASH.getPattern();
    public static final String DATE_WITH_DASH = DateTimeFormattersEnum.DATE_WITH_DASH.getPattern();
    public static final String TIME_WITH_COLON = DateTimeFormattersEnum.TIME_WITH_COLON.getPattern();
    /**
     * 取得當下時間
     *
     * @return 當下時間字串
     */
    public static String getNowTime() {
        LocalDateTime now = LocalDateTime.now();
        return localDateTimeToString(now, null);
    }

    /**
     * 取得當下時間
     *
     * @param format 時間格式
     * @return 當下時間字串
     */
    public static String getNowTime(String format) {
        LocalDateTime now = LocalDateTime.now();
        return localDateTimeToString(now, format);
    }

    /**
     * 根據指定的天數偏移，取得新的日期時間。
     * 正數為未來日期，負數為過去日期。
     *
     * @param dateTime 日期時間字串
     * @param n 天數偏移（正數或負數）
     * @param format 時間格式
     * @return 指定偏移後的日期時間字串
     */
    public static String getAdjustedDateTime(String dateTime, int n, String format) {
        TemporalAccessor temporal = stringToTemporal(dateTime, format);
        if (temporal == null) {
            return null;
        }
        if (temporal instanceof LocalDateTime) {
            LocalDateTime futureDateTime = ((LocalDateTime) temporal).plusDays(n);
            log.info("futureDateTime: {}", futureDateTime);
            return localDateTimeToString(futureDateTime, format);
        } else if (temporal instanceof LocalDate ) {
            LocalDate futureDate =((LocalDate) temporal).plusDays(n);
            log.info("futureDate: {}", futureDate);
            return localDateToString(futureDate, format);
        } else {
            return null;
        }
    }

    /**
     * 取得兩個日期時間之間的天數
     *
     * @param startDateTime 開始日期時間字串
     * @param endDateTime 結束日期時間字串
     * @param format 時間格式
     * @return 兩個日期時間之間的天數
     */
    public static long getDaysBetween(String startDateTime, String endDateTime, String format) {
        TemporalAccessor start = stringToTemporal(startDateTime, format);
        TemporalAccessor end = stringToTemporal(endDateTime, format);
        if (start == null || end == null) {
            return 0;
        }
        if (start instanceof LocalDateTime && end instanceof LocalDateTime) {
            return Duration.between((LocalDateTime) start, (LocalDateTime) end).toDays();
        } else if (start instanceof LocalDate && end instanceof LocalDate) {
            return ChronoUnit.DAYS.between((LocalDate) start, (LocalDate) end);
        } else {
            return 0;
        }
    }

    /**
     * 將 LocalDateTime 轉換為字串
     *
     * @param localDateTime LocalDateTime
     * @param format 時間格式
     * @return 時間字串
     */
    public static String localDateTimeToString(LocalDateTime localDateTime, String format) {
        format = format == null ? DATE_TIME_WITH_DASH : format;
        if(localDateTime == null || getValidFormat(format) == null) {
            return null;
        }
        return localDateTime.format(DateTimeFormattersEnum.getFormatterEnum(format)
                .getFormatter());
    }

    /**
     * 將 LocalDate 轉換為字串
     *
     * @param localDate LocalDate
     * @param format 時間格式
     * @return 時間字串
     */
    public static String localDateToString(LocalDate localDate, String format) {
        format = format == null ? DATE_WITH_DASH : format;
        if(localDate == null || getValidFormat(format) == null) {
            return null;
        }
        return localDate.format(DateTimeFormattersEnum.getFormatterEnum(format)
                .getFormatter());
    }

    /**
     * 將 LocalTime 轉換為字串
     *
     * @param localTime LocalTime
     * @param format 時間格式
     * @return 時間字串
     */
    public static String localTimeToString(LocalTime localTime, String format) {
        format = format == null ? TIME_WITH_COLON : format;
        if(localTime == null || getValidFormat(format) == null) {
            return null;
        }
        return localTime.format(DateTimeFormattersEnum.getFormatterEnum(format)
                .getFormatter());
    }

    /**
     * 將字串轉換為時間物件
     *
     * @param dateTimeStr 時間字串
     * @param format 時間格式
     * @return TemporalAccessor 物件 (LocalDateTime, LocalDate, LocalTime)
     * @throws DateTimeParseException 如果解析失敗
     */
    public static TemporalAccessor stringToTemporal(String dateTimeStr, String format) {
        if (dateTimeStr == null) {
            return null;
        }
        DateTimeFormatter formatter = (!isValidFormat(format))
                ? DateTimeFormattersEnum.getFormatterEnum(DATE_TIME_WITH_DASH).getFormatter()
                : getValidFormat(format);

        return parseTemporal(dateTimeStr, formatter);
    }

    /**
     * 將時間字串轉換為 LocalDateTime
     *
     * @param dateTimeStr 時間字串
     * @param format 時間格式
     * @return LocalDateTime 物件
     * @throws IllegalArgumentException 如果格式不包含時間元素
     */
    public static LocalDateTime stringToLocalDateTime(String dateTimeStr, String format) {
        TemporalAccessor temporal = stringToTemporal(dateTimeStr, format);
        if (temporal instanceof LocalDateTime) {
            return (LocalDateTime) temporal;
        }
        throw new IllegalArgumentException("無法將字串轉換為 LocalDateTime: " + dateTimeStr + "，格式不包含時間元素");
    }

    /**
     * 將字串轉換為 LocalDate
     *
     * @param dateStr 日期字串
     * @param format 日期格式
     * @return LocalDate 物件
     * @throws IllegalArgumentException 如果格式包含時間元素
     */
    public static LocalDate stringToLocalDate(String dateStr, String format) {
        TemporalAccessor temporal = stringToTemporal(dateStr, format);
        if (temporal instanceof LocalDate) {
            return (LocalDate) temporal;
        }
        throw new IllegalArgumentException("無法將字串轉換為 LocalDate: " + dateStr);
    }

    /**
     * 將 LocalDateTime 轉換為 Date
     *
     * @param localDateTime LocalDateTime 物件
     * @return Date 物件
     */
    public static Date localDateTimeToDate(LocalDateTime localDateTime) {
        if (localDateTime == null) {
            return null;
        }
        return Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());
    }

    /**
     * 將 Date 轉換為 LocalDateTime
     *
     * @param date Date 物件
     * @return LocalDateTime 物件
     */
    public static LocalDateTime dateToLocalDateTime(Date date) {
        if (date == null) {
            log.warn("Date is null");
            return null;
        }
        return LocalDateTime.ofInstant(date.toInstant(), ZoneId.systemDefault());
    }

    /**
     * 檢查指定的日期時間是否在範圍內
     *
     * @param dateTime       待檢查的日期時間
     * @param startDateTime  範圍的開始日期時間
     * @param endDateTime    範圍的結束日期時間
     * @param format         日期時間格式
     * @return true 如果在範圍內，false 如果不在範圍內或格式無效
     */
    public static boolean isWithinTimeRange(String dateTime, String startDateTime, String endDateTime, String format) {
        log.info("dateTime: {}, startDateTime: {}, endDateTime: {}, format: {}", dateTime, startDateTime, endDateTime, format);
        if (dateTime == null || startDateTime == null || endDateTime == null || format == null) {
            return false;
        }

        try {
            DateTimeFormatter dateTimeFormatter = getValidFormat(format);

            TemporalAccessor dateTimeObj = parseTemporal(dateTime, dateTimeFormatter);
            TemporalAccessor startDateTimeObj = parseTemporal(startDateTime, dateTimeFormatter);
            TemporalAccessor endDateTimeObj = parseTemporal(endDateTime, dateTimeFormatter);

            return compareTemporal(dateTimeObj, startDateTimeObj) >= 0 &&
                    compareTemporal(dateTimeObj, endDateTimeObj) <= 0;
        } catch (DateTimeParseException e) {
            return false;
        }
    }

    /**
     * 嘗試解析日期時間
     *
     * @param dateTime  日期時間字串
     * @param formatter 日期時間格式
     * @return TemporalAccessor，可能是 LocalDateTime、LocalDate 或 LocalTime
     */
    private static TemporalAccessor parseTemporal(String dateTime, DateTimeFormatter formatter) {
        try {
            return LocalDateTime.parse(dateTime, formatter);
        } catch (DateTimeParseException e) {
            try {
                return LocalDate.parse(dateTime, formatter);
            } catch (DateTimeParseException ex) {
                try {
                    return LocalTime.parse(dateTime, formatter);
                } catch (DateTimeParseException exc) {
                    if (log.isErrorEnabled()) {
                        log.error("無法解析日期時間 : {}", dateTime);
                    }
                    throw exc;
                }
            }
        }
    }

    /**
     * 比較兩個 TemporalAccessor
     *
     * @param startDate 第一個 TemporalAccessor
     * @param endDate 第二個 TemporalAccessor
     * @return 正數表示 startDate > endDate，負數表示 startDate < endDate，0 表示相等
     */
    private static int compareTemporal(TemporalAccessor startDate, TemporalAccessor endDate) {
        if (startDate instanceof LocalDateTime && endDate instanceof LocalDateTime) {
            return ((LocalDateTime) startDate).compareTo((LocalDateTime) endDate);
        } else if (startDate instanceof LocalDate && endDate instanceof LocalDate) {
            LocalDateTime startOfDay = ((LocalDate) startDate).atStartOfDay();
            LocalDateTime endOfDay = ((LocalDate) endDate).atStartOfDay();
            return startOfDay.compareTo(endOfDay);
        } else if (startDate instanceof LocalTime && endDate instanceof LocalTime) {
            return ((LocalTime) startDate).compareTo((LocalTime) endDate);
        } else {
            throw new IllegalArgumentException("Incompatible TemporalAccessor types for comparison");
        }
    }

    /**
     * 檢查格式是否有效
     *
     * @param format 日期時間格式
     * @return true 如果格式有效，false 如果無效
     */
    public static boolean isValidFormat(String format) {
        if(format == null) {
            return false;
        }
        return getValidFormat(format) != null;
    }

    /**
     * 取得有效的日期時間格式
     *
     * @param format 日期時間格式
     * @return DateTimeFormatter
     */
    public static DateTimeFormatter getValidFormat(String format) {
        try {
            return DateTimeFormattersEnum.getFormatterEnum(format).getFormatter();
        } catch (IllegalArgumentException e) {
            try {
                return DateTimeFormatter.ofPattern(format);
            } catch (IllegalArgumentException ex) {
                if (log.isErrorEnabled()) {
                    log.error("無效的時間格式 : {}", format);
                }
                return null;
            }
        }
    }

    /**
     * 動態解析日期時間格式
     *
     * @param dateTime 日期時間字串
     * @param formats  可接受的日期時間格式列表
     * @return TemporalAccessor，可能是 LocalDateTime、LocalDate 或 LocalTime
     * @throws DateTimeParseException 如果所有格式都無法解析
     */
    public static TemporalAccessor parseWithDynamicFormats(String dateTime, List<String> formats) {
        for (String format : formats) {
            try {
                DateTimeFormatter formatter = DateTimeFormattersEnum.getFormatterEnum(format).getFormatter();
                log.info("開始解析時間格式 : {}", format);
                return parseTemporal(dateTime, formatter);
            } catch (DateTimeParseException ignored) {
                // 如果解析失敗，繼續嘗試下一個格式
                log.warn("解析時間格式 : {}  失敗", format);
            }
        }
        throw new DateTimeParseException("找不到有效的時間格式 : " + dateTime, dateTime, 0);
    }

    /**
     * 將字串轉換為 LocalDateTime，並根據 isEndOfDay 參數決定是開始還是結束時間
     *
     * @param date 日期字串
     * @param isEndOfDay 是否為結束時間
     * @param format 時間格式
     * @return LocalDateTime 物件
     */
    public static LocalDateTime parseToLocalDateTime(String date, boolean isEndOfDay, String format) {
        if (StringUtils.isNotEmpty(date)) {
            LocalDate localDate = DateTimeUtils.stringToLocalDate(date, format);
            return isEndOfDay ? localDate.atTime(LocalTime.MAX) : localDate.atStartOfDay();
        }
        return null;
    }
}

