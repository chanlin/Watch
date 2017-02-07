package com.jajale.watch.utils;

import com.jajale.watch.entitydb.GrowRecord;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by athena on 2015/12/7.
 * Email: lizhiqiang@bjjajale.com
 */
public class GrowRecordUtils {



    public static int[] getMyArrayMonthRecord(List<GrowRecord> records) {

        if (records != null) {
            List<Integer> list = new ArrayList<Integer>();

            for (int i = 0; i < records.size(); i++) {
                if (isRationalData(records.get(i))) {
                    list.add(records.get(i).month);
                }
            }
            int[] myMonths = new int[list.size()];
            for (int i = 0; i < list.size(); i++) {
                myMonths[i] = list.get(i);
                L.e("myMonths[" + i + "]" + "==" + myMonths[i]);
            }
            return reverse(myMonths);
        }

        return null;
    }


    public static int[] getMyArrayHeightRecord(List<GrowRecord> records) {

        if (records != null) {

            List<Integer> list = new ArrayList<Integer>();

            for (int i = 0; i < records.size(); i++) {
                if (isRationalData(records.get(i))) {
                    list.add((int) records.get(i).height);
                }

            }


            int[] myHeights = new int[list.size()];

            for (int i = 0; i < list.size(); i++) {
                myHeights[i] = list.get(i);
                L.e("myHeights[" + i + "]" + "==" + myHeights[i]);
            }


            return reverse(myHeights);
        }

        return null;
    }

    public static int[] getMyArrayWeightRecord(List<GrowRecord> records) {

        if (records != null) {

            List<Integer> list = new ArrayList<Integer>();

            for (int i = 0; i < records.size(); i++) {
                if (isRationalData(records.get(i))) {
                    list.add((int) records.get(i).weight);
                }

            }
            int[] myWeights = new int[list.size()];

            for (int i = 0; i < list.size(); i++) {
                myWeights[i] = list.get(i);
                L.e("myWeights[" + i + "]" + "==" + myWeights[i]);
            }
            return reverse(myWeights);
        }

        return null;
    }

    private static int[] reverse(int[] rt) {

        int r[] = new int[rt.length]; //定义一个和要转换数组同长度的数组

        for (int i = 0; i < rt.length; i++) {
            r[rt.length - 1 - i] = rt[i]; //从最后向前填充r数组
        }
            return r;
        }


    private static boolean isRationalData(GrowRecord growRecord) {

        if (growRecord.weight > 80 || growRecord.height > 180 || growRecord.month > 168 || growRecord.month < 0 || growRecord.weight < 0 || growRecord.height < 20) {
            return false;
        } else {
            return true;
        }
    }

    public static String getAgeByBirthday(String recordDay, String birthday) {
        //根据生日算年龄
        try {
            if (!CMethod.isEmptyOrZero(birthday)) {
                SimpleDateFormat df = new SimpleDateFormat(birthday.length() > 10 ? "yyyy-MM-dd HH:mm:ss" : "yyyy-MM-dd");
                Date date = df.parse(birthday);

                Date date_record = df.parse(recordDay);
                return getAgeByBirthday(date_record, date);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * 根据用户生日计算年龄
     */
    public static String getAgeByBirthday(Date recordDay, Date birthday) {
        Calendar cal = Calendar.getInstance();
        if (cal.before(birthday)) {
            throw new IllegalArgumentException(
                    "The birthDay is before Now.It's unbelievable!");
        }
        cal.setTime(recordDay);
        int yearNow = cal.get(Calendar.YEAR);
        int monthNow = cal.get(Calendar.MONTH) + 1;
//        int dayOfMonthNow = cal.get(Calendar.DAY_OF_MONTH);

        cal.setTime(birthday);
        int yearBirth = cal.get(Calendar.YEAR);
        int monthBirth = cal.get(Calendar.MONTH) + 1;
//        int dayOfMonthBirth = cal.get(Calendar.DAY_OF_MONTH);

        int subYear = yearNow - yearBirth;
        int subMonth = monthNow - monthBirth;

        int month_ = subYear * 12 + subMonth;

        String year = month_ / 12 == 0 ? "" : month_ / 12 + "岁";
        String month = month_ % 12 == 0 ? "" : month_ % 12 + "个月";

        if ("".equals(year) && "".equals(month)) {
            return "不满一个月" + "，";
        } else {
            return year + month + "，";
        }


    }

    public static int getMonthByBirthday(String recordDay, String birthday) {
        //根据生日算年龄
        try {
            if (!CMethod.isEmptyOrZero(birthday)) {
                SimpleDateFormat df = new SimpleDateFormat(birthday.length() > 10 ? "yyyy-MM-dd HH:mm:ss" : "yyyy-MM-dd");
                Date date = df.parse(birthday);
                Date date_record = df.parse(recordDay);
                return getMonthByBirthday(date_record, date);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }


    /**
     * 根据用户生日计算年龄
     */
    public static int getMonthByBirthday(Date recordDay, Date birthday) {
        Calendar cal = Calendar.getInstance();
        if (cal.before(birthday)) {
            throw new IllegalArgumentException(
                    "The birthDay is before Now.It's unbelievable!");
        }
        cal.setTime(recordDay);
        int yearNow = cal.get(Calendar.YEAR);
        int monthNow = cal.get(Calendar.MONTH) + 1;
//        int dayOfMonthNow = cal.get(Calendar.DAY_OF_MONTH);

        cal.setTime(birthday);
        int yearBirth = cal.get(Calendar.YEAR);
        int monthBirth = cal.get(Calendar.MONTH) + 1;
//        int dayOfMonthBirth = cal.get(Calendar.DAY_OF_MONTH);

        int subYear = yearNow - yearBirth;
        int subMonth = monthNow - monthBirth;
        return subYear * 12 + subMonth;
    }

}
