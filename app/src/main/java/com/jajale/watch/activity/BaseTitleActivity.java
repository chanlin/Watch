package com.jajale.watch.activity;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.jajale.watch.BaseActivity;
import com.jajale.watch.R;
import com.jajale.watch.utils.L;

/**
 * Created by athena on 2015/12/4.
 * Email: lizhiqiang@bjjajale.com
 */
public class BaseTitleActivity extends BaseActivity {
    private TextView tv_left;
    private TextView tv_middle;
    private TextView tv_right;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setContentView(R.layout.activity_base_title);

        initView();
    }

    @Override
    public void setContentView(int layoutResID) {
        FrameLayout fl_container = (FrameLayout) findViewById(R.id.fl_container);
        LayoutInflater lInflater = LayoutInflater.from(getApplicationContext());
        if (null != fl_container) {
            fl_container.removeAllViews();
        }
        fl_container.addView(lInflater.inflate(layoutResID, null));
    }

    private void initView() {
        tv_left = (TextView) findViewById(R.id.tv_left_2);
        tv_middle = (TextView) findViewById(R.id.tv_middle);
        tv_right = (TextView) findViewById(R.id.tv_right);
    }

    protected void setTitleLeft(String str) {
        tv_left.setText(str);
    }

    protected void setTitleLeftBg(int resid) {
        tv_left.setBackgroundResource(resid);
    }

    protected void setTitleMiddle(String str) {
        L.e("123===tv_middle==="+str);
        tv_middle.setText(str);
    }

    protected void setTitleMiddle(int resid) {
        tv_middle.setText(getResources().getString(resid));
    }

    protected void setTitleRight(String str) {
        tv_right.setText(str);
    }

    protected void setTitleRightBg(int resid) {
        tv_right.setBackgroundResource(resid);
    }

    protected void setTitleRightClick(View.OnClickListener listener) {
        tv_right.setOnClickListener(listener);
    }

    protected void setTitleLeftClick(View.OnClickListener listener) {
        tv_left.setOnClickListener(listener);
    }
}
