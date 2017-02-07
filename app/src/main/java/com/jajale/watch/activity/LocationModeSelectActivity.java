package com.jajale.watch.activity;

import android.content.SharedPreferences;
import android.os.Bundle;
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
import com.jajale.watch.R;
import com.jajale.watch.entity.SmartWatchListData;
import com.jajale.watch.entitydb.SmartWatch;
import com.jajale.watch.entityno.UMeventId;
import com.jajale.watch.listener.HttpClientListener;
import com.jajale.watch.utils.CMethod;
import com.jajale.watch.utils.HttpUtils;
import com.jajale.watch.utils.L;
import com.jajale.watch.utils.LoadingDialog;
import com.jajale.watch.utils.Md5Util;
import com.jajale.watch.utils.SmartWatchUtils;
import com.jajale.watch.utils.T;
import com.jajale.watch.utils.UrlAddressUrils;
import com.umeng.analytics.MobclickAgent;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * 定位模式选择
 * <p/>
 * Created by lilonghui on 2015/11/27.
 * Email:lilonghui@bjjajale.com
 */
public class LocationModeSelectActivity extends BaseActivity implements View.OnClickListener {


    //    1-正常 2-省电 3-高频(根据接口定义设置)
    private final int MODE_LOW = 60;
    private final int MODE_MID = 10;
    private final int MODE_HIGH = 1;

    private String imei, URL, service_mode, upmode;
    RequestQueue queue;

    private SmartWatch watch;

    SharedPreferences sp;
    SharedPreferences.Editor editor;

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
    @InjectView(R.id.location_mode_select_state_low)
    ImageView locationModeSelectStateLow;
    @InjectView(R.id.location_mode_select_rl_low)
    RelativeLayout locationModeSelectRlLow;
    @InjectView(R.id.location_mode_select_state_mid)
    ImageView locationModeSelectStateMid;
    @InjectView(R.id.location_mode_select_rl_mid)
    RelativeLayout locationModeSelectRlMid;
    @InjectView(R.id.location_mode_select_state_high)
    ImageView locationModeSelectStateHigh;
    @InjectView(R.id.location_mode_select_rl_high)
    RelativeLayout locationModeSelectRlHigh;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location_mode_select);
        ButterKnife.inject(this);
        tvMiddle.setText(getResources().getString(R.string.location_mode_title_text));
        ivLeft.setImageResource(R.drawable.title_goback_selector);
        ivLeft.setOnClickListener(this);

//        watch = getIntent().getParcelableExtra(SmartWatchListData.KEY);//获取intent传递过来的数据

        locationModeSelectRlLow.setOnClickListener(this);
        locationModeSelectRlMid.setOnClickListener(this);
        locationModeSelectRlHigh.setOnClickListener(this);

        loadingDialog = new LoadingDialog(this);

        sp = getApplicationContext().getSharedPreferences("NotDisturb", getApplicationContext().MODE_PRIVATE);// 创建对象
        editor = sp.edit();
        String upmode = sp.getString("upmode", "");
        if ("".equals(upmode)) {
            upmode = MODE_LOW + "";
        }
//        setModeSelectState(watch.getWork_mode());//初始状态，根据获取的结果判断
        setModeSelectState(Integer.parseInt(upmode));
        queue = Volley.newRequestQueue(getApplicationContext());
        Bundle bundle=getIntent().getExtras();
        service_mode = bundle.getString("service_mode");//读出数据
        URL = bundle.getString("URL");
        imei = bundle.getString("imei");

        MobclickAgent.onEvent(this, UMeventId.UMENG_POSITIONING_MODE_SETTING);


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
            case R.id.location_mode_select_rl_low://低频率选择
                loadingDialog.show(getResources().getString(R.string.send_instruct), AppConstants.MaxIndex);
                setLocationModeToNetWork(MODE_LOW);

                MobclickAgent.onEvent(this, UMeventId.UMENG_LOW_FREQUENCY_MODE_NUMBER);
                break;
            case R.id.location_mode_select_rl_mid://正常频率选择
                loadingDialog.show(getResources().getString(R.string.send_instruct), AppConstants.MaxIndex);
                setLocationModeToNetWork(MODE_MID);
                MobclickAgent.onEvent(this, UMeventId.UMENG_NORMAL_MODE_NUMBER);
                break;
            case R.id.location_mode_select_rl_high://高频率选择
                loadingDialog.show(getResources().getString(R.string.send_instruct), AppConstants.MaxIndex);
                setLocationModeToNetWork(MODE_HIGH);
                MobclickAgent.onEvent(this, UMeventId.UMENG_HIGH_FREQUENCY_MODE_NUMBER);
                break;
        }
    }

    /**
     * 从服务器设置定位模式
     */
    private void setLocationModeToNetWork(final int mode) {
        if (!CMethod.isNet(getApplicationContext())) {
            T.s(getResources().getString(R.string.no_network));
            return;
        }
        String sign = Md5Util.stringmd5(imei, service_mode, mode + "");
        String path = URL + "service=" + service_mode + "&imei=" + imei + "&upmode=" + mode + "&sign=" + sign;
        L.e("sign==" + path);
        StringRequest request = new StringRequest(path, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                loadingDialog.dismissAndStopTimer();
                setModeSelectState(mode);
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONObject jsondata = jsonObject.getJSONObject("data");
                    int code = jsondata.getInt("code");
                    if (code == 0) {
                        T.s(getResources().getString(R.string.send_code_success));
                        editor.putString("upmode", mode + "");
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


    /**
     * 切换选中状态
     *
     * @param mode_low 状态模式
     */
    private void setModeSelectState(int mode_low) {
        locationModeSelectStateLow.setVisibility(View.GONE);
        locationModeSelectStateLow.setImageResource(R.mipmap.radio_button_on);
        locationModeSelectStateMid.setVisibility(View.GONE);
        locationModeSelectStateMid.setImageResource(R.mipmap.radio_button_on);
        locationModeSelectStateHigh.setVisibility(View.GONE);
        locationModeSelectStateHigh.setImageResource(R.mipmap.radio_button_on);

        switch (mode_low) {
            case MODE_LOW://低频率选择
                locationModeSelectStateLow.setVisibility(View.VISIBLE);
                break;
            case MODE_MID://正常频率选择
                locationModeSelectStateMid.setVisibility(View.VISIBLE);
                break;
            case MODE_HIGH://高频率选择
                locationModeSelectStateHigh.setVisibility(View.VISIBLE);
                break;
        }
    }



    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {

        super.onPause();
    }


}
