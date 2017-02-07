package com.jajale.watch.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.jajale.watch.R;
import com.jajale.watch.cviews.AlarmPickerView;
import com.jajale.watch.listener.SingleStringListener;
import com.jajale.watch.utils.CMethod;
import com.jajale.watch.utils.DateUtils;
import com.jajale.watch.utils.T;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Created by athena on 2015/11/20.
 * Email: lizhiqiang@bjjajale.com
 */
public class BirthdayDialog extends Dialog implements View.OnClickListener {

    private SingleStringListener mListener;
    private String birthday;
    private AlarmPickerView number_picker_year, number_picker_month, number_picker_day;

    private String datemonth_long[] = {"01", "03", "05", "07", "08", "10", "12"};

    boolean isLeapYear = false;
    String mTitle;
    private int differ =20 ;


    public BirthdayDialog(Context context, String title, String birthday,int differ ,SingleStringListener mListener) {
        super(context);
        this.mListener = mListener;
        this.birthday = birthday;
        this.mTitle = title;
        if (differ > 20){
            this.differ = differ;
        }


        setContentView(R.layout.dialog_birthday);
        Window window = getWindow();
        WindowManager.LayoutParams params = window.getAttributes();
        params.gravity = Gravity.CENTER;
        window.setBackgroundDrawableResource(android.R.color.transparent);
        window.setAttributes(params);
        setCanceledOnTouchOutside(true);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        findViewById(R.id.btn_ok).setOnClickListener(this);
        findViewById(R.id.btn_cancel).setOnClickListener(this);
        TextView tv_dialog_title = (TextView) findViewById(R.id.tv_dialog_title);
        tv_dialog_title.setText(mTitle);

//        findViewById(R.id.num_down_year).setOnClickListener(this);
//        findViewById(R.id.num_down_month).setOnClickListener(this);
//        findViewById(R.id.num_down_day).setOnClickListener(this);
//        findViewById(R.id.num_up_year).setOnClickListener(this);
//        findViewById(R.id.num_up_month).setOnClickListener(this);
//        findViewById(R.id.num_up_day).setOnClickListener(this);

        number_picker_year = (AlarmPickerView) findViewById(R.id.number_picker_year);
        number_picker_month = (AlarmPickerView) findViewById(R.id.number_picker_month);
        number_picker_day = (AlarmPickerView) findViewById(R.id.number_picker_day);


        Date date = new Date();
        SimpleDateFormat matter = new SimpleDateFormat("yyyy");
        String currentYear = matter.format(date);
        int yearMax = Integer.parseInt(currentYear);
        List<String> years = new ArrayList<String>();
        for (int i = yearMax - differ; i <= yearMax; i++) {
            years.add(i + "");
        }
        number_picker_year.setData(years);
        List<String> months = new ArrayList<String>();
        for (int i = 1; i <= 12; i++) {
            months.add(String.format("%02d", i));
        }
        number_picker_month.setData(months);

        try {
            isLeapYear = DateUtils.isLeapYear(Integer.parseInt(number_picker_year.getResult()));
            setDaysData(number_picker_month.getResult());
        } catch (Exception e) {
        }

        number_picker_year.setOnSelectListener(new AlarmPickerView.onSelectListener() {
            @Override
            public void onSelect(String text) {
                try {
                    isLeapYear = DateUtils.isLeapYear(Integer.parseInt(text));
                } catch (Exception e) {
                }

            }
        });
        number_picker_month.setOnSelectListener(new AlarmPickerView.onSelectListener() {
            @Override
            public void onSelect(String text) {
                setDaysData(text);
            }
        });


        if (!CMethod.isEmpty(birthday)) {
            String[] birthdays = birthday.split("-");
            if (birthdays.length == 3) {
                number_picker_year.setSelected(birthdays[0]);
                number_picker_month.setSelected(birthdays[1]);
                number_picker_day.setSelected(birthdays[2]);
            }
        } else {
            String[] birthday = CMethod.getFullDay().split("-");
            if (birthday.length == 3) {
                number_picker_year.setSelected(birthday[0]);
                number_picker_month.setSelected(birthday[1]);
                number_picker_day.setSelected(birthday[2]);
            }
        }
    }

