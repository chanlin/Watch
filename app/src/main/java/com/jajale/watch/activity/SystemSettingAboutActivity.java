package com.jajale.watch.activity;

import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;

import com.jajale.watch.BaseActivity;
import com.jajale.watch.R;
import com.jajale.watch.utils.CMethod;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * 关于
 * <p/>
 * Created by lilonghui on 2015/12/5.
 * Email:lilonghui@bjjajale.com
 */
public class SystemSettingAboutActivity extends BaseActivity implements View.OnClickListener {

    @InjectView(R.id.iv_left)
    ImageView ivLeft;
    @InjectView(R.id.tv_middle)
    TextView tvMiddle;
    @InjectView(R.id.web_content)
    WebView web_content;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        ButterKnife.inject(this);
        //设置标题
        tvMiddle.setText(getResources().getString(R.string.system_setting_app_about));
        ivLeft.setImageResource(R.drawable.title_goback_selector);


        bindListener();

        web_content.loadUrl("http://health.tentinet.com/Mobile/About/us");
    }

    private void bindListener() {
        ivLeft.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        if (CMethod.isFastDoubleClick()){
            return;
        }
        switch (v.getId()) {

            case R.id.iv_left:
                finish();
                break;
        }

    }
}
