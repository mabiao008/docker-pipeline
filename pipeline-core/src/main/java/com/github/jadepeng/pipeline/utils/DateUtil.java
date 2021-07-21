package com.github.jadepeng.pipeline.utils;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author JunMeng
 * @date 2018年7月25日
 */
public class DateUtil {

    private static final Logger log = LoggerFactory.getLogger(DateUtil.class);

    private static final int BIT = 10;

    private static SimpleDateFormat datetimeFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private static SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
    private static SimpleDateFormat dateMinuteFormat = new SimpleDateFormat("yyyyMMddHHmm");

    private static SimpleDateFormat excelDateFormat = new SimpleDateFormat("MM/dd/yy");
    private static SimpleDateFormat excelDateTimeFormat = new SimpleDateFormat("MM/dd/yy HH:mm");

    /**
     * 上海时区
     */
    public final static ZoneOffset SHANGHAI = ZoneOffset.ofHours(8);

    /**
     * SimpleDateFormat 线程不安全
     *
     * @param timestamp
     * @param type
     * @return
     */
    public static synchronized String timestampToString(String timestamp, String type) {
        try {
            Date date = new Date(Long.parseLong(timestamp));
            if (type.equals("datetime")) {
                return datetimeFormat.format(date);
            } else if (type.equals("dateMinute")) {
                return dateMinuteFormat.format(date);
            }
            return dateFormat.format(date);
        } catch (Exception e) {
            log.error(e.getMessage(), e.getStackTrace());
        }
        return timestamp;
    }


    public static synchronized String timestampToDateString(String timestamp) {
        return timestampToString(timestamp, "date");
    }

    public static synchronized String timestampToDateTimeString(String timestamp) {
        return timestampToString(timestamp, "datetime");
    }

    public static synchronized String timestampToDateMinuteString(Long timestamp) {
        return timestampToString(String.valueOf(timestamp), "dateMinute");
    }

    /**
     * long -> String
     */
    public static String long2string(Long date) {
        if (null == date || 0 >= date) {
            return "";
        }
        DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        if (BIT == length(date)) {
            date = date * 1000;
        }
        return sdf.format(date);
    }

    /**
     * long -> String(Month)
     */
    public static String long2MonthString(Long date) {
        if (null == date || 0 >= date) {
            return "";
        }
        DateFormat sdf = new SimpleDateFormat("yyyy-MM");
        if (BIT == length(date)) {
            date = date * 1000;
        }
        return sdf.format(date);
    }

    /**
     * long -> String(Date)
     */
    public static String long2dateString(Long date) {
        if (null == date || 0 >= date) {
            return "";
        }
        DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        if (BIT == length(date)) {
            date = date * 1000;
        }
        return sdf.format(date);
    }

    public static String long2string2(Long date) {
        if (null == date || 0 >= date) {
            return "";
        }
        DateFormat sdf = new SimpleDateFormat("yyyyMMdd-HHmmss");
        if (BIT == length(date)) {
            date = date * 1000;
        }
        return sdf.format(date);
    }


    /**
     * String ->long 精确到秒级别
     */
    public static long string2secondsLong(String date) {
        if (StringUtils.isBlank(date)) {
            return 0;
        }
        date = date.replaceAll("/", "-");
        Timestamp timestamp = Timestamp.valueOf(date);
        return null == timestamp ? 0 : timestamp.getTime() / 1000;
    }

    /**
     * String ->long 精确到毫秒级别
     */
    public static long string2millsLong(String date) {
        if (StringUtils.isBlank(date)) {
            return 0;
        }
        date = date.replaceAll("/", "-");
        Timestamp timestamp = Timestamp.valueOf(date);
        return null == timestamp ? 0 : timestamp.getTime();
    }

    /**
     * 判断整型的日期位数
     */
    private static int length(long date) {
        int length = 0;
        while (date > 0) {
            length = length + 1;
            date = date / 10;
        }
        return length;
    }


    /**
     * double 转  Date 时间
     *
     * @param dVal
     */
    public static Date DoubleToDate(Double dVal) {
        Date oDate = new Date();
        @SuppressWarnings("deprecation")
        //系统时区偏移 1900/1/1 到 1970/1/1 的 25569 天
        long localOffset = oDate.getTimezoneOffset() * 60000;
        oDate.setTime((long) ((dVal - 25569) * 24 * 3600 * 1000 + localOffset));

        return oDate;
    }



