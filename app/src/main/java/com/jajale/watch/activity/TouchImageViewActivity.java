package com.jajale.watch.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.jajale.watch.BaseActivity;
import com.jajale.watch.R;
import com.jajale.watch.image.JJLImageLoader;

/**
 * Created by llh on 16-4-13.
 */
public class TouchImageViewActivity extends BaseActivity {


    private ImageView imageview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_touch_imageview);
        imageview = (ImageView) findViewById(R.id.my_touch_imageview);
        String path = getIntent().getStringExtra("image_url_path");
        JJLImageLoader.download(TouchImageViewActivity.this, path,imageview);
       findViewById(R.id.iv_left).setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               finish();
           }
       });
        imageview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }
}
