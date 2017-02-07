package com.jajale.watch.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.widget.ImageView;

import com.jajale.watch.BaseActivity;
import com.jajale.watch.BaseApplication;
import com.jajale.watch.IntentAction;
import com.jajale.watch.R;
import com.jajale.watch.dao.SqliteHelper;
import com.jajale.watch.entitydb.User;
import com.jajale.watch.message.SocketService;
import com.jajale.watch.utils.CMethod;
import com.jajale.watch.utils.L;
import com.jajale.watch.utils.LastLoginUtils;
import com.jajale.watch.utils.SplashPictureUtils;
import com.umeng.analytics.AnalyticsConfig;
import com.umeng.analytics.MobclickAgent;

/**
 * 启动页
 * <p/>
 * Created by athena on 2015/11/12.
 * Email: lizhiqiang@bjjajale.com
 */
public class SplashActivity extends BaseActivity {

    private ImageView iv_splash_root_bg;
    private String openTag = "";

    SharedPreferences sp;
    SharedPreferences.Editor editor;
    String imei, name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = new Intent(this, SocketService.class);
        startService(intent);
        if (!this.isTaskRoot()) { //判断该Activity是不是任务空间的源Activity，“非”也就是说是被系统重新实例化出来
            //如果你就放在launcher Activity中话，这里可以直接return了
            Intent mainIntent = getIntent();
            String action = mainIntent.getAction();
            if (mainIntent.hasCategory(Intent.CATEGORY_LAUNCHER) && action.equals(Intent.ACTION_MAIN)) {
                finish();
                return;//finish()之后该活动会继续执行后面的代码，你可以logCat验证，加return避免可能的exception
            }
        }

        setContentView(R.layout.activity_splash);
        L.i("splash activity oncreate !!");


        openTag = getIntent().getStringExtra(IntentAction.OPEN_SPLASH_TAB);
        //创建快捷图标
//        if (!CMethod.isExistShortCut(this)) {
//            CMethod.createShortcut(this);
//        }
        //Umeng 配置
        MobclickAgent.updateOnlineConfig(this);
        AnalyticsConfig.enableEncrypt(true);

        iv_splash_root_bg = (ImageView) findViewById(R.id.iv_splash_root_bg);
        SplashPictureUtils splashPictureUtils = new SplashPictureUtils(this);
        splashPictureUtils.setSplashBg(iv_splash_root_bg);

        sp = getApplicationContext().getSharedPreferences("NotDisturb", getApplicationContext().MODE_PRIVATE);// 创建对象
        editor = sp.edit();
        imei = sp.getString("IMEI", "");
        name = sp.getString("Telephone_Number", "");
        delayToHomePage();
    }


    public void delayToHomePage() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(2 * 1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                LastLoginUtils lastLoginUtils = new LastLoginUtils(SplashActivity.this);
//                if (!CMethod.isEmpty(lastLoginUtils.getPhone()) && lastLoginUtils.isLogin()) {
//                    String userID = lastLoginUtils.getUserId();
//                    if (CMethod.isEmpty(userID)) {
//                        openHomePageActivity();
//                    } else {
//                        SqliteHelper sqliteHelper = new SqliteHelper(userID);
//                        //初始化数据库
//                        BaseApplication.setBaseHelper(sqliteHelper);
//                        User user=new User();
//                        user.userID=userID;
//                        //初始化UserInfo
//                        BaseApplication.setUserInfo(user);
                        Intent intent = new Intent();
//                        intent.setClass(SplashActivity.this, HomeSecActivity.class);
                        if (!name.equals("")) {//判断储存的用户名、密码是否为空:为""登录界面，否则进入首页
                            intent.setClass(SplashActivity.this, HomeSecActivity.class);//跳转到首页
                        } else {
                            intent.setClass(SplashActivity.this, HomePageActivity.class);//跳转到登录界面
                        }
                        if (!CMethod.isEmpty(openTag)) {
                            intent.putExtra(IntentAction.OPEN_SPLASH_TAB, openTag);
                        }
                        startActivity(intent);
                        finish();
//                    }

//                } else {
//                    openHomePageActivity();
//                }

            }
        }).start();
    }


    private void openHomePageActivity() {
        Intent intent = new Intent();
        intent.setClass(SplashActivity.this, HomePageActivity.class);
        if (!CMethod.isEmpty(openTag)) {
            intent.putExtra(IntentAction.OPEN_SPLASH_TAB, openTag);
        }
        startActivity(intent);
        finish();
    }




    private void leave2Home() {
        leave2Home("", "");
    }

    private void leave2Home(String username, String password) {
        Intent intent = new Intent(SplashActivity.this, HomeSecActivity.class);
        intent.putExtra(IntentAction.OPEN_HONE_FROM, "splash");
        intent.putExtra(IntentAction.OPEN_HONE_ID, username);
        intent.putExtra(IntentAction.OPEN_HONE_PW, password);
        if (!CMethod.isEmpty(openTag)) {
            intent.putExtra(IntentAction.OPEN_SPLASH_TAB, openTag);
        }
        startActivity(intent);
        finish();

    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        L.e("splash activity onNewIntent !!");


    }

    @Override
    protected void onResume() {
        super.onResume();
//        L.e("splash activity onResume !!");
//        L.e("splashonResume"+getPhoneNumber()) ;
//        L.e("------");
    }

    private String getPhoneNumber() {
        TelephonyManager mTelephonyMgr;
        mTelephonyMgr = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        return mTelephonyMgr.getLine1Number();
    }
}
