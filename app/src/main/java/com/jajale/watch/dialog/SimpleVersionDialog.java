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
public class SimpleVersionDialog extends Dialog implements View.OnClickListener {

    private SimpleClickListener sClickListener;
    private String dialogTitle = ""; //dialog 标题
    private CharSequence bodyUp = ""; // dialog文本1
    private String leftBtn = "";// 左按钮描述
    private String rightBtn = "";// 右按钮描述
    private int type;

    public SimpleVersionDialog(Context context, String title, CharSequence bodyUp, CharSequence bodyDown, String leftBtn, String rightBtn, int type, SimpleClickListener listener) {
        super(context);
        this.sClickListener = listener;
        this.dialogTitle = title;
        this.bodyUp = bodyUp;
        this.leftBtn = leftBtn;
        this.rightBtn = rightBtn;
        this.type = type;

        int layoutId =R.layout.dialog_version_simple_base;
        setContentView(layoutId);
        Window window = getWindow();
        WindowManager.LayoutParams params = window.getAttributes();
        params.gravity = Gravity.CENTER;
        window.setBackgroundDrawableResource(android.R.color.transparent);
        window.setAttributes(params);
        setCancelable(false);
        setCanceledOnTouchOutside(false);
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ((TextView) findViewById(R.id.tv_dialog_title)).setText(dialogTitle);
        ((TextView) findViewById(R.id.dialog_body_up)).setText(bodyUp);



        if (type == 0) {
            findViewById(R.id.iv_cancel).setOnClickListener(this);
            findViewById(R.id.iv_cancel).setVisibility(View.VISIBLE);
        }else{
            findViewById(R.id.iv_cancel).setVisibility(View.GONE);
            SimpleVersionDialog.this.setCancelable(false);
        }

        ((Button) findViewById(R.id.btn_ok)).setText(rightBtn);
        findViewById(R.id.btn_ok).setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_cancel:
                dismiss();
                if (sClickListener != null) {
                    sClickListener.cancle();
                }
                break;
            case R.id.iv_cancel:
                dismiss();
                if (sClickListener != null) {
                    sClickListener.cancle();
                }
                break;
            case R.id.btn_ok:
                if (type == 0)
                    dismiss();
                if (sClickListener != null) {
                    sClickListener.ok();
                }
                break;
        }
    }


}