    public String pack(String src) {
        return (src.length() == 1) ? ("0" + src) : src;
    }


    private boolean isLongMonth(String text) {
        for (int i = 0; i < datemonth_long.length; i++) {
            if (text.equals(datemonth_long[i])) {
                return true;
            }
        }
        return false;
    }


    private void setDaysData(String month) {

        List<String> days = new ArrayList<String>();
        int days_max;
        if (month.equals("")) {
            days_max = 31;
        } else {
            if (month.equals("02")) {//二月单独判断
                if (isLeapYear) {
                    days_max = 29;
                } else {
                    days_max = 28;
                }

            } else if (isLongMonth(month)) {//31天的月份
                days_max = 31;
            } else {
                days_max = 30;
            }
        }


        for (int i = 1; i <= days_max; i++) {
            days.add(String.format("%02d", i));
        }
        number_picker_day.setData(days);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_ok:
                if(isReasonDate()){
                    mListener.choiced(number_picker_year.getResult() + "-" + pack(number_picker_month.getResult()) + "-" + pack(number_picker_day.getResult()));//返回这种数据格式  "birthday": "1990-09-31",
                    dismiss();
                }else{
                    T.s("所选时间不能超过当前日期");
                }


                break;
            case R.id.btn_cancel:
                dismiss();
                break;
//            case R.id.num_up_year:
//                number_picker_year.previous();
//                break;
//            case R.id.num_up_month:
//                number_picker_month.previous();
//                break;
//            case R.id.num_up_day:
//                number_picker_day.previous();
//                break;
//            case R.id.num_down_year:
//                number_picker_year.next();
//                break;
//            case R.id.num_down_month:
//                number_picker_month.next();
//                break;
//            case R.id.num_down_day:
//                number_picker_day.next();
//                break;
        }
    }

    private boolean isReasonDate() {
        if (mTitle.equals("生日")||mTitle.equals("记录日期")||mTitle.equals("已接种日期")) {
//            Long my_date = Long.parseLong(dataOne(number_picker_year.getResult() + "-" + number_picker_month.getResult() + "-" + number_picker_day.getResult()));
//            Long dq_date=Long.parseLong(dataOne(CMethod.getFullDay()));
//            String current =
//            long result = my_date.longValue()-dq_date.longValue() ;
//
//            if (){
//              return false;
//            }else{
//                return  true;
//            }
            int select_year = Integer.parseInt(number_picker_year.getResult());
            int select_month =  Integer.parseInt(number_picker_month.getResult());
            int select_day =  Integer.parseInt(number_picker_day.getResult());

            String [] now = CMethod.getFullDay().split("-");
            if (select_year>Integer.parseInt(now[0])){
                return false;
            }else if(select_year == Integer.parseInt(now[0])){
                if (select_month > Integer.parseInt(now[1])){
                    return false;
                }else if (select_month == Integer.parseInt(now[1])){
                    if (select_day > Integer.parseInt(now[2])){
                        return false;
                    }
                }
            }
        }
        return true;

//        return true;
    }

    /**
     * 掉此方法输入所要转换的时间输入例如（"2014-06-14-16-09-00"）返回时间戳
     *
     * @param time
     * @return
     */
    public String dataOne(String time) {
        SimpleDateFormat sdr = new SimpleDateFormat("yyyy-MM-dd",
                Locale.CHINA);
        Date date;
        String times = null;
        try {
            date = sdr.parse(time);
            long l = date.getTime();
            String stf = String.valueOf(l);
            times = stf.substring(0, 10);

        } catch (ParseException e) {
            e.printStackTrace();
        }
        return times;
    }

}
