package com.jajale.watch.activity;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import com.jajale.watch.AppConstants;
import com.jajale.watch.R;
import com.jajale.watch.entity.SPKeyConstants;
import com.jajale.watch.utils.CMethod;
import com.jajale.watch.utils.PhoneSPUtils;

/**
 * 联通协议
 * Created by athena on 2015/12/4.
 * Email: lizhiqiang@bjjajale.com
 */
public class UnicomAgreementActivity extends BaseTitleActivity implements View.OnClickListener {

    private WebView webView;
    private ProgressBar pb;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_authentication);
        initTitle();

        initWebView();
        webView.loadUrl(AppConstants.UNICOM_AGREEMENT);
    }



    private void initWebView(){
        webView = (WebView) findViewById(R.id.webView);
        pb = (ProgressBar) findViewById(R.id.pb);
        findViewById(R.id.btn_agree).setOnClickListener(this);
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);

        // 注册调用接口对象
//        webView.addJavascriptInterface(new ControlPannel(this,webrespHandler),"control");
//        webView.addJavascriptInterface(new ZfbFactory(this, zfbHandler), "zfbFactory");

        // 加载第三方wap地址
        webView.requestFocus(View.FOCUS_DOWN);
        webView.setWebViewClient(mWebViewClient);
        webView.setWebChromeClient(mWebChromeClient);
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


    private void initTitle(){
        setTitleMiddle("联通套餐用卡协议");
    }


    @Override
    public void onClick(View v) {
        if (CMethod.isFastDoubleClick()){
            return;
        }
        switch (v.getId()){
            case R.id.btn_agree:
                PhoneSPUtils phoneSPUtils=new PhoneSPUtils(this);
                phoneSPUtils.save(SPKeyConstants.IS_READ_UNICOM_AGREEMENT,true);
                startMyActivity(AuthenticationActivity.class);
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
            return super.onCreateWindow(view, dialog, userGesture, resultMsg);
        }

        @Override
        public void onShowCustomView(View view, CustomViewCallback callback) {
            super.onShowCustomView(view, callback);
        }

        @Override
        public void onCloseWindow(WebView window) {
            super.onCloseWindow(window);
        }

        @Override
        public void onProgressChanged(WebView view, int newProgress) {
            super.onProgressChanged(view, newProgress);
            if (newProgress == 100) {
                pb.setVisibility(View.GONE);
            } else {
                if (pb.getVisibility() == View.GONE)
                    pb.setVisibility(View.VISIBLE);
                pb.setProgress(newProgress);
            }
        }
    };

    private WebViewClient mWebViewClient = new WebViewClient() {

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
        }

        @Override
        public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
            super.onReceivedError(view, errorCode, description, failingUrl);
        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            return super.shouldOverrideUrlLoading(view, url);
        }
    };
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK)) {
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
