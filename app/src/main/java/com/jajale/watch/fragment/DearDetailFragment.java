package com.jajale.watch.fragment;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Cache;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.jajale.watch.AppConstants;
import com.jajale.watch.BaseApplication;
import com.jajale.watch.BroadcastConstants;
import com.jajale.watch.IntentAction;
import com.jajale.watch.R;
import com.jajale.watch.activity.AlarmClockActivity;
import com.jajale.watch.activity.BabyInfoActivity;
import com.jajale.watch.activity.BindWatchActivity;
import com.jajale.watch.activity.EducationActivity;
import com.jajale.watch.activity.FamilyMemberActivity;
import com.jajale.watch.activity.LocationModeSelectActivity;
import com.jajale.watch.activity.NotDisturbActivity;
import com.jajale.watch.activity.SOSSettingActivity;
import com.jajale.watch.activity.SystemSettingsActivity;
import com.jajale.watch.activity.TelephoneSettingActivity;
import com.jajale.watch.cviews.BabyChoosePopupWindow;
import com.jajale.watch.entity.SmartWatchListData;
import com.jajale.watch.entitydb.SmartWatch;
import com.jajale.watch.entityno.UMeventId;
import com.jajale.watch.interfaces.CreateSuccessInterface;
import com.jajale.watch.listener.HttpClientListener;
import com.jajale.watch.listener.ListListener;
import com.jajale.watch.listener.SimpleClickListener;
import com.jajale.watch.message.MySocketUtils;
import com.jajale.watch.utils.CMethod;
import com.jajale.watch.utils.DialogUtils;
import com.jajale.watch.utils.HttpUtils;
import com.jajale.watch.utils.L;
import com.jajale.watch.utils.LastLoginUtils;
import com.jajale.watch.utils.LoadingDialog;
import com.jajale.watch.utils.Md5Util;
import com.jajale.watch.utils.NotificationUtils;
import com.jajale.watch.utils.SmartWatchUtils;
import com.jajale.watch.utils.T;
import com.jajale.watch.utils.UrlAddressUrils;
import com.umeng.analytics.MobclickAgent;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

/**
 * 宝贝fragment 含有某个宝贝的详细内容，及可切换功能
 * <p/>
 * Created by athena on 2015/11/13.
 * Email: lizhiqiang@bjjajale.com
 */
public class DearDetailFragment extends BaseFragment implements View.OnClickListener {


    private int layouts[] = {
//            R.id.watch_tools_education,
//            R.id.watch_tools_insurance,
            R.id.watch_tools_alarm,
            R.id.watch_tools_find_watch,
            R.id.watch_tools_not_disturb,
            R.id.watch_tools_sos,
            R.id.watch_tools_telophone,
            R.id.watch_tools_family,
            R.id.watch_tools_pedometer,
            R.id.watch_tools_find_location,
            R.id.watch_tools_remote_shutdown};
//    private String titles[] = {
////            "在线教育",
////            "领取儿童意外、重疾保险",
//            "闹钟",
//            "找手表",
//            "免打扰模式",
//            "SOS设置",
//            "手表电话薄",
//            "家庭成员",
//            "奖励小红花",
//            "定位模式",
//            "远程关机"};
    private int titles[] = {
            R.string.alarmclock,
            R.string.findwatch,
            R.string.notdisturb_mode,
            R.string.sos,
            R.string.watchtellphonebook,
            R.string.familymembers,
            R.string.Rewardsflower,
            R.string.locationmode,
            R.string.Remote_Shutdown
    };


    private int icon_drawables[] = {
//            R.mipmap.icon_watch_tools_education,
//            R.mipmap.icon_watch_tools_insurance,
            R.mipmap.icon_watch_tools_alarm,
            R.mipmap.icon_watch_tools_find_watch,
            R.mipmap.icon_watch_tools_not_disturb,
            R.mipmap.icon_watch_tools_sos,
            R.mipmap.icon_watch_tools_telephone,
            R.mipmap.icon_watch_tools_family,
            R.mipmap.icon_watch_tools_pedometer,
            R.mipmap.icon_watch_tools_find_location,
            R.mipmap.icon_watch_tools_remote_shutdown};

