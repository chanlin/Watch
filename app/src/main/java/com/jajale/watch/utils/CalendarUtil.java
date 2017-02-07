package com.jajale.watch.utils;

/**
 * Created by lilonghui on 2016/1/19.
 * Email:lilonghui@bjjajale.com
 */

import java.text.DateFormat;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;


public class CalendarUtil {

    private int weeks = 0;// 用来全局控制 上一周，本周，下一周的周数变化
    private int MaxDate; // 一月最大天数
    private int MaxYear; // 一年最大天数


    public static void showView() {
        CalendarUtil tt = new CalendarUtil();
//        L.e("获取当天日期:" + tt.getNowTime("yyyy-MM-dd"));
//        L.e("获取本周一日期:" + tt.getMondayOFWeek());
//        L.e("获取本周日的日期~:" + tt.getCurrentWeekday());
//        L.e("获取上周一日期:" + tt.getPreviousWeekday());
//        L.e("获取上周日日期:" + tt.getPreviousWeekSunday());
//        L.e("获取下周一日期:" + tt.getNextMonday());
//        L.e("获取下周日日期:" + tt.getNextSunday());
//        L.e("获得相应周的周六的日期:" + tt.getNowTime("yyyy-MM-dd"));
        L.e("获取上sss个月的天数:" + tt.getPreviousLastDay());
        L.e("获取上个月的天数:" + tt.getPreviousDay());
        L.e("获取本月第一天日期:" + tt.getFirstDayOfMonth());
        L.e("获取本月最后一天日期:" + tt.getDefaultDay());
        L.e("获取上月第一天日期:" + tt.getPreviousMonthFirst());
        L.e("获取上月最后一天的日期:" + tt.getPreviousMonthEnd());
        L.e("获取下月第一天日期:" + tt.getNextMonthFirst());
        L.e("获取下月最后一天日期:" + tt.getNextMonthEnd());
        L.e("获取本年的第一天日期:" + tt.getCurrentYearFirst());
        L.e("获取本年最后一天日期:" + tt.getCurrentYearEnd());
        L.e("获取去年的第一天日期:" + tt.getPreviousYearFirst());
        L.e("获取去年的最后一天日期:" + tt.getPreviousYearEnd());
        L.e("获取明年第一天日期:" + tt.getNextYearFirst());
        L.e("获取明年最后一天日期:" + tt.getNextYearEnd());
        L.e("获取本季度第一天:" + tt.getThisSeasonFirstTime(11));
        L.e("获取本季度最后一天:" + tt.getThisSeasonFinallyTime(11));
        L.e("获取两个日期之间间隔天数2008-12-1~2008-9.29:"
                + CalendarUtil.getTwoDay("2008-12-1", "2008-9-29"));
        L.e("获取当前月的第几周：" + tt.getWeekOfMonth());
        L.e("获取当前年份：" + tt.getYear());
        L.e("获取当前月份：" + tt.getMonth());
        L.e("获取今天在本年的第几天：" + tt.getDayOfYear());
        L.e("获得今天在本月的第几天(获得当前日)：" + tt.getDayOfMonth());
        L.e("获得今天在本周的第几天：" + tt.getDayOfWeek());
        L.e("获得半年后的日期："
                + tt.convertDateToString(tt.getTimeYearNext()));
    }

    public static class CalendarData {
        String calendar_hint;
        String day;
        boolean canSelect;
        String month;
        boolean isToday;
        boolean isSelect;
        String  title;
        String dateTime;

        public String getDateTime() {
            return dateTime;
        }

        public void setDateTime(String dateTime) {
            this.dateTime = dateTime;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public boolean isSelect() {
            return isSelect;
        }

        public void setIsSelect(boolean isSelect) {
            this.isSelect = isSelect;
        }

        public boolean isToday() {
            return isToday;
        }

        public void setIsToday(boolean isToday) {
            this.isToday = isToday;
        }

        public String getMonth() {
            return month;
        }

        public void setMonth(String month) {
            this.month = month;
        }

        public boolean isCanSelect() {
            return canSelect;
        }

        public void setCanSelect(boolean canSelect) {
            this.canSelect = canSelect;
        }

        public String getDay() {
            return day;
        }

        public void setDay(String day) {
            this.day = day;
        }

        public String getCalendar_hint() {
            return calendar_hint;
        }

        public void setCalendar_hint(String calendar_hint) {
            this.calendar_hint = calendar_hint;
        }
    }

