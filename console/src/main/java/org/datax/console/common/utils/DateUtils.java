package org.datax.console.common.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 日期处理
 * @author ChengJie
 * @desciption
 * @date 2018/12/26 20:47
 */
public class DateUtils extends org.apache.commons.lang3.time.DateUtils {

    private final static Logger logger = LoggerFactory.getLogger(DateUtils.class);
    /**
     * 时间格式(yyyy-MM-dd)
     */
    public final static String DATE_PATTERN = "yyyy-MM-dd";
    /**
     * 时间格式(yyyy-MM-dd HH:mm:ss)
     */
    public final static String DATE_TIME_PATTERN = "yyyy-MM-dd HH:mm:ss";

    /**
     * 日期无分隔格式(yyyyMMdd)
     */
    public final static String DATE_NO_SEPARATION_PATTERN = "yyyyMMdd";

    /**
     * 时间无分隔格式(yyyyMMddHHmmss)
     */
    public final static String TIME_NO_SEPARATION_PATTERN = "yyyyMMddHHmmss";

    public static String format(Date date) {
        return format(date, DATE_PATTERN);
    }

    public static String format(Date date, String pattern) {
        if (date != null) {
            SimpleDateFormat df = new SimpleDateFormat(pattern);
            return df.format(date);
        }
        return null;
    }

    /**
     * 计算距离现在多久，非精确
     *
     * @param date
     * @return
     */
    public static String getTimeBefore(Date date) {
        Date now = new Date();
        long l = now.getTime() - date.getTime();
        long day = l / (24 * 60 * 60 * 1000);
        long hour = (l / (60 * 60 * 1000) - day * 24);
        long min = ((l / (60 * 1000)) - day * 24 * 60 - hour * 60);
        long s = (l / 1000 - day * 24 * 60 * 60 - hour * 60 * 60 - min * 60);
        String r = "";
        if (day > 0) {
            r += day + "天";
        } else if (hour > 0) {
            r += hour + "小时";
        } else if (min > 0) {
            r += min + "分";
        } else if (s > 0) {
            r += s + "秒";
        }
        r += "前";
        return r;
    }

    /**
     * 计算距离现在多久，精确
     *
     * @param date
     * @return
     */
    public static String getTimeBeforeAccurate(Date date) {
        Date now = new Date();
        long l = now.getTime() - date.getTime();
        long day = l / (24 * 60 * 60 * 1000);
        long hour = (l / (60 * 60 * 1000) - day * 24);
        long min = ((l / (60 * 1000)) - day * 24 * 60 - hour * 60);
        long s = (l / 1000 - day * 24 * 60 * 60 - hour * 60 * 60 - min * 60);
        String r = "";
        if (day > 0) {
            r += day + "天";
        }
        if (hour > 0) {
            r += hour + "小时";
        }
        if (min > 0) {
            r += min + "分";
        }
        if (s > 0) {
            r += s + "秒";
        }
        r += "前";
        return r;
    }

    /**
     * 解析时间
     * @param str
     * @return
     * @throws ParseException
     */
    public static Date parseDate(String str) throws ParseException {
        return parseDate(str, new String[]{DATE_PATTERN, DATE_TIME_PATTERN, DATE_NO_SEPARATION_PATTERN, TIME_NO_SEPARATION_PATTERN});
    }

    /**
     * 日期加天数
     * @param day 天数
     * @return
     */
    /*public static Date addDays(Date d,int day) {

        Calendar calendar = new GregorianCalendar();
        calendar.setTime(d);
        calendar.add(calendar.DATE, day);

        return calendar.getTime();
    }*/

    /** 
     * 获取指定时间之前day 天的时间数组
     * @param date
     * @param day 之前天数
     * @return 时间数组
     * @author: ColwnfishYang
     * @date: 2018/11/29 
     */ 
    public static Date[] getAgoDateByDay(Date date, int day) {
        Date[] weekDates = new Date[day];
        for (int i = 0; i < day; i++) {
            weekDates[i] = addDays(date, -i);
        }
        return weekDates;
    }