    private ImageView pedometer_checkbox;
    public static final String TAG = "DearDetailFragment";
    private CreateSuccessInterface createSuccessInterface;
    public LoadingDialog loadingDialog;
    private List<SmartWatch> watches;
    private SmartWatch watch;
    private BabyChoosePopupWindow menuWindow;
    private int present_position = 0;
    private int ever_position = -1;
    private TextView midTitle;
    private ImageView iv_left;
    private ImageView iv_right;
    private ImageView dear_head_view;
    private TextView dear_tv_name;
    private ImageView dear_iv_battery;
    private MySocketUtils socketUtils;
    private LastLoginUtils lastLoginUtils;
    private Button watch_tools_btn_remove_bind;
    private View layout_insurance;
    private ScrollView dearScrollView;
    private RelativeLayout rl_baby_root;
    private String send_code_success;
    private String uesr_id;

    private BaseApplication baseApplication;
    private View view_title;
    private View view_background;
    private ImageView title_arrow_iv;
    private LinearLayout title_ll_middle;
    private AlphaAnimation mShowAction;
    private View rootView;

    String service_find = "watch.find_dev";//寻找手表
    String service_shutdown = "watch.shutdown";//关机
    String service_sosnumber = "watch.set_sos_number";//SOS
    String service_contact_a = "watch.set_contact_a";//设置电话本前五个
    String service_contact_b = "watch.set_contact_b";//设置电话本前五个
    String service_mode = "watch.set_upload_mode";//定位模式
    String service_alarm = "watch.set_alarm";//设置闹钟
    String service_silence = "watch.set_silence";//设置免打扰时间
    String service_add_honor = "watch.add_honor";//添加小红花
    String service_clear_honor = "watch.clear_honor";//清零小红花；
    String service_bind = "watch.bind_watch";//绑定手表
    String service_unbind = "watch.unbind_watch";//解绑手表
    String user_id;
    String imei;
    String sign = "";
    String URL = "http://lib.huayinghealth.com/lib-x/?";
    SharedPreferences sp;
    SharedPreferences.Editor editor;

    public DearDetailFragment() {
    }

    public static DearDetailFragment getInstance() {
        DearDetailFragment fragment = new DearDetailFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        socketUtils = new MySocketUtils(getActivity());
        lastLoginUtils = new LastLoginUtils(getActivity());

        sp = getActivity().getSharedPreferences("NotDisturb", getActivity().MODE_PRIVATE);// 创建对象
        editor = sp.edit();
        imei = sp.getString("IMEI", "");
        user_id = sp.getString("Telephone_Number", "");

    }