    public static List<CalendarData> getCalendarList() {
        List<CalendarData> list = new ArrayList<CalendarData>();

        int dayOfWeek = getDayOfWeek();
        int today = getDayOfMonth();
        int month = getMonth();
        int previousDay = getPreviousDay();
        int defaultDay = getDefaultDay();
        int previousLastDay = getPreviousLastDay();

        int previousMonth = getPreviousMonth();
        int previousLastMonth = getPreviousLastMonth();
        int nextMonth = getNextMonth();



        int length0 = dayOfWeek + 28 - today - previousDay;
        int length1 = (dayOfWeek + 28 - today) > previousDay ? previousDay : (dayOfWeek + 28 - today);
        int length2 = today;
        int length3 = 7 - dayOfWeek;

        for (int i = 0; i < length0; i++) {
            CalendarData data = new CalendarData();
            data.setDay("" + (previousLastDay - length0 + i + 1));
            data.setCanSelect(true);
            if (data.getDay().equals("1")){
                data.setMonth(previousLastMonth+"月");
            }else{
                data.setMonth("");
            }

            data.setTitle(previousLastMonth + "-" + (previousLastDay - length0 + i + 1));
            data.setIsToday(false);
            data.setIsSelect(false);
            if (previousLastMonth<getMonth()){
                data.setDateTime(getYear() + "-" + previousLastMonth + "-" + data.getDay());
            }else{
                data.setDateTime(getYear()-1 + "-" + previousLastMonth + "-" + data.getDay());
            }
            list.add(data);
        }

        for (int i = 0; i < length1; i++) {
            CalendarData data = new CalendarData();
            data.setDay("" + (previousDay - length1 + i + 1));
            data.setCanSelect(true);
            if (data.getDay().equals("1")){
                data.setMonth(previousMonth+"月");
            }else{
                data.setMonth("");
            }
            data.setTitle(previousMonth + "-" + (previousDay - length1 + i + 1));
            data.setIsToday(false);
            data.setIsSelect(false);
            if (previousMonth<getMonth()){
                data.setDateTime(getYear() + "-" + previousMonth + "-" + data.getDay());
            }else{
                data.setDateTime(getYear()-1 + "-" + previousMonth + "-" + data.getDay());
            }

            list.add(data);
        }

        for (int i = 0; i < length2; i++) {
            CalendarData data = new CalendarData();
            data.setDay("" + (i + 1));
            data.setCanSelect(true);
            if (i == 0) {
                data.setMonth(month + "月");
            } else {
                data.setMonth("");
            }

            if (i == length2 - 1) {
                data.setIsToday(true);
                data.setIsSelect(true);
            } else {
                data.setIsToday(false);
                data.setIsSelect(false);
            }
            data.setTitle(month + "-" + (i + 1));
            data.setDateTime(getYear() + "-" + getMonth() + "-" + data.getDay());
            list.add(data);
        }
        for (int i = 0; i < length3; i++) {
            CalendarData data = new CalendarData();
            int day = today + i + 1;
            if (day > defaultDay) {
                data.setDay("" + (day - defaultDay ));
                if (day - defaultDay  == 1) {
                    data.setMonth(nextMonth + "月");
                } else {
                    data.setMonth("");
                }
            } else {
                data.setDay("" + (today + i + 1));
                data.setMonth("");
            }
            data.setCanSelect(false);
            data.setTitle(nextMonth + "-" + (today + i + 1));
            data.setIsToday(false);
            data.setIsSelect(false);

            if (nextMonth>getMonth())
            {
                data.setDateTime(getYear() + "-"+ nextMonth + "-"+ data.getDay());
            }else{
                data.setDateTime((getYear()+1) + "-"+ nextMonth+ "-" + data.getDay());
            }
            list.add(data);
        }


        return list;
    }


    public static int getYear() {
        return Calendar.getInstance().get(Calendar.YEAR);
    }


    public static int getMonth() {
        return Calendar.getInstance().get(Calendar.MONTH) + 1;
    }


    public static int getDayOfYear() {
        return Calendar.getInstance().get(Calendar.DAY_OF_YEAR);
    }


