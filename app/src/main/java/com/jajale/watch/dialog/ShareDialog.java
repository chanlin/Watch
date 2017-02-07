package com.jajale.watch.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.jajale.watch.R;
import com.jajale.watch.listener.ShareListener;
import com.jajale.watch.utils.CMethod;
import com.jajale.watch.utils.T;

/**
 * Created by athena on 2015/11/20.
 * Email: lizhiqiang@bjjajale.com
 */
public class ShareDialog extends Dialog {
    private Context context;
    private  ShareListener listener;

    public ShareDialog(Context context) {
        this(context, R.style.Theme_Dialog_From_Bottom);
        // TODO Auto-generated constructor stub
    }

    public ShareDialog(Context context, ShareListener listener) {
        this(context, R.style.Theme_Dialog_From_Bottom);
        this.listener=listener;
        // TODO Auto-generated constructor stub
    }

    public ShareDialog(Context context, int theme) {
        super(context, theme);
        // TODO Auto-generated constructor stub
        this.context = context;
        init();
    }


    private void init() {
        this.setCanceledOnTouchOutside(true);
        this.setCancelable(true);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_share_bottom);

        initViews();
        initValues();
    }

    private void initViews() {
        findViewById(R.id.share_wx).setOnClickListener(clickListener);
        findViewById(R.id.share_wx_friend).setOnClickListener(clickListener);
        findViewById(R.id.share_sina).setOnClickListener(clickListener);

    }

    private void initValues() {
        // 不能写在init()中
        Window window = getWindow();
        WindowManager.LayoutParams lp = window.getAttributes();
        DisplayMetrics dm = context.getResources().getDisplayMetrics();
        lp.width = dm.widthPixels;//让dialog的宽占满屏幕的宽
        lp.gravity = Gravity.BOTTOM;//出现在底部
        window.setAttributes(lp);

    }

    View.OnClickListener clickListener = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            // TODO Auto-generated method stub

            if (!CMethod.isNet(context)) {
                T.s("请检查网络连接");
                return;
            }

            switch (v.getId()) {
                case R.id.share_wx://分享到微信
                    listener.shareWX();
                    dismiss();
                    break;
                case R.id.share_wx_friend://分享到微信朋友圈
                    listener.shareWXF();
                    dismiss();
                    break;

                case R.id.share_sina://分享到新浪微博
                    listener.shareSina();
                    dismiss();
                    break;

                default:
                    break;
            }
        }

    };


}


