package com.jajale.watch.activity;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.jajale.watch.BaseActivity;
import com.jajale.watch.R;
import com.jajale.watch.adapter.SeeTheWordFragmentAdapter;
import com.jajale.watch.utils.L;
import com.viewpagerindicator.TabPageIndicator;


/**
 * Created by athena on 16-5-23.
 * Email: guokaimin@bjjajale.com
 */
public class SeeTheWordActivity extends BaseActivity {
    ImageView ivLeft;
    TextView tvMiddle;

    private ViewPager viewPager;
    private TabPageIndicator indicator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        L.e("guokm", "called");
        setContentView(R.layout.activity_see_the_world);

        initView();

        initViewPager();
    }


    private void initView() {
        viewPager = (ViewPager) findViewById(R.id.pager);
        indicator = (TabPageIndicator) findViewById(R.id.indicator);
        tvMiddle = (TextView) findViewById(R.id.tv_middle);
        ivLeft = (ImageView) findViewById(R.id.iv_left);
        Intent intent = getIntent();
        if (intent.hasExtra("itemTitle")) {
            tvMiddle.setText(intent.getStringExtra("itemTitle"));
        } else {
            tvMiddle.setText(getResources().getString(R.string.see_the_word_title));
        }
        ivLeft.setImageResource(R.drawable.title_goback_selector);
        ivLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }


    private void initViewPager() {
        SeeTheWordFragmentAdapter fragmentAdapter = new SeeTheWordFragmentAdapter(SeeTheWordActivity.this, getSupportFragmentManager());
        viewPager.setAdapter(fragmentAdapter);
        viewPager.setOffscreenPageLimit(2);
        indicator.setViewPager(viewPager);
        viewPager.setScrollContainer(false);
    }

    @Override
    public void onBackPressed() {

        super.onBackPressed();
    }

    /**
     * 当横竖屏切换时会调用该方法
     * @author
     */
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        L.i("guokm", "=====<<<  onConfigurationChanged  >>>=====see");
        super.onConfigurationChanged(newConfig);

    }

}