    /**
     * double 转  DateTime 时间
     *
     * @param dVal
     */
    public static Date DoubleToDateTime(Double dVal) {
        Date oDate = new Date();
        @SuppressWarnings("deprecation")
        //系统时区偏移 1900/1/1 到 1970/1/1 的 25569 天
        long localOffset = oDate.getTimezoneOffset() * 60000;
        // +1误差处理
        oDate.setTime((long) ((dVal - 25569) * 24 * 3600 * 1000 + localOffset) + 1);

        return oDate;
    }


    public static Date getExcelDate(String obj) throws ParseException {
        return excelDateFormat.parse(obj);
    }


    public static Date getExcelDateTime(String obj) throws ParseException {
        return excelDateTimeFormat.parse(obj);
    }


    /**
     * 时间转double
     *
     * @param date
     * @return 返回值类似：43322.3770190278
     */
    public static double Date2Double(Date date) {
        @SuppressWarnings("deprecation")
        long localOffset = date.getTimezoneOffset() * 60000;
        double dd = (double) (date.getTime() - localOffset) / 24 / 3600 / 1000 + 25569.0000000;
        DecimalFormat df = new DecimalFormat("#.0000000000");//先默认保留10位小数

        return Double.valueOf(df.format(dd));
    }

    /**
     * 获取当前时间毫秒
     *
     * @return
     */
    public static long getCurrentTimeMillis() {
        return System.currentTimeMillis();
    }

    /**
     * 获取n天前的时间 秒数
     *
     * @return
     */
    public static long getBeforeNDaysSec(int n) {
        return LocalDateTime.of(LocalDate.now().minusDays(n - 1), LocalTime.of(0, 0, 0)).toEpochSecond(SHANGHAI);
    }

    /**
     * 获取n天前的时间
     *
     * @param n
     * @return
     */
    public static LocalDateTime getBeforeNDaysDateTime(int n) {
        return LocalDateTime.of(LocalDate.now().minusDays(n - 1), LocalTime.of(0, 0, 0));
    }

    /**
     * 获取n月前的时间 秒数
     *
     * @param n
     * @return
     */
    public static long getBeforeNMonthsSec(int n) {
        return LocalDateTime.of(LocalDate.now().minusMonths(n - 1).withDayOfMonth(1), LocalTime.of(0, 0, 0))
            .toEpochSecond(SHANGHAI);
    }

    /**
     * 获取n月前的时间
     *
     * @param n
     * @return
     */
    public static LocalDateTime getBeforeNMonthsDateTime(int n) {
        return LocalDateTime.of(LocalDate.now().minusMonths(n - 1).withDayOfMonth(1), LocalTime.of(0, 0, 0));
    }

    /**
     * 获取当天最后时间点 秒数
     *
     * @return
     */
    public static long currentDayLastSec() {
        return LocalDateTime.of(LocalDate.now(), LocalTime.of(23, 59, 59)).toEpochSecond(SHANGHAI);
    }

    /**
     * 获取当天最后时间点
     *
     * @return
     */
    public static LocalDateTime currentDayLastDateTime() {
        return LocalDateTime.of(LocalDate.now(), LocalTime.of(23, 59, 59));
    }

    /**
     * 获取当前时间前n天日期
     *
     * @return
     */
    public static List<String> getBeforeNDays(int n) {
        List<String> days = new ArrayList<String>(n);
        LocalDate start = LocalDate.now().minusDays(n - 1);
        for (int i = 0; i < n; i++) {
            days.add(start.toString());
            start = start.plusDays(1);
        }
        return days;
    }

    /**
     * 获取当前时间前N个月日期数组
     *
     * @return
     */
    public static List<String> getBeforeNMonths(int n) {
        List<String> months = new ArrayList<String>(n);
        LocalDate start = LocalDate.now().minusMonths(n - 1);
        for (int i = 0; i < n; i++) {
            months.add(start.getYear() + (start.getMonthValue() < 10 ? "-0" : "-") + start.getMonthValue());
            start = start.plusMonths(1);
        }
        return months;
    }

}
