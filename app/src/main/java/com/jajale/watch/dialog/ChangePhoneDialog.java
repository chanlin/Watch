package com.jajale.watch.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;

import com.jajale.watch.R;
import com.jajale.watch.listener.SingleStringListener;
import com.jajale.watch.utils.T;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by athena on 2015/11/19.
 * Email: lizhiqiang@bjjajale.com
 */
public class ChangePhoneDialog extends Dialog implements View.OnClickListener {
    private Context mContext;
//    private TextView tv_size;
    private EditText et_input_dialog_content;
    private SingleStringListener sClickListener;
    private TextView dialog_body_bottom;
    private String mHint;
    private String mWarn;
    private String mTitle;
    private TextView tv_dialog_title;

    public ChangePhoneDialog(Context context, SingleStringListener listener, String title, String hint, String warn) {
        super(context);

        this.mContext = context;
        this.sClickListener = listener;
        this.mHint = hint;
        this.mWarn = warn;
        this.mTitle = title;

        setContentView(R.layout.change_phone_dialog);
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
        et_input_dialog_content.setFilters(new InputFilter[]{new InputFilter() {
            @Override
            public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
                if (dest.length() > 10) {
                    //这里可以写一些字数超过时弹出Toast
//                    T.s("您输入的字数过长");
                    return "";//长度超过了当前的字符就不要显示了，也就不返回了
                } else {
//                    tv_size.setText((dest.length() + 1) + "/20");
                    return source;//没超过就送给editText
                }
            }
        }});
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_cancel:
                dismiss();
                break;
            case R.id.btn_ok:
                String result = et_input_dialog_content.getText().toString();
                if (!TextUtils.isEmpty(result)) {

                    if (isPhoneNumber(result)){
                        dismiss();
                        if (sClickListener != null) {
                            sClickListener.choiced(et_input_dialog_content.getText().toString() + "");
                        }
                    }else{
                        T.s(mContext.getResources().getString(R.string.phone_not_correct));
                    }

                } else {
                    T.s(mContext.getResources().getString(R.string.ente_phone_number));
                }

                break;
        }
    }



    /**
     * 检查是否为有效的手机号码
     *
     * @param mobile
     * @return
     */
    public static boolean isPhoneNumber(String mobile) {

        String phoneRegex = "1[0-9]{10}";// 手机号格式正则表达式验证

        Pattern regex;
        Matcher matcher;

        regex = Pattern.compile(phoneRegex);
        matcher = regex.matcher(mobile);

        if (matcher.matches()) {
            return true;
        }

        return false;
    }

}
