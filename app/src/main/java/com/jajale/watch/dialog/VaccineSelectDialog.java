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
public class VaccineSelectDialog extends Dialog implements View.OnClickListener {

    private SimpleClickListener sClickListener;
    private String mTitle;

    public VaccineSelectDialog(Context context, String title, SimpleClickListener listener){
        super(context);
//        this.mContext = context ;
        this.sClickListener = listener;
        this.mTitle = title;


        setContentView(R.layout.dialog_vaccine);
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
        ((TextView)findViewById(R.id.tv_dialog_title)).setText(mTitle);
        ((Button)findViewById(R.id.btn_cancel)).setText("已接种");
        ((Button)findViewById(R.id.btn_ok)).setText("未接种");

        findViewById(R.id.btn_cancel).setOnClickListener(this);
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
            case R.id.btn_ok:
                dismiss();
                if (sClickListener != null) {
                    sClickListener.ok();
                }
                break;
        }
    }


}
