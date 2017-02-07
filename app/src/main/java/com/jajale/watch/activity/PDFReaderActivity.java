package com.jajale.watch.activity;

import android.app.Activity;
import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.jajale.watch.R;

import com.jajale.watch.fragment.PdfFragment;


/**
 * Created by selim_tekinarslan on 10.10.2014.
 */
public class PDFReaderActivity extends Activity {
    private static final String TAG = "PDFReaderActivity";
    private static final String SAMPLE_FILE = "demo.pdf";
    private static final String FILE_PATH = "filepath";
    private static final String SEARCH_TEXT = "text";
    private PdfFragment fragment;
    private static Context context;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pdf);
        TextView   midTitle = (TextView)findViewById(R.id.tv_middle);
        //左边按钮
        ImageView  iv_left = (ImageView)findViewById(R.id.iv_left);
        iv_left.setImageResource(R.drawable.title_goback_selector);

        //右边按钮
        midTitle.setText("阅读");
        iv_left.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        context = PDFReaderActivity.this;
        openPdfWithFragment();

    }




    public void openPdfWithFragment() {
        fragment = new PdfFragment();
        Bundle args = new Bundle();
//        args.putString(FILE_PATH, Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + SAMPLE_FILE);
        Intent intent=getIntent();
        String path=intent.getStringExtra(FILE_PATH);
        args.putString(FILE_PATH, path);
        fragment.setArguments(args);
        FragmentManager fragmentManager = getFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.content_frame, fragment).commit();
    }

}
