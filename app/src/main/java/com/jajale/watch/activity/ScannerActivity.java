package com.jajale.watch.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.hardware.Camera;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.Result;
import com.jajale.watch.AppConstants;
import com.jajale.watch.BaseApplication;
import com.jajale.watch.BroadcastConstants;
import com.jajale.watch.CodeConstants;
import com.jajale.watch.R;
import com.jajale.watch.entity.BindWatchData;
import com.jajale.watch.entity.SmartWatchListData;
import com.jajale.watch.entitydb.Message;
import com.jajale.watch.entitydb.MsgMember;
import com.jajale.watch.entitydb.SmartWatch;
import com.jajale.watch.entityno.MessageContentType;
import com.jajale.watch.factory.MessageFactory;
import com.jajale.watch.factory.MsgMemberFactory;
import com.jajale.watch.listener.HttpClientListener;
import com.jajale.watch.thirdpart.qrcode.FormatSelectorDialogFragment;
import com.jajale.watch.thirdpart.qrcode.MessageDialogFragment;
import com.jajale.watch.thirdpart.qrcode.ZXingScannerView;
import com.jajale.watch.utils.CMethod;
import com.jajale.watch.utils.DataConversionUtils;
import com.jajale.watch.utils.HttpUtils;
import com.jajale.watch.utils.L;
import com.jajale.watch.utils.LoadingDialog;
import com.jajale.watch.utils.Md5Util;
import com.jajale.watch.utils.MessageUtils;
import com.jajale.watch.utils.PhoneSPUtils;
import com.jajale.watch.utils.SmartWatchUtils;
import com.jajale.watch.utils.T;
import com.jajale.watch.utils.UrlAddressUrils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * 使用二维码扫描绑定手表
 * <p/>
 * Created by lilonghui on 2015/11/17.
 * Email:lilonghui@bjjajale.com
 */

