package com.docmanager.constants;

import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import lombok.Getter;

@Getter
public enum DateTimeFormattersEnum {
    DATE_TIME_WITH_MILLIS_SLASH("yyyy/MM/dd HH:mm:ss.SSS"),
    DATE_TIME_WITH_MILLIS_DASH("yyyy-MM-dd HH:mm:ss.SSS"),
    DATE_TIME_WITH_SLASH("yyyy/MM/dd HH:mm:ss"),
    DATE_TIME_WITH_DASH("yyyy-MM-dd HH:mm:ss"),
    DATE_WITH_SLASH("yyyy/MM/dd"),
    DATE_WITH_DASH("yyyy-MM-dd"),
    DATE_YYYYMMDD("yyyyMMdd"),
    TIME_WITH_MILLIS("HH:mm:ss.SSS"),
    TIME_WITH_COLON("HH:mm:ss");

    private final DateTimeFormatter formatter;
    private final String pattern;

    private static final Map<String, DateTimeFormattersEnum> FORMAT_MAP = new HashMap<>();

    static {
        for (DateTimeFormattersEnum formatEnum : values()) {
            FORMAT_MAP.put(formatEnum.pattern, formatEnum);
        }
    }

    DateTimeFormattersEnum(String pattern) {
        this.pattern = pattern;
        this.formatter = DateTimeFormatter.ofPattern(pattern);
    }

    public static DateTimeFormattersEnum getFormatterEnum(String format) {
        DateTimeFormattersEnum formatEnum = FORMAT_MAP.get(format);
        if (formatEnum == null) {
            throw new IllegalArgumentException("無效的時間格式 : " + format);
        }
        return formatEnum;
    }
}

