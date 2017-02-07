package com.jajale.watch.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.jajale.watch.R;
import com.jajale.watch.listener.SimpleClickListener;

/**
 * Created by athena on 2015/11/20.
 * Email: lizhiqiang@bjjajale.com
 */
public class MoveDataDialog extends Dialog implements View.OnClickListener {

    private SimpleClickListener sClickListener;
    private String dialogTitle = "" ; //dialog 标题
    private CharSequence bodyUp = "" ; // dialog文本1
    private CharSequence bodyDown = "";// dialog文本2
    private String leftBtn = "" ;// 左按钮描述
    private String rightBtn = "";// 右按钮描述
//    private int laytouID = -1;


    public MoveDataDialog(Context context, String title, CharSequence bodyUp, CharSequence bodyDown, String leftBtn, String rightBtn, SimpleClickListener listener){
        super(context);
//        this.mContext = context ;
        this.sClickListener = listener;
        this.dialogTitle = title;
        this.bodyUp = bodyUp;
        this.bodyDown = bodyDown;
        this.leftBtn = leftBtn;
        this.rightBtn = rightBtn;
        setContentView(R.layout.dialog_simple_base);
        Window window = getWindow();
        WindowManager.LayoutParams params = window.getAttributes();
        params.gravity = Gravity.CENTER;
        window.setBackgroundDrawableResource(android.R.color.transparent);
        window.setAttributes(params);


    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ((TextView)findViewById(R.id.tv_dialog_title)).setText(dialogTitle);
        ((TextView)findViewById(R.id.dialog_body_up)).setText(bodyUp);
        if (bodyDown.equals("")){
            findViewById(R.id.dialog_body_bottom).setVisibility(View.GONE);
        }else{
            ((TextView)findViewById(R.id.dialog_body_bottom)).setText(bodyDown);
        }


        ((Button)findViewById(R.id.btn_cancel)).setText(leftBtn);
        ((Button)findViewById(R.id.btn_ok)).setText(rightBtn);

        findViewById(R.id.btn_cancel).setOnClickListener(this);
        findViewById(R.id.btn_ok).setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_cancel:
                if (sClickListener != null) {
                    sClickListener.cancle();
                }
                break;
            case R.id.btn_ok:
                if (sClickListener != null) {
                    sClickListener.ok();
                }
                break;
        }
    }


}
