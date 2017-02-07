package com.jajale.watch.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.jajale.watch.AppConstants;
import com.jajale.watch.BaseActivity;
import com.jajale.watch.BaseApplication;
import com.jajale.watch.IntentAction;
import com.jajale.watch.R;
import com.jajale.watch.cviews.AlarmPickerView;
import com.jajale.watch.entity.SmartWatchListData;
import com.jajale.watch.entitydb.Clock;
import com.jajale.watch.entitydb.SmartWatch;
import com.jajale.watch.listener.HttpClientListener;
import com.jajale.watch.utils.CMethod;
import com.jajale.watch.utils.ClockUtils;
import com.jajale.watch.utils.HttpUtils;
import com.jajale.watch.utils.L;
import com.jajale.watch.utils.LoadingDialog;
import com.jajale.watch.utils.Md5Util;
import com.jajale.watch.utils.T;
import com.jajale.watch.utils.UrlAddressUrils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * 设置闹钟
 * <p/>
 * Created by lilonghui on 2015/11/25.
 * Email:lilonghui@bjjajale.com
 */
public class SetAlarmClockActivity extends BaseActivity implements View.OnClickListener {
    public static String weeks[] = new String[7];
    private boolean booleans[] = {false, false, false, false, false, false, false};
    private String period = "上午";
    private String hour = "07";
    private String minute = "30";
    private SmartWatch watch;
    //private SocketTools socketTools;
    SharedPreferences sp;
    SharedPreferences.Editor editor;
    private String service_alarm, URL, sign, imei, repetition, clocks, alarm;
    private String[] c;
    RequestQueue queue;

    @InjectView(R.id.iv_left)
    ImageView ivLeft;
    @InjectView(R.id.tv_left_2)
    TextView tvLeft2;
    @InjectView(R.id.tv_middle)
    TextView tvMiddle;
    @InjectView(R.id.tv_right)
    TextView tvRight;
    @InjectView(R.id.iv_right)
    ImageView ivRight;
    @InjectView(R.id.set_alarm_tv_repeat)
    TextView setAlarmTvRepeat;
    @InjectView(R.id.iv_tonext)
    ImageView ivTonext;
    @InjectView(R.id.alarm_rl_set_repeat)
    RelativeLayout alarmRlSetRepeat;
    @InjectView(R.id.set_alarm_tv_baby_name)
    TextView setAlarmTvBabyName;
    @InjectView(R.id.dear_iv_tonext)
    ImageView dearIvTonext;
    @InjectView(R.id.number_picker_period)
    AlarmPickerView numberPickerPeriod;
    @InjectView(R.id.number_picker_hour)
    AlarmPickerView numberPickerHour;
    @InjectView(R.id.number_picker_minute)
    AlarmPickerView numberPickerMinute;


    private LoadingDialog loadingDialog;
    //private Clock clock;
    private int clock_position, disturb;
    private List<Clock> clockList;


    private String time;
    private String type;
    private String values;
    @Override
    protected void onStart() {
        super.onStart();

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_alarm_clock);
        ButterKnife.inject(this);

        //设置title
        tvMiddle.setText(getResources().getString(R.string.alarm_set_alarm));
        ivLeft.setImageResource(R.drawable.title_goback_selector);
        ivLeft.setOnClickListener(this);
        tvRight.setText(getResources().getString(R.string.save));
        tvRight.setOnClickListener(this);

        //进入设置重复周期
        alarmRlSetRepeat.setOnClickListener(this);

        //获取intent数据
        //watch = getIntent().getParcelableExtra(SmartWatchListData.KEY);//手表数据
        //clock_position = getIntent().getExtras().getInt("clock_position", 0);//当前编辑的position
        queue = Volley.newRequestQueue(getApplicationContext());
        Bundle bundle=getIntent().getExtras();
        clock_position = bundle.getInt("clock_position");//闹钟item position
        c = bundle.getStringArray("clock");//获取闹钟时间数据
        service_alarm = bundle.getString("service_alarm");//读出数据
        URL = bundle.getString("URL");
        imei = bundle.getString("imei");
        repetition = bundle.getString("repeat");//获取天数(周一、二...）
        clocks = bundle.getString("clocks");

        sp = getApplicationContext().getSharedPreferences("NotDisturb", getApplicationContext().MODE_PRIVATE);// 创建对象
        editor = sp.edit();
        disturb = clock_position + 1;
        String clock = sp.getString("clock" + disturb, "");
        L.e("repetition" + clock);
        //clockList = ClockUtils.getClockListFromDB(watch.getUser_id());//从数据库获取闹钟列表数据
        //clock= clockList.get(clock_position);//当前闹钟数据

