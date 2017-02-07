package com.jajale.watch.dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.jajale.watch.R;
import com.jajale.watch.utils.DensityUtil;

/**
 * Created by athena on 2015/11/20.
 * Email: lizhiqiang@bjjajale.com
 */
public class MapUiSettingDialog extends Dialog  {

    private int checkedId;
    private RadioGroup.OnCheckedChangeListener listener;
    private RadioGroup pop_radiogroup;
//    private int marginTop ;
    private View close_btn;
    private int windowWidth;

    public MapUiSettingDialog(Context context, int checkedId, RadioGroup.OnCheckedChangeListener listener) {
        super(context);
        this.checkedId = checkedId;
        this.listener = listener;
    }

//    public MapUiSettingDialog(Context context, int checkedId, RadioGroup.OnCheckedChangeListener listener, int marginTop) {
//        super(context);
//        this.checkedId = checkedId;
//        this.listener = listener;
//        this.marginTop = marginTop;
//    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_map_ui_setting);

        Window dialogWindow = getWindow();
        dialogWindow.setGravity(Gravity.TOP);
        WindowManager.LayoutParams params = dialogWindow.getAttributes();
        params.width = WindowManager.LayoutParams.MATCH_PARENT;
        params.height = WindowManager.LayoutParams.MATCH_PARENT;
        dialogWindow.setAttributes(params);
        dialogWindow.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        DisplayMetrics dm = new DisplayMetrics();
        getWindow().getWindowManager().getDefaultDisplay().getMetrics(dm);
        windowWidth = dm.widthPixels;
        initView();
    }

    private void initView() {
        close_btn = findViewById(R.id.iv_cancel);
        close_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        findViewById(R.id.root).setOnClickListener(new View.OnClickListener() {//点击其他位置，关闭dialog
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });

        //设置radiogroup的drawabletop
        Resources resources = getContext().getResources();
        int width = DensityUtil.dip2px(getContext(), 96);
        int height = DensityUtil.dip2px(getContext(), 69);
        if (windowWidth<=480){
            width = (int)(width/1.125);
            height = (int)(height/1.125);
        }

        RadioButton uisettings_satellite_btn = (RadioButton) findViewById(R.id.uisettings_satellite_btn);
        Drawable drawable1 = resources.getDrawable(R.drawable.selector_map_satellite);
        drawable1.setBounds(0, 0, width, height);
        uisettings_satellite_btn.setCompoundDrawables(null,drawable1,null,null);

        RadioButton uisettings_2d_btn = (RadioButton) findViewById(R.id.uisettings_2d_btn);
        Drawable drawable2 = resources.getDrawable(R.drawable.selector_map_2d);
        drawable2.setBounds(0, 0, width, height);
        uisettings_2d_btn.setCompoundDrawables(null,drawable2,null,null);

        RadioButton uisettings_3d_btn = (RadioButton) findViewById(R.id.uisettings_3d_btn);
        Drawable drawable3 = resources.getDrawable(R.drawable.selector_map_3d);
        drawable3.setBounds(0, 0, width, height);
        uisettings_3d_btn.setCompoundDrawables(null,drawable3,null,null);


        pop_radiogroup = (RadioGroup)findViewById(R.id.radiogroup);
        pop_radiogroup.check(checkedId);
        pop_radiogroup.setOnCheckedChangeListener(listener);
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (pop_radiogroup!=null){
            //弹出动画
            pop_radiogroup.setPivotX(windowWidth- DensityUtil.dip2px(getContext(),32));
            pop_radiogroup.setPivotY(0);
            pop_radiogroup.setScaleX(0);
            pop_radiogroup.setScaleY(0);

            pop_radiogroup.animate().setDuration(200).scaleX(1).scaleY(1).start();
        }
    }

}
