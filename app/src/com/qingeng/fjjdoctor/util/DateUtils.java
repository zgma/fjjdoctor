package com.qingeng.fjjdoctor.util;

import android.text.TextUtils;
import android.util.Log;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class DateUtils {
    public final static String YYYYMMDD = "yyyy-MM-dd";
    public final static String YYYYMM = "yyyy-MM";
    public final static String YYYYMMDDHHMM = "yyyy-MM-dd HH:mm";
    public final static String MMDDHHMM = "MM-dd HH:mm";
    public final static String HHMM = "HH:mm";
    public final static String YYYYMMDDHHMMSS2 = "yyyyMMddHHmmSS";
    public final static String YYYYMMDDHHMMSS = "yyyy-MM-dd HH:mm:ss";
    public final static String YYYYMM_CHINA = "yyyy年MM月";

    /***
     * 获得固定格式的format日期
     *
     * @param timeDate
     * @param format
     * @return
     */
    public static String getFormatByStringDate(String timeDate, String format) {
        String dateStr = "";
        if (TextUtils.isEmpty(timeDate)) {
            dateStr = "";
        }
        try {
            SimpleDateFormat simple = new SimpleDateFormat(format);
            Date old = parseToDate(timeDate);
            dateStr = simple.format(old);
        } catch (Exception e) {
            dateStr = timeDate;
        }
        return dateStr;
    }

    public static String getFormatByDate(String timeDate, String format) {
        String dateStr = "";
        Date date = null;
        if (TextUtils.isEmpty(timeDate)) {
            dateStr = "";
        }
        try {
            SimpleDateFormat simple = new SimpleDateFormat("MMM d, yyyy K:m:s a", Locale.ENGLISH);
            date = simple.parse(timeDate);
            SimpleDateFormat simple1 = new SimpleDateFormat(format);
            dateStr = simple1.format(date);
        } catch (Exception e) {
            dateStr = timeDate;
        }
        return dateStr;
    }

    /**
     * 字符串时间格式化成Date
     *
     * @param dateString
     * @return
     */
    public static Date parseToDate(String dateString) {
        if (TextUtils.isEmpty(dateString)) {
            dateString = getTimeString("yyyy-MM-dd");
        }
        String pattern;
        if (dateString.length() > 10) {
            pattern = "yyyy-MM-dd HH:mm:ss";
        } else {
            pattern = "yyyy-MM-dd";
        }
        SimpleDateFormat formatter = new SimpleDateFormat(pattern, Locale.getDefault());
        try {
            return formatter.parse(dateString);
        } catch (ParseException var4) {
            return new Date();
        }
    }

    /**
     * 字符串时间格式化成Date
     *
     * @param dateString
     * @return
     */
    public static Date parseToDate(String dateString, String format) {
        if (TextUtils.isEmpty(dateString)) {
            dateString = getTimeString(format);
        }
        SimpleDateFormat formatter = new SimpleDateFormat(format, Locale.getDefault());
        try {
            return formatter.parse(dateString);
        } catch (ParseException var4) {
            return new Date();
        }
    }

    /**
     * 获取当前时间
     *
     * @param format
     * @return
     */
    public static String getTimeString(String format) {
        SimpleDateFormat formatter = new SimpleDateFormat(format, Locale.CHINA);
        return formatter.format(new Date());
    }


    /**
     * 当前时间里提供时间的天数，如果有余数，天数+1
     *
     * @param date
     * @return
     */
    public static int getDays(String date) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            long time = sdf.parse(date).getTime();
            long nowTime = System.currentTimeMillis();
            long days = (nowTime - time) / (60 * 60 * 1000 * 24);
            if ((nowTime - time) % (60 * 60 * 1000 * 24) > 0 && days != 0) {
                days = days + 1;
            }
            return (int) days;
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    /**
     * 当前时间里提供时间的天数，如果有余数，天数+1
     *
     * @param date
     * @return
     */
    public static int getDayes(String date) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            long time = sdf.parse(date).getTime();
            long nowTime = System.currentTimeMillis();
            long days = (nowTime - time) / (60 * 60 * 1000 * 24);
            if ((nowTime - time) % (60 * 60 * 1000 * 24) > 0 && days != 0) {
                days = days + 1;
            }
            return (int) days;
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    /**
     * 两个时间的差，第二个参数大于第一个参数
     *
     * @param date1
     * @param date2
     * @return
     */
    public static int getDays(String date1, String date2) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            long time1 = sdf.parse(date1).getTime();
            long time2 = sdf.parse(date2).getTime();
            long days = 0;
            if (time1 > time2) {
                days = (time1 - time2) / (60 * 60 * 1000 * 24);
                if ((time1 - time2) % (60 * 60 * 1000 * 24) > 0) {
                    days = days + 1;
                }
            } else {
                days = (time2 - time1) / (60 * 60 * 1000 * 24);
                if ((time2 - time1) % (60 * 60 * 1000 * 24) > 0) {
                    days = days + 1;
                }
            }
            return (int) days;
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    /**
     * 两个时间的差
     *
     * @param date1
     * @param date2
     * @param format 格式
     * @return
     */
    public static int getDays(String date1, String date2, String format) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat(format);
            long time1 = sdf.parse(date1).getTime();
            long time2 = sdf.parse(date2).getTime();
            long days = (time2 - time1) / (60 * 60 * 1000 * 24);
//            if((time2 - time1)%(60*60*1000*24)>0){
//                days = days+1;
//            }else {
//                days = days-1;
//            }
            return (int) days;
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    /**
     * 两个时间的差
     *
     * @param date1
     * @param date2
     * @param format 格式
     * @return
     */
    public static int getHours(String date1, String date2, String format) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat(format);
            long time1 = sdf.parse(date1).getTime();
            long time2 = sdf.parse(date2).getTime();
            long days = (time2 - time1) / (60 * 60 * 1000);
//            if((time2 - time1)%(60*60*1000*24)>0){
//                days = days+1;
//            }else {
//                days = days-1;
//            }
            return (int) days;
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    /**
     * 两个时间相差距离多少天多少小时多少分多少秒
     *
     * @param str1 时间参数 1 格式：1990-01-01 12:00:00
     * @param str2 时间参数 2 格式：2009-01-01 12:00:00
     * @return String 返回值为：xx天xx小时xx分xx秒
     */
    public static String getDistanceTime(String str1, String str2) {
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date one;
        Date two;
        long day = 0;
        long hour = 0;
        long min = 0;
        long sec = 0;
        try {
            one = df.parse(str1);
            two = df.parse(str2);
            long time1 = one.getTime();
            long time2 = two.getTime();
            long diff;
            if (time1 < time2) {
                diff = time2 - time1;
            } else {
                diff = time1 - time2;
            }
            day = diff / (24 * 60 * 60 * 1000);
            hour = (diff / (60 * 60 * 1000) - day * 24);
            min = ((diff / (60 * 1000)) - day * 24 * 60 - hour * 60);
            sec = (diff / 1000 - day * 24 * 60 * 60 - hour * 60 * 60 - min * 60);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        if (day == 0 && hour != 0 && min != 0 && sec != 0) {
            return hour + "小时" + min + "分" + sec + "秒";
        }
        if (day == 0 && hour == 0 && min != 0 && sec != 0) {
            return min + "分" + sec + "秒";
        }
        if (day == 0 && hour == 0 && min == 0 && sec != 0) {
            return sec + "秒";
        }
        return day + "天" + hour + "小时" + min + "分" + sec + "秒";
    }

    //获取指定月份的第一天
    public static String getFirstDayOfMonth(String year, String month) {
        Calendar cal = Calendar.getInstance();
        //设置年份
        cal.set(Calendar.YEAR, Integer.parseInt(year));
        //设置月份
        cal.set(Calendar.MONTH, Integer.parseInt(month) - 1);
        //获取某月最小天数
        int firstDay = cal.getActualMinimum(Calendar.DAY_OF_MONTH);
        //设置日历中月份的最小天数
        cal.set(Calendar.DAY_OF_MONTH, firstDay);
        //格式化日期
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String firstDayOfMonth = sdf.format(cal.getTime());
        return firstDayOfMonth;
    }

    //获取指定月份的最后一天
    public static String getLastDayOfMonth(String year, String month) {
        Calendar cal = Calendar.getInstance();
        //设置年份
        cal.set(Calendar.YEAR, Integer.parseInt(year));
        //设置月份
        cal.set(Calendar.MONTH, Integer.parseInt(month) - 1);
        //获取某月最大天数
        int lastDay = cal.getActualMaximum(Calendar.DAY_OF_MONTH);
        //设置日历中月份的最大天数
        cal.set(Calendar.DAY_OF_MONTH, lastDay);
        //格式化日期
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String lastDayOfMonth = sdf.format(cal.getTime());
        return lastDayOfMonth;
    }

    public static int compareDate(String dateString_01, String dateString_02, String simpleDateFormatStr) {
        int datetime = 0;
        SimpleDateFormat sdf = new SimpleDateFormat(simpleDateFormatStr);
        try {
            Date date_01 = sdf.parse(dateString_01);
            Date date_02 = sdf.parse(dateString_02);
            Log.v("ssss", date_01.before(date_02) + ""); //true，当 date_01 小于 date_02 时，为 true，否则为 false
            Log.v("ssss", date_02.after(date_01) + ""); //true，当 date_02 大于 date_01 时，为 true，否则为 false
            Log.v("ssss", date_01.compareTo(date_02) + ""); //-1，当 date_01 小于 date_02 时，为 -1
            Log.v("ssss", date_02.compareTo(date_01) + ""); //1，当 date_02 大于 date_01 时，为 1
            Log.v("ssss", date_02.compareTo(date_02) + ""); //0，当两个日期相等时，为 0
            datetime = date_01.compareTo(date_02);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return datetime;
    }


    public static int compareDate(String dateString_01, String dateString_02) {
        int datetime = 0;
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
        try {
            Date date_01 = sdf.parse(dateString_01);
            Date date_02 = sdf.parse(dateString_02);
            Log.v("ssss", date_01.before(date_02) + ""); //true，当 date_01 小于 date_02 时，为 true，否则为 false
            Log.v("ssss", date_02.after(date_01) + ""); //true，当 date_02 大于 date_01 时，为 true，否则为 false
            Log.v("ssss", date_01.compareTo(date_02) + ""); //-1，当 date_01 小于 date_02 时，为 -1
            Log.v("ssss", date_02.compareTo(date_01) + ""); //1，当 date_02 大于 date_01 时，为 1
            Log.v("ssss", date_02.compareTo(date_02) + ""); //0，当两个日期相等时，为 0
            datetime = date_01.compareTo(date_02);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return datetime;
    }


    public static int getDatePoor(Date endDate, Date nowDate) {
        int hour = -1;
        try {
            long nd = 1000 * 24 * 60 * 60;
            long nh = 1000 * 60 * 60;
            long nm = 1000 * 60;
            // long ns = 1000;
            // 获得两个时间的毫秒时间差异
            long diff = endDate.getTime() - nowDate.getTime();
            // 计算差多少天
            //long day = diff / nd;
            // 计算差多少小时
            hour = (int) (diff / nh);
            // 计算差多少分钟
            //long min = diff % nd % nh / nm;
            // 计算差多少秒//输出结果
            // long sec = diff % nd % nh % nm / ns;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return hour;
    }


    public static int getDatePoorMinutes(Date endDate, Date nowDate) {
        int minutes = -1;
        try {
            long nd = 1000 * 24 * 60 * 60;
            long nh = 1000 * 60 * 60;
            long nm = 1000 * 60;
            // long ns = 1000;
            // 获得两个时间的毫秒时间差异
            long diff = endDate.getTime() - nowDate.getTime();
            // 计算差多少天
            //long day = diff / nd;
            // 计算差多少小时
            //hour = (int) (diff / nh);
            // 计算差多少分钟
            minutes = (int) (diff / nm);
            // 计算差多少秒//输出结果
            // long sec = diff % nd % nh % nm / ns;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return minutes;
    }

    public static int getMinutes(String s, String e) {
        SimpleDateFormat formatter = new SimpleDateFormat("HH:mm");
        Date sDate = formatter.parse(s, new ParsePosition(0));
        Date eDate = formatter.parse(e, new ParsePosition(0));
        return getDatePoorMinutes(sDate, eDate);
    }


    public static String getDateString(Date date) {
        SimpleDateFormat simple = new SimpleDateFormat(YYYYMMDD);
        return simple.format(date);
    }

    public static String getDateString(Date date, String format) {
        SimpleDateFormat simple = new SimpleDateFormat(format);
        return simple.format(date);
    }

    public static int pastMinutes(String dateStr) {
        SimpleDateFormat dfs = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        int mins;
        try {
            Date begin = dfs.parse(dateStr);
            Date end = new Date();
            long between = (end.getTime() - begin.getTime()) / 1000;//除以1000是为了转换成秒
            mins = (int) (between / 60);
        } catch (Exception e) {
            e.printStackTrace();
            mins = 0;
        }
        return mins;
    }

    public static int pastMinutes(String dateStr1, String dateStr2) {
        SimpleDateFormat dfs = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        int mins;
        try {
            Date begin = dfs.parse(dateStr2);
            Date end = dfs.parse(dateStr1);
            long between = (end.getTime() - begin.getTime()) / 1000;//除以1000是为了转换成秒
            mins = (int) (between / 60);
        } catch (Exception e) {
            e.printStackTrace();
            mins = 0;
        }
        return mins;
    }


    public static boolean onTime(String startTimeStr, String endTimeStr) {
        SimpleDateFormat df = new SimpleDateFormat(DateUtils.YYYYMMDDHHMMSS);
        Date now = null;
        Date beginTime = null;
        Date endTime = null;
        try {
            now = df.parse(df.format(new Date()));
            beginTime = df.parse(startTimeStr);
            endTime = df.parse(endTimeStr);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return belongCalendar(now, beginTime, endTime);
    }

    /**
     * 判断时间是否在时间段内
     *
     * @param nowTime
     * @param beginTime
     * @param endTime
     * @return
     */
    public static boolean belongCalendar(Date nowTime, Date beginTime, Date endTime) {
/*        try{
            Calendar date = Calendar.getInstance();
            date.setTime(nowTime);

            Calendar begin = Calendar.getInstance();
            begin.setTime(beginTime);

            Calendar end = Calendar.getInstance();
            end.setTime(endTime);

            if (date.after(begin) && date.before(end)) {
                return true;
            } else {
                return false;
            }
        }catch (Exception e){
            return false;
        }*/
        try {
            return nowTime.getTime() >= beginTime.getTime() && nowTime.getTime() <= endTime.getTime();
        } catch (Exception e) {
            return false;
        }
    }

    private static final long ONE_MINUTE = 60000L;
    private static final long ONE_HOUR = 3600000L;
    private static final long ONE_DAY = 86400000L;
    private static final long ONE_WEEK = 604800000L;

    private static final String ONE_SECOND_AGO = "秒前";
    private static final String ONE_MINUTE_AGO = "分钟前";
    private static final String ONE_HOUR_AGO = "小时前";
    private static final String ONE_DAY_AGO = "天前";
    private static final String ONE_MONTH_AGO = "月前";
    private static final String ONE_YEAR_AGO = "年前";


    public static String format(String dateStr, String format) throws ParseException {
        SimpleDateFormat dateFormat = new SimpleDateFormat(format);
        Date date = dateFormat.parse(dateStr);

        long delta = new Date().getTime() - date.getTime();
        if (delta < 1L * ONE_MINUTE) {
            long seconds = toSeconds(delta);
            return (seconds <= 0 ? 1 : seconds) + ONE_SECOND_AGO;
        }
        if (delta < 45L * ONE_MINUTE) {
            long minutes = toMinutes(delta);
            return (minutes <= 0 ? 1 : minutes) + ONE_MINUTE_AGO;
        }
        if (delta < 24L * ONE_HOUR) {
            long hours = toHours(delta);
            return (hours <= 0 ? 1 : hours) + ONE_HOUR_AGO;
        }
        if (delta < 48L * ONE_HOUR) {
            return "昨天";
        }
        if (delta < 30L * ONE_DAY) {
            long days = toDays(delta);
            return (days <= 0 ? 1 : days) + ONE_DAY_AGO;
        }
        if (delta < 12L * 4L * ONE_WEEK) {
            long months = toMonths(delta);
            return (months <= 0 ? 1 : months) + ONE_MONTH_AGO;
        } else {
            long years = toYears(delta);
            return (years <= 0 ? 1 : years) + ONE_YEAR_AGO;
        }
    }

    private static long toSeconds(long date) {
        return date / 1000L;
    }

    private static long toMinutes(long date) {
        return toSeconds(date) / 60L;
    }

    private static long toHours(long date) {
        return toMinutes(date) / 60L;
    }

    private static long toDays(long date) {
        return toHours(date) / 24L;
    }

    private static long toMonths(long date) {
        return toDays(date) / 30L;
    }

    private static long toYears(long date) {
        return toMonths(date) / 365L;
    }


}
