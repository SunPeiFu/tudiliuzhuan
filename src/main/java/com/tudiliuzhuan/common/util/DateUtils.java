package com.tudiliuzhuan.common.util;


import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;

/**
 * 日期操作辅助类
 *
 * @author weizq
 * @date 2019-08-12
 */
@Slf4j
public final class DateUtils extends org.apache.commons.lang3.time.DateUtils {

    private DateUtils() {
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

    /**
     * 时间单位
     **/
    public interface DATEUNIT {
        String DAY = "day";
        String HOUR = "hour";
        String MINUTE = "minute";
        String SECOND = "second";
    }

    /**
     * 获取当前日期
     *
     * @param pattern 日期格式，默认 yyyy-MM-dd
     * @return String
     */
    public static String getNowDate(String pattern) {
        if (StringUtils.isEmpty(pattern)) {
            pattern = PATTERN.YYYY_MM_DD;
        }
        return format(new Date(), pattern);
    }

    /**
     * 获取当前日期
     *
     * @param pattern 日期格式，默认 yyyy-MM-dd
     * @return String
     */
    public static Date getDateNow(String pattern) {
        String nowDateStr = getNowDate(pattern);
        return stringToDate(nowDateStr, pattern);
    }

    /**
     * 获取当前timestamp
     *
     * @return String
     */
    public static String getNowTimestamp() {
        return getNowDate(PATTERN.YYYYMMDDHHMMSSSSS);
    }

    /**
     * 将java.util.Date对象转化为java.sql.Timestamp对象
     *
     * @param date Date 要转化的java.util.Date对象
     * @return Timestamp
     */
    public static Timestamp getTimestamp(Date date) {
        if (date == null) {
            date = new Date();
        }

        return new Timestamp(date.getTime());
    }

    /**
     * 将java.sql.Timestamp对象转化为java.util.Date对象
     *
     * @param timestamp 要转化的java.sql.Timestamp对象
     * @return Date
     */
    public static Date getDate(Timestamp timestamp) {
        return timestamp;
    }

    /**
     * date格式化日期
     *
     * @param date    Date
     * @param pattern String
     * @return Date
     */
    public static Date formatDate(Date date, String pattern) {
        if (date == null) {
            date = new Date();
        }
        if (StringUtils.isEmpty(pattern)) {
            pattern = PATTERN.YYYY_MM_DD;
        }
        try {
            return new SimpleDateFormat(pattern).parse(format(date, pattern));
        } catch (ParseException e) {
            return null;
        }
    }

    /**
     * 功能描述:把字符串 格式化为输出匹配的时间格式
     * @date 2020/8/6 6:09 下午
     * @author zengzhong
     * @param dateStr
     * @param pattern
     * @return java.lang.String
     */
    public static String formatStr(String dateStr, String pattern) {
        Date format = str2DateByStrLength(dateStr);
        if (format == null) {
            return "";
        }
        return getFormatDateTime(format, pattern);
    }

    /**
     * 根据字符串长度格式化日期
     *
     * @param dateStr 字符型日期
     * @return Date 日期
     */
    public static Date str2DateByStrLength(String dateStr) {
        Date date = null;
        if (StringUtils.isNotBlank(dateStr)) {
            dateStr = dateStr.trim();
            switch (dateStr.length()) {
                case 6:
                    date = str2Date(dateStr, PATTERN.HHMMSS);
                    break;
                case 8:
                    if (dateStr.contains(":")) {
                        date = str2Date(dateStr, PATTERN.HH_MM_SS);
                    } else {
                        date = str2Date(dateStr, PATTERN.YYYYMMDD);
                    }
                    break;
                case 9:
                    if (StringUtils.contains(dateStr, "月")) {
                        date = str2Date(dateStr, PATTERN.DD_MM_YY);
                    } else {
                        date = str2Date(dateStr, PATTERN.DD_MMM_YY);
                    }
                    break;
                case 10:
                    date = str2Date(dateStr, PATTERN.YYYY_MM_DD);
                    break;
                case 11:
                    date = str2Date(dateStr, PATTERN.YYYYMMDD2);
                    break;
                case 12:
                    date = str2Date(dateStr, PATTERN.YYYYMMDDHHMM);
                    break;
                case 14:
                    date = str2Date(dateStr, PATTERN.YYYYMMDDHHMMSS);
                    break;
                case 15:
                    date = str2Date(dateStr, PATTERN.MMDDYYYYHHMMAA);
                    break;
                case 17:
                    if (StringUtils.contains(dateStr, ":")) {
                        date = str2Date(dateStr, PATTERN.YYYYMMDD_HH_MM_SS_SSS);
                    } else {
                        date = str2Date(dateStr, PATTERN.YYYYMMDDHHMMSSSSS);
                    }
                    break;
                case 18:
                case 19:
                    // 修正 2019-05-20T11:28:46 转换
                    // 修正 2018/12/17 9:30:23
                    dateStr = dateStr.replace("/", "-");
                    //dateStr = StringUtils.replaceLetter(dateStr, " ");
                    date = str2Date(dateStr, PATTERN.YYYY_MM_DD_HH_MM_SS);
                    break;
                case 23:
                    dateStr = dateStr.replace("T", " ");
                    date = str2Date(dateStr, PATTERN.YYYY_MM_DD_HH_MM_SS_SSS);
                    break;
                default:
                    if (dateStr.indexOf("PM") > 0 || dateStr.indexOf("AM") > 0) {
                        date = str2Date(dateStr, PATTERN.MMDDYYYYHMA);
                    }
                    log.error(String.format("无法解析的日期类型:%s", dateStr));
                    break;
            }
        }
        return date;
    }

    /**
     * Timestamp日期格式化
     *
     * @param timestamp Timestamp
     * @param pattern   String
     * @return Timestamp
     */
    public static Timestamp formatTimestamp(Timestamp timestamp, String pattern) {
        if (timestamp == null) {
            return null;
        }
        if (StringUtils.isEmpty(pattern)) {
            pattern = PATTERN.YYYY_MM_DD;
        }
        Date date = formatDate(getDate(timestamp), pattern);
        return getTimestamp(date);
    }

    /**
     * 格式化日期
     *
     * @param date 日期对象
     * @return String
     */
    public static String format(Object date) {
        return format(date, PATTERN.YYYY_MM_DD);
    }

    /**
     * 格式化日期
     *
     * @param date    日期
     * @param pattern 格式
     * @return String
     */
    public static String format(Object date, String pattern) {
        if (date == null) {
            return null;
        }
        if (StringUtils.isEmpty(pattern)) {
            return format(date);
        }
        return new SimpleDateFormat(pattern).format(date);
    }

    /**
     * 字符串转换为日期
     *
     * @param dateStr 日期时间戳
     * @param pattern 格式
     * @return Date
     */
    public static Date stringToDate(String dateStr, String pattern) {
        if (StringUtils.isBlank(dateStr)) {
            return null;
        }
        try {
            if (StringUtils.isBlank(pattern)) {
                int len = dateStr.length();
                if (PATTERN.HHMMSS.length() == len) {
                    return new SimpleDateFormat(PATTERN.HHMMSS).parse(dateStr);
                } else if (PATTERN.YYYYMMDD.length() == len) {
                    return new SimpleDateFormat(PATTERN.YYYYMMDD).parse(dateStr);
                } else if (PATTERN.YYYY_MM_DD.length() == len) {
                    return new SimpleDateFormat(PATTERN.YYYY_MM_DD).parse(dateStr);
                } else if (PATTERN.DD_MM_YY.length() == len) {
                    return new SimpleDateFormat(PATTERN.DD_MM_YY).parse(dateStr);
                } else if (PATTERN.YYYYMMDDHHMMSS.length() == len) {
                    return new SimpleDateFormat(PATTERN.YYYYMMDDHHMMSS).parse(dateStr);
                } else if (PATTERN.YYYYMMDDHHMMSSSSS.length() == len) {
                    return new SimpleDateFormat(PATTERN.YYYYMMDDHHMMSSSSS).parse(dateStr);
                } else if (PATTERN.YYYY_MM_DD_HH_MM_SS.length() == len) {
                    return new SimpleDateFormat(PATTERN.YYYY_MM_DD_HH_MM_SS).parse(dateStr);
                } else if (PATTERN.YYYY_MM_DD_HH_MM_SS_SSS.length() == len) {
                    return new SimpleDateFormat(PATTERN.YYYY_MM_DD_HH_MM_SS_SSS).parse(dateStr);
                } else if (PATTERN.MMDDYYYYHHMMAA.length() == len) {
                    return new SimpleDateFormat(PATTERN.MMDDYYYYHHMMAA).parse(dateStr);
                }
                return null;
            }
            return new SimpleDateFormat(pattern).parse(dateStr);
        } catch (ParseException e) {
            return null;
        }
    }

    /**
     * 字符串转换为timestamp
     *
     * @param dateStr 日期时间戳
     * @param pattern 格式
     * @return Timestamp
     */
    public static Timestamp stringToTimestamp(String dateStr, String pattern) {
        Date date = stringToDate(dateStr, pattern);
        return getTimestamp(date);
    }

    /**
     * 日期格式化.
     *
     * @param date    当前日期
     * @param pattern 格式化字符串
     * @return 返回格式化日期
     */
    public static String getFormatDateTime(Date date, String pattern) {
        return format(date, pattern);
    }

    /**
     * 日期计算.
     *
     * @param date   日期
     * @param field  计算字段
     * @param amount 时间步长
     * @return Date
     */
    public static Date addDate(Date date, int field, int amount) {
        if (date == null) {
            return null;
        }
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(field, amount);
        return calendar.getTime();
    }

//	/**
//	 * 字符串转换为日期:不支持yyM[M]d[d]格式
//	 * 
//	 * @param date 日期
//	 * @return 日期对象
//	 */
//	public static Date stringToDate(String date) {
//		if (StringUtils.isEmpty(date)) {
//			return null;
//		}
//		String separator = String.valueOf(date.charAt(4));
//		String pattern = "yyyyMMdd";
//		if (!separator.matches("\\d*")) {
//			pattern = "yyyy" + separator + "MM" + separator + "dd";
//			if (date.length() < 10) {
//				pattern = "yyyy" + separator + "M" + separator + "d";
//			}
//		} else if (date.length() < 8) {
//			pattern = "yyyyMd";
//		}
//		pattern += " HH:mm:ss.SSS";
//		pattern = pattern.substring(0, Math.min(pattern.length(), date.length()));
//		try {
//			return new SimpleDateFormat(pattern).parse(date);
//		} catch (ParseException e) {
//			return null;
//		}
//	}

    /**
     * 功能：当前时间增加秒数。
     *
     * @param date    时间
     * @param seconds 正值时时间延后，负值时时间提前。
     * @return Date
     */
    public static Date getDateBeforeOrAfterSeconds(Date date, int seconds) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.SECOND, calendar.get(Calendar.SECOND) + seconds);
        return new Date(calendar.getTimeInMillis());
    }

    /**
     * 功能：当前时间增加分钟数。
     *
     * @param date    时间
     * @param minutes 正值时时间延后，负值时时间提前。
     * @return 计算后的时间
     */
    public static Date getDateBeforeOrAfterMinutes(Date date, int minutes) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.MINUTE, calendar.get(Calendar.MINUTE) + minutes);
        return new Date(calendar.getTimeInMillis());
    }

    /**
     * 功能：当前时间增加小时数。
     *
     * @param date  时间
     * @param hours 正值时时间延后，负值时时间提前。
     * @return 计算后的时间
     */
    public static Date getDateBeforeOrAfterHours(Date date, int hours) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.HOUR, calendar.get(Calendar.HOUR) + hours);
        return new Date(calendar.getTimeInMillis());
    }

    /**
     * 功能描述: 计算出date day天之前或之后的日期
     *
     * @param date
     * @param days
     * @param pattern
     * @return java.lang.String
     * @date 2019/10/28 12:58 下午
     * @author zengzhong
     */
    public static String getDateBeforeOrAfterDays(String date, int days, String pattern) {
        Date endDate = DateUtils.stringToDate(date, pattern);
        Date nextEndDate = DateUtils.getDateBeforeOrAfterDays(endDate, days);
        return DateUtils.getFormatDateTime(nextEndDate, pattern);

    }

    /**
     * 计算出date day天之前或之后的日期
     *
     * @param date {@link Date} 日期
     * @param days int 天数，正数为向后几天，负数为向前几天
     * @return 返回Date日期类型
     */
    public static Date getDateBeforeOrAfterDays(Date date, int days) {
        Calendar now = Calendar.getInstance();
        now.setTime(date);
        now.set(Calendar.DATE, now.get(Calendar.DATE) + days);
        return now.getTime();
    }

    private static List<Calendar> holidayList = new ArrayList<Calendar>(); // 节假日列表
    private static List<Calendar> workdayList = new ArrayList<Calendar>(); // 补班日列表

    /**
     * 计算相加day天，并且排除节假日和周末后的日期
     *
     * @param date 当前的日期
     * @param day  相加天数
     * @return return Date Calendar 返回类型 返回相加day天，并且排除节假日和周末后的日期
     */
    public static Date getDateAfterWorkDays(Date date, int day) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        try {
            for (int i = 0; i < day; i++) {
                calendar.add(Calendar.DAY_OF_MONTH, 1);
                if (checkHoliday(calendar)) {
                    i--;
                }
            }
            return calendar.getTime();
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return calendar.getTime();
    }

    /**
     * 验证日期是否是节假日
     *
     * @param calendar 传入需要验证的日期
     * @return boolean 返回类型 返回true是节假日，返回false不是节假日
     * @throws Exception 日期计算例外
     */
    public static boolean checkHoliday(Calendar calendar) throws Exception {
        // 判断日期是否是周六周日
        if (calendar.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY
                || calendar.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY) {
            // 判断日期是否是补班日
            for (Calendar ca : workdayList) {
                if (ca.get(Calendar.MONTH) == calendar.get(Calendar.MONTH)
                        && ca.get(Calendar.DAY_OF_MONTH) == calendar.get(Calendar.DAY_OF_MONTH)
                        && ca.get(Calendar.YEAR) == calendar.get(Calendar.YEAR)) {
                    return false;
                }
            }
            return true;
        }
        // 判断日期是否是节假日
        for (Calendar ca : holidayList) {
            if (ca.get(Calendar.MONTH) == calendar.get(Calendar.MONTH)
                    && ca.get(Calendar.DAY_OF_MONTH) == calendar.get(Calendar.DAY_OF_MONTH)
                    && ca.get(Calendar.YEAR) == calendar.get(Calendar.YEAR)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 计算出date monthes月之前或之后的日期
     *
     * @param date    日期
     * @param monthes 月数，正数为向后几天，负数为向前几天
     * @return Date
     */
    public static Date getDateBeforeOrAfterMonthes(Date date, int monthes) {
        Calendar now = Calendar.getInstance();
        now.setTime(date);
        now.set(Calendar.MONTH, now.get(Calendar.MONTH) + monthes);
        return now.getTime();
    }

    /**
     * 计算出date years年之前或之后的日期
     *
     * @param date  日期
     * @param years 年数，正数为向后几年，负数为向前几年
     * @return Date
     */
    public static Date getDateBeforeOrAfterYears(Date date, int years) {
        Calendar now = Calendar.getInstance();
        now.setTime(date);
        now.set(Calendar.YEAR, now.get(Calendar.YEAR) + years);
        return now.getTime();
    }

    /**
     * 取得季度月
     *
     * @param date Date 日期
     * @return Date 季度数
     */
    public static Date[] getSeasonDate(Date date) {
        Date[] seasonArray = new Date[3];

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);

        int season = getSeason(date);
        if (season == 1) {
            // 第一季度
            calendar.set(Calendar.MONTH, Calendar.JANUARY);
            seasonArray[0] = calendar.getTime();
            calendar.set(Calendar.MONTH, Calendar.FEBRUARY);
            seasonArray[1] = calendar.getTime();
            calendar.set(Calendar.MONTH, Calendar.MARCH);
            seasonArray[2] = calendar.getTime();
        } else if (season == 2) {
            // 第二季度
            calendar.set(Calendar.MONTH, Calendar.APRIL);
            seasonArray[0] = calendar.getTime();
            calendar.set(Calendar.MONTH, Calendar.MAY);
            seasonArray[1] = calendar.getTime();
            calendar.set(Calendar.MONTH, Calendar.JUNE);
            seasonArray[2] = calendar.getTime();
        } else if (season == 3) {
            // 第三季度
            calendar.set(Calendar.MONTH, Calendar.JULY);
            seasonArray[0] = calendar.getTime();
            calendar.set(Calendar.MONTH, Calendar.AUGUST);
            seasonArray[1] = calendar.getTime();
            calendar.set(Calendar.MONTH, Calendar.SEPTEMBER);
            seasonArray[2] = calendar.getTime();
        } else if (season == 4) {
            // 第四季度
            calendar.set(Calendar.MONTH, Calendar.OCTOBER);
            seasonArray[0] = calendar.getTime();
            calendar.set(Calendar.MONTH, Calendar.NOVEMBER);
            seasonArray[1] = calendar.getTime();
            calendar.set(Calendar.MONTH, Calendar.DECEMBER);
            seasonArray[2] = calendar.getTime();
        }
        return seasonArray;
    }

    /**
     * 1 第一季度 2 第二季度 3 第三季度 4 第四季度
     *
     * @param date 日期
     * @return int 季度数
     */
    public static int getSeason(Date date) {

        int season = 0;

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        int month = calendar.get(Calendar.MONTH);
        switch (month) {
            case Calendar.JANUARY:
            case Calendar.FEBRUARY:
            case Calendar.MARCH:
                season = 1;
                break;
            case Calendar.APRIL:
            case Calendar.MAY:
            case Calendar.JUNE:
                season = 2;
                break;
            case Calendar.JULY:
            case Calendar.AUGUST:
            case Calendar.SEPTEMBER:
                season = 3;
                break;
            case Calendar.OCTOBER:
            case Calendar.NOVEMBER:
            case Calendar.DECEMBER:
                season = 4;
                break;
            default:
                break;
        }
        return season;
    }

    /**
     * 检验是否是工作日
     *
     * @param dtDate 日期
     * @return boolean
     */
    public static boolean isWorkDate(Date dtDate) {
        long day = 0;
        Calendar clDate = Calendar.getInstance();
        clDate.setTime(dtDate);
        day = clDate.get(Calendar.DAY_OF_WEEK);
        if (day == Calendar.SATURDAY || day == Calendar.SUNDAY) {
            return false;
        } else {
            return true;
        }
    }

    /**
     * 判断是否是闰年
     *
     * @param year 待判断的年
     * @return boolean 返回结果
     */
    public static boolean isLeapYear(int year) {
        return ((year % 4 == 0 && year % 100 != 0) || year % 400 == 0);
    }

    /**
     * 判断是否是闰年
     *
     * @param date 待判断的日期
     * @return boolean 返回结果
     */
    public static boolean isLeapYear(Date date) {
        int year = getYear(date);
        return ((year % 4 == 0 && year % 100 != 0) || year % 400 == 0);
    }

    /**
     * 取得日期：年
     *
     * @param date 日期
     * @return int 当前年
     */
    public static int getYear(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar.get(Calendar.YEAR);
    }

    /**
     * 取得日期：年
     *
     * @param timestamp Timestamp
     * @return int
     */
    public static int getYear(Timestamp timestamp) {

        return getYear(getDate(timestamp));
    }

    /**
     * 取得日期：月
     *
     * @param date 日期
     * @return int 当前月份
     */
    public static int getMonth(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        int month = calendar.get(Calendar.MONTH);
        return month + 1;
    }

    /**
     * 取得日期：月
     *
     * @param timestamp Timestamp
     * @return int
     */
    public static int getMonth(Timestamp timestamp) {

        return getMonth(getDate(timestamp));
    }

    /**
     * 取得日期：天
     *
     * @param date 日期
     * @return int 天数
     */
    public static int getDay(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar.get(Calendar.DAY_OF_MONTH);
    }

    /**
     * 取得日期：天
     *
     * @param timestamp Timestamp
     * @return int
     */
    public static int getDay(Timestamp timestamp) {

        return getDay(getDate(timestamp));
    }

    /**
     * 获取日期：小时
     *
     * @param date 日期
     * @return int 小时数
     */
    public static int getHour(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar.get(Calendar.HOUR_OF_DAY);
    }

    /**
     * 获取日期：小时
     *
     * @param timestamp Timestamp
     * @return int
     */
    public static int getHour(Timestamp timestamp) {

        return getHour(getDate(timestamp));
    }

    /**
     * 获取日期：分钟
     *
     * @param date 日期
     * @return 分钟数
     */
    public static int getMinute(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar.get(Calendar.MINUTE);
    }

    /**
     * 获取日期：分钟
     *
     * @param timestamp
     * @return
     */
    public static int getMinute(Timestamp timestamp) {

        return getMinute(getDate(timestamp));
    }

    /**
     * 获取日期：秒
     *
     * @param date 日期
     * @return 秒数
     */
    public static int getSecond(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar.get(Calendar.SECOND);
    }

    /**
     * 获取当前前时间的时间戳
     *
     * @return 时间戳
     */
    public static int getCurTimestamp() {

        return (int) (System.currentTimeMillis() / 1000L);

    }

    /**
     * 时间戳转日期
     *
     * @param timestamp 带转换日期
     * @param format    日期格式
     * @return 转换后日期格式
     */

    public static String timeStamp2Date(long timestamp, String format) {
        DateFormat df = new SimpleDateFormat(format);
        return df.format(new Date(timestamp));
    }

    /**
     * 获取系统时间
     *
     * @return 系统时间
     */
    public static String getSystemTime() {

        return timeStamp2Date(getCurTimestamp(), PATTERN.YYYY_MM_DD_HH_MM_SS);
    }

    /**
     * 取得当天日期是周几
     *
     * @param date 当前日期
     * @return 周几
     */
    public static int getWeekDay(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        int weekOfYear = calendar.get(Calendar.DAY_OF_WEEK);
        return weekOfYear - 1;
    }

    public static int FIRST_DAY_OF_WEEK = Calendar.MONDAY; // 中国周一是一周的第一天

    /**
     * 根据日期取得对应周周一日期
     *
     * @param date 当前日期
     * @return 对应周周一日期
     */
    public static Date getMondayOfWeek(Date date) {
        Calendar monday = Calendar.getInstance();
        monday.setTime(date);
        monday.setFirstDayOfWeek(FIRST_DAY_OF_WEEK);
        monday.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
        return monday.getTime();
    }

    /**
     * 根据日期取得对应周周日日期
     *
     * @param date 当前日期
     * @return 对应周周日日期
     */
    public static Date getSundayOfWeek(Date date) {
        Calendar sunday = Calendar.getInstance();
        sunday.setTime(date);
        sunday.setFirstDayOfWeek(FIRST_DAY_OF_WEEK);
        sunday.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
        return sunday.getTime();
    }

    /**
     * 取得月第一天
     *
     * @param date 当前日期
     * @return 月第一天
     */
    public static Date getFirstDateOfMonth(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMinimum(Calendar.DAY_OF_MONTH));
        return calendar.getTime();
    }

    /**
     * 取得月最后一天
     *
     * @param date 当前日期
     * @return 最后一天号
     */
    public static Date getLastDateOfMonth(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
        return calendar.getTime();
    }

    /**
     * 取得季度第一天
     *
     * @param date 当前日期
     * @return 第一天号
     */
    public static Date getFirstDateOfSeason(Date date) {
        return getFirstDateOfMonth(getSeasonDate(date)[0]);
    }

    /**
     * 取得季度最后一天
     *
     * @param date 当前日期
     * @return 最后一天号
     */
    public static Date getLastDateOfSeason(Date date) {
        return getLastDateOfMonth(getSeasonDate(date)[2]);
    }


    public static Calendar calendar = null;

    /**
     * 取出日期的毫秒.
     *
     * @param date 传入日期
     * @return 毫秒
     */
    public static long getMillis(Date date) {
        calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar.getTimeInMillis();
    }

    /**
     * 得到两个时间之间的差距，根据type可返回分别以天，时，分为单位的整数
     *
     * @param d1   开始日
     * @param d2   结束日
     * @param type day\minute\hour\second
     * @return 差值
     */
    public static int getBetweenDateByType(Date d1, Date d2, String type) {
        long time1 = d1.getTime();
        long time2 = d2.getTime();
        long space = (time1 - time2) / 1000;
        int day = (int) space / (24 * 60 * 60);
        int hour = (int) (space - day * 24 * 60 * 60) / (60 * 60);
        int minute = (int) (space - day * 24 * 60 * 60 - hour * 60 * 60) / 60;
        int second = (int) (space - day * 24 * 60 * 60 - hour * 60 * 60);
        if (DATEUNIT.DAY.equals(type)) {
            return day;
        } else if (DATEUNIT.MINUTE.equals(type)) {
            return minute;
        } else if (DATEUNIT.HOUR.equals(type)) {
            return hour;
        } else if (DATEUNIT.SECOND.equals(type)) {
            return second;
        }
        return minute;
    }

    /**
     * Date计算：计算两个日期之间的天数
     *
     * @param beginDate 开始日
     * @param endDate   结束日
     * @return 如果beginDate 在 endDate之后返回负数 ，反之返回正数
     */
    public static int daysOfTwoDate(Date beginDate, Date endDate) {

        Calendar beginCalendar = Calendar.getInstance();
        Calendar endCalendar = Calendar.getInstance();

        beginCalendar.setTime(beginDate);
        endCalendar.setTime(endDate);

        return daysOfTwoDate(beginCalendar, endCalendar);
    }

    /**
     * Timestamp计算：计算两个日期之间的天数
     *
     * @param beginTimestamp
     * @param endTimestamp
     * @return
     */
    public static int daysOfTwoDate(Timestamp beginTimestamp, Timestamp endTimestamp) {

        return daysOfTwoDate(getDate(beginTimestamp), getDate(endTimestamp));
    }

    /**
     * 计算两个日期之间的天数
     *
     * @param d1 开始日
     * @param d2 结束日
     * @return 如果d1 在 d2 之后返回负数 ，反之返回正数
     */
    public static int daysOfTwoDate(Calendar d1, Calendar d2) {
        int days = 0;
        int years = d1.get(Calendar.YEAR) - d2.get(Calendar.YEAR);
        if (years == 0) {
            // 同一年中
            days = d2.get(Calendar.DAY_OF_YEAR) - d1.get(Calendar.DAY_OF_YEAR);
            return days;
        } else if (years > 0) {
            // 不同一年
            for (int i = 0; i < years; i++) {
                d2.add(Calendar.YEAR, 1);
                days += -d2.getActualMaximum(Calendar.DAY_OF_YEAR);
                if (d1.get(Calendar.YEAR) == d2.get(Calendar.YEAR)) {
                    days += d2.get(Calendar.DAY_OF_YEAR) - d1.get(Calendar.DAY_OF_YEAR);
                    return days;
                }
            }
        } else {
            for (int i = 0; i < -years; i++) {
                d1.add(Calendar.YEAR, 1);
                days += d1.getActualMaximum(Calendar.DAY_OF_YEAR);
                if (d1.get(Calendar.YEAR) == d2.get(Calendar.YEAR)) {
                    days += d2.get(Calendar.DAY_OF_YEAR) - d1.get(Calendar.DAY_OF_YEAR);
                    return days;
                }
            }
        }
        return days;
    }

    /**
     * 间隔天数.
     *
     * @param startDate 起始日期
     * @param endDate   截止日期
     * @return 间隔天数
     */
    public static Integer getDayBetween(Date startDate, Date endDate) {
        Calendar cal = Calendar.getInstance();

        cal.setTime(startDate);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        long startMil = cal.getTimeInMillis();

        cal.setTime(endDate);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);

        long space = cal.getTimeInMillis() - startMil;
        return (int) (space / (60 * 60 * 24 * 1000L));
    }

    public static List<Date> getRangeDates(Date startDate, Date endDate) {
        List<Date> dates = new ArrayList<>();
        if (startDate == null || endDate == null) {
            return dates;
        }
        int len = getDayBetween(startDate, endDate);
        for (int k = 0; k <= len; k++) {
            dates.add(addDate(startDate, Calendar.DATE, k));
        }
        return dates;
    }

    /**
     * 间隔月
     *
     * @param startDate 起始日期
     * @param endDate   截止日期
     * @return 间隔月数
     */
    public static Integer getMonthBetween(Date startDate, Date endDate) {
        if (startDate == null || endDate == null || !startDate.before(endDate)) {
            return null;
        }
        Calendar start = Calendar.getInstance();
        start.setTime(startDate);
        Calendar end = Calendar.getInstance();
        end.setTime(endDate);
        int year1 = start.get(Calendar.YEAR);
        int year2 = end.get(Calendar.YEAR);
        int month1 = start.get(Calendar.MONTH);
        int month2 = end.get(Calendar.MONTH);
        int spaceMonth = (year2 - year1) * 12;
        spaceMonth = spaceMonth + month2 - month1;
        return spaceMonth;
    }

    /**
     * 间隔月，多一天就多算一个月
     *
     * @param startDate 起始日期
     * @param endDate   截止日期
     * @return 间隔月数
     */
    public static Integer getMonthBetweenWithDay(Date startDate, Date endDate) {
        if (startDate == null || endDate == null || !startDate.before(endDate)) {
            return null;
        }
        Calendar start = Calendar.getInstance();
        start.setTime(startDate);
        Calendar end = Calendar.getInstance();
        end.setTime(endDate);
        int year1 = start.get(Calendar.YEAR);
        int year2 = end.get(Calendar.YEAR);
        int month1 = start.get(Calendar.MONTH);
        int month2 = end.get(Calendar.MONTH);
        int spaceMonth = (year2 - year1) * 12;
        spaceMonth = spaceMonth + month2 - month1;
        int day1 = start.get(Calendar.DAY_OF_MONTH);
        int day2 = end.get(Calendar.DAY_OF_MONTH);
        if (day1 <= day2) {
            spaceMonth++;
        }
        return spaceMonth;
    }

    /**
     * 判断一个字符串是否是日期格式.
     *
     * @param str 待验证日期
     * @return 是否为日期格式
     */
    public static boolean isValidDate(String str) {
        boolean convertSuccess = true;
        // 指定日期格式为四位年/两位月份/两位日期，注意yyyy/MM/dd区分大小写；
        SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd HH:mm");
        try {
            // 设置lenient为false.
            // 否则SimpleDateFormat会比较宽松地验证日期，比如2007/02/29会被接受，并转换成2007/03/01
            format.setLenient(false);
            format.parse(str);
        } catch (ParseException e) {
            // 如果throw java.text.ParseException或者NullPointerException，就说明格式不对
            convertSuccess = false;
        }
        return convertSuccess;
    }

    /**
     * 计算指定日期所在周的周一日期
     *
     * @param dateStr 日期时间，要求格式：yyyy-MM-dd
     * @return
     */
    public static Date getFirstDayOfWeek(String dateStr) {
        Calendar calendar = DateUtils.getDayOfWeek(dateStr);
        if (calendar == null) {
            return null;
        }
        SimpleDateFormat sdf = new SimpleDateFormat(PATTERN.YYYY_MM_DD);
        return stringToDate(sdf.format(calendar.getTime()), PATTERN.YYYY_MM_DD);
    }

    /**
     * 计算指定日期所在周的周日日期
     *
     * @param dateStr 日期时间，要求格式：yyyy-MM-dd
     * @return Date
     */
    public static Date getLastDayOfWeek(String dateStr) {

        Calendar calendar = DateUtils.getDayOfWeek(dateStr);
        if (calendar == null) {
            return null;
        }
        SimpleDateFormat sdf = new SimpleDateFormat(PATTERN.YYYY_MM_DD);
        calendar.add(Calendar.DATE, 6);
        return stringToDate(sdf.format(calendar.getTime()), PATTERN.YYYY_MM_DD);
    }

    /**
     * 功能描述：格式化日期
     *
     * @param dateStr String 字符型日期
     * @param format  String 格式
     * @return Date 日期
     */
    public static Date str2Date(String dateStr, String format) {
        Date date = null;
        if (StringUtils.isBlank(dateStr)) {
            return null;
        }

        try {
            DateFormat dateFormat = new SimpleDateFormat(format, Locale.US);
            date = dateFormat.parse(dateStr);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return date;
    }

    /**
     * 功能描述：格式化输出日期
     *
     * @param date   Date 日期
     * @param format String 格式
     * @return 返回字符型日期
     */
    public static String date2Str(Date date, String format) {
        String result = StringUtils.EMPTY;
        try {
            if (date != null) {
                DateFormat dateFormat = new SimpleDateFormat(format);
                result = dateFormat.format(date);
            }
        } catch (Exception e) {
            log.error(e.getMessage(),e);
        }
        return result;
    }

    /**
     * Date转LocalDate
     **/
    public static LocalDate date2LocalDate(Date date) {
        LocalDateTime localDateTime = dateToLocalDateTime(date);
        return localDateTime == null ? null : localDateTime.toLocalDate();
    }

    /**
     * LocalDate转Date
     **/
    public static Date localDate2Date(LocalDate localDate) {
        if (localDate == null) {
            return null;
        }
        return localDateTimeToDate(localDate.atStartOfDay());
    }


    /**
     * date转为localDateTime
     *
     * @author liuwenjie
     **/
    public static LocalDateTime dateToLocalDateTime(Date date) {
        if (date == null) {
            return null;
        }
        return date.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
    }

    /**
     * date转为localDateTime
     *
     * @author liuwenjie
     **/
    public static LocalDate dateToLocalDate(Date date) {
        if (date == null) {
            return null;
        }
        return date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
    }

    /**
     * localDateTime转为date
     *
     * @author liuwenjie
     **/
    public static Date localDateTimeToDate(LocalDateTime localDateTime) {
        if (localDateTime == null) {
            return null;
        }
        return Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());
    }

    /**
     * 获取每周第一天
     *
     * @param dateStr String
     * @return Calendar
     */
    private static Calendar getDayOfWeek(String dateStr) {
        if (StringUtils.isEmpty(dateStr)) {
            return null;
        }

        try {
            SimpleDateFormat sdf = new SimpleDateFormat(PATTERN.YYYY_MM_DD);
            Date date = sdf.parse(dateStr);

            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);

            int dayWeek = calendar.get(Calendar.DAY_OF_WEEK);
            if (1 == dayWeek) {
                calendar.add(Calendar.DAY_OF_MONTH, -1);
            }

            calendar.setFirstDayOfWeek(Calendar.MONDAY);

            int day = calendar.get(Calendar.DAY_OF_WEEK);
            calendar.add(Calendar.DATE, calendar.getFirstDayOfWeek() - day);
            return calendar;
        } catch (ParseException e) {
        }

        return null;
    }


    /**
     * 功能：如果date1 早于 date2 返回true，否则返回false
     *
     * @param date1
     * @param date2
     * @return 返回比较结果
     */
    public static boolean compareDate(Date date1, Date date2) {
        Date format1 = formatDate(date1, PATTERN.YYYYMMDD);
        Date format2 = formatDate(date2, PATTERN.YYYYMMDD);
        if (null != format1 && null != format2) {
            if (format1.before(format2)) {
                return true;
            }
        } else {
            return false;
        }

        return false;
    }

    /**
     * 判断当前时间是否在[startTime, endTime]区间，注意时间格式要一致
     *
     * @param nowTime   当前时间
     * @param startTime 开始时间
     * @param endTime   结束时间
     * @return
     */
    public static boolean isEffectiveDate(Date nowTime, Date startTime, Date endTime) {
        if (nowTime.getTime() == startTime.getTime()
                || nowTime.getTime() == endTime.getTime()) {
            return true;
        }

        Calendar date = Calendar.getInstance();
        date.setTime(nowTime);

        Calendar begin = Calendar.getInstance();
        begin.setTime(startTime);

        Calendar end = Calendar.getInstance();
        end.setTime(endTime);

        if (date.after(begin) && date.before(end)) {
            return true;
        } else {
            return false;
        }
    }
}
