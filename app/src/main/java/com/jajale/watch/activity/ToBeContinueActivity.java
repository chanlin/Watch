package com.jajale.watch.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.jajale.watch.BaseActivity;
import com.jajale.watch.R;

/**
 * Created by athena on 2016/2/19.
 * Email: lizhiqiang@bjjajale.com
 */
public class ToBeContinueActivity extends BaseActivity implements View.OnClickListener {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tobe_continue);

        String source = getIntent().getStringExtra("title");

        View title = findViewById(R.id.title);
        TextView midTitle = (TextView) title.findViewById(R.id.tv_middle);
        midTitle.setText(source);

        ImageView ivLeft = (ImageView) findViewById(R.id.iv_left);
        ivLeft.setImageResource(R.drawable.title_goback_selector);
        ivLeft.setOnClickListener(this);




    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.iv_left:
                Finish();
//                doSearch();



                break;
        }
    }




    private void Finish(){
        finish();

    }

}