    public static int getDayOfMonth() {
        return Calendar.getInstance().get(Calendar.DAY_OF_MONTH);
    }


    public static int getDayOfWeek() {
        return Calendar.getInstance().get(Calendar.DAY_OF_WEEK);
    }


    public static int getWeekOfMonth() {
        return Calendar.getInstance().get(Calendar.DAY_OF_WEEK_IN_MONTH);
    }


    public static Date getTimeYearNext() {
        Calendar.getInstance().add(Calendar.DAY_OF_YEAR, 183);
        return Calendar.getInstance().getTime();
    }


    public static String convertDateToString(Date dateTime) {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        return df.format(dateTime);
    }


    public static String getTwoDay(String sj1, String sj2) {
        SimpleDateFormat myFormatter = new SimpleDateFormat("yyyy-MM-dd");
        long day = 0;
        try {
            java.util.Date date = myFormatter.parse(sj1);
            java.util.Date mydate = myFormatter.parse(sj2);
            day = (date.getTime() - mydate.getTime()) / (24 * 60 * 60 * 1000);
        } catch (Exception e) {
            return "";
        }
        return day + "";
    }


    public static String getWeek(String sdate) {
        // 再转换为时间
        Date date = CalendarUtil.strToDate(sdate);
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        // int hour=c.get(Calendar.DAY_OF_WEEK);
        // hour中存的就是星期几了，其范围 1~7
        // 1=星期日 7=星期六，其他类推
        return new SimpleDateFormat("EEEE").format(c.getTime());
    }


    public static Date strToDate(String strDate) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        ParsePosition pos = new ParsePosition(0);
        Date strtodate = formatter.parse(strDate, pos);
        return strtodate;
    }


    public static long getDays(String date1, String date2) {
        if (date1 == null || date1.equals(""))
            return 0;
        if (date2 == null || date2.equals(""))
            return 0;
        // 转换为标准时间
        SimpleDateFormat myFormatter = new SimpleDateFormat("yyyy-MM-dd");
        java.util.Date date = null;
        java.util.Date mydate = null;
        try {
            date = myFormatter.parse(date1);
            mydate = myFormatter.parse(date2);
        } catch (Exception e) {
        }
        long day = (date.getTime() - mydate.getTime()) / (24 * 60 * 60 * 1000);
        return day;
    }


    public static int getDefaultDay() {
        String str = "";
        SimpleDateFormat sdf = new SimpleDateFormat("dd");

        Calendar lastDate = Calendar.getInstance();
        lastDate.set(Calendar.DATE, 1);// 设为当前月的1号
        lastDate.add(Calendar.MONTH, 1);// 加一个月，变为下月的1号
        lastDate.add(Calendar.DATE, -1);// 减去一天，变为当月最后一天

        str = sdf.format(lastDate.getTime());
        return Integer.parseInt(str);
    }


    public String getPreviousMonthFirst() {
        String str = "";
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

        Calendar lastDate = Calendar.getInstance();
        lastDate.set(Calendar.DATE, 1);// 设为当前月的1号
        lastDate.add(Calendar.MONTH, -1);// 减一个月，变为下月的1号
        // lastDate.add(Calendar.DATE,-1);//减去一天，变为当月最后一天

        str = sdf.format(lastDate.getTime());
        return str;
    }


    public String getFirstDayOfMonth() {
        String str = "";
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

        Calendar lastDate = Calendar.getInstance();
        lastDate.set(Calendar.DATE, 1);// 设为当前月的1号
        str = sdf.format(lastDate.getTime());
        return str;
    }


    public String getCurrentWeekday() {
        weeks = 0;
        int mondayPlus = this.getMondayPlus();
        GregorianCalendar currentDate = new GregorianCalendar();
        currentDate.add(GregorianCalendar.DATE, mondayPlus + 6);
        Date monday = currentDate.getTime();

        DateFormat df = DateFormat.getDateInstance();
        String preMonday = df.format(monday);
        return preMonday;
    }


    public String getNowTime(String dateformat) {
        Date now = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat(dateformat);// 可以方便地修改日期格式
        String hehe = dateFormat.format(now);
        return hehe;
    }


