package com.xk.sb.common.utils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class DateTool {
    public final static String SHORTMON = "yyyyMM";
    public final static String SHORTIME = "yyyyMMdd";
    public final static String LONGTIME = "yyyy-MM-dd HH:mm:ss";
    private static SimpleDateFormat longFormat = new SimpleDateFormat(LONGTIME);
    private static SimpleDateFormat shortFormat = new SimpleDateFormat(SHORTIME);
    private static SimpleDateFormat shortMonFormat = new SimpleDateFormat(SHORTMON);

    /**
     * 返回当前日期
     *
     * @return yyyyMMdd
     */
    public static String getCurrDay() {
        Calendar calendar = Calendar.getInstance();
        return shortFormat.format(calendar.getTime());
    }

    /**
     * 返回当前月份呢
     *
     * @return yyyyMM
     */
    public static String getCurrMon() {
        Calendar calendar = Calendar.getInstance();
        return shortMonFormat.format(calendar.getTime());
    }

    /**
     * 返回当前时间
     *
     * @return yyyyMMddHHmmss
     */
    public static String getCurrTime() {
        Calendar calendar = Calendar.getInstance();
        return longFormat.format(calendar.getTime());
    }

    /**
     * @描述 计算time和当前时间相差几天
     * @作者 chrimer(林江毅)
     * @创建时间 2015年3月5日 上午10:44:24
     */
    public static Long countDay(String time) {
        String currentTime = getCurrDay();

        DateFormat df = new SimpleDateFormat(SHORTIME);
        Calendar c1 = Calendar.getInstance();
        Calendar c2 = Calendar.getInstance();
        try {
            c1.setTime(df.parse(time));
            c2.setTime(df.parse(currentTime));
        } catch (java.text.ParseException e) {
            System.err.println("格式不正确");
        }
        Long day = (c1.getTimeInMillis() - c2.getTimeInMillis()) / 1000 / 60 / 60 / 24;
        return day;
    }

    public static void main(String[] args) {
        System.out.println(countDay("20191110"));
    }


}
