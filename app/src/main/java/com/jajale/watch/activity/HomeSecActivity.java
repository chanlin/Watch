package com.jajale.watch.activity;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.KeyEvent;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.jajale.watch.AppConstants;
import com.jajale.watch.BaseApplication;
import com.jajale.watch.BaseWatchOrderActivity;
import com.jajale.watch.BroadcastConstants;
import com.jajale.watch.IntentAction;
import com.jajale.watch.PublicSwitch;
import com.jajale.watch.R;
import com.jajale.watch.adapter.FragmentTabAdapter;
import com.jajale.watch.cviews.BadgeView;
import com.jajale.watch.dialog.DownLoadDialog;
import com.jajale.watch.dialog.MoveDataDialog;
import com.jajale.watch.entity.AppInfo;
import com.jajale.watch.entity.AuthenticationData;
import com.jajale.watch.entity.HYReturnMessageListData;
import com.jajale.watch.entity.MoveData;
import com.jajale.watch.entity.ResultEntity;
import com.jajale.watch.entity.ReturnMessageData;
import com.jajale.watch.entity.ReturnMessageListData;
import com.jajale.watch.entity.SPKeyConstants;
import com.jajale.watch.entity.SmartWatchListData;
import com.jajale.watch.entitydb.MsgMember;
import com.jajale.watch.entitydb.SmartWatch;
import com.jajale.watch.fragment.DearDetailFragment;
import com.jajale.watch.fragment.DiscoverFragment;
import com.jajale.watch.fragment.Location2Fragment;
import com.jajale.watch.fragment.MsgFragment;
import com.jajale.watch.listener.HttpClientListener;
import com.jajale.watch.listener.ListListener;
import com.jajale.watch.listener.OnFinishListener;
import com.jajale.watch.listener.SimpleClickListener;
import com.jajale.watch.listener.VersionUpdateListener;
import com.jajale.watch.listener.VoiceDownloadListener;
import com.jajale.watch.manager.JJLActivityManager;
import com.jajale.watch.message.MySocketUtils;
import com.jajale.watch.utils.AccountUtils;
import com.jajale.watch.utils.CMethod;
import com.jajale.watch.utils.DateUtils;
import com.jajale.watch.utils.FileUtils;
import com.jajale.watch.utils.HttpUtils;
import com.jajale.watch.utils.L;
import com.jajale.watch.utils.LastLoginUtils;
import com.jajale.watch.utils.LoadingDialog;
import com.jajale.watch.utils.Md5Util;
import com.jajale.watch.utils.MessageUtils;
import com.jajale.watch.utils.MoveDataUtils;
import com.jajale.watch.utils.NotificationUtils;
import com.jajale.watch.utils.PhoneSPUtils;
import com.jajale.watch.utils.SmartWatchUtils;
import com.jajale.watch.utils.T;
import com.jajale.watch.utils.UrlAddressUrils;
import com.jajale.watch.utils.UserUtils;
import com.jajale.watch.utils.VersionUpdateUtils;
import com.jajale.watch.utils.WhiteLoadingDialog;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

//import com.jajale.watch.utils.ChildUtils;

/**
 * Created by lilonghui on 2015/11/13.
 * Email:lilonghui@bjjajale.com
 */
public class HomeSecActivity extends BaseWatchOrderActivity implements AMapLocationListener {
    public List<Fragment> fragments = new ArrayList<Fragment>();
    private RadioGroup rgs;
    private BadgeView badge0;
    private BadgeView badge1;
    private BadgeView badge2;
    private BadgeView badge3;
//    public static boolean isChangeWatchData = false;
    public static boolean enterBackground = true; // 程序进入后台
    private String openTab;
    private DownLoadDialog downLoadDialog;
    public static Activity mActivity;
    private PhoneSPUtils phoneSPUtils;
    private LoadingDialog loadingDialog;
    public boolean watchDataHasChanged = false;
    public boolean isbackgroundLogin = false;

    //    private int tab_position = 0;
    public FragmentTabAdapter tabAdapter;

    private int tab_index = 0;
    private DiscoverFragment discoverFragment;
    private Location2Fragment locationFragment;
    private DearDetailFragment dearDetailFragment;


    public static boolean refreshLocation = false;

