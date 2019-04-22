package io.grant.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 时间 工具类
 */
public class DateUtils {
    public static final String FORMAT_6  = "yyyyMM";
    public static final String FORMAT_8  = "yyyyMMdd";
    public static final String FORMAT_10 = "yyyy-MM-dd";
    public static final String FORMAT_14 = "yyyyMMddHHmmss";
    public static final String FORMAT_17 = "yyyyMMddHHmmssSSS";
    public static final String FORMAT_19 = "yyyy-MM-dd HH:mm:ss";
    public static final String FORMAT_23 = "yyyy-MM-dd HH:mm:ss.SSS";

    /**
     * 日期转换
     * @param str
     * @return
     */
    public static String dateParse(String str, String p1, String p2) {
        SimpleDateFormat sdf = new SimpleDateFormat(p1);
        Date date = null;
        try {
            date = sdf.parse(str);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        sdf = new SimpleDateFormat(p2);
        str = sdf.format(date);
        return str;
    }

    /**
     * 日期转换
     * 20190101 转成 2019-01-01
     * @param str
     * @return
     */
    public static String dateParse(String str) {
        return dateParse(str, FORMAT_8, FORMAT_10);
    }

    /**
     * 日期转换
     * @param date
     * @param p1
     * @return
     */
    public static String dateParse(Date date, String p1) {
        SimpleDateFormat sdf = new SimpleDateFormat(p1);
        return sdf.format(date);
    }

    /***
     * 获取当前日期 	格式：201001
     * @return
     */
    public static String getMonth(){
        SimpleDateFormat sdf = new SimpleDateFormat(FORMAT_6);
        Date now = new Date();
        String nowString = sdf.format(now);
        return nowString;
    }

    /**
     * 获取当前时间
     * @param format
     * @return
     */
    public static String getDate(String format){
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        Date now = new Date();
        String nowString = sdf.format(now);
        return nowString;
    }

    /***
     * 获取当前日期 	格式：yyyyMMddHHmmss
     * @return
     */
    public static String getDate14(){
       return getDate(FORMAT_14);
    }

    public static String getDate17(){
        return getDate(FORMAT_17);
    }
}
