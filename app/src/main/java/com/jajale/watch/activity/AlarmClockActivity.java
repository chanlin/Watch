package com.jajale.watch.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.jajale.watch.AppConstants;
import com.jajale.watch.BaseActivity;
import com.jajale.watch.BaseApplication;
import com.jajale.watch.R;
import com.jajale.watch.adapter.AlarmClockListAdapter;
import com.jajale.watch.adapter.NotDisturbListAdapter;
import com.jajale.watch.entity.ClockData;
import com.jajale.watch.entity.SmartWatchListData;
import com.jajale.watch.entitydb.Clock;
import com.jajale.watch.entitydb.SmartWatch;
import com.jajale.watch.listener.SettingSwitchListener;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * 设置的闹钟列表
 * <p/>
 * Created by lilonghui on 2015/11/25.
 * Email:lilonghui@bjjajale.com
 */
public class AlarmClockActivity extends BaseActivity implements View.OnClickListener {
    private SmartWatch watch;
    private AlarmClockListAdapter adapter;
    private LoadingDialog loadingDialog;

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
    @InjectView(R.id.alarm_lv_content)
    ListView alarmLvContent;

    SharedPreferences sp;
    SharedPreferences.Editor editor;
    private String service_alarm, URL, imei;
    private List<Map<String, Object>> listItems = new ArrayList<Map<String, Object>>();
    List<Clock> clocks = new ArrayList<Clock>();//获取闹钟数据
    private String[] c = new String[]{"09:00", "10:00", "11:00"};//闹钟时间
    private String[] onOff = new String[]{"1", "1", "1"};//闹钟开关
    private String[] repeat = new String[]{"0000000", "0000000", "0000000"};//响铃 数据格式：0000000
    private String ck, repetition, OnOff, type;
    RequestQueue queue;

    private SettingSwitchListener settingSwitchListener =new SettingSwitchListener() {
        @Override
        public void setSwitch(int position, int onOff) {
            setClockToNetWork(position,onOff);//设置闹钟到服务器
        }
    };

    @Override
    protected void onResume() {
        super.onResume();
        refreshListForDBorNetWork();
    }


    /**
     * 刷新闹钟列表
     */
    private void refreshListForDBorNetWork() {
        clocks.clear();
//        List<Clock> clocks = ClockUtils.getClockListFromDB(watch.getUser_id());
        for(int i = 0; i < c.length; i++) {
            Clock clock = new Clock();
            int n = i + 1;
            ck = sp.getString("clock" + n, "");//闹钟时间
            repetition = sp.getString("values" + n, "");//响铃 数据格式：0000000
            OnOff = sp.getString("onoff" + n, "");//闹钟开关
            type = sp.getString("type" + n, "");//闹钟开关
            clock.setTime("".equals(ck)? c[i] : ck);
            clock.setOnOFF(Integer.parseInt("".equals(OnOff)? onOff[i] : OnOff));
            clock.setSetValues("".equals(repetition)? repeat[i] : repetition);
            clock.setType(Integer.parseInt("".equals(type)? "1" : type));
            clocks.add(clock);
        }
        if (clocks != null && clocks.size() != 0) {
            setAlarmClockListView(clocks);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm_clock);
        ButterKnife.inject(this);
        queue = Volley.newRequestQueue(getApplicationContext());
        loadingDialog = new LoadingDialog(this);
        tvMiddle.setText(getResources().getString(R.string.alarm_title_text));
        ivLeft.setImageResource(R.drawable.title_goback_selector);
        ivLeft.setOnClickListener(this);


//        watch = getIntent().getParcelableExtra(SmartWatchListData.KEY);//获取intent传递过来的数据
        Bundle bundle=getIntent().getExtras();
        service_alarm = bundle.getString("service_alarm");//读出数据
        URL = bundle.getString("URL");
        imei = bundle.getString("imei");
        sp = getApplicationContext().getSharedPreferences("NotDisturb", getApplicationContext().MODE_PRIVATE);// 创建对象
        editor = sp.edit();

        if (CMethod.isNet(AlarmClockActivity.this)) {
//            getClockFromNetWork();
        } else {
            T.s(getResources().getString(R.string.no_network));
        }

    }