    RequestQueue queue;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_dear_detail, container, false);
        queue = Volley.newRequestQueue(getActivity());
        baseApplication = (BaseApplication) getActivity().getApplication();
        present_position = baseApplication.getPresent_position();
        if (createSuccessInterface != null) {
            createSuccessInterface.createSuccess();
        }
        return rootView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        List<SmartWatch> watches = SmartWatchUtils.getWatchList();
        setBabyData(watches);
        loadingDialog = new LoadingDialog(getActivity());
        send_code_success = getActivity().getResources().getString(R.string.send_code_success2);

        initView(view);
        setView(view);

    }

    boolean isChange = true;


    @Override
    public void onResume() {
        super.onResume();
        if (present_position != baseApplication.getPresent_position()) {
            isChange = true;
        }
        present_position = baseApplication.getPresent_position();
        if (isChange) {
            isChange = false;

            L.e("123===setBabyData==onResume");
            setBabyData(watches);
            setView(rootView);
        }

    }

    private void initView(View view) {
        // Title 文字
        View title = view.findViewById(R.id.title);
        midTitle = (TextView) title.findViewById(R.id.tv_middle);
        //左边按钮
        iv_left = (ImageView) title.findViewById(R.id.iv_left);
        //右边按钮
        iv_right = (ImageView) title.findViewById(R.id.iv_right);
        view_title = view.findViewById(R.id.title);
        view_background = getActivity().findViewById(R.id.view_background);

        title_arrow_iv = (ImageView) title.findViewById(R.id.title_arrow_iv);
        title_ll_middle = (LinearLayout) title.findViewById(R.id.title_ll_middle);

        dear_head_view = (ImageView) view.findViewById(R.id.dear_head_view);
        dear_iv_battery = (ImageView) view.findViewById(R.id.dear_iv_battery);
        dear_tv_name = (TextView) view.findViewById(R.id.dear_tv_name);
        watch_tools_btn_remove_bind = (Button) view.findViewById(R.id.watch_tools_btn_remove_bind);

        dearScrollView = (ScrollView) view.findViewById(R.id.dearScrollView);

        rl_baby_root = (RelativeLayout) view.findViewById(R.id.rl_baby_root);


        mShowAction = new AlphaAnimation(0.1f, 2.0f);

        mShowAction.setDuration(300);

    }

    /**
     * scrollView滑动到最顶部
     */
    private void notifyScrollView() {
        dearScrollView.fullScroll(ScrollView.FOCUS_UP);
    }

    private void setView(View view) {
        baseApplication.setPresent_position(present_position);
        midTitle.setText(getResources().getString(R.string.function_name_dear));
        iv_left.setImageResource(R.mipmap.title_add);
        iv_left.setOnClickListener(this);


        title_ll_middle.setOnClickListener(this);
        //上一条position设置为当前position
        ever_position = present_position;

        iv_right.setImageResource(R.mipmap.title_setting);
        iv_right.setOnClickListener(this);
        watch_tools_btn_remove_bind.setOnClickListener(this);
        View watch_tools_pedometer = view.findViewById(R.id.watch_tools_pedometer);
        watch_tools_pedometer.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                DialogUtils.clear_honorDialog(getActivity(), new SimpleClickListener() {
                    @Override
                    public void ok() {
                        clear_honor();//清零小红花
                    }

                    @Override
                    public void cancle() {
                    }
                });
                return false;
            }
        });

        for (int i = 0; i < layouts.length; i++) {
            View layout = view.findViewById(layouts[i]);
            if (i == 1) {
                layout_insurance = layout;
            }
            layout.setOnClickListener(this);
            TextView textView = (TextView) layout.findViewById(R.id.watch_tools_tv);
            ImageView imageView = (ImageView) layout.findViewById(R.id.watch_tools_iv);
            textView.setText(getResources().getString(titles[i]));
            imageView.setImageResource(icon_drawables[i]);

        }


        //顶部显示图片（宝贝）
        String sex = sp.getString("sex", "");
        String nick_name = sp.getString("nick_name", "");
        if (sex.equals("0")) {
            dear_head_view.setImageResource(R.drawable.dear_deatil_girl_head_view_selector);
        } else {
            dear_head_view.setImageResource(R.drawable.dear_deatil_boy_head_view_selector);
        }
        if (!"".equals(nick_name)) {
            dear_tv_name.setText(nick_name);
        } else {
            dear_tv_name.setText(getResources().getString(R.string.function_name_dear));
        }
