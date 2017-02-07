package com.jajale.watch.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.jajale.watch.R;
import com.jajale.watch.listener.SingleStringListener;

/**
 * Created by athena on 2015/11/19.
 * Email: lizhiqiang@bjjajale.com
 */
public class ChangeNickDialog extends Dialog implements View.OnClickListener {
    private Context mContext;
    //    private TextView tv_size;
    private EditText et_input_dialog_content;
    private SingleStringListener sClickListener;
    private TextView dialog_body_bottom;
    private String mHint;
    private String mWarn;
    private String mTitle;
    private TextView tv_dialog_title;

    public ChangeNickDialog(Context context, SingleStringListener listener, String title, String hint, String warn) {
        super(context);

        this.mContext = context;
        this.sClickListener = listener;
        this.mHint = hint;
        this.mWarn = warn;
        this.mTitle = title;

        setContentView(R.layout.change_nick_dialog);
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

        et_input_dialog_content = (EditText) findViewById(R.id.et_input_dialog_content);
        dialog_body_bottom = (TextView) findViewById(R.id.dialog_body_bottom);
        tv_dialog_title = (TextView) findViewById(R.id.tv_dialog_title);
//        tv_size = (TextView) findViewById(R.id.size);
        findViewById(R.id.btn_ok).setOnClickListener(this);
        findViewById(R.id.btn_cancel).setOnClickListener(this);
        dialog_body_bottom.setText(mWarn);
        et_input_dialog_content.setHint(mHint);
        tv_dialog_title.setText(mTitle);

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_cancel:
                dismiss();
                break;
            case R.id.btn_ok:
                String result = et_input_dialog_content.getText().toString().trim();

                if (result.replace(" ", "").equals(""))
                    Toast.makeText(mContext, "输入不能为空", Toast.LENGTH_SHORT).show();
                else if (!TextUtils.isEmpty(result)) {
                    dismiss();
                    if (sClickListener != null) {
                        sClickListener.choiced(et_input_dialog_content.getText().toString() + "");
                    }
                }

                break;
        }
    }
}
