package com.jajale.watch.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jajale.watch.AppConstants;
import com.jajale.watch.BaseActivity;
import com.jajale.watch.BaseApplication;
import com.jajale.watch.CodeConstants;
import com.jajale.watch.R;
import com.jajale.watch.dialog.DownLoadDialog;
import com.jajale.watch.entity.InstructionData;
import com.jajale.watch.entity.SmartWatchListData;
import com.jajale.watch.entitydb.SmartWatch;
import com.jajale.watch.factory.GsonFactory;
import com.jajale.watch.listener.HttpClientListener;
import com.jajale.watch.listener.SimpleClickListener;
import com.jajale.watch.listener.VersionUpdateListener;
import com.jajale.watch.utils.CMethod;
import com.jajale.watch.utils.DialogUtils;
import com.jajale.watch.utils.HttpUtils;
import com.jajale.watch.utils.L;
import com.jajale.watch.utils.LastLoginUtils;
import com.jajale.watch.utils.LoadingDialog;
import com.jajale.watch.utils.NotificationUtils;
import com.jajale.watch.utils.PhoneSPUtils;
import com.jajale.watch.utils.T;
import com.jajale.watch.utils.VersionUpdateUtils;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * 系统设置
 * <p/>
 * Created by lilonghui on 2015/12/5.
 * Email:lilonghui@bjjajale.com
 */
public class SystemSettingsActivity extends BaseActivity implements View.OnClickListener {

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

    @InjectView(R.id.system_setting_question)
    LinearLayout systemSettingQuestion;
    @InjectView(R.id.system_setting_app_update)
    LinearLayout systemSettingAppUpdate;
    @InjectView(R.id.system_setting_app_about)
    LinearLayout systemSettingAppAbout;
    @InjectView(R.id.system_setting_button_logoff)
    Button systemSettingButtonLogoff;
    @InjectView(R.id.system_setting_feedback)
    LinearLayout systemSettingFeedback;


    private boolean isDestroy = false;
    private PhoneSPUtils phoneSPUtils;


    private SmartWatch watch;
    private LoadingDialog loadingDialog;
    private BaseApplication baseApplication;

    SharedPreferences sp;
    SharedPreferences.Editor editor, editor1;
    String name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_system_setting);
        baseApplication = (BaseApplication)getApplication();
        loadingDialog = new LoadingDialog(this);
//        baseApplication = (BaseApplication) getApplication();
        ButterKnife.inject(this);
        //设置标题
        tvMiddle.setText(getResources().getString(R.string.system_setting_title_text));
        ivLeft.setImageResource(R.drawable.title_goback_selector);
        phoneSPUtils = new PhoneSPUtils(SystemSettingsActivity.this);

        bindListener();
        watch = getIntent().getParcelableExtra(SmartWatchListData.KEY);

        sp = getApplicationContext().getSharedPreferences("NotDisturb", getApplicationContext().MODE_PRIVATE);// 创建对象
        editor = sp.edit();
    }


    private void bindListener() {
        ivLeft.setOnClickListener(this);
        systemSettingFeedback.setOnClickListener(this);
        systemSettingQuestion.setOnClickListener(this);
        systemSettingAppUpdate.setOnClickListener(this);
        systemSettingAppAbout.setOnClickListener(this);
        systemSettingButtonLogoff.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        if (CMethod.isFastDoubleClick()) {
            return;
        }
        switch (v.getId()) {

            case R.id.iv_left:
                finish();
                break;
            case R.id.system_setting_feedback://意见反馈

//
                Intent intent_feedback = new Intent(SystemSettingsActivity.this, SystemSettingFeedbackActivity.class);
                startActivity(intent_feedback);


                break;
            case R.id.system_setting_question://常见问题

                //getInstructionFormNetwork();

                break;
            case R.id.system_setting_app_update://检查更新
                VersionUpdateUtils.update(SystemSettingsActivity.this, new VersionUpdateListener() {

                    @Override
                    public void next() {
                    }

                    @Override
                    public void downLoad(String url) {
                        DownLoadDialog downLoadDialog = new DownLoadDialog(SystemSettingsActivity.this, url);
                        downLoadDialog.show();
                    }

                    @Override
                    public void showToast(String message) {
                        T.s(message);
                    }
                });
                break;
            case R.id.system_setting_app_about://关于华英智联
                Intent intent_about = new Intent(SystemSettingsActivity.this, SystemSettingAboutActivity.class);
                startActivity(intent_about);

                break;
            case R.id.system_setting_button_logoff://退出登录

                L.e("退出登录 被点击");

                editor.putString("Telephone_Number2", sp.getString("Telephone_Number",""));
                editor.putString("Telephone_Number", "");
                editor.putString("update", "");
                editor.commit();
                //清除listview数据
                SharedPreferences sp1 = getSharedPreferences("file1", MODE_PRIVATE);
                editor1 = sp1.edit();
                editor1.putString("num","");
                editor1.commit();

                Intent intent = new Intent(SystemSettingsActivity.this, HomePageActivity.class);
//                Intent intent = new Intent(SystemSettingsActivity.this, HomePageActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                if (HomeSecActivity.mActivity != null)
                    HomeSecActivity.mActivity.finish();//清除首页不回退
                        startActivity(intent);
                        finish();

                break;


        }

    }


    private void openNewsActivityV2(String url, String title) {
        Intent i = new Intent();
        i.setClass(SystemSettingsActivity.this, NormalWebViewActivity.class);
        i.putExtra("info_url", url);
        i.putExtra("info_title", title);
        startActivity(i);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        isDestroy = true;
    }


    /**
     * 获取问题列表并保存
     */
    private void getInstructionFormNetwork() {


        loadingDialog.show();
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("imei", watch.getWatch_imei_binded());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        HttpUtils.PostDataToWeb(AppConstants.JAVA_GET_ENTRANCE_URL, CodeConstants.GET_INSTRUCTIONS_CODE, jsonObject, new HttpClientListener() {
            @Override
            public void onSuccess(String result) {
                loadingDialog.dismiss();
                InstructionData data = GsonFactory.newInstance().fromJson(result, InstructionData.class);
                openNewsActivityV2(data.getReq_url(), data.getMo_name());
            }

            @Override
            public void onFailure(String result) {
                loadingDialog.dismiss();
                T.s(result);
            }

            @Override
            public void onError() {
                loadingDialog.dismiss();
            }
        });


    }


}
