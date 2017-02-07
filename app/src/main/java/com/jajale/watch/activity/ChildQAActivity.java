package com.jajale.watch.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.jajale.watch.BaseActivity;
import com.jajale.watch.R;
import com.jajale.watch.utils.CMethod;

//import android.support.v7.app.AppCompatActivity;


public class ChildQAActivity extends BaseActivity implements View.OnClickListener {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_childqa);
        initTitleView();
        findViewById(R.id.pre_school_btn).setOnClickListener(this);
        findViewById(R.id.post_school_btn).setOnClickListener(this);
    }

    /**
     * 初始化titleview
     */
    private void initTitleView() {
        View title = findViewById(R.id.title);
        TextView midTitle = (TextView) title.findViewById(R.id.tv_middle);
        Intent intent = getIntent();
        if (intent.hasExtra("itemTitle")) {
            midTitle.setText(intent.getStringExtra("itemTitle"));
        } else {
            midTitle.setText(getResources().getString(R.string.child_qa_title));
        }
        ImageView iv_left = (ImageView) title.findViewById(R.id.iv_left);

        iv_left.setImageResource(R.drawable.title_goback_selector);
        iv_left.setOnClickListener(this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onClick(View v) {
        Intent intent;
        if (CMethod.isFastDoubleClick()) {
            return;
        }

        switch (v.getId()) {
            case R.id.iv_left:
                finish();
                break;
            case R.id.pre_school_btn:
                intent = new Intent(this, PerSchoolActivity.class);
                startActivity(intent);
                break;
            case R.id.post_school_btn:
                intent = new Intent(this, PostSchoolActivity.class);
                startActivity(intent);
                break;
        }

    }
}
