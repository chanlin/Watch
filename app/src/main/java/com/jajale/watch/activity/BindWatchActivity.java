package com.jajale.watch.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.jajale.watch.BaseActivity;
import com.jajale.watch.IntentAction;
import com.jajale.watch.R;
import com.jajale.watch.fragment.DearDetailFragment;
import com.jajale.watch.listener.SimpleClickListener;
import com.jajale.watch.utils.CMethod;
import com.jajale.watch.utils.DialogUtils;
import com.jajale.watch.utils.LastLoginUtils;

/**
 * 绑定手表入口页面
 * <p/>
 * Created by lilonghui on 2015/11/16.
 * Email:lilonghui@bjjajale.com
 */
public class BindWatchActivity extends BaseActivity implements View.OnClickListener {

    public static Activity bindWatchActivity;

    String activityName="";
    String URL, user_id, service_bind;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bindwatch);
        bindWatchActivity = this;
        Intent intent=getIntent();
        activityName= intent.getStringExtra("activity_name");
        initTitleView();

        findViewById(R.id.bindwatch_btn_qrcode).setOnClickListener(this);
        findViewById(R.id.bindwatch_btn_imei).setOnClickListener(this);

        Bundle bundle=getIntent().getExtras();
        user_id = bundle.getString("user_id");//读出数据
        URL = bundle.getString("URL");
        service_bind = bundle.getString("service_bind");

    }

    /**
     * 初始化titleview
     */
    private void initTitleView() {
        View title = findViewById(R.id.title);
        TextView midTitle = (TextView) title.findViewById(R.id.tv_middle);
        midTitle.setText(getResources().getString(R.string.bindwatch_title_text));
        ImageView iv_left = (ImageView) title.findViewById(R.id.iv_left);

        TextView tvRight = (TextView) title.findViewById(R.id.tv_right);

        String className=getIntent().getStringExtra(IntentAction.KEY_CLASS_NAME);
        //  当从宝贝列表进来时，没有退出功能及加上返回键
        if (!CMethod.isEmpty(className)&&className.equals(DearDetailFragment.TAG)){
            iv_left.setImageResource(R.drawable.title_goback_selector);
            iv_left.setOnClickListener(this);
        }else{
            tvRight.setText(getResources().getString(R.string.quit_out));
            tvRight.setOnClickListener(this);
            tvRight.setTextColor(getResources().getColor(R.color.white));
        }

    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        //当表不为0，执行默认的方法babyInfoActivity
        if("babyInfoActivity".equals(activityName)){
            CMethod.ExitApp(BindWatchActivity.this);
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onClick(View v) {
        if (CMethod.isFastDoubleClick()){
            return;
        }
        switch (v.getId()) {
            case R.id.bindwatch_btn_qrcode://通过扫描二维码添加手表
                Intent openCameraIntent = new Intent(this, ScannerActivity.class);
                Bundle mBundle_bind1 = new Bundle();
                mBundle_bind1.putString("user_id", user_id);
                mBundle_bind1.putString("URL", URL);
                mBundle_bind1.putString("service_bind", service_bind);
                openCameraIntent.putExtras(mBundle_bind1);
                openCameraIntent.putExtra("from_tag","bind");
                startActivity(openCameraIntent);
                break;


            case R.id.bindwatch_btn_imei://通过输入IMEI号码添加手表
                Intent bind_watch_imei_Intent = new Intent(this, BindWatchOfIMEIActivity.class);
                Bundle mBundle_bind = new Bundle();
                mBundle_bind.putString("user_id", user_id);
                mBundle_bind.putString("URL", URL);
                mBundle_bind.putString("service_bind", service_bind);
                bind_watch_imei_Intent.putExtras(mBundle_bind);
                startActivity(bind_watch_imei_Intent);
                break;

            case R.id.iv_left://
                finish();
                break;
            case R.id.tv_right://退出
                DialogUtils.logOffDialog(this, new SimpleClickListener() {
                    @Override
                    public void ok() {
                        LastLoginUtils lastLoginUtils = new LastLoginUtils(BindWatchActivity.this);
                        lastLoginUtils.logout();

                        Intent intent = new Intent(BindWatchActivity.this, HomePageActivity.class);
                        if (HomeSecActivity.mActivity != null)
                            HomeSecActivity.mActivity.finish();
                        startActivity(intent);
                        finish();
                    }

                    @Override
                    public void cancle() {

                    }
                });
                break;

        }

    }


}