        weeks = new String[]{getResources().getString(R.string.sunday), getResources().getString(R.string.monday),
                getResources().getString(R.string.tuesday), getResources().getString(R.string.wednesday),
                getResources().getString(R.string.thursday), getResources().getString(R.string.friday),
                getResources().getString(R.string.saturday)};

        loadingDialog = new LoadingDialog(this);

        //以下for循环是设置重复周期TextView,将Boolean数组转化为字符串
        //String[] strings = clock.getSetValues().split("");
        L.e("repetition" + repetition);
        String[] strings = repetition.split("");
        if (strings.length - 1 == booleans.length) {
            for (int i = 1; i < strings.length; i++) {
                if (strings[i].equals("0"))
                    booleans[i-1]=false;
                else
                    booleans[i-1]=true;
            }
            setRepeatTextView(booleans);
        }
        initAlarmClock();

    }





    /**
     * 初始化闹钟值
     */
    private void initAlarmClock() {
        if (!"".equals(sp.getString("nick_name", ""))) {
            setAlarmTvBabyName.setText(sp.getString("nick_name", ""));//提醒给哪个宝贝
        }

        //时段
        List<String> periods = new ArrayList<String>();
        periods.add(getResources().getString(R.string.morning));
        periods.add(getResources().getString(R.string.afternoon));
        periods.add(getResources().getString(R.string.morning));
        periods.add(getResources().getString(R.string.afternoon));
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
        numberPickerPeriod.setData(periods);
        numberPickerHour.setData(hours);
        numberPickerMinute.setData(minutes);


        //接收的上一个页面的数据，将时间指定为当前闹钟时间
        try {
            String[] timeHourAndMinute = clocks.split(":");

            if (Integer.parseInt(timeHourAndMinute[0]) > 12) {
                numberPickerPeriod.setSelected(periods.get(1));
                numberPickerHour.setSelected(hours.get(Integer.parseInt(timeHourAndMinute[0]) - 12 - 1));
            } else {
                numberPickerPeriod.setSelected(periods.get(0));
                numberPickerHour.setSelected(hours.get(Integer.parseInt(timeHourAndMinute[0]) - 1));
            }

            numberPickerMinute.setSelected(minutes.get(Integer.parseInt(timeHourAndMinute[1])));
        } catch (Exception e) {

        }
        //时段选中记录
        numberPickerPeriod.setOnSelectListener(new AlarmPickerView.onSelectListener() {
            @Override
            public void onSelect(String text) {
                period = text;
            }
        });
        //小时选中记录
        numberPickerHour.setOnSelectListener(new AlarmPickerView.onSelectListener() {

            @Override
            public void onSelect(String text) {
                hour = text;
            }
        });
        //分钟选中记录
        numberPickerMinute.setOnSelectListener(new AlarmPickerView.onSelectListener() {

            @Override
            public void onSelect(String text) {
                minute = text;
            }
        });


    }


    /**
     * 获取从选择周期传回来的boolean数组
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == 1) {
            boolean[] back_booleans = data.getBooleanArrayExtra(IntentAction.KEY_REPEAT_DATE_BOOLEAN_ARRAY);
            setRepeatTextView(back_booleans);
        }
    }

    /**
     * 将boolean数组转化为现实的字符串，全选为每天，不选为不重复，其余为周一，三。。
     * @param back_booleans
     */
    private void setRepeatTextView(boolean[] back_booleans) {
        String back_string = " ";
        boolean ischeckall = true;
        for (int i = 0; i < back_booleans.length; i++) {
            booleans = back_booleans;
            if (back_booleans[i]) {
                back_string = back_string + weeks[i].replace("周", "") + "、";
            } else {
                ischeckall = false;
            }
        }
        back_string = back_string.substring(0, back_string.length() - 1);
        if (ischeckall) {
            setAlarmTvRepeat.setText(getResources().getString(R.string.everyday));
        } else if ("".equals(back_string)){
            setAlarmTvRepeat.setText(getResources().getString(R.string.no_repeat));
        } else {
            setAlarmTvRepeat.setText(back_string);
        }
//        back_string = ischeckall ? "每天" : back_string.equals("") ? "不重复" : back_string;
//        setAlarmTvRepeat.setText(back_string);
    }


    @Override
    public void onClick(View v) {

        if (CMethod.isFastDoubleClick()){
            return;
        }
        switch (v.getId()) {
            case R.id.iv_left://返回键
                finish();
                break;
            case R.id.tv_right://保存
                if (!CMethod.isNet(getApplicationContext())) {
                    T.s(getResources().getString(R.string.no_network));
                    return;
                }
                loadingDialog.show(getResources().getString(R.string.send_instruct), AppConstants.MaxIndex);
                setCountDownToDismissDialog(loadingDialog);
                period = numberPickerPeriod.getResult();
                hour = numberPickerHour.getResult();
                minute = numberPickerMinute.getResult();

                time = "";
                type = "";
                values = "";

                //以下计算为编辑当前唯一闹钟指令
                String data;
                //将时间变为24小时制
                if (period.equals(getResources().getString(R.string.morning))) {
                    time = hour + ":" + minute;
                } else {
                    time =( (Integer.parseInt(hour) + 12)==24?"00":(Integer.parseInt(hour) + 12)) + ":" + minute;
                }
                //从每天，不重复，和其他做相对处理
                if (setAlarmTvRepeat.getText().toString().equals(getResources().getString(R.string.no_repeat))) {
                    data = time + "-1-1";
                    type = "1";

                } else if (setAlarmTvRepeat.getText().toString().equals(getResources().getString(R.string.everyday))) {
                    data = time + "-1-2";
                    type = "2";
                    values = "1111111";
                } else {
                    for (int i = 0; i < booleans.length; i++) {
                        if (booleans[i]) {
                            values = values + "1";
                        } else {
                            values = values + "0";
                        }
                    }
                    data = time + "-1-3-" + values;
                    type = "3";
                }

                L.e("data==" + data);
                //以下为编辑当前闹钟的总指令
                String code_only;//单一指令
                String code_all = "";// 总指令
                for (int i = 1; i < 4; i++) {
                    String clock = sp.getString("clock" + i, "");
                    if (clock.length() == 0) {
                        clock = c[i - 1];
                    }
                    String onoff = sp.getString("onoff" + i, "");
                    if (onoff.length() == 0) {
                        onoff = 1 + "";
                    }
                    String type = sp.getString("type" + i, "");
                    if (type.length() == 0) {
                        type = 1 + "";
                    }
                    String values = sp.getString("values" + i, "");
                    if (values.length() == 0) {
                        values = 0000000 + "";
                    }
                    if (!type.equals("3")) {
                        code_only = clock + "-" + onoff + "-" + type;
                    } else {
                        code_only = clock + "-" + onoff + "-" + type + "-" + values;
                    }
                    if (i == disturb) {
                        code_only = data;
                    }
                    code_all = code_all + code_only + ",";
                }
                code_all = code_all.substring(0, code_all.length() - 1);//去掉最后的","
                L.e("clock_code_all===" + code_all);
                alarm = code_all;

                setClockToNetWork();
                break;
            case R.id.alarm_rl_set_repeat://设置重复日期
                Intent intent = new Intent(SetAlarmClockActivity.this, RepeatDateAlarmActivity.class);
                intent.putExtra(IntentAction.KEY_REPEAT_DATE_BOOLEAN_ARRAY, booleans);
                startActivityForResult(intent, 1);
                break;
        }
    }


    /**
     * 向服务器发送编辑闹钟数据
     */
    private void setClockToNetWork() {
        sign = Md5Util.stringmd5(alarm, imei, service_alarm);
        String path = URL + "service=" + service_alarm + "&imei=" + imei + "&alarm=" + alarm + "&sign=" + sign;
        Log.e("not_disturb_code_all===" , path);
        StringRequest request = new StringRequest(path, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                loadingDialog.dismissAndStopTimer();
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONObject jsondata = jsonObject.getJSONObject("data");
                    int code = jsondata.getInt("code");
                    if (code == 0) {
                        T.s(getResources().getString(R.string.send_code_success));
                        editor.putString("clock" + disturb, time);
                        editor.putString("onoff" + disturb, 1 + "");//保存后默认打开开关
                        editor.putString("type" + disturb, type);
                        editor.putString("values" + disturb, values);
                        editor.commit();
                    } else if (code == 1) {
                        T.s(getResources().getString(R.string.not_online));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                loadingDialog.dismissAndStopTimer();
            }
        });
        queue.add(request);

    }


    private void dialogDismiss() {
        if(loadingDialog!=null){
            loadingDialog.dismissAndStopTimer();
        }

    }


}
