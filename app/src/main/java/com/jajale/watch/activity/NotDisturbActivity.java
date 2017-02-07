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
import com.jajale.watch.adapter.NotDisturbListAdapter;
import com.jajale.watch.entity.NotDisturbData;
import com.jajale.watch.entity.SmartWatchListData;
import com.jajale.watch.entitydb.Clock;
import com.jajale.watch.entitydb.NotDisturb;
import com.jajale.watch.entitydb.SmartWatch;
import com.jajale.watch.listener.HttpClientListener;
import com.jajale.watch.listener.SettingSwitchListener;
import com.jajale.watch.utils.CMethod;
import com.jajale.watch.utils.HttpUtils;
import com.jajale.watch.utils.L;
import com.jajale.watch.utils.LoadingDialog;
import com.jajale.watch.utils.Md5Util;
import com.jajale.watch.utils.NotDisturbUtils;
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
 * 免打扰列表
 * <p>
 * Created by lilonghui on 2015/11/27.
 * Email:lilonghui@bjjajale.com
 */
public class NotDisturbActivity extends BaseActivity implements View.OnClickListener {


    private SmartWatch watch;
    private String null_time = "00:00";

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
    @InjectView(R.id.not_disturb_lv_content)
    ListView notDisturbLvContent;
    private NotDisturbListAdapter adapter;
    private List<Map<String, Object>> listItems = new ArrayList<Map<String, Object>>();
    List<NotDisturb> notDisturbList = new ArrayList<NotDisturb>();//免打扰数据
//    private String[] s = new String[]{"21:10-07:30", "09:00-12:00", "14:00-16:00"};//免打扰时段
    private String[] beginTimes = new String[]{"00:00", "00:00", "00:00"};//开始时段
    private String[] endTimes = new String[]{"00:00", "00:00", "00:00"};//结束时段
    private String[] on0ff = new String[]{"0", "0", "0"};//免打扰开关
    private String time, OnOff, beginTime, endTime;
    SharedPreferences sp;
    SharedPreferences.Editor editor;
    private String service_silence, URL, imei;
    RequestQueue queue;

    private SettingSwitchListener settingSwitchListener = new SettingSwitchListener() {
        @Override
        public void setSwitch(int position, int onOff) {
            setNotDisturbToNetWork(position, onOff);
            L.e("onOff==" + onOff + "  position==" + position);
        }
    };