    String service_message_list = "watch.get_new_message_list";//获取新微聊信息列表
    String user_id;
    String imei;
    String URL = "http://lib.huayinghealth.com/lib-x/?";
    SharedPreferences sp;
    SharedPreferences.Editor editor;
    RequestQueue queue;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //registerReceiver();
        initLocationOptions();
        openTab = getIntent().getStringExtra(IntentAction.OPEN_SPLASH_TAB);
        mActivity = this;
        phoneSPUtils = new PhoneSPUtils(HomeSecActivity.this);
        loadingDialog = new LoadingDialog(this);
        whiteLoadingDialog = new WhiteLoadingDialog(this);
        if (!CMethod.isEmpty(openTab)) {
            if (openTab.equals("notification")) {
                tab_index = 1;
            } else if (openTab.equals("DHBMessage")) {
                tab_index = 2;
            } else {
                tab_index = 0;
            }
        } else {
            tab_index = 0;
        }

        queue = Volley.newRequestQueue(getApplicationContext());
        sp = getApplicationContext().getSharedPreferences("NotDisturb", getApplicationContext().MODE_PRIVATE);// 创建对象
        editor = sp.edit();
        imei = sp.getString("IMEI", "");
        user_id = sp.getString("Telephone_Number", "");

        sendRefreshWatchListReceiver(user_id);//MySocketManager.java
        initView();


        JJLActivityManager.getInstance().killExceptItem(this);
        BaseApplication.setHomeActivity(this);
        NotificationUtils.cancelAll(this);
        allMoveUserData();

