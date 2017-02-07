package com.jajale.watch.activity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

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
import com.jajale.watch.entitydb.NotDisturb;
import com.jajale.watch.entitydb.SmartWatch;
import com.jajale.watch.listener.HttpClientListener;
import com.jajale.watch.listener.SingleStringListener;
import com.jajale.watch.utils.CMethod;
import com.jajale.watch.utils.DialogUtils;
import com.jajale.watch.utils.HttpUtils;
import com.jajale.watch.utils.L;
import com.jajale.watch.utils.LoadingDialog;
import com.jajale.watch.utils.Md5Util;
import com.jajale.watch.utils.NotDisturbUtils;
import com.jajale.watch.utils.T;
import com.jajale.watch.utils.UrlAddressUrils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * 设置免打扰列表
 * <p/>
 * Created by lilonghui on 2015/11/27.
 * Email:lilonghui@bjjajale.com
 */
public class SetNotDisturbActivity extends BaseActivity implements View.OnClickListener {
    private SmartWatch watch;
    private LoadingDialog loadingDialog;
    private String start_time;
    private String end_time;
    private int edit_position;
    private List<NotDisturb> notBidturbLists;
    private NotDisturb notDisturb;
    private int disturb;
    private String[] beginTimes, endTimes;
    SharedPreferences sp;
    SharedPreferences.Editor editor;
    RequestQueue queue;
    private String service_silence, URL, sign, imei, silence, beginTime, endTime;


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
    @InjectView(R.id.set_not_disturb_tv_start_time)
    TextView setNotDisturbTvStartTime;
    @InjectView(R.id.iv1)
    ImageView iv1;
    @InjectView(R.id.set_not_disturb_rl_start_time)
    RelativeLayout setNotDisturbRlStartTime;
    @InjectView(R.id.set_not_disturb_tv_end_time)
    TextView setNotDisturbTvEndTime;
    @InjectView(R.id.iv2)
    ImageView iv2;
    @InjectView(R.id.set_not_disturb_rl_end_time)
    RelativeLayout setNotDisturbRlEndTime;


    /**
     * 向服务器编辑免打扰信息
     */
    private void setNotDisturbToNetWork() {
        if (!CMethod.isNet(getApplicationContext())) {
            T.s(getResources().getString(R.string.no_network));
            return;
        }
        sign = Md5Util.stringmd5(imei, service_silence, silence);
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
                        editor.putString("beginTime" + disturb, start_time);
                        editor.putString("endTime" + disturb, end_time);
                        editor.putString("disturb_onoff" +disturb, 1 + "");
                        editor.commit();
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
                loadingDialog.dismiss();
            }
        });
        queue.add(request);
    }
    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_not_disturb);
        ButterKnife.inject(this);
        tvMiddle.setText(getResources().getString(R.string.not_disturb_set_disturb));
        ivLeft.setImageResource(R.drawable.title_goback_selector);
        ivLeft.setOnClickListener(this);
        //保存
        tvRight.setText(getResources().getString(R.string.save));
        tvRight.setOnClickListener(this);
        setNotDisturbRlStartTime.setOnClickListener(this);
        setNotDisturbRlEndTime.setOnClickListener(this);
        loadingDialog = new LoadingDialog(this);


        //watch = getIntent().getParcelableExtra(SmartWatchListData.KEY);//手表数据
        //edit_position = getIntent().getExtras().getInt("notDisturb_position", 0);//当前编辑的position

        //notBidturbLists = NotDisturbUtils.getNotDisTurbListFromDB(watch.getUser_id());//从数据库获取当前免打扰列表
        //notDisturb = notBidturbLists.get(edit_position);

        queue = Volley.newRequestQueue(getApplicationContext());
        Bundle bundle=getIntent().getExtras();
        edit_position = bundle.getInt("notDisturb_position");//读出数据
        beginTimes = bundle.getStringArray("beginTimes");
        endTimes = bundle.getStringArray("endTimes");
        beginTime = bundle.getString("beginTime");
        endTime = bundle.getString("endTime");
        service_silence = bundle.getString("service_silence");//读出数据
        URL = bundle.getString("URL");
        imei = bundle.getString("imei");

        sp = getApplicationContext().getSharedPreferences("NotDisturb", getApplicationContext().MODE_PRIVATE);// 创建对象
        editor = sp.edit();
        disturb = edit_position + 1;

        setNotDisturbTvStartTime.setText(beginTime);
        setNotDisturbTvEndTime.setText(endTime);
    }



    @Override
    public void onClick(View v) {

        if (CMethod.isFastDoubleClick()){
            return;
        }

        switch (v.getId())
        {
            case R.id.iv_left://返回键
                finish();
                break;
            case R.id.tv_right://保存
                start_time = setNotDisturbTvStartTime.getText().toString();
                end_time = setNotDisturbTvEndTime.getText().toString();

                if (!start_time.equals("")&&!end_time.equals(""))
                {
                    if (start_time.equals(end_time)){
                        Toast.makeText(SetNotDisturbActivity.this, getResources().getString(R.string.sametime), Toast.LENGTH_SHORT).show();
                    }else{
                        if (!CMethod.isNet(getApplicationContext())) {
                            T.s(getResources().getString(R.string.no_network));
                            return;
                        }
                        loadingDialog.show(getResources().getString(R.string.loading_text), AppConstants.MaxIndex);
                        //以下计算为编辑总指令
                        String code_only;//单一指令
                        String code_all = "";// 总指令
                        for (int i = 0; i < beginTimes.length; i++) {
                            int n = i + 1;
                            String start = sp.getString("beginTime" + n, "");
                            String end = sp.getString("endTime" + n, "");
                            String onoff = sp.getString("disturb_onoff" + n, "");
                            String startTime = "".equals(start)? beginTimes[i] : start;
                            String endTime = "".equals(end)? endTimes[i] : end;
                            if ("0".equals(onoff)) {
                                startTime = "00:00";
                                endTime = "00:00";
                            }
                            if (i == edit_position){//当条目为该编辑时，为当前编辑的时间
                                startTime = start_time;
                                endTime = end_time;
                            }
                            code_only = startTime + "-" + endTime;
                            code_all = code_all + code_only+ ",";
                        }
                        code_all = code_all.substring(0, code_all.length() - 1);
                        L.e("clock_code_all===" + code_all);
                        silence = code_all;

                        setNotDisturbToNetWork();
                    }

                }


                break;
            case R.id.set_not_disturb_rl_start_time://开始时间选择
                start_time = setNotDisturbTvStartTime.getText().toString();
                DialogUtils.selectTimeDialog(SetNotDisturbActivity.this, getResources().getString(R.string.starttime),start_time,new SingleStringListener() {
                    @Override
                    public void choiced(String result) {
                        setNotDisturbTvStartTime.setText(result);
                    }
                });

                break;
            case R.id.set_not_disturb_rl_end_time://结束时间选择
                end_time = setNotDisturbTvEndTime.getText().toString();
                DialogUtils.selectTimeDialog(SetNotDisturbActivity.this, getResources().getString(R.string.endtime),end_time,new SingleStringListener() {
                    @Override
                    public void choiced(String result) {
                        setNotDisturbTvEndTime.setText(result);
                    }
                });
                break;
        }
    }




    private void dialogDismiss() {
        if(loadingDialog!=null){
            loadingDialog.dismissAndStopTimer();
        }

    }

}