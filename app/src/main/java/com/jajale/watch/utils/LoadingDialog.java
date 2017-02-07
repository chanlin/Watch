package com.jajale.watch.utils;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jajale.watch.AppConstants;
import com.jajale.watch.R;

import java.util.Timer;
import java.util.TimerTask;


public class LoadingDialog {
    private LayoutInflater mInflater;
    private MyDialog mDialog;
    private Activity mActivity;
    private static int DEFAULT_WIDTH = 100; // 默认宽度
    private static int DEFAULT_HEIGHT = 100;// 默认高度
    private final TextView loading_message;
    private Timer timer;
    private TimerTask timerTask;


    public LoadingDialog(Activity activity) {
        mActivity = activity;
        mInflater = LayoutInflater.from(activity);
        View view = mInflater.inflate(R.layout.layout_dialog_loading_no_dismiss, null);
        mDialog = new MyDialog(activity, 0, 0, view, R.style.Translucent_NoTitle);
        WindowManager.LayoutParams lp=mDialog.getWindow().getAttributes();
        lp.dimAmount=0.5f;
        mDialog.getWindow().setAttributes(lp);
        mDialog.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        loading_message = (TextView) view.findViewById(R.id.loading_message);
//        view.findViewById(R.id.delete_img).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                dismiss();
//            }
//        });
        mDialog.setCancelable(false);// 不可以用“返回键”取消
        mDialog.setCanceledOnTouchOutside(false);
        mDialog.setContentView(view, new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
    }
    public LoadingDialog(Activity activity,boolean isPosition) {
        mActivity = activity;
        mInflater = LayoutInflater.from(activity);
        View view = mInflater.inflate(R.layout.layout_dialog_loading_position, null);
        mDialog = new MyDialog(activity, 0, 0, view, R.style.Translucent_NoTitle);
        loading_message = (TextView) view.findViewById(R.id.loading_message);
//        view.findViewById(R.id.delete_img).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                dismiss();
//            }
//        });
        mDialog.setCancelable(false);// 不可以用“返回键”取消
        mDialog.setCanceledOnTouchOutside(false);
        mDialog.setContentView(view, new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
    }

    public void Cancelable(boolean cancelable){
        mDialog.setCancelable(cancelable);
    }

//    public LoadingDialog(Activity activity, boolean isCanDismiss) {
//        mActivity = activity;
//        mInflater = LayoutInflater.from(activity);
//        View view = mInflater.inflate(R.layout.layout_dialog_loading_no_dismiss, null);
//        mDialog = new MyDialog(activity, 0, 0, view, R.style.Translucent_NoTitle);
//
//        loading_message = (TextView) view.findViewById(R.id.loading_message);
//        ImageView view_dismiss = (ImageView) view.findViewById(R.id.delete_img);
//        if (!isCanDismiss) {
//            view_dismiss.setVisibility(View.GONE);
//
//        } else {
//            view_dismiss.setVisibility(View.VISIBLE);
//            view_dismiss.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    dismiss();
//                }
//            });
//        }
//
//        mDialog.setCancelable(false);// 不可以用“返回键”取消
//        mDialog.setCanceledOnTouchOutside(false);
//        mDialog.setContentView(view, new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
//    }


    public void show() {
        show(null);
    }

    public void show(String message) {
        if (mActivity != null && !mActivity.isFinishing()) {
            if (!CMethod.isEmpty(message)) {
                loading_message.setText(message);
            } else {
                loading_message.setText(R.string.loading_text);
            }
            try {
                mDialog.show();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void show(int messageId) {
        show(mActivity.getResources().getString(messageId));
    }

    public void show(String message, int secondsInt) {
        if (mActivity != null && !mActivity.isFinishing()) {
            if (!CMethod.isEmpty(message)) {
                loading_message.setText(message);
            } else {
                loading_message.setText(R.string.loading_text);
            }
            try {
                mDialog.show();
                timer = new Timer();
                timerTask = new TimerTask() {
                    @Override
                    public void run() {
                        mDialog.dismiss();
                    }
                };

                if (timer != null && timerTask != null)
                    timer.schedule(timerTask, secondsInt);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void showAndToast(String message,final String mmToastString) {
        if (mActivity != null && !mActivity.isFinishing()) {
            if (!CMethod.isEmpty(message)) {
                loading_message.setText(message);
            } else {
                loading_message.setText(R.string.loading_text);
            }
            try {
                mDialog.show();
                timer = new Timer();
                timerTask = new TimerTask() {
                    @Override
                    public void run() {
                        mActivity.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                T.s(mmToastString);
                            }
                        });
                        mDialog.dismiss();
                    }
                };

                if (timer != null && timerTask != null)
                    timer.schedule(timerTask, AppConstants.MaxIndex);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }




    public boolean isShowing() {
        return mDialog.isShowing();
    }

    public void dismiss() {

        if (mActivity != null && !mActivity.isFinishing() && mDialog.isShowing()) {
            mDialog.dismiss();
        }

    }

    public void dismissAndStopTimer() {

        if (mActivity != null && !mActivity.isFinishing() && mDialog.isShowing()) {
            mDialog.dismiss();
        }
        if (timer != null) {
            timer.cancel();
            timer = null;
        }

        if (timerTask != null) {
            timerTask.cancel();
            timerTask = null;
        }

    }

    class MyDialog extends Dialog {

        public MyDialog(Context context, View layout, int style) {
            this(context, DEFAULT_WIDTH, DEFAULT_HEIGHT, layout, style);
        }

        public MyDialog(Context context, int width, int height, View layout,
                        int style) {
            super(context, style);
            setContentView(layout);
            Window window = getWindow();
            WindowManager.LayoutParams params = window.getAttributes();
            params.gravity = Gravity.CENTER;
            window.setBackgroundDrawableResource(android.R.color.transparent);
            window.setAttributes(params);
        }

    }


}