        //删除历史录音文件
        Deleteamrfile();
    }

    private void Deleteamrfile() {
        File f = new File("/storage/sdcard0/Android/data/com.jajale.watch/cache");
        if (f.exists()) {
            File[] fl = f.listFiles();

            for (int i=0; i< fl.length; i++) {
                if (fl[i].toString().endsWith(".amr") || fl[i].toString().endsWith(".jpg")) {
                    long time = fl[i].lastModified();
                    Date currentTime1 = new Date(time);//文件时间
                    SimpleDateFormat formatter1 = new SimpleDateFormat("yyyy-MM-dd");
                    String dateString1 = formatter1.format(currentTime1);

                    Date currentTime = new Date();//当前时间
                    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
                    String dateString = formatter.format(currentTime);

                    String data = getTwoDay(dateString, dateString1);//得到二个日期间的间隔天数

                    //L.e("当前时间:"+ dateString + " 文件时间:"+dateString1 + " 相隔天数:"+data);
                    if (data.equals("7")) {//删除三天前的音频文件
                        fl[i].delete();
                        //L.e("已删除：" + fl[i].toString());
                    }
                }
            }
        }

    }

    public static String getTwoDay(String sj1, String sj2) {//得到两个日期间的间隔天数
        SimpleDateFormat myFormatter = new SimpleDateFormat("yyyy-MM-dd");
        long day = 0;
        try {
            java.util.Date date = myFormatter.parse(sj1);
            java.util.Date mydate = myFormatter.parse(sj2);
            day = (date.getTime() - mydate.getTime()) / (24 * 60 * 60 * 1000);
        } catch (Exception e) {
            return "";
        }
        return day + "";
    }

    private BroadcastReceiver updateWatchListReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();

            if (action.equals(BroadcastConstants.WATCH_LIST_REFRESH_RECEIVER)) {
                refreshNetWorkWatchToFragment();
            }
        }
    };


    public void refreshNetWorkWatchToFragment() {
        String user_id = BaseApplication.getUserInfo().userID;
        if (!CMethod.isEmpty(user_id)) {
            SmartWatchUtils.getWatchListFromNetWork(user_id, new ListListener<SmartWatch>() {
                @Override
                public void onError(String message) {
                    refreshLocationFragment(null);
                }

                @Override
                public void onSuccess(List<SmartWatch> watches) {
                    new updateHomeDotTask().execute();

                    refreshDearDetailFragment(watches);
                    refreshLocationFragment(watches);
                    refreshDiscoverFragment(watches);


                }
            });
        }


    }

    private void registerReceiver() {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(BroadcastConstants.WATCH_LIST_REFRESH_RECEIVER);
        registerReceiver(updateWatchListReceiver, intentFilter);
    }


    private void initView() {
        {
            locationFragment = Location2Fragment.getInstance();//定位
            MsgFragment msgFragment = MsgFragment.getInstance();//消息
            //discoverFragment = DiscoverFragment.getInstance();//发现
            dearDetailFragment = DearDetailFragment.getInstance();//宝贝

            fragments.add(locationFragment);
            fragments.add(msgFragment);
            //fragments.add(discoverFragment);
            fragments.add(dearDetailFragment);


            rgs = (RadioGroup) findViewById(R.id.tabs_rg);
            L.e("123====tab_index====" + tab_index);
            tabAdapter = new FragmentTabAdapter(this, tab_index, fragments, R.id.tab_content,
                    rgs);

            badge0 = new BadgeView(this, findViewById(R.id.dot_one));
            badge1 = new BadgeView(this, findViewById(R.id.dot_two));
            //badge2 = new BadgeView(this, findViewById(R.id.dot_three));
            badge3 = new BadgeView(this, findViewById(R.id.dot_four));


            //选择某个tab监听
            tabAdapter
                    .setOnRgsExtraCheckedChangedListener(new FragmentTabAdapter.OnRgsExtraCheckedChangedListener() {
                        public void OnRgsExtraCheckedChanged(RadioGroup radioGroup, int checkedId, int index) {
                            switch (index) {

                                case 0:
                                    break;
                                case 1:
                                    break;
                                case 2:
                                    break;
                                case 3:
                                    break;
                                case 4:
                                    break;

                            }
                        }
                    });

        }
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
//land
        } else if (this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
//port
        }
    }

    private void refreshLocationFragment(List<SmartWatch> watches) {
        try {
            if (locationFragment != null) {
                locationFragment.refreshWatchList(watches);
            } else {
                L.e("123====locationFragment == null");
            }
        } catch (Exception e) {
            refreshLocation = true;
            L.e("123====locationFragment == Exception");
        }
    }

    private void refreshDiscoverFragment(List<SmartWatch> watches) {
        try {
            if (discoverFragment != null)
                discoverFragment.refreshWatchList(watches);
        } catch (Exception e) {
        }
    }

    private void refreshDearDetailFragment(List<SmartWatch> watches) {
        try {
            if (dearDetailFragment != null)
                dearDetailFragment.refreshWatchList(watches);
        } catch (Exception e) {
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);

        L.e("onNewIntent");
        fromUnkonw(intent);
    }


    /**
     * 别的地方条过来的
     *
     * @param intent
     */
    private void fromUnkonw(Intent intent) {

        if (IntentAction.GET_SYS_MSG_OK.equals(intent.getAction())) {
        }
        updateNewMessageCount(0);
    }


    public void updateNewMessageCount(int tag) {

    }

    /**
     * 从网络获取watch数据并刷新数据库
     */


    @Override
    protected void onResume() {
        super.onResume();

        L.e("234===homeSecActivity==onResume()");
        updateHomeDot();
    }


    @Override
    protected void onPause() {
        super.onPause();
        L.e("onPause()");
    }


    @Override
    protected void onStop() {
        super.onStop();
        L.e("__location___onStop()");
        if (BaseApplication.mapClient != null) {
            BaseApplication.mapClient.stopLocation();
            PublicSwitch.isPositionFirst = true;
        }
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        L.e("__location___onRestart()");
        if (BaseApplication.mapClient != null) {
            BaseApplication.mapClient.startLocation();
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

//        mMapView.onSaveInstanceState(outState);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        //unregisterReceiver(updateWatchListReceiver);
    }

    long firstTime = 0;

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK)) {
            long secondTime = System.currentTimeMillis();
            if (secondTime - firstTime > 2000) {
                Toast.makeText(HomeSecActivity.this, getResources().getString(R.string.exit_program), Toast.LENGTH_SHORT).show();
                firstTime = secondTime;// 更新firstTime
                return true;
            } else {
                CMethod.ExitApp(HomeSecActivity.this);
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    private class updateHomeDotTask extends AsyncTask<Void, Void, String[]> {
        @Override
        protected String[] doInBackground(Void... params) {
            try {
                Thread.sleep(800);
            } catch (InterruptedException e) {
            }
            return null;
        }

        @Override
        protected void onPostExecute(String[] result) {
            super.onPostExecute(result);
            updateHomeDot();
        }
    }

    private void updateHomeDot() {
        final int unreadCount = MessageUtils.getAllUnreadCount();
        L.e("123====unreadCount===" + unreadCount);

        HomeSecActivity.this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (unreadCount > 0) {
                    badge1.showNumber("" + unreadCount);
                } else {
                    badge1.hide();
                }
            }
        });
    }


    public static AMapLocationClient mapClient;
    private AMapLocationClientOption options;


    private void initLocationOptions() {

        mapClient = new AMapLocationClient(getApplicationContext());
        options = new AMapLocationClientOption();
        options.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
        options.setNeedAddress(true);
        options.setInterval(PublicSwitch.LOCATION_CALLBACK_FREQUENCY_HIGH);//设置请求时间间隔 单位毫秒

        mapClient.setLocationOption(options);
        mapClient.setLocationListener(this);
        mapClient.startLocation();

    }

    @Override
    public void onLocationChanged(AMapLocation aMapLocation) {
        //高德地图的回调
        //116.423205,40.037556
        L.e("333333333333333333333__lat_base_gaode==" + aMapLocation.getLatitude() + "__lon_base_gaode===" + aMapLocation.getLongitude());
        L.e("GaoDeMap===>>>" + aMapLocation.getAddress() + "<====>" + aMapLocation.getLongitude() + "<====>" + aMapLocation.getLatitude());

        if (aMapLocation.getLatitude() != 0) {
            L.e("GaoDeMap===============================000000000000");
            options.setInterval(PublicSwitch.LOCATION_CALLBACK_FREQUENCY_LOW);
        }
        L.e("GaoDeMap---isFirst==" + PublicSwitch.isPositionFirst);
//        if (!PublicSwitch.isPositionFirst) {
        AppInfo.getInstace().setLatitude(aMapLocation.getLatitude());
        AppInfo.getInstace().setLongitude(aMapLocation.getLongitude());
        AppInfo.getInstace().setAddrStr(aMapLocation.getAddress());
        Intent intent = new Intent();
        intent.setAction(BroadcastConstants.POSITION_REFRESH_RECEIVER);
        intent.putExtra("position_lat", aMapLocation.getLatitude());
        intent.putExtra("position_lon", aMapLocation.getLongitude());
        sendBroadcast(intent);
    }

    @Override
    public void onReceiveCode(String code) {
        super.onReceiveCode(code);

        if ("2".equals(code)) {//拉取最新消息
            uplateNewMsgMessagelist();
        }

        if ("3".equals(code)){//密码错误
            T.s(getResources().getString(R.string.phone_or_password_mistake));
        }
    }


    /**
     * 拉取最新消息
     */
    private void uplateNewMsgMessagelist() {
        String sign = Md5Util.stringmd5(imei, service_message_list, user_id);
        String url = URL + "service=" + service_message_list + "&imei=" + imei + "&user_id=" + user_id + "&sign=" + sign;
        L.e("sign===" + url);
        StringRequest request = new StringRequest(url, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Gson gson = new Gson();
                L.e("sign==response=="+response.toString());
                editor.putString("new_message_notice", "1");
                editor.commit();
                //写文件,日期命名的文件
                DateUtils data = new DateUtils(mActivity);
                String fileName = "huaying.dat";
                    FileUtils.writeString2File(mActivity,fileName,response.toString(),false);
                    //发广播
                    Intent intent = new Intent();
                    intent.setAction(BroadcastConstants.ACTION_MESSAGE_UPDATA);
                    sendBroadcast(intent);
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                loadingDialog.dismissAndStopTimer();
            }
        });
        queue.add(request);

    }

    //数据迁移-------------------------------------------------------------------------

    private WhiteLoadingDialog whiteLoadingDialog;
    private MoveDataDialog moveDataDialog;


    private void allMoveUserData() {

        LastLoginUtils lastLoginUtils = new LastLoginUtils(HomeSecActivity.this);
        if (!CMethod.isEmpty(lastLoginUtils.getPhone()) && lastLoginUtils.isLogin()) {
            boolean isNeedMove = phoneSPUtils.getBoolean("userDataCanMove"+lastLoginUtils.getPhone(), true);
            L.e("moveData====isneedMove==" + isNeedMove);
            if (isNeedMove) {
                L.e("moveData====判断迁移");
                getRegisterStateHasMoveData(lastLoginUtils.getPhone(), lastLoginUtils.getPassword());
            }else{

            }
        } else {

        }
    }

    private void moveData(final String user_name, final String pass) {
        whiteLoadingDialog.show("数据更新中...");

        final SimpleClickListener simpleClickListener = new SimpleClickListener() {
            @Override
            public void ok() {
                MoveDataUtils.callPhone(HomeSecActivity.this);
            }

            @Override
            public void cancle() {
                moveDataDialog.dismiss();
                moveData(user_name, pass);
            }
        };
        MoveDataUtils.moveData(user_name, pass, "", new HttpClientListener() {
            @Override
            public void onSuccess(String result) {
                whiteLoadingDialog.dismiss();
                Gson gson = new Gson();
                MoveData moveData = gson.fromJson(result, MoveData.class);
                if (moveData != null) {
                    int code = moveData.getCode();
                    if (code == 200) {
                        L.e("moveData====迁移完成，去自动登录");
                        //去自动登录
                        phoneSPUtils.save("userDataCanMove"+user_name, false);
                       // initCheckData();
                    } else {
                        showFialDialog("数据更新失败，请重试", "重试", "联系客服", simpleClickListener);
                    }
                }

            }

            @Override
            public void onFailure(String result) {
                whiteLoadingDialog.dismiss();
                showFialDialog("数据更新失败，请重试", "重试", "联系客服", simpleClickListener);
            }

            @Override
            public void onError() {
                whiteLoadingDialog.dismiss();
                showFialDialog("数据更新失败，请重试", "重试", "联系客服", simpleClickListener);
            }
        });


    }


    private void checkUserNoNetWork(final String user_name, final String passWord) {

        whiteLoadingDialog.dismiss();
        showFialDialog("无网络连接，请重试", "重试", "联系客服", new SimpleClickListener() {
            @Override
            public void ok() {
                MoveDataUtils.callPhone(HomeSecActivity.this);
            }

            @Override
            public void cancle() {
                moveDataDialog.dismiss();
                getRegisterStateHasMoveData(user_name, passWord);
            }
        });

    }


    /**
     * java检测是否注册，进行判断进入登录页面还是注册页面
     */
    private void getRegisterStateHasMoveData(final String user_name, final String passWord) {
        whiteLoadingDialog.show("数据更新中...");
        if (!CMethod.isNet(HomeSecActivity.this)) {
            checkUserNoNetWork(user_name, passWord);
            return;
        }
        final SimpleClickListener simpleClickListener = new SimpleClickListener() {
            @Override
            public void ok() {
                MoveDataUtils.callPhone(HomeSecActivity.this);
            }

            @Override
            public void cancle() {
                moveDataDialog.dismiss();
                getRegisterStateHasMoveData(user_name, passWord);
            }
        };
        MoveDataUtils.checkuser(user_name, new HttpClientListener() {
            @Override
            public void onSuccess(String result) {
                whiteLoadingDialog.dismiss();
                Gson gson = new Gson();
                MoveData moveData = gson.fromJson(result, MoveData.class);

                if (moveData != null) {
                    String data = moveData.getData();
                    int code = moveData.getCode();

                    if (code == 200) {

                        if (MoveDataUtils.CAN_MOVE.equals(data)) {
                            L.e("moveData====去迁移");
                            moveData(user_name, passWord);
                        } else {
                            L.e("moveData====不需要迁移，去自动登录");
                            phoneSPUtils.save("userDataCanMove"+user_name, false);
                           // initCheckData();
                        }

                    } else {
                       // openHomePageActivity();
                    }
                }

            }

            @Override
            public void onFailure(String result) {
                whiteLoadingDialog.dismiss();
                showFialDialog("数据更新失败，请重试", "重试", "联系客服", simpleClickListener);
            }

            @Override
            public void onError() {
                whiteLoadingDialog.dismiss();
                showFialDialog("数据更新失败，请重试", "重试", "联系客服", simpleClickListener);
            }
        });


    }


    private void showFialDialog(String title, String left, String right, SimpleClickListener listener) {
        moveDataDialog = new MoveDataDialog(HomeSecActivity.this, "提示", title, "", left, right, listener);
        moveDataDialog.show();
        moveDataDialog.setCancelable(false);
        moveDataDialog.setCanceledOnTouchOutside(false);
    }


}
