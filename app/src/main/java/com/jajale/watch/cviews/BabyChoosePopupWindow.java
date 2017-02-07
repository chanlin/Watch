package com.jajale.watch.cviews;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnKeyListener;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.AlphaAnimation;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.PopupWindow;

import com.jajale.watch.R;
import com.jajale.watch.adapter.BabyChoosePopupWindowGirdViewAdapter;
import com.jajale.watch.entitydb.SmartWatch;

import java.util.List;


public class BabyChoosePopupWindow extends PopupWindow {

    //    private Button btn_cancel;
    private View mMenuView;
    boolean ismDismiss;
    private boolean showAdd = true;
    private final AlphaAnimation mHiddenAction;

    @SuppressWarnings({"deprecation", "deprecation"})
    public BabyChoosePopupWindow(final Activity context, int present_position, List<SmartWatch> watches, AdapterView.OnItemClickListener itemsOnClick, final ImageView view, final View viewBackground) {
        super(context);
        this.showAdd = showAdd;
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mMenuView = inflater.inflate(R.layout.layout_baby_dear_popup_window, null);

//        btn_cancel = (Button) mMenuView.findViewById(R.id.btn_cancel);

        mHiddenAction = new AlphaAnimation(2.0f, 0.1f);
        mHiddenAction.setDuration(500);
        GridView gridView = (GridView) mMenuView.findViewById(R.id.baby_choose_popup_window_gv);
        BabyChoosePopupWindowGirdViewAdapter adapter = new BabyChoosePopupWindowGirdViewAdapter(context, present_position, watches);
        gridView.setAdapter(adapter);

        gridView.setOnItemClickListener(itemsOnClick);

        this.setContentView(mMenuView);
        this.setWidth(LayoutParams.FILL_PARENT);
        this.setHeight(LayoutParams.WRAP_CONTENT);
        this.setFocusable(true);
        this.setAnimationStyle(R.style.DearPopupWindowAnimation);

        ColorDrawable dw = new ColorDrawable(0x00000000);
        this.setBackgroundDrawable(dw);
//        WindowManager.LayoutParams params=context.getWindow().getAttributes();
//        params.alpha=0.5f;
//        context.getWindow().setAttributes(params);
        mMenuView.setFocusableInTouchMode(true);
        mMenuView.setOnKeyListener(new OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                // TODO Auto-generated method stub
                if (keyCode == KeyEvent.KEYCODE_MENU) {
                    if (ismDismiss) {
                        dismiss();
                    }
                    ismDismiss = true;
                }
                return false;
            }
        });


        this.setOnDismissListener(new OnDismissListener() {
            @Override
            public void onDismiss() {
                view.setImageResource(R.mipmap.title_arrow_down);
                if (viewBackground != null) {
                    viewBackground.startAnimation(mHiddenAction);
                    viewBackground.setVisibility(View.GONE);
                }

            }
        });


    }

}
