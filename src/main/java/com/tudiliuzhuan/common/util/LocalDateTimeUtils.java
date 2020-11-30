package com.tudiliuzhuan.common.util;

import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

/**
 * @author liuwenjie
 * @since 2020/5/28
 */
@Slf4j
public class LocalDateTimeUtils {
    private LocalDateTimeUtils() {
    }

    /**
     * 日期格式
     **/
    public interface PATTERN {
        String HHMMSS = "HHmmss";
        String HH_MM_SS = "HH:mm:ss";
        String YYYYMM = "yyyyMM";
        String YYYYMMDD = "yyyyMMdd";
        String YYYYMMDD2 = "yyyy年MM月dd日";
        String YYYY_MM_DD = "yyyy-MM-dd";
        String DD_MM_YY = "dd-MM月-yy";
        String DD_MMM_YY = "dd-MMM-yy";
        String YYYYMMDDHHMM = "yyyyMMddHHmm";
        String YYYYMMDDHHMMSS = "yyyyMMddHHmmss";
        String YYYYMMDDHHMMSSSSS = "yyyyMMddHHmmssSSS";
        String YYYY_MM_DD_HH_MM_SS = "yyyy-MM-dd HH:mm:ss";
        String YYYY_MM_DD_HH_MM_SS_2 = "yyyy/MM/dd HH:mm:ss";
        String YYYY_MM_DD_HH_MM_SS_SSS = "yyyy-MM-dd HH:mm:ss.SSS";
        String YYYYMMDD_HH_MM_SS_SSS = "yyyyMMdd HH:mm:ss";
        // 12小时制，带AM或PM
        String MMDDYYYYHHMMAA = "MMddyyyyhh:mmaa";
        String MMDDYYYYHMA = "MMddyyyyhmma";
    }

    private static final DateTimeFormatter YYYY_MM_DD_HH_MM_SS = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    private static final DateTimeFormatter YYYYMMDDHHMMSS = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
    private static final DateTimeFormatter YYYY_MM_DD = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private static final DateTimeFormatter HH_MM_SS = DateTimeFormatter.ofPattern("HH:mm:ss");

    private static final Map<String, DateTimeFormatter> DATE_TIME_FORMATTER_MAP = new HashMap<>();

    static {
        DATE_TIME_FORMATTER_MAP.put(PATTERN.YYYY_MM_DD_HH_MM_SS, YYYY_MM_DD_HH_MM_SS);
        DATE_TIME_FORMATTER_MAP.put(PATTERN.YYYYMMDDHHMMSS, YYYYMMDDHHMMSS);
        DATE_TIME_FORMATTER_MAP.put(PATTERN.YYYY_MM_DD, YYYY_MM_DD);
        DATE_TIME_FORMATTER_MAP.put(PATTERN.HH_MM_SS, HH_MM_SS);
    }

    /**
     * 功能描述:转换为LocalDate
     *
     * @param dateStr
     * @param format
     * @return java.time.LocalDate
     * @date 2020/8/5 12:54 下午
     * @author zengzhong
     */
    public static LocalDate str2LocalDate(String dateStr, String format) {
        if (StringUtils.isEmpty(dateStr)) {
            return null;
        }
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern(format);
        return LocalDate.parse(dateStr, fmt);
    }

    public static LocalDate str2LocalDate(String dateStr) {
        LocalDateTime localDateTime = str2LocalDateTime(dateStr);
        if (localDateTime == null) {
            return null;
        }
        return localDateTime.toLocalDate();
    }

    public static String localDate2String(LocalDate localDate) {
        return YYYY_MM_DD.format(localDate);
    }

    public static String localDate2String(LocalDate localDate, String pattern) {
        if (localDate == null) {
            return "";
        }
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(pattern);
        return dateTimeFormatter.format(localDate);
    }

    /**
     * 功能描述:时间格式字符串 按照指定的格式输出
     *
     * @param dateStr
     * @param format
     * @return java.lang.String
     * @date 2020/8/6 10:53 上午
     * @author zengzhong
     */
    public static String str2LocalDateStr(String dateStr, String format) {
        LocalDate localDate = str2LocalDate(dateStr);
        return localDate2String(localDate, format);
    }

    /**
     * 功能描述:转换为LocalDateTime
     *
     * @param dateStr
     * @param format
     * @return java.time.LocalDateTime
     * @date 2020/8/5 12:55 下午
     * @author zengzhong
     */
    public static LocalDateTime str2LocalDateTime(String dateStr, String format) {
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern(format);
        return LocalDateTime.parse(dateStr, fmt);
    }