    private int getMondayPlus() {
        Calendar cd = Calendar.getInstance();
        // 获得今天是一周的第几天，星期日是第一天，星期二是第二天......
        int dayOfWeek = cd.get(Calendar.DAY_OF_WEEK) - 1; // 因为按中国礼拜一作为第一天所以这里减1
        if (dayOfWeek == 1) {
            return 0;
        } else {
            return 1 - dayOfWeek;
        }
    }


    public String getMondayOFWeek() {
        weeks = 0;
        int mondayPlus = this.getMondayPlus();
        GregorianCalendar currentDate = new GregorianCalendar();
        currentDate.add(GregorianCalendar.DATE, mondayPlus);
        Date monday = currentDate.getTime();

        DateFormat df = DateFormat.getDateInstance();
        String preMonday = df.format(monday);
        return preMonday;
    }


    public String getSaturday() {
        int mondayPlus = this.getMondayPlus();
        GregorianCalendar currentDate = new GregorianCalendar();
        currentDate.add(GregorianCalendar.DATE, mondayPlus + 7 * weeks + 6);
        Date monday = currentDate.getTime();
        DateFormat df = DateFormat.getDateInstance();
        String preMonday = df.format(monday);
        return preMonday;
    }


    public String getPreviousWeekSunday() {
        weeks = 0;
        weeks--;
        int mondayPlus = this.getMondayPlus();
        GregorianCalendar currentDate = new GregorianCalendar();
        currentDate.add(GregorianCalendar.DATE, mondayPlus + weeks);
        Date monday = currentDate.getTime();
        DateFormat df = DateFormat.getDateInstance();
        String preMonday = df.format(monday);
        return preMonday;
    }


    public String getPreviousWeekday() {
        weeks--;
        int mondayPlus = this.getMondayPlus();
        GregorianCalendar currentDate = new GregorianCalendar();
        currentDate.add(GregorianCalendar.DATE, mondayPlus + 7 * weeks);
        Date monday = currentDate.getTime();
        DateFormat df = DateFormat.getDateInstance();
        String preMonday = df.format(monday);
        return preMonday;
    }


    public String getNextMonday() {
        weeks++;
        int mondayPlus = this.getMondayPlus();
        GregorianCalendar currentDate = new GregorianCalendar();
        currentDate.add(GregorianCalendar.DATE, mondayPlus + 7);
        Date monday = currentDate.getTime();
        DateFormat df = DateFormat.getDateInstance();
        String preMonday = df.format(monday);
        return preMonday;
    }


    public String getNextSunday() {

        int mondayPlus = this.getMondayPlus();
        GregorianCalendar currentDate = new GregorianCalendar();
        currentDate.add(GregorianCalendar.DATE, mondayPlus + 7 + 6);
        Date monday = currentDate.getTime();
        DateFormat df = DateFormat.getDateInstance();
        String preMonday = df.format(monday);
        return preMonday;
    }

    private int getMonthPlus() {
        Calendar cd = Calendar.getInstance();
        int monthOfNumber = cd.get(Calendar.DAY_OF_MONTH);
        cd.set(Calendar.DATE, 1);// 把日期设置为当月第一天
        cd.roll(Calendar.DATE, -1);// 日期回滚一天，也就是最后一天
        MaxDate = cd.get(Calendar.DATE);
        if (monthOfNumber == 1) {
            return -MaxDate;
        } else {
            return 1 - monthOfNumber;
        }
    }


    public String getPreviousMonthEnd() {
        String str = "";
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

        Calendar lastDate = Calendar.getInstance();
        lastDate.add(Calendar.MONTH, -1);// 减一个月
        lastDate.set(Calendar.DATE, 1);// 把日期设置为当月第一天
        lastDate.roll(Calendar.DATE, -1);// 日期回滚一天，也就是本月最后一天
        str = sdf.format(lastDate.getTime());
        return str;
    }

    public static int getPreviousDay() {
        String str = "";
        SimpleDateFormat sdf = new SimpleDateFormat("dd");

        Calendar lastDate = Calendar.getInstance();
        lastDate.add(Calendar.MONTH, -1);// 减一个月
        lastDate.set(Calendar.DATE, 1);// 把日期设置为当月第一天
        lastDate.roll(Calendar.DATE, -1);// 日期回滚一天，也就是本月最后一天
        str = sdf.format(lastDate.getTime());
        return Integer.parseInt(str);
    }

