package com.jajale.watch.cviews;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;

import com.jajale.watch.R;


public class ReaderGuidePopupWindow extends PopupWindow {

    private Button btn_cancel;
    private View mMenuView;
    private ImageView publish, my, search;


    @SuppressWarnings({"deprecation", "deprecation"})
    public ReaderGuidePopupWindow(Activity context) {
        super(context);
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mMenuView = inflater.inflate(R.layout.layout_reader_guide, null);

        RelativeLayout rl_all= (RelativeLayout) mMenuView.findViewById(R.id.rl_all);
        rl_all.setOnClickListener((OnClickListener) context);

        this.setContentView(mMenuView);
        this.setWidth(LayoutParams.FILL_PARENT);
        this.setHeight(LayoutParams.WRAP_CONTENT);
        this.setFocusable(true);
        ColorDrawable dw = new ColorDrawable(0x00000000);
        this.setBackgroundDrawable(dw);
        mMenuView.setFocusableInTouchMode(true);
        mMenuView.setOnKeyListener(new OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                // TODO Auto-generated method stub
                if (keyCode == KeyEvent.KEYCODE_MENU) {
                    dismiss();
                }
                return false;
            }
        });

    }

}