    private LoadingDialog loadingDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_not_disturb);
        ButterKnife.inject(this);
        tvMiddle.setText(getResources().getString(R.string.not_disturb_title_text));
        ivLeft.setImageResource(R.drawable.title_goback_selector);
        ivLeft.setOnClickListener(this);

        loadingDialog = new LoadingDialog(this);
        //watch = getIntent().getParcelableExtra(SmartWatchListData.KEY);
        queue = Volley.newRequestQueue(getApplicationContext());
        Bundle bundle=getIntent().getExtras();
        service_silence = bundle.getString("service_silence");//读出数据
        URL = bundle.getString("URL");
        imei = bundle.getString("imei");
        sp = getApplicationContext().getSharedPreferences("NotDisturb", getApplicationContext().MODE_PRIVATE);// 创建对象
        editor = sp.edit();

        //getNotDisturbList();//从网络获取免打扰列表
        notDisturbLvContent.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (CMethod.isFastDoubleClick()) {
                    return;
                }
                openSetNotDisturbActivity(position);
            }
        });


    }


    /**
     * 进入设置
     *
     * @param position
     */
    public void openSetNotDisturbActivity(int position) {

        Intent intent = new Intent(NotDisturbActivity.this, SetNotDisturbActivity.class);
        Bundle mBundle = new Bundle();
        //mBundle.putParcelable(SmartWatchListData.KEY, watch);//传递宝贝信息
        mBundle.putInt("notDisturb_position", position);
        mBundle.putStringArray("beginTimes", beginTimes);
        mBundle.putStringArray("endTimes", endTimes);
        mBundle.putString("beginTime", notDisturbList.get(position).getBeginTime());
        mBundle.putString("endTime", notDisturbList.get(position).getEndTime());
        mBundle.putString("service_silence", service_silence);
        mBundle.putString("imei", imei);
        mBundle.putString("URL", URL);
        intent.putExtras(mBundle);
        startActivity(intent);
    }


    @Override
    protected void onResume() {
        super.onResume();
        refreshNotDisturbList();
//        adapter.notifyDataSetChanged();
    }

    /**
     * 刷新列表 先从数据库取，若没有，则进入网络取
     */
    private void refreshNotDisturbList() {
//        List<NotDisturb> notDisturbList = NotDisturbUtils.getNotDisTurbListFromDB(watch.getUser_id());
        notDisturbList.clear();
        for(int i = 0; i < beginTimes.length; i++) {
            NotDisturb notDisturb = new NotDisturb();
            int n = i + 1;
            beginTime = sp.getString("beginTime" + n, "");
            endTime = sp.getString("endTime" + n, "");
            OnOff = sp.getString("disturb_onoff" + n, "");
            notDisturb.setBeginTime("".equals(beginTime)? beginTimes[i] : beginTime);
            notDisturb.setEndTime("".equals(endTime)? endTimes[i] : endTime);
            notDisturb.setOnOFF(Integer.parseInt("".equals(OnOff)? on0ff[i] : OnOff));
            notDisturbList.add(notDisturb);
        }
        if (notDisturbList != null && notDisturbList.size() != 0) {
            setNotDisturbListView(notDisturbList);
        }
    }

    /**
     * 免打扰列表
     *
     * @param notDisturbs
     */
    private void setNotDisturbListView(List<NotDisturb> notDisturbs) {
        if (notDisturbs != null && notDisturbs.size() != 0) {
            if (adapter != null && adapter.notDisturbList.size() != 0) {
                adapter.notDisturbList = notDisturbs;
                adapter.notifyDataSetChanged();
            } else {
                adapter = new NotDisturbListAdapter(this, notDisturbs, settingSwitchListener);
                notDisturbLvContent.setAdapter(adapter);
            }
        }
    }

    /**
     * 从网络获取免打扰列表
     */
    private void getNotDisturbList() {

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("user_id", BaseApplication.getUserInfo().userID);//用户ID
            jsonObject.put("friend_id", watch.getUser_id());//手表ID
        } catch (JSONException e) {
            e.printStackTrace();
        }
        HttpUtils.PostDataToWeb(UrlAddressUrils.CODE_API, AppConstants.JAVA_WATCH_DISTURB_LIST_URL, jsonObject, new HttpClientListener() {
            @Override
            public void onSuccess(String result) {
                Gson gson = new Gson();
                NotDisturbData fromJson = gson.fromJson(result, NotDisturbData.class);
                if (fromJson == null)
                    return;
                List<NotDisturb> notDisturbs = fromJson.getDisturbList();
                if (notDisturbs == null)
                    return;
                for (int i = 0; i < notDisturbs.size(); i++) {
                    notDisturbs.get(i).setWatchId(watch.getUser_id());
                    notDisturbs.get(i).setId(watch.getUser_id() + "_" + notDisturbs.get(i).getNumber());
                }
                NotDisturbUtils.refreshDB(notDisturbs, watch.getUser_id());//有免打扰数据库则更新，无则创建
                //setNotDisturbListView(notDisturbs);
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
     * 向服务器post编辑免打扰数据
     */
    private void setNotDisturbToNetWork(final int position, final int onOff) {

        if (!CMethod.isNet(getApplicationContext())) {
            T.s(getResources().getString(R.string.no_network));
            return;
        }
        notDisturbList.get(position).setOnOFF(onOff);
        //以下为编辑当前闹钟的总指令
        String code_only;//单一指令
        String code_all = "";// 总指令
        for (int i = 0; i < notDisturbList.size(); i++) {
            code_only = notDisturbList.get(i).getBeginTime() + "-" + notDisturbList.get(i).getEndTime();
            if (notDisturbList.get(i).getOnOFF() == 0) {
                code_only = "00:00-00:00";
            }
            code_all = code_all + code_only + ",";
        }
        code_all = code_all.substring(0, code_all.length() - 1);//去掉最后的","
        L.e("clock_code_all===" + code_all);
        String silence = code_all;

        loadingDialog.show();
        String sign = Md5Util.stringmd5(imei, service_silence, silence);
        String path = URL + "service=" + service_silence + "&imei=" + imei + "&silence=" + silence + "&sign=" + sign;
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
                        editor.putString("disturb_onoff" + n, notDisturbList.get(position).getOnOFF() + "");//保存后默认打开开关
                        editor.commit();
                        setNotDisturbListView(notDisturbList);//更新数据表
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


    }

    private void dialogDismiss() {
        if (loadingDialog != null) {
            loadingDialog.dismissAndStopTimer();
        }

    }

    @Override
    public void onClick(View v) {
        if (CMethod.isFastDoubleClick()) {
            return;
        }
        switch (v.getId()) {
            case R.id.iv_left://返回键
                finish();
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();


    }
}