    public static int getPreviousLastDay() {
        String str = "";
        SimpleDateFormat sdf = new SimpleDateFormat("dd");

        Calendar lastDate = Calendar.getInstance();
        lastDate.add(Calendar.MONTH, -2);// 减一个月
        lastDate.set(Calendar.DATE, 1);// 把日期设置为当月第一天
        lastDate.roll(Calendar.DATE, -1);// 日期回滚一天，也就是本月最后一天
        str = sdf.format(lastDate.getTime());
        return Integer.parseInt(str);
    }
    public static int getPreviousMonth() {
        String str = "";
        SimpleDateFormat sdf = new SimpleDateFormat("MM");

        Calendar lastDate = Calendar.getInstance();
        lastDate.add(Calendar.MONTH, -1);// 减一个月
        lastDate.set(Calendar.DATE, 1);// 把日期设置为当月第一天
        lastDate.roll(Calendar.DATE, -1);// 日期回滚一天，也就是本月最后一天
        str = sdf.format(lastDate.getTime());
        return Integer.parseInt(str);
    }

    public static int getPreviousLastMonth() {
        String str = "";
        SimpleDateFormat sdf = new SimpleDateFormat("MM");

        Calendar lastDate = Calendar.getInstance();
        lastDate.add(Calendar.MONTH, -2);// 减一个月
        lastDate.set(Calendar.DATE, 1);// 把日期设置为当月第一天
        lastDate.roll(Calendar.DATE, -1);// 日期回滚一天，也就是本月最后一天
        str = sdf.format(lastDate.getTime());
        return Integer.parseInt(str);
    }


    public String getNextMonthFirst() {
        String str = "";
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

        Calendar lastDate = Calendar.getInstance();
        lastDate.add(Calendar.MONTH, 1);// 减一个月
        lastDate.set(Calendar.DATE, 1);// 把日期设置为当月第一天
        str = sdf.format(lastDate.getTime());
        return str;
    }


    public String getNextMonthEnd() {
        String str = "";
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

        Calendar lastDate = Calendar.getInstance();
        lastDate.add(Calendar.MONTH, 1);// 加一个月
        lastDate.set(Calendar.DATE, 1);// 把日期设置为当月第一天
        lastDate.roll(Calendar.DATE, -1);// 日期回滚一天，也就是本月最后一天
        str = sdf.format(lastDate.getTime());
        return str;
    }

    public static int getNextMonth() {
        String str = "";
        SimpleDateFormat sdf = new SimpleDateFormat("MM");

        Calendar lastDate = Calendar.getInstance();
        lastDate.add(Calendar.MONTH, 1);// 加一个月
        lastDate.set(Calendar.DATE, 1);// 把日期设置为当月第一天
        lastDate.roll(Calendar.DATE, -1);// 日期回滚一天，也就是本月最后一天
        str = sdf.format(lastDate.getTime());
        return Integer.parseInt(str);
    }


    public String getNextYearEnd() {
        String str = "";
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

        Calendar lastDate = Calendar.getInstance();
        lastDate.add(Calendar.YEAR, 1);// 加一个年
        lastDate.set(Calendar.DAY_OF_YEAR, 1);
        lastDate.roll(Calendar.DAY_OF_YEAR, -1);
        str = sdf.format(lastDate.getTime());
        return str;
    }


    public String getNextYearFirst() {
        String str = "";
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

        Calendar lastDate = Calendar.getInstance();
        lastDate.add(Calendar.YEAR, 1);// 加一个年
        lastDate.set(Calendar.DAY_OF_YEAR, 1);
        str = sdf.format(lastDate.getTime());
        return str;

    }


    private int getMaxYear() {
        Calendar cd = Calendar.getInstance();
        cd.set(Calendar.DAY_OF_YEAR, 1);// 把日期设为当年第一天
        cd.roll(Calendar.DAY_OF_YEAR, -1);// 把日期回滚一天。
        int MaxYear = cd.get(Calendar.DAY_OF_YEAR);
        return MaxYear;
    }

    private int getYearPlus() {
        Calendar cd = Calendar.getInstance();
        int yearOfNumber = cd.get(Calendar.DAY_OF_YEAR);// 获得当天是一年中的第几天
        cd.set(Calendar.DAY_OF_YEAR, 1);// 把日期设为当年第一天
        cd.roll(Calendar.DAY_OF_YEAR, -1);// 把日期回滚一天。
        int MaxYear = cd.get(Calendar.DAY_OF_YEAR);
        if (yearOfNumber == 1) {
            return -MaxYear;
        } else {
            return 1 - yearOfNumber;
        }
    }