    /**
     *
     * 功能描述:
     * 将Date 转成LocalDate
     * @auther ColwnfishYang
     * created on 2019-05-07 10:56:35
     * @param date
     * @return LocalDate
     */
    public static LocalDate toLocalDate(Date date) {
        Instant instant = Optional.ofNullable(date).orElseThrow(IllegalArgumentException::new).toInstant();
        ZoneId zoneId = ZoneId.systemDefault();
        return LocalDateTime.ofInstant(instant, zoneId).toLocalDate();
    }

    /**
     *
     * 功能描述:
     * 将LocalDate 转成Date
     * @auther ColwnfishYang
     * created on 2019-05-07 10:56:35
     * @param localDate
     * @return Date
     */
    public static Date toDate(LocalDate localDate) {
        return Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
    }

    /**
     * 获取指定时间当月的所有日期
     * @param date
     * @return 当月的所有日期
     * @author: ColwnfishYang
     * @date: 2018/11/29
     */
    public static Date[] getAllDayOfMonthByDate(Date date) {
        // 将Date 转成LocalDate
        LocalDate localDate = toLocalDate(date);
        int lengthOfMonth = localDate.lengthOfMonth();
        Date[] monthDayDates = new Date[lengthOfMonth];
        int year = localDate.getYear();
        int month = localDate.getMonth().getValue();
        for (int i = 1; i <= lengthOfMonth; i++) {
            // 将LocalDate 转成Date
            monthDayDates[i - 1] =  toDate(LocalDate.of(year, month, i));
        }
        return monthDayDates;
    }

    /**
     * 获取间隔内的的所有日期
     * @param startDate 开始日期
     * @param endDate 结束日期
     * @param closedIntervalFlag 闭区间标识
     * @return 间隔内所有日期
     * @author: ColwnfishYang
     * @date: 2018/11/29
     */
    public static List<Date> getBetweenDate(Date startDate, Date endDate, boolean closedIntervalFlag) {
        List<Date> dateList = new LinkedList<Date>();
        if (closedIntervalFlag) {
            dateList.add(startDate);
        }
        Date date = addDays(startDate, 1);
        while (date.before(endDate)){
            dateList.add(date);
            date = addDays(date, 1);
        };
        if (closedIntervalFlag) {
            dateList.add(endDate);
        }
        return dateList;
    }

    /**
     * 补充时间段之间的日期
     * 时间格式：2019-01-01
     *
     * @param strStartDate
     * @param strEndDate
     *
     * @return 日期字符串集合 List<String>
     */
    public static List<String> supplementDate(String strStartDate, String strEndDate) throws ParseException {
        List<String> lstDate = new ArrayList<String>();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date startDate = sdf.parse(strStartDate + " 00:00:00");
        Date endDate = sdf.parse(strEndDate + " 23:59:59");

        lstDate.add(strStartDate);
        long afterDay = startDate.getTime() + 24 * 60 * 60 * 1000;
        while (afterDay < endDate.getTime()) {
            lstDate.add(new SimpleDateFormat("yyyy-MM-dd").format(new Date(afterDay)));
            afterDay += 24 * 60 * 60 * 1000;
        }
        return lstDate;
    }

    /**
     * 获取最近days天数的时间列表
     * @param days
     * @return
     */
    public static List<Date> getLastDays(int days){

        List<Date> dateList = new ArrayList<>();
        try{
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Date date = sdf.parse(sdf.format(new Date()));
            int count=0;
            while (days>count){
                dateList.add(date);
                date = addDays(date, -1);
                count++;
            }
        }catch (Exception e){}
        return dateList.stream().sorted().collect(Collectors.toList());
    }

    public static String getYestodayString(String format){
        Calendar calendar=Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.add(Calendar.DAY_OF_YEAR,-1);
        return format(calendar.getTime(),format);
    }

}
