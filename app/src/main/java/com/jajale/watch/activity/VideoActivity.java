package com.jajale.watch.activity;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Message;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import com.google.gson.Gson;
import com.jajale.watch.AppConstants;
import com.jajale.watch.BaseActivity;
import com.jajale.watch.R;
import com.jajale.watch.entity.AppInfo;
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
 * Created by athena on 2016/3/6.
 * Email: lizhiqiang@bjjajale.com
 */
public class VideoActivity extends BaseActivity {

    private WebView webView;
    private ProgressBar pb;
    private LoadingDialog loadingDialog;
    private String url = "http://yuntv.letv.com/bcloud.html?uu=pyo48zevyh&vu=6437844a55&auto_play=1&gpcflag=1&width=854&height=480";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //隐藏标题栏
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        //隐藏状态栏
        //定义全屏参数
        int flag = WindowManager.LayoutParams.FLAG_FULLSCREEN;
        //获得当前窗体对象
        Window window = VideoActivity.this.getWindow();
        //设置当前窗体为全屏显示
        window.setFlags(flag, flag);

        setContentView(R.layout.activity_web_page);
        loadingDialog = new LoadingDialog(this);
        getBundle();
        initWebView();

        if (!CMethod.isNet(VideoActivity.this)) {
            T.s("请检查网络连接");
            return;

        }


    }

    private void getBundle() {
        String msg_id = getIntent().getStringExtra("info_msg_id");
        getHtmlByMsgId(msg_id);

//        url = getIntent().getStringExtra("info_url");
//        int width = AppInfo.getInstace().getScreenWidth() /2  ;
//        int height = AppInfo.getInstace().getScreenHeight() /2;
//
//        url=url+"&width="+width+"&height="+height;
//        if (width > height){
//            url = url.replace("<#W#>", width+"");
//            url = url.replace("<#H#>",height+"");
//        }else {
//            url = url.replace("<#W#>", height+"");
//            url = url.replace("<#H#>",width+"");
//        }

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
                InfoWebData data = gson.fromJson(result,InfoWebData.class);
                String video_url = data.getDetailed();
                int width = AppInfo.getInstace().getScreenWidth() / 2;
                int height = AppInfo.getInstace().getScreenHeight() / 2;
                video_url = video_url + "&width=" + width + "&height=" + height;
                webView.loadUrl(video_url);
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

    private void initWebView() {
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

    private WebViewClient mWebViewClient = new WebViewClient() {

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);

            VideoActivity.this.url = url;

            if (!VideoActivity.this.isFinishing()) {
                pb.setProgress(100);
                pb.setVisibility(View.GONE);
                loadingDialog.dismiss();
            }
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
            if (!VideoActivity.this.isFinishing()) {
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
            L.e("onReceivedError~~~");
        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            return super.shouldOverrideUrlLoading(view, url);
        }
    };

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
            webView.clearCache(true);
            webView.stopLoading();
            webView.goBack();
            webView.destroy();
            L.e("onCloseWindow~~~");
        }

        @Override
        public void onProgressChanged(WebView view, int newProgress) {
            super.onProgressChanged(view, newProgress);
            pb.setProgress(newProgress);
        }
    };


    @Override
    protected void onPause() {
        super.onPause();
        webView.onPause();

    }

    @Override
    protected void onResume() {
        super.onResume();
        webView.onResume();
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        webView.clearCache(true);
        webView.stopLoading();
        webView.goBack();
        webView.destroy();
    }
}