    /**
     * 功能描述:字符串转LocalDateTime 支持各种字符串格式
     *
     * @param dateStr
     * @return java.time.LocalDateTime
     * @date 2020/8/30 10:57 上午
     * @author zengzhong
     */
    public static LocalDateTime str2LocalDateTime(String dateStr) {
        LocalDateTime date = null;
        LocalDate localDate;
        if (StringUtils.isNotEmpty(dateStr)) {
            dateStr = dateStr.trim();
            switch (dateStr.length()) {
                case 6:
                    localDate = str2LocalDate(dateStr, DateUtils.PATTERN.HHMMSS);
                    date = LocalDateTime.of(localDate, LocalTime.of(0, 0, 0));
                    break;
                case 8:
                    if (dateStr.contains(":")) {
                        localDate = str2LocalDate(dateStr, DateUtils.PATTERN.HH_MM_SS);
                        date = LocalDateTime.of(localDate, LocalTime.of(0, 0, 0));
                    } else {
                        localDate = str2LocalDate(dateStr, DateUtils.PATTERN.YYYYMMDD);
                        date = LocalDateTime.of(localDate, LocalTime.of(0, 0, 0));
                    }
                    break;
                case 9:
                    if (dateStr.contains("月")) {
                        localDate = str2LocalDate(dateStr, DateUtils.PATTERN.DD_MM_YY);
                        date = LocalDateTime.of(localDate, LocalTime.of(0, 0, 0));
                    } else {
                        localDate = str2LocalDate(dateStr, DateUtils.PATTERN.DD_MMM_YY);
                        date = LocalDateTime.of(localDate, LocalTime.of(0, 0, 0));
                    }
                    break;
                case 10:
                    localDate = str2LocalDate(dateStr, DateUtils.PATTERN.YYYY_MM_DD);
                    date = LocalDateTime.of(localDate, LocalTime.of(0, 0, 0));
                    break;
                case 11:
                    localDate = str2LocalDate(dateStr, DateUtils.PATTERN.YYYYMMDD2);
                    date = LocalDateTime.of(localDate, LocalTime.of(0, 0, 0));
                    break;
                case 12:
                    date = str2LocalDateTime(dateStr, DateUtils.PATTERN.YYYYMMDDHHMM);
                    break;
                case 14:
                    date = str2LocalDateTime(dateStr, DateUtils.PATTERN.YYYYMMDDHHMMSS);
                    break;
                case 15:
                    date = str2LocalDateTime(dateStr, DateUtils.PATTERN.MMDDYYYYHHMMAA);
                    break;
                case 17:
                    if (dateStr.contains(":")) {
                        date = str2LocalDateTime(dateStr, DateUtils.PATTERN.YYYYMMDD_HH_MM_SS_SSS);
                    } else {
                        date = str2LocalDateTime(dateStr, DateUtils.PATTERN.YYYYMMDDHHMMSSSSS);
                    }
                    break;
                case 18:
                case 19:
                    // 修正 2019-05-20T11:28:46 转换
                    // 修正 2018/12/17 9:30:23
                    dateStr = dateStr.replace("/", "-");
                    //dateStr = StringUtils.replaceLetter(dateStr, " ");
                    date = str2LocalDateTime(dateStr, DateUtils.PATTERN.YYYY_MM_DD_HH_MM_SS);
                    break;
                case 23:
                    dateStr = dateStr.replace("T", " ");
                    date = str2LocalDateTime(dateStr, DateUtils.PATTERN.YYYY_MM_DD_HH_MM_SS_SSS);
                    break;
                default:
                    if (dateStr.indexOf("PM") > 0 || dateStr.indexOf("AM") > 0) {
                        date = str2LocalDateTime(dateStr, DateUtils.PATTERN.MMDDYYYYHMA);
                    }
                    log.error(String.format("无法解析的日期类型:%s", dateStr));
                    break;
            }
        }
        return date;
    }

    public static String localDateTime2String(LocalDateTime localDateTime) {
        return YYYY_MM_DD_HH_MM_SS.format(localDateTime);
    }

    /**
     * LocalDateTime转字符串
     *
     * @param localDateTime
     * @param format
     * @return
     */
    public static String localDateTime2String(LocalDateTime localDateTime, String format) {
        if (localDateTime == null || StringUtils.isEmpty(format)) {
            return null;
        }
        DateTimeFormatter formatter = DATE_TIME_FORMATTER_MAP.get(format);
        if (formatter == null) {
            formatter = DateTimeFormatter.ofPattern(format);
        }
        return formatter.format(localDateTime);
    }

    public static String formatNow() {
        return YYYY_MM_DD_HH_MM_SS.format(LocalDateTime.now());
    }

    public static String formatNow(String pattern) {
        return DateTimeFormatter.ofPattern(pattern).format(LocalDateTime.now());
    }

    public static String localTime2String(LocalTime localTime) {
        return HH_MM_SS.format(localTime);
    }


    public static LocalTime string2LocalTime(String localTimeStr) {
        return LocalTime.parse(localTimeStr, HH_MM_SS);
    }

    public static void main(String[] args) {

        LocalDate localDate = LocalDateTimeUtils.str2LocalDate("20171121");

        LocalDateTime now = LocalDateTime.now();
        String localDateTime2String = localDateTime2String(now);
        System.out.println(localDateTime2String);
        System.out.println(str2LocalDateTime(localDateTime2String));

        System.out.println("---------------------");
        String localDate2String = localDate2String(now.toLocalDate());
        System.out.println(localDate2String);
        System.out.println(str2LocalDate(localDate2String));

        System.out.println("---------------------");
        String localTime2String = localTime2String(now.toLocalTime());
        System.out.println(localTime2String);
        System.out.println(string2LocalTime(localTime2String));
    }
}
