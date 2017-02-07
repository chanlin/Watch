package com.jajale.watch.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.jajale.watch.BaseActivity;
import com.jajale.watch.R;
import com.jajale.watch.fragment.IndexPostSchoolFragment;

//import android.support.v7.app.AppCompatActivity;


public class PostSchoolActivity extends BaseActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perschool);
        initTitleView();


        getSupportFragmentManager().beginTransaction()
                .add(R.id.ll, new IndexPostSchoolFragment()).commit();
    }




    /**
     * 初始化titleview
     */
    private void initTitleView() {
        TextView midTitle = (TextView)findViewById(R.id.tv_middle);
        midTitle.setText(getResources().getString(R.string.child_qa_title));
        ImageView iv_left = (ImageView)findViewById(R.id.iv_left);

        iv_left.setImageResource(R.drawable.title_goback_selector);
        iv_left.setOnClickListener(this);
    }
    @Override
    public void onClick(View view) {
        if(view.getId()==R.id.iv_left){
            finish();
        }
    }
}
