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

import java.util.ArrayList;
import java.util.List;

/**
 * Created by athena on 2015/11/20.
 * Email: lizhiqiang@bjjajale.com
 */
public class SelectTimeDialog extends Dialog implements View.OnClickListener {

    private SingleStringListener mListener;
    private AlarmPickerView number_picker_period, number_picker_hour, number_picker_minute;
    private String time;
    private String title;

    public SelectTimeDialog(Context context,String title ,String time, SingleStringListener mListener) {
        super(context);
        this.mListener = mListener;
        this.time = time;
        this.title = title;
        setContentView(R.layout.dialog_select_time);
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
        tv_dialog_title.setText(title);


        number_picker_period = (AlarmPickerView) findViewById(R.id.number_picker_period);
        number_picker_hour = (AlarmPickerView) findViewById(R.id.number_picker_hour);
        number_picker_minute = (AlarmPickerView) findViewById(R.id.number_picker_minute);


        List<String> periods = new ArrayList<String>();
        periods.add("上午");
        periods.add("下午");
        periods.add("上午");
        periods.add("下午");
        //小时
        List<String> hours = new ArrayList<String>();
        for (int i = 0; i < 12; i++) {
            hours.add((i + 1) < 10 ? "0" + (i + 1) : "" + (i + 1));
        }
        //分钟
        List<String> minutes = new ArrayList<String>();
        for (int i = 0; i < 60; i++) {
            minutes.add(i < 10 ? "0" + i : "" + i);
        }
        number_picker_period.setData(periods);
        number_picker_hour.setData(hours);
        number_picker_minute.setData(minutes);


        try {
            String[] timeHourAndMinute = time.split(":");

            if (Integer.parseInt(timeHourAndMinute[0]) > 12) {
                number_picker_period.setSelected(periods.get(1));
                number_picker_hour.setSelected(hours.get(Integer.parseInt(timeHourAndMinute[0]) - 12 - 1));
            } else {
                number_picker_period.setSelected(periods.get(0));
                number_picker_hour.setSelected(hours.get(Integer.parseInt(timeHourAndMinute[0]) - 1));
            }

            number_picker_minute.setSelected(minutes.get(Integer.parseInt(timeHourAndMinute[1])));
        } catch (Exception e) {

        }

    }

    public String pack(String src) {
        return (src.length() == 1) ? ("0" + src) : src;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_ok:
                String backTime = "";
//                mListener.choiced(number_picker_period.getResult() + "-" + pack(number_picker_hour.getResult()) + "-" + pack(number_picker_minute.getResult()));//返回这种数据格式  "birthday": "1990-09-31",
                if (number_picker_period.getResult().equals("上午")) {
                    backTime = pack(number_picker_hour.getResult()) + ":" + pack(number_picker_minute.getResult());
                } else {
                    String  hour=(Integer.parseInt(number_picker_hour.getResult()) + 12)==24?"00":(Integer.parseInt(number_picker_hour.getResult()) + 12)+"";
                    backTime = pack(hour + ":" + pack(number_picker_minute.getResult()));
                }
                mListener.choiced(backTime);//返回这种数据格式  "birthday": "18:45",
                dismiss();
                break;
            case R.id.btn_cancel:
                dismiss();
                break;

        }
    }
}
