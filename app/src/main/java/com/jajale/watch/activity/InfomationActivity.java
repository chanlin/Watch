package com.jajale.watch.activity;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Message;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.google.gson.Gson;
import com.jajale.watch.AppConstants;
import com.jajale.watch.R;
import com.jajale.watch.entity.InfoWebData;
import com.jajale.watch.listener.HttpClientListener;
import com.jajale.watch.utils.CMethod;
import com.jajale.watch.utils.HttpUtils;
import com.jajale.watch.utils.L;
import com.jajale.watch.utils.LoadingDialog;
import com.jajale.watch.utils.T;
import com.jajale.watch.utils.UrlAddressUrils;

import org.json.JSONException;
import org.json.JSONObject;


/**
 * Created by lizhiqiang on 19/2/16.
 */
public class InfomationActivity extends BaseTitleActivity implements View.OnClickListener {
    private String title = "";
    private String msg_id = "" ;
    private LoadingDialog loadingDialog;
    private WebView webView;
    private ProgressBar pb;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_page);
        loadingDialog = new LoadingDialog(this);
        loadingDialog.Cancelable(true);
        getBundle();
        initTitle();
        initWebView();

        getHtmlByMsgId(msg_id);
    }


    private void getHtmlByMsgId(String msg_id){
//        String test_url = "http://192.168.1.101:20000/User/Entrance";

        if (CMethod.isEmpty(msg_id))
            return;
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("id",msg_id);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        HttpUtils.PostDataToWeb(UrlAddressUrils.CODE_API, AppConstants.JAVA_INFOR_MATION_DETAIL_URL, jsonObject, new HttpClientListener() {
            @Override
            public void onSuccess(String result) {
                L.e(result);
                Gson gson = new Gson();
                InfoWebData data = gson.fromJson(result, InfoWebData.class);

                loadHtml(data.getDetailed());
            }

            @Override
            public void onFailure(String result) {
                T.s(result);
                loadingDialog.dismiss();
            }

            @Override
            public void onError() {
                loadingDialog.dismiss();
            }
        });

    }



    private void loadHtml(String code){
        L.e(code);
        String html = code.replaceAll("☆", " ");

        webView.loadDataWithBaseURL("www.mypage.html",html,"text/html","utf-8",null);
    }


    private void getBundle(){
        msg_id = getIntent().getStringExtra("info_msg_id");





    }



    private void initTitle(){
        setTitleMiddle(title);
        ImageView iv_left = (ImageView) findViewById(R.id.iv_left);
        iv_left.setImageResource(R.drawable.title_goback_selector);
        iv_left.setOnClickListener(this);
    }

    private void initWebView(){
        webView = (WebView) findViewById(R.id.webView);
        pb = (ProgressBar) findViewById(R.id.pb);
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);

        // 注册调用接口对象
//        webView.addJavascriptInterface(new ControlPannel(this,webrespHandler),"control");
//        webView.addJavascriptInterface(new ZfbFactory(this, zfbHandler), "zfbFactory");

        // 加载第三方wap地址
        webView.requestFocus(View.FOCUS_DOWN);
        webView.setWebViewClient(mWebViewClient);
//        webView.setWebChromeClient(mWebChromeClient);
//        webView.setWebChromeClient(new WebChromeClient());
        webSettings.setBlockNetworkImage(false);
        webSettings.setDefaultTextEncodingName("UTF-8");
        // webSettings.setPluginsEnabled(true);
        webSettings.setSupportMultipleWindows(true);
        webSettings.setTextSize(WebSettings.TextSize.NORMAL);

        webView.setScrollbarFadingEnabled(true);
        webView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
        webSettings.setSaveFormData(false);
        webSettings.setSavePassword(false);
        webSettings.setCacheMode(WebSettings.LOAD_NO_CACHE);
        webSettings.setDatabaseEnabled(true);
        webSettings.setDomStorageEnabled(true);
        webSettings.setDatabasePath("/data/data/" + getPackageName() + "/databases/");

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.iv_left:
                Finish();
                break;
        }
    }


    private void Finish(){
        finish();
    }

    private WebChromeClient mWebChromeClient = new WebChromeClient() {
        @Override
        public boolean onJsTimeout() {
            return super.onJsTimeout();
        }

        @Override
        public boolean onCreateWindow(WebView view, boolean dialog, boolean userGesture, Message resultMsg) {
            if (!loadingDialog.isShowing()) {
                loadingDialog.show();
            }
//                T.s(url);
            pb.setProgress(0);
            pb.setVisibility(View.VISIBLE);

            return super.onCreateWindow(view, dialog, userGesture, resultMsg);
        }

        @Override
        public void onShowCustomView(View view, CustomViewCallback callback) {
            super.onShowCustomView(view, callback);
            pb.setProgress(100);
            pb.setVisibility(View.GONE);
            loadingDialog.dismiss();
        }

        @Override
        public void onCloseWindow(WebView window) {
            super.onCloseWindow(window);
        }

        @Override
        public void onProgressChanged(WebView view, int newProgress) {
            super.onProgressChanged(view, newProgress);
            pb.setProgress(newProgress);
        }
    };

    private WebViewClient mWebViewClient = new WebViewClient() {

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);

            if (!InfomationActivity.this.isFinishing()) {
                pb.setProgress(100);
                pb.setVisibility(View.GONE);
                loadingDialog.dismiss();
            }
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
            if (!InfomationActivity.this.isFinishing()) {
                if (!loadingDialog.isShowing()) {
                    loadingDialog.show();
                }
//                T.s(url);
                pb.setProgress(0);
                pb.setVisibility(View.VISIBLE);

            }
        }

        @Override
        public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
            super.onReceivedError(view, errorCode, description, failingUrl);
            loadingDialog.dismiss();
        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            return super.shouldOverrideUrlLoading(view, url);
        }
    };
}
