package com.jajale.watch.activity;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.jajale.watch.R;
import com.jajale.watch.utils.CMethod;
import com.jajale.watch.utils.L;
import com.jajale.watch.utils.LoadingDialog;

import java.lang.ref.WeakReference;

/**
 * Created by athena on 2015/12/4.
 * Email: lizhiqiang@bjjajale.com
 */
public class InsuranceArticleActivity extends BaseTitleActivity implements View.OnClickListener {

    public static final int INSUR_CONFIRM = 10101;
    public static final int INSUR_CANCEL = 10102;

    public static int openID = 0 ;
//    public static String titleStr = "";
    private String url = "http://cms.bjjajale.com/cms/insurance/proto.do?i=";
    private String title = "";
    private WebView webView;
    private ProgressBar pb;
    private LoadingDialog loadingDialog;
    private Handler webrespHandler;

//    private String fromTab = "";
//    private boolean isLoad = false;




    static class MyHandler extends Handler {
        private WeakReference<InsuranceArticleActivity> mActivity;
        public MyHandler(InsuranceArticleActivity activity) {
            mActivity = new WeakReference<InsuranceArticleActivity>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case INSUR_CONFIRM:
                    L.e("同意了条款");
                    break;
                case INSUR_CANCEL:
                    L.e("取消了条款");
                    break;
            }


        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_page);
        webrespHandler = new MyHandler(this);
        loadingDialog = new LoadingDialog(this);
        getBundle();
        initTitle();

        initWebView();
//        if (isLoad){
//            webView.loadUrl("http://www.baidu.com");
//        }else {
            webView.loadUrl(url + openID);
//        }

    }

    private void getBundle(){
//        fromTab = getIntent().getStringExtra("from");
//
//        if (!CMethod.isEmpty(fromTab)){
//            isLoad = true;
//        }

        openID = getIntent().getIntExtra("openID", 0);
//        i.putExtra("openTitle",openTitle);
        title = getIntent().getStringExtra("openTitle");
        if (CMethod.isEmpty(title)){
            title  = "领取儿童意外、重疾保险";
        }

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


    private void initTitle(){
        setTitleMiddle(title);
        ImageView iv_left = (ImageView) findViewById(R.id.iv_left);
        iv_left.setImageResource(R.drawable.title_goback_selector);
//        setTitleRightBg(R.drawable.btn_refresh_selector);
//        setTitleLeftClick(this);
        iv_left.setOnClickListener(this);


        setTitleRightClick(this);
    }


    @Override
    public void onClick(View v) {
        if (CMethod.isFastDoubleClick()){
            return;
        }
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

            InsuranceArticleActivity.this.url = url;

            if (!InsuranceArticleActivity.this.isFinishing()) {
                pb.setProgress(100);
                pb.setVisibility(View.GONE);
                loadingDialog.dismiss();
            }
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
            if (!InsuranceArticleActivity.this.isFinishing()) {
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
