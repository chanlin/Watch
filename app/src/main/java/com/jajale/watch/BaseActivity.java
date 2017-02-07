package com.jajale.watch;

import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

import com.jajale.watch.activity.HomeSecActivity;
import com.jajale.watch.dao.SqliteHelper;
import com.jajale.watch.entitydb.User;
import com.jajale.watch.manager.JJLActivityManager;
import com.jajale.watch.utils.CMethod;
import com.jajale.watch.utils.L;
import com.jajale.watch.utils.LoadingDialog;
import com.umeng.analytics.MobclickAgent;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;


/**
 * Created by athena on 2015/11/11.
 * Email: lizhiqiang@bjjajale.com
 */
public class BaseActivity extends FragmentActivity {

    protected boolean isDismiss = false;
    protected Timer timer;
    public static final int  KITKAT=19;//4.4

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        JJLActivityManager.getInstance().addActivity(this);
        setTheme(R.style.Theme_Animation_Activity_RightInRightOut);
/*//        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        // 4.4及以上版本开启
        int API_LEVEL=Build.VERSION.SDK_INT;
        if (API_LEVEL>= KITKAT) {
            setTranslucentStatus(true);
        }
        SystemBarTintManager tintManager = new SystemBarTintManager(this);
        tintManager.setStatusBarTintEnabled(true);
        tintManager.setNavigationBarTintEnabled(true);

        // 自定义颜色
//        tintManager.setTintColor(Color.GREEN);
//        tintManager.setNavigationBarTintResource(R.mipmap.nav_bar);
        tintManager.setTintResource(R.mipmap.nav_bar);*/
    }

/*    @SuppressLint("NewApi")
    private void setTranslucentStatus(boolean on) {
        Window win = getWindow();
        WindowManager.LayoutParams winParams = win.getAttributes();
        final int bits = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
        if (on) {
            winParams.flags |= bits;
        } else {
            winParams.flags &= ~bits;
        }
        win.setAttributes(winParams);
    }*/

    @Override
    protected void onStop() {
        super.onStop();
//        RequestManager.getInstance().cancelAll(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        JJLActivityManager.getInstance().removeActivity(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);

        new Thread(new Runnable() {
            @Override
            public void run() {
                if (HomeSecActivity.enterBackground) {
                    //从后台回来激活正常消息拉取

                }
            }
        }).start();

    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);

        new Thread(new Runnable() {
            @Override
            public void run() {

                if (isApplicationSentToBackground()) {
                    if (BaseApplication.getUserInfo() != null) {
                        //进入后台
                        HomeSecActivity.enterBackground = true;

                    }
                }
            }
        }).start();

    }

    public boolean isApplicationSentToBackground() {
        ActivityManager am = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> tasks = am.getRunningTasks(1);
        if (!tasks.isEmpty()) {
            ComponentName topActivity = tasks.get(0).topActivity;
            if (!topActivity.getPackageName().equals(getPackageName())) {
                return true;
            }
        }
        return false;
    }

    /**
     * 设置计时关闭dialog
     */
    public void setCountDownToDismissDialog(final LoadingDialog loadingDialog) {
        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                if (loadingDialog != null)
                    loadingDialog.dismiss();

            }
        }, 5000); //设置5秒的时长
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putSerializable("User_data", BaseApplication.getUserInfo());
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        if (savedInstanceState != null) {
            try {
                User user = (User) savedInstanceState.getSerializable("User_data");
                if (user != null) {
                    BaseApplication.setUserInfo(user);
                    if (!CMethod.isEmpty(user.userID)) {
                        SqliteHelper sqliteHelper = new SqliteHelper(user.userID);
                        BaseApplication.setBaseHelper(sqliteHelper);
                    }
                }

            } catch (Exception e) {

            }
        }
    }


    protected double parseDouble(String string) {

        if (!CMethod.isEmpty(string)) {
            return Double.parseDouble(string);
        }
        return 0;
    }

    protected void startMyActivity(Class<?> mClass) {
        startMyActivity(mClass, null);
    }

    protected void startMyActivity(Class<?> mClass, Bundle mBundle) {
        Intent intent = new Intent(this, mClass);
        if (mBundle != null) {
            intent.putExtras(mBundle);
        }
        startActivity(intent);
    }


    /**
     * 发送广播刷新watch列表
     */
    protected void sendRefreshWatchListReceiver() {
        Intent intent = new Intent();
        intent.setAction(BroadcastConstants.WATCH_LIST_REFRESH_RECEIVER);
        sendBroadcast(intent);
    }

    //发送socket通知
    protected void sendRefreshWatchListReceiver(String user_id) {
        L.e("socket1s");
        Intent intent = new Intent();
        intent.putExtra("user_id", user_id);
        intent.setAction(BroadcastConstants.SOCKET_TRUE_OR_FLASE_RECEIVER);
        sendBroadcast(intent);
    }
}
