package com.jajale.watch.dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;

import com.jajale.watch.R;
import com.jajale.watch.activity.NewsPaperActivityV2;

/**
 * Created by athena on 2016/3/15.
 * Email: lizhiqiang@bjjajale.com
 */
public class RecruitDialog extends Dialog implements View.OnClickListener {
    private Context mContext;
    private Button btn_ok;
    private ImageView btn_cancle;


    public RecruitDialog(Context context) {
        super(context, R.style.Dialog_Fullscreen);
        this.mContext = context;
        setContentView(R.layout.dialog_recruit_base);
        Window window = getWindow();
        WindowManager.LayoutParams params = window.getAttributes();
        params.gravity = Gravity.CENTER;
        window.setBackgroundDrawableResource(android.R.color.transparent);
        window.setAttributes(params);
        setCanceledOnTouchOutside(true);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        btn_ok = (Button) findViewById(R.id.btn_dialog_ok);
        btn_cancle = (ImageView) findViewById(R.id.btn_dialog_cancle);
        btn_ok.setOnClickListener(this);
        btn_cancle.setOnClickListener(this);


        btn_ok.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        btn_ok.setTextColor(mContext.getResources().getColor(R.color.white));
                        break;

                    case MotionEvent.ACTION_UP:
                        btn_ok.setTextColor(mContext.getResources().getColor(R.color.app_color));
                        break;

                }


                return false;
            }
        });


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_dialog_ok:
//                openNewsActivity("大河报", AppConstants.DAHEBAO_WEBVIEW_URL + BaseApplication.getUserInfo().userID);

                dismiss();
                break;
            case R.id.btn_dialog_cancle:
                dismiss();
                break;
        }
    }

    private void openNewsActivity(String title, String url) {
        Intent i = new Intent();
        i.setClass(mContext, NewsPaperActivityV2.class);
        i.putExtra("info_url", url);
        i.putExtra("info_title", title);
        mContext.startActivity(i);
    }

}
