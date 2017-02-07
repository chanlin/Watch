package com.jajale.watch.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

import com.jajale.watch.R;
import com.jajale.watch.listener.FamilyDialogListener;

/**
 * Created by athena on 2015/11/20.
 * Email: lizhiqiang@bjjajale.com
 */
public class FamilySettingDialog extends Dialog implements View.OnClickListener {


    public static int FAMILY_TYPE_ONE=1;
    public static int FAMILY_TYPE_TWO=2;
    private FamilyDialogListener sClickListener;
   private int mType;
    private  Context mContext;


    public FamilySettingDialog(Context context, int type, FamilyDialogListener listener){
        super(context);
        this.mContext = context ;
        this.sClickListener = listener;
        this.mType = type;

        setContentView(R.layout.dialog_family_setting);
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

        Button  btn_edit_relation= (Button) findViewById(R.id.edit_relation);//关系
        Button  btn_set_manger= (Button) findViewById(R.id.set_manger);//设为管理员
        Button  btn_delete_member= (Button) findViewById(R.id.delete_member);//删除联系人
        Button  btn_cancel= (Button) findViewById(R.id.btn_cancel);//取消

        if (mType==FAMILY_TYPE_ONE){
            btn_edit_relation.setVisibility(View.GONE);
            btn_set_manger.setVisibility(View.VISIBLE);
            btn_set_manger.setBackgroundResource(R.drawable.dialog_button_top_selector);
            btn_delete_member.setVisibility(View.VISIBLE);
            btn_delete_member.setBackgroundResource(R.drawable.item_selector);
        }else{
            btn_edit_relation.setVisibility(View.VISIBLE);
            btn_edit_relation.setBackgroundResource(R.drawable.dialog_button_top_selector);
            btn_set_manger.setVisibility(View.GONE);
            btn_delete_member.setVisibility(View.GONE);
        }
        btn_cancel.setBackgroundResource(R.drawable.dialog_button_bottom_selector);

        btn_edit_relation.setOnClickListener(this);
        btn_set_manger.setOnClickListener(this);
        btn_delete_member.setOnClickListener(this);
        btn_cancel.setOnClickListener(this);



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
            case R.id.edit_relation://编辑关系
                dismiss();
                if (sClickListener != null) {
                    sClickListener.onEditRelation();
                }
                break;
            case R.id.set_manger://设置管理员
                dismiss();
                if (sClickListener != null) {
                    sClickListener.onManger();
                }
                break;
            case R.id.delete_member://删除联系人
                dismiss();
                if (sClickListener != null) {
                    sClickListener.onDelete();
                }
                break;

        }
    }


}
