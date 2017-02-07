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
import android.widget.TextView;

import com.jajale.watch.R;


public class ReaderTitlePopupWindow extends PopupWindow {

    private Button btn_cancel;
    private View mMenuView;
    private ImageView publish, my, search;


    @SuppressWarnings({"deprecation", "deprecation"})
    public ReaderTitlePopupWindow(Activity context, OnClickListener onClick,String  title) {
        super(context);
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mMenuView = inflater.inflate(R.layout.layout_common_title, null);

        RelativeLayout layout_title= (RelativeLayout) mMenuView.findViewById(R.id.layout_title);
//        layout_title.setBackgroundColor(context.getResources().getColor(R.color.edu_title_bg));
        layout_title.setBackgroundResource(R.mipmap.nav_bar);

        TextView textView= (TextView) mMenuView.findViewById(R.id.tv_middle);
        textView.setText(title);

        ImageView iv_right= (ImageView) mMenuView.findViewById(R.id.iv_right);
        iv_right.setImageResource(R.mipmap.book_list);
        ImageView iv_left= (ImageView) mMenuView.findViewById(R.id.iv_left);
        iv_left.setImageResource(R.drawable.title_goback_selector);

        iv_left.setOnClickListener(onClick);
        iv_right.setOnClickListener(onClick);
        this.setContentView(mMenuView);
        this.setWidth(LayoutParams.FILL_PARENT);
        this.setHeight(LayoutParams.WRAP_CONTENT);
        this.setFocusable(true);
        this.setAnimationStyle(R.style.PopupWindowAnimation);
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