public class ScannerActivity extends FragmentActivity implements
        MessageDialogFragment.MessageDialogListener,
        ZXingScannerView.ResultHandler,
        FormatSelectorDialogFragment.FormatSelectorDialogListener, View.OnClickListener {
    private static final String FLASH_STATE = "FLASH_STATE";
    private static final String AUTO_FOCUS_STATE = "AUTO_FOCUS_STATE";
    private static final String SELECTED_FORMATS = "SELECTED_FORMATS";
    private ZXingScannerView mScannerView;
    private PhoneSPUtils appSP;
    private boolean mFlash;
    private boolean mAutoFocus;
    private ArrayList<Integer> mSelectedIndices;
    public static int width;
    private boolean isDestroy = false;
    private LoadingDialog loadingDialog;

    private TextView tv_title_des , tv_bottom_des;
    private ImageView iv_title_watch ;

    private String fromTag = "" ;

    RequestQueue queue;
    SharedPreferences sp;
    SharedPreferences.Editor editor;
    private String user_id, URL, service_bind;

    @Override
    public void onCreate(Bundle state) {
        super.onCreate(state);
        WindowManager wm = this.getWindowManager();
        width = wm.getDefaultDisplay().getWidth();
        appSP = new PhoneSPUtils(ScannerActivity.this);
        if (state != null) {
            mFlash = state.getBoolean(FLASH_STATE, false);
            mAutoFocus = state.getBoolean(AUTO_FOCUS_STATE, true);
            mSelectedIndices = state.getIntegerArrayList(SELECTED_FORMATS);
        } else {
            mFlash = false;
            mAutoFocus = true;
            mSelectedIndices = null;
        }
        fromTag = getIntent().getStringExtra("from_tag");
        setContentView(R.layout.activity_qrcode_camera);
        loadingDialog = new LoadingDialog(this);
        TextView text1 = (TextView) findViewById(R.id.text1);
        RelativeLayout layout = (RelativeLayout) findViewById(R.id.relative_camera);

        tv_title_des = (TextView) findViewById(R.id.qrcode_top_hint);
        iv_title_watch = (ImageView) findViewById(R.id.imageview_top);
        tv_bottom_des = (TextView) findViewById(R.id.qrcode_bottom_hint);
        if (fromTag.equals("web")){
            tv_title_des.setVisibility(View.INVISIBLE);
            iv_title_watch.setVisibility(View.INVISIBLE);
            tv_bottom_des.setText(getResources().getString(R.string.code_inside_box));
        }
        mScannerView = (ZXingScannerView) findViewById(R.id.zxingview);
        setupFormats();
        initTitleView();
        if (isCameraCanUse()) {
            text1.setVisibility(View.GONE);
            layout.setVisibility(View.VISIBLE);
        } else {
            text1.setVisibility(View.VISIBLE);
            layout.setVisibility(View.GONE);
        }

        queue = Volley.newRequestQueue(getApplicationContext());
        Bundle bundle=getIntent().getExtras();
        user_id = bundle.getString("user_id");//读出数据
        URL = bundle.getString("URL");
        service_bind = bundle.getString("service_bind");
        sp = getApplicationContext().getSharedPreferences("NotDisturb", getApplicationContext().MODE_PRIVATE);// 创建对象
        editor = sp.edit();
    }

    /**
     * 初始化title view
     */
    private void initTitleView() {
        View title = findViewById(R.id.title);
        TextView midTitle = (TextView) title.findViewById(R.id.tv_middle);
        if (fromTag.equals("web")){
            midTitle.setText(getResources().getString(R.string.scan_code));
        }else {
            midTitle.setText(getResources().getString(R.string.bindwatch_title_text));
        }
        ImageView iv_left = (ImageView) title.findViewById(R.id.iv_left);
        iv_left.setImageResource(R.drawable.title_goback_selector);
        iv_left.setOnClickListener(this);
    }

    /**
     * 判断摄像头是否被调用
     *
     * @return
     */
    public static boolean isCameraCanUse() {
        boolean canUse = true;
        Camera mCamera = null;
        try {
            mCamera = Camera.open();
        } catch (Exception e) {
            canUse = false;
        }
        if (canUse) {
            mCamera.release();
            mCamera = null;
        }

        return canUse;
    }

    @Override
    public void onResume() {
        super.onResume();
        mScannerView.setResultHandler(this);
        mScannerView.startCamera();
        mScannerView.setFlash(mFlash);
        mScannerView.setAutoFocus(mAutoFocus);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(FLASH_STATE, mFlash);
        outState.putBoolean(AUTO_FOCUS_STATE, mAutoFocus);
        outState.putIntegerArrayList(SELECTED_FORMATS, mSelectedIndices);
    }

    public void handleResult(Result rawResult) {
        try {
            Uri notification = RingtoneManager
                    .getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            Ringtone r = RingtoneManager.getRingtone(getApplicationContext(),
                    notification);
            r.play();

            if (CMethod.isNet(ScannerActivity.this)){
                if (fromTag.equals("web")){
                    L.e("扫描结果是:" + rawResult.getText());
                    Intent data = new Intent();
                    data.putExtra("qr_code",rawResult.getText());
                    setResult(CodeConstants.ScanOk, data);
                    finish();

//                        setWebScannerResult("043fc81f-a591-499b-abf0-5dfa2f8e80e7", "www.bjjajale.com");

                }else {
                    if (isImeiNumber(rawResult.getText())) {
                        L.e("扫描结果是" + rawResult.getText());
//                        bindWatch(rawResult.getText(), BaseApplication.getUserInfo().userID);
                        bindWatch(rawResult.getText(), user_id);
                        Intent intent = new Intent(getApplicationContext(), HomeSecActivity.class);
                        startActivity(intent);
                    } else {
                        Toast.makeText(ScannerActivity.this, getResources().getString(R.string.not_valid_imei), Toast.LENGTH_SHORT).show();
                        finish();
                    }
                }


            }else {
                T.s(getResources().getString(R.string.no_network));
                setResult(CodeConstants.ScanCancel);
                finish();
            }

        } catch (Exception e) {
            finish();
        }


    }

    public void showMessageDialog(String message) {
        DialogFragment fragment = MessageDialogFragment.newInstance(
                "Scan Results", message, this);
        fragment.show(getSupportFragmentManager(), "scan_results");
    }

    public void closeMessageDialog() {
        closeDialog("scan_results");
    }

    public void closeFormatsDialog() {
        closeDialog("format_selector");
    }

    public void closeDialog(String dialogName) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        DialogFragment fragment = (DialogFragment) fragmentManager
                .findFragmentByTag(dialogName);
        if (fragment != null) {
            fragment.dismiss();
        }
    }

    @Override
    public void onDialogPositiveClick(DialogFragment dialog) {
        // Resume the camera
        mScannerView.startCamera();
        mScannerView.setFlash(mFlash);
        mScannerView.setAutoFocus(mAutoFocus);
    }

    @Override
    public void onFormatsSaved(ArrayList<Integer> selectedIndices) {
        mSelectedIndices = selectedIndices;
        setupFormats();
    }

    public void setupFormats() {
        List<BarcodeFormat> formats = new ArrayList<BarcodeFormat>();
        if (mSelectedIndices == null || mSelectedIndices.isEmpty()) {
            mSelectedIndices = new ArrayList<Integer>();
            for (int i = 0; i < ZXingScannerView.ALL_FORMATS.size(); i++) {
                mSelectedIndices.add(i);
            }
        }

        for (int index : mSelectedIndices) {
            formats.add(ZXingScannerView.ALL_FORMATS.get(index));
        }
        if (mScannerView != null) {
            mScannerView.setFormats(formats);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        mScannerView.stopCamera();
        closeMessageDialog();
        closeFormatsDialog();
    }

    @Override
    public void onClick(View v) {
        if (CMethod.isFastDoubleClick()){
            return;
        }
        switch (v.getId()) {
            case R.id.iv_left://返回键
                finish();
                break;

        }
    }

    /**
     * 检查是否为有效的IMEI码
     *
     * @param mobile
     * @return
     */
    public boolean isImeiNumber(String mobile) {

        String phoneRegex = "[0-9]{15}";// 验证码格式正则表达式验证

        Pattern regex;
        Matcher matcher;

        regex = Pattern.compile(phoneRegex);
        matcher = regex.matcher(mobile);

        if (matcher.matches()) {
            return true;
        }

        return false;
    }
    /**
     * JAVA绑定手表
     */
    private void bindWatch(final String imei, final String userID) {
        loadingDialog.show();
        editor.putString("IMEI", imei);
        editor.putString("Telephone_Number", user_id);
        editor.commit();
        //L.e("扫描结果是:");
        String sign = Md5Util.stringmd5(imei, service_bind, user_id);
        String path = URL + "service=" + service_bind + "&imei=" + imei + "&user_id=" + user_id + "&sign=" + sign;
        StringRequest request = new StringRequest(path, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                loadingDialog.dismiss();
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONObject jsondata = jsonObject.getJSONObject("data");
                    int code = jsondata.getInt("code");
                    if (code == 0) {
                        T.s(getResources().getString(R.string.bind_success));
//                        Intent intent = new Intent(getApplicationContext(), HomeSecActivity.class);
//                        intent.putExtra(IntentAction.OPEN_HONE_FROM, "familyselect");
//                        startActivity(intent);
                    } else if (code == 1) {
                        T.s(getResources().getString(R.string.is_binding));
                    }

                } catch (JSONException e) {
                    // TODO Auto-generated catch block
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


    @Override
    protected void onDestroy() {
        super.onDestroy();
        isDestroy = true;
    }





}