    public String getCurrentYearFirst() {
        int yearPlus = this.getYearPlus();
        GregorianCalendar currentDate = new GregorianCalendar();
        currentDate.add(GregorianCalendar.DATE, yearPlus);
        Date yearDay = currentDate.getTime();
        DateFormat df = DateFormat.getDateInstance();
        String preYearDay = df.format(yearDay);
        return preYearDay;
    }

    // 获得本年最后一天的日期 *
    public String getCurrentYearEnd() {
        Date date = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy");// 可以方便地修改日期格式
        String years = dateFormat.format(date);
        return years + "-12-31";
    }

    // 获得上年第一天的日期 *
    public String getPreviousYearFirst() {
        Date date = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy");// 可以方便地修改日期格式
        String years = dateFormat.format(date);
        int years_value = Integer.parseInt(years);
        years_value--;
        return years_value + "-1-1";
    }

    // 获得上年最后一天的日期
    public String getPreviousYearEnd() {
        weeks--;
        int yearPlus = this.getYearPlus();
        GregorianCalendar currentDate = new GregorianCalendar();
        currentDate.add(GregorianCalendar.DATE, yearPlus + MaxYear * weeks
                + (MaxYear - 1));
        Date yearDay = currentDate.getTime();
        DateFormat df = DateFormat.getDateInstance();
        String preYearDay = df.format(yearDay);
        return preYearDay;
    }


    public String getThisSeasonFirstTime(int month) {
        int array[][] = {{1, 2, 3}, {4, 5, 6}, {7, 8, 9}, {10, 11, 12}};
        int season = 1;
        if (month >= 1 && month <= 3) {
            season = 1;
        }
        if (month >= 4 && month <= 6) {
            season = 2;
        }
        if (month >= 7 && month <= 9) {
            season = 3;
        }
        if (month >= 10 && month <= 12) {
            season = 4;
        }
        int start_month = array[season - 1][0];
        int end_month = array[season - 1][2];

        Date date = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy");// 可以方便地修改日期格式
        String years = dateFormat.format(date);
        int years_value = Integer.parseInt(years);

        int start_days = 1;// years+"-"+String.valueOf(start_month)+"-1";//getLastDayOfMonth(years_value,start_month);
        int end_days = getLastDayOfMonth(years_value, end_month);
        String seasonDate = years_value + "-" + start_month + "-" + start_days;
        return seasonDate;

    }


    public String getThisSeasonFinallyTime(int month) {
        int array[][] = {{1, 2, 3}, {4, 5, 6}, {7, 8, 9}, {10, 11, 12}};
        int season = 1;
        if (month >= 1 && month <= 3) {
            season = 1;
        }
        if (month >= 4 && month <= 6) {
            season = 2;
        }
        if (month >= 7 && month <= 9) {
            season = 3;
        }
        if (month >= 10 && month <= 12) {
            season = 4;
        }
        int start_month = array[season - 1][0];
        int end_month = array[season - 1][2];

        Date date = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy");// 可以方便地修改日期格式
        String years = dateFormat.format(date);
        int years_value = Integer.parseInt(years);

        int start_days = 1;// years+"-"+String.valueOf(start_month)+"-1";//getLastDayOfMonth(years_value,start_month);
        int end_days = getLastDayOfMonth(years_value, end_month);
        String seasonDate = years_value + "-" + end_month + "-" + end_days;
        return seasonDate;

    }


    private int getLastDayOfMonth(int year, int month) {
        if (month == 1 || month == 3 || month == 5 || month == 7 || month == 8
                || month == 10 || month == 12) {
            return 31;
        }
        if (month == 4 || month == 6 || month == 9 || month == 11) {
            return 30;
        }
        if (month == 2) {
            if (isLeapYear(year)) {
                return 29;
            } else {
                return 28;
            }
        }
        return 0;
    }


    public boolean isLeapYear(int year) {
        return (year % 4 == 0 && year % 100 != 0) || (year % 400 == 0);
    }


    public boolean isLeapYear2(int year) {
        return new GregorianCalendar().isLeapYear(year);
    }
}