//        dear_iv_battery.setImageResource(SmartWatchUtils.getElectricityView(watch.getElectricities()));

        rl_baby_root.setOnClickListener(this);


    }


    private void setBabyData(List<SmartWatch> mWatches) {
        if (mWatches != null) {
            watches = mWatches;
        } else {
            watches = SmartWatchUtils.getWatchList();
        }
        if (watches == null)
            return;
        if (watches.size() != 0) {
            if (present_position < watches.size()) {
                watch = watches.get(present_position);
            } else {
                watch = watches.get(0);
            }

        } else {
            L.e("list.size = 0");
            Intent intent = new Intent(getActivity(), BindWatchActivity.class);
            startActivity(intent);
            getActivity().finish();
        }

    }


    @Override
    public void onDetach() {
        super.onDetach();
    }


    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public boolean fragmentOnActivityResult(int requestCode, int resultCode, Intent data) {
        return super.fragmentOnActivityResult(requestCode, resultCode, data);

    }

    @Override
    public void refreshWatchList(List<SmartWatch> watches) {
        super.refreshWatchList(watches);
        L.e("123===refreshWatchList==onResume");
        setBabyData(watches);
        notifyScrollView();
        setView(rootView);
    }

    @Override
    public void onClick(View v) {
        if (CMethod.isFastDoubleClick()) {
            return;
        }
        if (!CMethod.isNet(getActivity()) && v.getId() != R.id.iv_left && v.getId() != R.id.iv_right) {
            T.s(getResources().getString(R.string.no_network));
            return;
        }
        switch (v.getId()) {

            case R.id.rl_baby_root://宝贝信息
                Intent intent_baby_info = new Intent(getActivity(), BabyInfoActivity.class);
                Bundle mBundle_baby_info = new Bundle();
                mBundle_baby_info.putParcelable(SmartWatchListData.KEY, watch);//传递宝贝信息
                mBundle_baby_info.putString("service_unbind", service_unbind);
                mBundle_baby_info.putString("imei", imei);
                mBundle_baby_info.putString("user_id", user_id);
                mBundle_baby_info.putString("URL", URL);
                intent_baby_info.putExtras(mBundle_baby_info);

                startActivity(intent_baby_info);
                break;

            case R.id.watch_tools_telophone://电话簿
                Intent intent_tel = new Intent(getActivity(), TelephoneSettingActivity.class);
                Bundle mBundle_tel = new Bundle();
                mBundle_tel.putString("service_contact_a", service_contact_a);
                mBundle_tel.putString("service_contact_b", service_contact_b);
                mBundle_tel.putString("imei", imei);
                mBundle_tel.putString("URL", URL);
                //mBundle_tel.putParcelable(SmartWatchListData.KEY, watch);//传递宝贝信息
                intent_tel.putExtras(mBundle_tel);
                startActivity(intent_tel);

                break;
            case R.id.title_ll_middle://title选择
                if (watches.size() > 1) {
                    title_arrow_iv.setImageResource(R.mipmap.title_arrow_up);

                    view_background.startAnimation(mShowAction);
                    view_background.setVisibility(View.VISIBLE);
                    menuWindow = new BabyChoosePopupWindow(getActivity(), present_position, watches, itemsOnClick, title_arrow_iv, view_background);
                    menuWindow.showAsDropDown(view_title, 0, 0);
                }


                break;
            case R.id.iv_left://添加手表

                Intent intent_bind_watch = new Intent(getActivity(), BindWatchActivity.class);
                intent_bind_watch.putExtra(IntentAction.KEY_CLASS_NAME, TAG);
                Bundle mBundle_bind = new Bundle();
                mBundle_bind.putString("service_bind", service_bind);
                mBundle_bind.putString("user_id", user_id);
                mBundle_bind.putString("URL", URL);
                intent_bind_watch.putExtras(mBundle_bind);
                getActivity().startActivity(intent_bind_watch);

                break;
            case R.id.iv_right://系统设置
                Intent intent_setting = new Intent(getActivity(), SystemSettingsActivity.class);
                Bundle mBundle_setting = new Bundle();
                mBundle_setting.putParcelable(SmartWatchListData.KEY, watch);//传递宝贝信息
                intent_setting.putExtras(mBundle_setting);
                startActivity(intent_setting);
                break;
            case R.id.watch_tools_sos://sos 设置
                //if (isManager()) {
                Intent intent_sos = new Intent(getActivity(), SOSSettingActivity.class);
                Bundle mBundle_sos = new Bundle();
                mBundle_sos.putString("service_sosnumber", service_sosnumber);
                mBundle_sos.putString("imei", imei);
                mBundle_sos.putString("URL", URL);
                //mBundle_sos.putParcelable(SmartWatchListData.KEY, watch);//传递宝贝信息
                intent_sos.putExtras(mBundle_sos);
                startActivity(intent_sos);
               // }
                break;
//            case R.id.watch_tools_education://在线教育
//                Intent intent_education = new Intent(getActivity(), EducationActivity.class);
//                startActivity(intent_education);
//                break;
            case R.id.watch_tools_alarm://闹钟
                Intent intent_alarm = new Intent(getActivity(), AlarmClockActivity.class);
                Bundle mBundle_alarm = new Bundle();
                mBundle_alarm.putString("service_alarm", service_alarm);
                mBundle_alarm.putString("imei", imei);
                mBundle_alarm.putString("URL", URL);
//                mBundle_alarm.putParcelable(SmartWatchListData.KEY, watch);//传递宝贝信息
                intent_alarm.putExtras(mBundle_alarm);
                startActivity(intent_alarm);
                MobclickAgent.onEvent(getActivity(), UMeventId.UMENG_ALARM_CLOCK_USE);

                break;
            case R.id.watch_tools_find_watch://找手表
                findWatch();
                //MobclickAgent.onEvent(getActivity(), UMeventId.UMENG_LOOKING_FOR_THE_NUMBER_OF_WATCHES);
                break;
            case R.id.watch_tools_family://家庭成员
                Intent intent_family_member = new Intent(getActivity(), FamilyMemberActivity.class);
                Bundle mBundle_family_member = new Bundle();
                mBundle_family_member.putParcelable(SmartWatchListData.KEY, watch);//传递宝贝信息
                intent_family_member.putExtras(mBundle_family_member);
                startActivity(intent_family_member);
                break;
            case R.id.watch_tools_find_location://定位模式
                Intent intent_location_mode_select = new Intent(getActivity(), LocationModeSelectActivity.class);
                Bundle mBundle_location_mode_select = new Bundle();
                mBundle_location_mode_select.putString("service_mode", service_mode);
                mBundle_location_mode_select.putString("imei", imei);
                mBundle_location_mode_select.putString("URL", URL);
//                mBundle_location_mode_select.putParcelable(SmartWatchListData.KEY, watch);//传递宝贝信息
                intent_location_mode_select.putExtras(mBundle_location_mode_select);
                startActivity(intent_location_mode_select);
                break;
            case R.id.watch_tools_not_disturb://免打扰模式
                Intent intent_not_disturb = new Intent(getActivity(), NotDisturbActivity.class);
                Bundle mBundle_not_disturb = new Bundle();
                mBundle_not_disturb.putString("service_silence", service_silence);
                mBundle_not_disturb.putString("imei", imei);
                mBundle_not_disturb.putString("URL", URL);
//                mBundle_not_disturb.putParcelable(SmartWatchListData.KEY, watch);//传递宝贝信息
                intent_not_disturb.putExtras(mBundle_not_disturb);
                startActivity(intent_not_disturb);
                MobclickAgent.onEvent(getActivity(), UMeventId.UMENG_FREE_USE_OF_FREQUENCY);
                break;
            case R.id.watch_tools_btn_remove_bind://解除绑定
                DialogUtils.removeBindDialog(getActivity(), new SimpleClickListener() {
                    @Override
                    public void ok() {
//                        unbindThisWatch();
                    }

                    @Override
                    public void cancle() {

                    }
                });
                MobclickAgent.onEvent(getActivity(), UMeventId.UMENG_NUMBER_OF_SOLUTIONS);
                break;
            case R.id.pedometer_checkbox://计步

                break;
            case R.id.watch_tools_pedometer://添加小红花
                DialogUtils.add_honorDialog(getActivity(), new SimpleClickListener() {
                    @Override
                    public void ok() {
                        add_honor();//添加小红花
                    }

                    @Override
                    public void cancle() {
                    }
                });
                break;
            case R.id.watch_tools_remote_shutdown://远程关机
                if (true) {
                    DialogUtils.remoteShutdownDialog(getActivity(), new SimpleClickListener() {
                        @Override
                        public void ok() {
                            powerOff();
                        }

                        @Override
                        public void cancle() {
                        }
                    });
                }
                break;
        }
    }

    //添加小红花
    private void add_honor() {
        String sign = Md5Util.stringmd5(imei, service_add_honor);
        loadingDialog.show(getResources().getString(R.string.send_instruct), AppConstants.MaxIndex);
        String url = URL + "service=" + service_add_honor + "&imei=" + imei + "&sign=" + sign;
        StringRequest request = new StringRequest(url, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                loadingDialog.dismissAndStopTimer();
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONObject jsondata = jsonObject.getJSONObject("data");
                    int code = jsondata.getInt("code");
                    if(code == 0){
                        T.s(getResources().getString(R.string.add_success));
                    }else if(code == 1){
                        T.s(getResources().getString(R.string.not_online));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                loadingDialog.dismissAndStopTimer();
            }
        });
        queue.add(request);
    }

    //清零小红花
    private void clear_honor() {
        String sign = Md5Util.stringmd5(imei, service_clear_honor);
        loadingDialog.show(getResources().getString(R.string.send_instruct), AppConstants.MaxIndex);
        String url = URL + "service=" + service_clear_honor + "&imei=" + imei + "&sign=" + sign;
        StringRequest request = new StringRequest(url, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                loadingDialog.dismissAndStopTimer();
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONObject jsondata = jsonObject.getJSONObject("data");
                    int code = jsondata.getInt("code");
                    if(code == 0){
                        T.s(getResources().getString(R.string.flower_reset));
                    }else if(code == 1){
                        T.s(getResources().getString(R.string.not_online));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                loadingDialog.dismissAndStopTimer();
            }
        });
        queue.add(request);
    }

    private View.OnClickListener onClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.btn_cancel:
                    menuWindow.dismiss();
                    break;
            }
        }
    };
    private AdapterView.OnItemClickListener itemsOnClick = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
            menuWindow.dismiss();
            if (position < watches.size()) {
                loadingDialog.show();
                String user_id = BaseApplication.getUserInfo().userID;
                if (!CMethod.isEmpty(user_id)) {
                    SmartWatchUtils.getWatchListFromNetWork(user_id, new ListListener<SmartWatch>() {
                        @Override
                        public void onError(String message) {
                            loadingDialog.dismiss();
                            present_position = position;
                            setBabyData(watches);
                            notifyScrollView();
                            setView(rootView);
                        }

                        @Override
                        public void onSuccess(List<SmartWatch> watches) {
                            loadingDialog.dismiss();
                            present_position = position;
                            setBabyData(watches);
                            notifyScrollView();
                            setView(rootView);
                        }
                    });
                }


            } else {
                Intent intent = new Intent(getActivity(), BindWatchActivity.class);
                intent.putExtra(IntentAction.KEY_CLASS_NAME, TAG);
                startActivity(intent);
            }

        }
    };

    private boolean isManager() {
        if (!watch.isManger()) {
            T.s(getResources().getString(R.string.admin_privileges));
        }
        return watch.isManger();
    }


    /**
     * 找手表
     */
    private void findWatch() {
        if (!CMethod.isNet(getActivity())) {
            T.s(getResources().getString(R.string.no_network));
            return;
        }
        sign = Md5Util.stringmd5(imei, service_find);
        loadingDialog.show(getResources().getString(R.string.send_instruct), AppConstants.MaxIndex);
        new Handler().postDelayed((new Runnable() {
            @Override
            public void run() {
                loadingDialog.dismiss();
            }
        }), 1000);
        String url = URL + "service=" + service_find + "&imei=" + imei + "&sign=" + sign;
        StringRequest request = new StringRequest(url, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                L.e("response" + response);
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONObject jsondata = jsonObject.getJSONObject("data");
                    int code = jsondata.getInt("code");
                    if(code == 0){

                    }else if(code == 1){
                        T.s(getResources().getString(R.string.not_online));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                loadingDialog.dismissAndStopTimer();
                L.e(getResources().getString(R.string.error));
            }
        });
        queue.add(request);
    }

    /**
     * 计步开关
     *
     * @param onOFF
     */
    private void switchStep(final int onOFF) {
        loadingDialog.show(getResources().getString(R.string.send_instruct), AppConstants.MaxIndex);
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("user_id", uesr_id);//用户ID
            jsonObject.put("watch_id", watch.getUser_id());//手表ID
            jsonObject.put("onOFF", onOFF);//记步开关    0- 关 1-开
        } catch (JSONException e) {
            e.printStackTrace();
        }
        HttpUtils.PostDataToWeb(UrlAddressUrils.CODE_CENTER, AppConstants.JAVA_WATCH_SWITCH_STEP_URL, jsonObject, new HttpClientListener() {
            @Override
            public void onSuccess(String result) {
                loadingDialog.dismissAndStopTimer();
                watch.setSwitch_step(onOFF);
                SmartWatchUtils.updateWatch(watch);
                if (onOFF == 1)
                    pedometer_checkbox.setImageResource(R.mipmap.switch_on);
                else
                    pedometer_checkbox.setImageResource(R.mipmap.switch_off_);
                T.s(send_code_success);
            }

            @Override
            public void onFailure(String result) {
                T.s(result);
                loadingDialog.dismissAndStopTimer();
            }

            @Override
            public void onError() {
                loadingDialog.dismissAndStopTimer();
            }
        });

    }

    /**
     * 远程关机
     */
    private void powerOff() {
        if (!CMethod.isNet(getActivity())) {
            T.s(getResources().getString(R.string.no_network));
            return;
        }
        sign = Md5Util.stringmd5(imei, service_shutdown);
        loadingDialog.show(getResources().getString(R.string.send_instruct), AppConstants.MaxIndex);
        new Handler().postDelayed((new Runnable() {
            @Override
            public void run() {
                loadingDialog.dismiss();
            }
        }), 1000);
        String url = URL + "service=" + service_shutdown + "&imei=" + imei + "&sign=" + sign;
        StringRequest request = new StringRequest(url, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONObject jsondata = jsonObject.getJSONObject("data");
                    int code = jsondata.getInt("code");
                    if(code == 0){

                    }else if(code == 1){
                        T.s(getResources().getString(R.string.not_online));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                loadingDialog.dismissAndStopTimer();
            }
        });
        queue.add(request);
    }


    /**
     * java删除管理员(此页面为解除绑定)
     */
    private void unbindThisWatch() {
        loadingDialog.show();
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("friend_id", watch.getUser_id());//手表ID
            jsonObject.put("user_id", BaseApplication.getUserInfo().userID);//当前执行操作的用户ID
            jsonObject.put("unbind_id", BaseApplication.getUserInfo().userID);//解除绑定的用户ID
        } catch (JSONException e) {
            e.printStackTrace();
        }
        HttpUtils.PostDataToWeb(UrlAddressUrils.CODE_API, AppConstants.JAVA_DELETE_RELATION_URL, jsonObject, new HttpClientListener() {
            @Override
            public void onSuccess(String result) {
                loadingDialog.dismiss();
                if (watches.size() > 1) {
                    if (present_position != 0) {
                        present_position = present_position - 1;
                    }
                }

                NotificationUtils.cancel(getActivity(), CMethod.parseInt(watch.getUser_id()));
                //成功删除绑定后数据库删除该手表信息
                SmartWatchUtils.deleteWatch(watch);


                watches = SmartWatchUtils.getWatchList();

                if (watches == null)
                    return;
                if (watches.size() != 0) {
                    if (present_position < watches.size()) {
                        watch = watches.get(present_position);
                    } else {
                        watch = watches.get(0);
                    }

                    notifyScrollView();
                    setView(rootView);
                    Intent intent = new Intent();
                    intent.setAction(BroadcastConstants.WATCH_LIST_REFRESH_RECEIVER);
                    getActivity().sendBroadcast(intent);

                } else {
                    L.e("list.size = 0");
                    Intent intent = new Intent(getActivity(), BindWatchActivity.class);
                    startActivity(intent);
                    getActivity().finish();
                }



            }

            @Override
            public void onFailure(String result) {
                loadingDialog.dismiss();
                T.s(result);
            }

            @Override
            public void onError() {
                loadingDialog.dismiss();
            }
        });
    }

}