    private void setAlarmClockListView(final List<Clock> clocks) {

        if (adapter != null && adapter.clocks != null) {
            adapter.clocks = clocks;
            adapter.notifyDataSetChanged();
        } else {
            adapter = new AlarmClockListAdapter(this, clocks, settingSwitchListener);
            alarmLvContent.setAdapter(adapter);
        }

        alarmLvContent.setOnItemClickListener(new AdapterView.OnItemClickListener() {//进入设置
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (CMethod.isFastDoubleClick()) {
                    return;
                }
                if (clocks != null && clocks.size() != 0) {
                    if (!CMethod.isFastDoubleClick()) {
                        //点击item跳转到编辑当前闹钟的Activity，传递的数据为手表实体类，闹钟实体类，当前position，当前闹钟总质量
                        Intent intent = new Intent(AlarmClockActivity.this, SetAlarmClockActivity.class);
                        Bundle mBundle = new Bundle();
//                        mBundle.putParcelable(SmartWatchListData.KEY, watch);//传递宝贝信息
                        mBundle.putInt("clock_position", position);
                        mBundle.putStringArray("clock", c);
                        mBundle.putString("clocks", clocks.get(position).getTime());
                        mBundle.putString("repeat", clocks.get(position).getSetValues());
                        mBundle.putString("service_alarm", service_alarm);
                        mBundle.putString("imei", imei);
                        mBundle.putString("URL", URL);
                        intent.putExtras(mBundle);
                        startActivity(intent);
                    }
                }

            }
        });

    }


    /**
     * 从服务器获取闹钟数据
     */
    private void getClockFromNetWork() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("user_id", BaseApplication.getUserInfo().userID);//用户ID
            jsonObject.put("friend_id", watch.getUser_id());//手表ID
        } catch (Exception e) {
            e.printStackTrace();
        }
        HttpUtils.PostDataToWeb(UrlAddressUrils.CODE_API, AppConstants.JAVA_WATCH_CLOCK_LIST_URL, jsonObject, new HttpClientListener() {
            @Override
            public void onSuccess(String result) {
                L.e("123===result=====" + result);
                Gson gson = new Gson();
                ClockData fromJson = gson.fromJson(result, ClockData.class);
                if (fromJson != null && fromJson.getClockList() != null && fromJson.getClockList().size() != 0) {
                    for (int i = 0; i < fromJson.getClockList().size(); i++) {
                        fromJson.getClockList().get(i).setWatchId(watch.getUser_id());
                        fromJson.getClockList().get(i).setId(watch.getUser_id() + "_" + fromJson.getClockList().get(i).getNumber());
                    }
                    ClockUtils.refreshDB(fromJson.getClockList(), watch.getUser_id());//闹钟数据库刷新
                    setAlarmClockListView(fromJson.getClockList());
                }

            }

            @Override
            public void onFailure(String result) {
                T.s(result);
            }

            @Override
            public void onError() {
            }
        });

    }


    /**
     * 当指令发送成功后向服务器发送
     */
    private void setClockToNetWork(final int position,final int onOff) {
        if (!CMethod.isNet(getApplicationContext())) {
            T.s(getResources().getString(R.string.no_network));
            return;
        }
        clocks.get(position).setOnOFF(onOff);//更改当前开关数据
        //以下为编辑当前闹钟的总指令
        String code_only;//单一指令
        String code_all = "";// 总指令
        for (int i = 0; i < clocks.size(); i++) {
            if (clocks.get(i).getType() != 3) {
                code_only = clocks.get(i).getTime() + "-" + clocks.get(i).getOnOFF() + "-" + clocks.get(i).getType();
            } else {
                code_only = clocks.get(i).getTime() + "-" + clocks.get(i).getOnOFF() + "-" + clocks.get(i).getType() + "-" + clocks.get(i).getSetValues();
            }
            code_all = code_all + code_only + ",";
        }
        code_all = code_all.substring(0, code_all.length() - 1);//去掉最后的","
        L.e("clock_code_all===" + code_all);
        String alarm = code_all;

        loadingDialog.show();
        String sign = Md5Util.stringmd5(alarm, imei, service_alarm);
        String path = URL + "service=" + service_alarm + "&imei=" + imei + "&alarm=" + alarm + "&sign=" + sign;
        Log.e("not_disturb_code_all===" , path);
        StringRequest request = new StringRequest(path, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONObject jsondata = jsonObject.getJSONObject("data");
                    int code = jsondata.getInt("code");
                    if (code == 0) {
                        T.s(getResources().getString(R.string.send_code_success));
                        int n = position + 1;
                        editor.putString("onoff" + n, clocks.get(position).getOnOFF() + "");//保存后默认打开开关
                        editor.commit();
                        setAlarmClockListView(clocks);//更新数据表
                        dialogDismiss();
                    } else if (code == 1) {
                        dialogDismiss();
                        T.s(getResources().getString(R.string.not_online));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    dialogDismiss();
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                loadingDialog.dismissAndStopTimer();
            }
        });
        queue.add(request);

//        Clock clock=adapter.clocks.get(position);
//        JSONObject jsonObject = new JSONObject();
//        try {
//            jsonObject.put("user_id", BaseApplication.getUserInfo().userID);//用户ID
//            jsonObject.put("watch_id", watch.getUser_id());//手表ID
//            jsonObject.put("number", clock.getNumber());//闹钟编号
//            jsonObject.put("onOFF", onOff);//闹钟开关
//            jsonObject.put("type", clock.getType());//闹钟类型
//            jsonObject.put("setValues",clock.getSetValues());//响铃   数据格式 ：0000000
//            jsonObject.put("time",clock.getTime()==null?"00:00":clock.getTime());//闹钟时间
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//        HttpUtils.PostDataToWeb(UrlAddressUrils.CODE_CENTER,AppConstants.JAVA_WATCH_SET_WATCH_CLOCK_URL, jsonObject, new HttpClientListener() {
//            @Override
//            public void onSuccess(String result) {
//                T.s("设置闹钟成功");
//                //本地数据库刷新
//                Clock clock_update = adapter.clocks.get(position);
//                clock_update.setOnOFF(onOff);
//                ClockUtils.updateClockData(clock_update);
//                dialogDismiss();
//            }
//
//            @Override
//            public void onFailure(String result) {
//                dialogDismiss();
//                T.s(result);
//            }
//
//            @Override
//            public void onError() {
//                dialogDismiss();
//            }
//        });

    }

    @Override
    public void onClick(View v) {

        if (CMethod.isFastDoubleClick()) {
            return;
        }

        switch (v.getId()) {
            case R.id.iv_left://返回键
                Finish();
                break;

        }

    }


    @Override
    protected void onDestroy() {
        super.onDestroy();

    }

    private void Finish() {
        finish();
    }



    private void dialogDismiss() {

        if (loadingDialog != null) {
            loadingDialog.dismissAndStopTimer();
        }

        //refreshListForDBorNetWork();
    }
}
