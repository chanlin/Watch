package com.jajale.watch.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.jajale.watch.R;
import com.jajale.watch.cviews.AlarmPickerView;
import com.jajale.watch.listener.SingleStringListener;

import java.util.List;

/**
 * Created by athena on 2015/12/7.
 * Email: lizhiqiang@bjjajale.com
 */
public class StringDialog extends Dialog implements View.OnClickListener {
//    private PickerView number_picker_1;
    private AlarmPickerView number_picker_1;
    private SingleStringListener mListener;
    private List<String> mData;
    private String mTitle, mUnit, mSelected ;

    public StringDialog(Context context, List<String> data, int title, String unit, SingleStringListener listener) {
        this(context, data, context.getResources().getString(title), unit, listener);
    }

    public StringDialog(Context context, List<String> data, int title, SingleStringListener listener) {
        this(context, data, context.getResources().getString(title), null, listener);
    }

    public StringDialog(Context context, List<String> data, String title, SingleStringListener listener) {
        this(context, data, title, null, listener);
    }

    public StringDialog(Context context, List<String> data, String title, String unit, SingleStringListener listener) {
        super(context);

        this.mData = data;
        this.mTitle = title;
        this.mUnit = unit;
        this.mListener = listener;

        setContentView(R.layout.string_picker_dialog_layout);
        Window window = getWindow();
        WindowManager.LayoutParams params = window.getAttributes();
        params.gravity = Gravity.CENTER;
        window.setBackgroundDrawableResource(android.R.color.transparent);
        window.setAttributes(params);
        setCanceledOnTouchOutside(true);

    }

    public StringDialog(Context context, List<String> data,String current ,String title, String unit, SingleStringListener listener) {
        this(context,data,title,unit,listener);
        this.mSelected = current;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        TextView tv_dialog_title = (TextView) findViewById(R.id.tv_dialog_title);
        tv_dialog_title.setText(mTitle);
//        TextView tv_unit = (TextView) findViewById(R.id.tv_unit);
//        if (!TextUtils.isEmpty(mUnit)) {
//            tv_unit.setText(mUnit);
//        }

        number_picker_1 = (AlarmPickerView) findViewById(R.id.number_picker_1);
        findViewById(R.id.btn_ok).setOnClickListener(this);
//        findViewById(R.id.num_up_1).setOnClickListener(this);
//        findViewById(R.id.num_down_1).setOnClickListener(this);

        number_picker_1.setData(mData);

        if(!TextUtils.isEmpty(mSelected)){
            number_picker_1.setSelected(mSelected);
        }

    }

    public void setSelected(String selected) {
        this.mSelected = selected;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_ok:
                mListener.choiced(number_picker_1.getResult());
                dismiss();
                break;
//            case R.id.num_up_1:
//                number_picker_1.previous();
//                break;
//            case R.id.num_down_1:
//                number_picker_1.next();
//                break;
        }
    }



}
