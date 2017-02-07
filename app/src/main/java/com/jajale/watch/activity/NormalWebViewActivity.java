package com.jajale.watch.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.TextView;

import com.jajale.watch.AppConstants;
import com.jajale.watch.CodeConstants;
import com.jajale.watch.R;
import com.jajale.watch.dialog.ShareDialog;
import com.jajale.watch.entity.AppInfo;
import com.jajale.watch.entity.RequestCode;
import com.jajale.watch.entity.ShareData;
import com.jajale.watch.factory.ControlPannel;
import com.jajale.watch.factory.ShareFactory;
import com.jajale.watch.listener.AreaListener;
import com.jajale.watch.listener.ShareListener;
import com.jajale.watch.listener.SingleStringListener;
import com.jajale.watch.utils.AccessTokenKeeper;
import com.jajale.watch.utils.CMethod;
import com.jajale.watch.utils.DialogUtils;
import com.jajale.watch.utils.ImageUtils;
import com.jajale.watch.utils.L;
import com.jajale.watch.utils.LoadingDialog;
import com.jajale.watch.utils.QiNiuUtils;
import com.jajale.watch.utils.ShareUtil;
import com.jajale.watch.utils.T;
import com.sina.weibo.sdk.api.WeiboMultiMessage;
import com.sina.weibo.sdk.api.share.BaseResponse;
import com.sina.weibo.sdk.api.share.IWeiboHandler;
import com.sina.weibo.sdk.api.share.IWeiboShareAPI;
import com.sina.weibo.sdk.api.share.SendMultiMessageToWeiboRequest;
import com.sina.weibo.sdk.api.share.WeiboShareSDK;
import com.sina.weibo.sdk.auth.AuthInfo;
import com.sina.weibo.sdk.auth.Oauth2AccessToken;
import com.sina.weibo.sdk.auth.WeiboAuthListener;
import com.sina.weibo.sdk.constant.WBConstants;
import com.sina.weibo.sdk.exception.WeiboException;


/**
 * Created by athena on 2016/3/6.
 * Email: lizhiqiang@bjjajale.com
 */
public class NormalWebViewActivity extends BaseTitleActivity implements View.OnClickListener, IWeiboHandler.Response {

    private WebView webView;
    private LoadingDialog loadingDialog;
    private String url = "";
    private String title = "";
    private NormalWebViewActivity activity;
    private ValueCallback<Uri> mUploadMessage;
    private final static int FILECHOOSER_RESULTCODE = 1;
    private IWeiboShareAPI mWeiboShareAPI;
    private String post_image_type = "1";

    private int right_btn_type = -1;
//    private int tab_index = 0 ;
    private boolean isCanBack=true;


    private TextView tv_right;

    private QiNiuUtils qiNiuUtils;
    public Handler webrespHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (!CMethod.isNet(NormalWebViewActivity.this)) {
                T.s("网络不给力，请稍后再试");
                return;
            }
            switch (msg.what) {
                case CodeConstants.START_SCAN://二维码扫描
                    openScanner();
                    break;
                case CodeConstants.CALL_SHARE://js调用分享
                    int type = msg.getData().getInt("share_type");
                    String share_url = msg.getData().getString("share_url");
                    String share_title = msg.getData().getString("share_title");
                    String share_content = msg.getData().getString("share_content");
                    String share_imgUrl = msg.getData().getString("share_imgUrl");

                    ShareData shareData = msg.getData().getParcelable("share_data_key");
                    sendShareData(shareData);

//                    downLoadPictureAndShare(type, share_url, share_title, share_content, share_imgUrl);
                    break;
                case CodeConstants.CALL_IMAGE://js调用相册
                    post_image_type = msg.getData().getString("image_type");
                    uploadPic();
                    break;
                case CodeConstants.CALL_TOAST://js弹出Toast
                    int toast_type = msg.getData().getInt("toast_type");
                    String toast_string = msg.getData().getString("toast_string");
                    CMethod.showToastByJs(toast_type, toast_string);
                    break;

                case CodeConstants.CALL_AREA://js弹出地址
                    final String linkage_type = msg.getData().getString("linkage_type");
                    DialogUtils.areaDialog(NormalWebViewActivity.this, new AreaListener() {
                        @Override
                        public void ok(String provinceId, String provinceName, String cityId, String cityName, String areaId, String areaName) {


                            StringBuilder sb = new StringBuilder();
                            sb.append(provinceName);
                            if (!CMethod.isEmpty(cityName) && !"市区".equals(cityName) && !"郊县".equals(cityName)) {
                                sb.append(cityName);
                            }
                            sb.append(areaName);
                            String result = sb.toString();
                            L.e("result is ---" + result);
                            webView.loadUrl("javascript:showLinkageCallback('" + result + "','"+linkage_type+"')");
                        }
                    });
                    break;
                case CodeConstants.CALL_CALENDAR://js弹出日历

                    final int calendar_type = msg.getData().getInt("calendar_type");
                    String calendar_title = msg.getData().getString("calendar_string");
                    DialogUtils.birthdayDialog(NormalWebViewActivity.this, calendar_title, "", 80, new SingleStringListener() {
                        @Override
                        public void choiced(String result) {
                            webView.loadUrl("javascript:showBirthday('" + result + "','" + calendar_type + "')");
                        }
                    });
                    break;
                case CodeConstants.CALL_CREDENTIALS:
                    //调用证件
                   final String credentials_type =msg.getData().getString("credentials_type");

                    DialogUtils.idTypeDialog(NormalWebViewActivity.this, 1, new SingleStringListener() {
                        @Override
                        public void choiced(String result) {

                            String card_name = result;
                            String card_Value = des2id(result);
                            webView.loadUrl("javascript:showCertificateCallback('"+card_name+"','"+card_Value+"','"+credentials_type+"')");

                        }
                    });
                     break;
                case CodeConstants.CALL_PHONE:
                    //打电话
                    String phoneNumber=msg.getData().getString("phone_number");
                    callPhoneState(phoneNumber);
                    break;
                case CodeConstants.CALL_TITLE:

                    int title_type = msg.getData().getInt("title_type");
                    String url = msg.getData().getString("title_url");

                    if (url.contains("close.do")) {
                        Finish();
                        return;
                    }

                    right_btn_type = title_type;
//                    setTitleMiddle(title_content);

                    String right_str = "";

//                    http://192.168.1.110:8082

                    switch (title_type) {
                        case 0:

                            break;
                        case 1:
                            right_str = "提交";
                            break;
                        case 2:
                            right_str = "提交";
                            break;
                        case 3:
                            right_str = "分享";
                            break;
                        default:
                            right_str = "";
                            break;
                    }
                    tv_right.setText(right_str);
                    if (!CMethod.isEmpty(url)) {
                        loadUrl(url);
                    }

                    break;

            }


        }
    };   private String des2id(String type_des){

        String result = "1";
//        if (type_des.equals("身份证")){
//
//        }else
        if (type_des.equals("护照")){
            result = "3" ;
        }else if (type_des.equals("军官证")){
            result = "2";
        }else if (type_des.equals("港澳通行证")){
            result = "4";
        }else if (type_des.equals("台湾通行证")){
            result = "5";
        }

        return result;
    }
    private WebSettings webSettings;
    private SwipeRefreshLayout swipeLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.Theme_Animation_Activity_RightInRightOut);

        activity = this;
        setContentView(R.layout.activity_news_web_page);
        loadingDialog = new LoadingDialog(this);
        loadingDialog.Cancelable(true);
        mWeiboShareAPI = WeiboShareSDK.createWeiboAPI(
                this, AppConstants.SINA_APP_KEY);
        mWeiboShareAPI.registerApp();

        qiNiuUtils = new QiNiuUtils(NormalWebViewActivity.this);

        if (savedInstanceState != null) {
            mWeiboShareAPI.handleWeiboResponse(getIntent(), this);
        }

        getBundle();
        initTitle();
        initWebView();
        showLoading();
        if (!CMethod.isNet(NormalWebViewActivity.this)) {
            T.s("请检查网络连接");
            return;
        }
        L.e("加载的url地址是：" + url);
//        webView.loadUrl(url + "&tab_index ="+tab_index);
        loadUrl(url);
    }

    /**
     * 拨号
     */
    private void callPhoneState(String present_phone) {
        Intent phoneIntent = new Intent("android.intent.action.CALL",
                Uri.parse("tel:" + present_phone));
        startActivity(phoneIntent);
    }

    private void loadUrl(String url) {

        if (netWorkState()==-1){
            T.s("网络不给力");
        }else{
//            url = url + "&net=" +netWorkState();
            if (webView != null) {
                webView.loadUrl(url);
            }
        }



    }


    private void showLoading(){
        swipeLayout.post(new Runnable() {
            @Override
            public void run() {
                swipeLayout.setRefreshing(true);
            }
        });

    }
    private void hideLoading(){
        swipeLayout.post(new Runnable() {
            @Override
            public void run() {
                swipeLayout.setRefreshing(false);
            }
        });

    }

    private int netWorkState() {
        if (CMethod.isNet(NormalWebViewActivity.this)) {
            String type = CMethod.getCurrentNetworkType(NormalWebViewActivity.this);
            if ("2G".equals(type)) {
                return 0;
            } else if ("无".equals(type) || "未知".equals(type)) {
                return -1;
            } else {
                return 1;
            }
        } else {
            return -1;
        }
    }

    private void initTitle() {
        setTitleMiddle(title);
        ImageView iv_left = (ImageView) findViewById(R.id.iv_left);
        iv_left.setImageResource(R.drawable.title_goback_selector);
        iv_left.setOnClickListener(this);
        tv_right = (TextView) findViewById(R.id.tv_right);
        tv_right.setOnClickListener(this);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        mWeiboShareAPI.handleWeiboResponse(intent, this);
    }

    private void getBundle() {

        url = getIntent().getStringExtra("info_url");
        title = getIntent().getStringExtra("info_title");

        if (CMethod.isEmpty(url)) {
            finish();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        loadingDialog.dismiss();
    }

    private void initWebView() {


        webView = (WebView) findViewById(R.id.webView);
//        pb = (ProgressBar) findViewById(R.id.pb);
        webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);

        // 注册调用接口对象
        webView.addJavascriptInterface(new ControlPannel(this, webrespHandler), "ControlPannel");
//        webView.addJavascriptInterface(new ZfbFactory(this, zfbHandler), "zfbFactory");

        // 加载第三方wap地址
        webView.requestFocus(View.FOCUS_DOWN);
        webView.setWebViewClient(mWebViewClient);
        webView.setWebChromeClient(new myWebChromeClient());
//        webView.setWebChromeClient(new WebChromeClient());
        webSettings.setDefaultTextEncodingName("UTF-8");
        // webSettings.setPluginsEnabled(true);
        webSettings.setSupportMultipleWindows(true);
        webSettings.setBlockNetworkImage(true);
        webSettings.setTextSize(WebSettings.TextSize.NORMAL);
        webSettings.setRenderPriority(WebSettings.RenderPriority.HIGH);
        webView.setScrollbarFadingEnabled(true);
        webView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);

        webSettings.setSaveFormData(false);
        webSettings.setSavePassword(false);
        webSettings.setCacheMode(WebSettings.LOAD_NO_CACHE);
        webSettings.setDatabaseEnabled(true);
        webSettings.setDomStorageEnabled(true);
        webSettings.setDatabasePath("/data/data/" + getPackageName() + "/databases/");

        swipeLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_container);
        swipeLayout.setColorSchemeColors(getResources().getColor(R.color.app_color));
        swipeLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {

            @Override
            public void onRefresh() {
                //重新刷新页面
//                webView.loadUrl(webView.getUrl());
                L.e("123===webView.getUrl()===" + webView.getUrl());
//                webView.reload();

                String[] arr = webView.getUrl().split("\\?");

                if (arr != null && arr.length > 1 && arr[0].contains("demeanour.do")) {
                    webView.loadUrl("javascript:refreshURL()");
                }

            }
        });

    }

    private WebViewClient mWebViewClient = new WebViewClient() {
        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            isCanBack=false;
            webSettings.setBlockNetworkImage(false);
            NormalWebViewActivity.this.url = url;
//            if (!NewsPaperActivityV2.this.isFinishing()) {
//                loadingDialog.dismiss();
//            }

            if (swipeLayout.isRefreshing()) {
                swipeLayout.setRefreshing(false);
            }
            hideLoading();
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
            L.e("123===onPageStarted===url===" + url);
            setIsRefresh(url);
//            if (!NewsPaperActivityV2.this.isFinishing()) {
//                if (!loadingDialog.isShowing()) {
//                    loadingDialog.show();
//                }
//            }
        }

        @Override
        public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
            super.onReceivedError(view, errorCode, description, failingUrl);

            loadingDialog.dismiss();
            hideLoading();
            L.e("123==onReceivedError~~~");
        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {

            L.e("should overload url------>" + url);
            if (url.contains("http://cloase.do")) {
                Finish();
                return false;
            } else {
//                return super.shouldOverrideUrlLoading(view, url+"&tab_index ="+tab_index);
                return super.shouldOverrideUrlLoading(view, url);
            }
        }
    };


    protected class myWebChromeClient extends WebChromeClient {
        // For Android 3.0+
        public void openFileChooser(ValueCallback<Uri> uploadMsg,
                                    String acceptType) {
            mUploadMessage = uploadMsg;
            Intent i = new Intent(Intent.ACTION_GET_CONTENT);
            i.addCategory(Intent.CATEGORY_OPENABLE);
            i.setType("image/*");
            startActivityForResult(Intent.createChooser(i, "选择要上传的文件"),
                    FILECHOOSER_RESULTCODE);
        }

        // For Android < 3.0
        public void openFileChooser(ValueCallback<Uri> uploadMsg) {
            mUploadMessage = uploadMsg;
            Intent i = new Intent(Intent.ACTION_GET_CONTENT);
            i.addCategory(Intent.CATEGORY_OPENABLE);
            i.setType("image/*");
            startActivityForResult(Intent.createChooser(i, "选择要上传的文件"),
                    FILECHOOSER_RESULTCODE);
        }

        public void openFileChooser(ValueCallback<Uri> uploadMsg,
                                    String acceptType, String capture) {
            openFileChooser(uploadMsg, acceptType);
        }

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
//            pb.setProgress(0);
//            pb.setVisibility(View.VISIBLE);

            return super.onCreateWindow(view, dialog, userGesture, resultMsg);
        }

        @Override
        public void onShowCustomView(View view, CustomViewCallback callback) {
            super.onShowCustomView(view, callback);
//            pb.setProgress(100);
//            pb.setVisibility(View.GONE);
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
//            pb.setProgress(newProgress);
            L.e("123====newProgress===" + newProgress);
        }

    }


    private void setIsRefresh(String loadUrl) {

        L.e("123===loadUrl===" + loadUrl);
        String[] arr = loadUrl.split("\\?");

        if (arr != null && arr.length >= 1 && arr[0].contains("demeanour.do")) {
            swipeLayout.setEnabled(true);
        } else {
            swipeLayout.setEnabled(false);
        }

    }

    @Override
    protected void onPause() {
        super.onPause();
        webView.onPause();

    }

    @Override
    protected void onResume() {
        super.onResume();
        webView.onResume();
        isOpenScanner = true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case FILECHOOSER_RESULTCODE://webview调取相册
                if (null == mUploadMessage) return;
                Uri result = data == null || resultCode != RESULT_OK ? null
                        : data.getData();
                mUploadMessage.onReceiveValue(result);
                mUploadMessage = null;
                break;
            case RequestCode.TAKE_FROM_CAMERA://照相
                if (resultCode == Activity.RESULT_OK) {
                    uploadImage2QiNiu(AppInfo.getInstace().getCameraTempPath());
                }
                break;
            case RequestCode.TAKE_FROM_PHOTO://图片
                if (resultCode == Activity.RESULT_OK) {
                    if (data.getData() != null) {
                        String imagepath = ImageUtils.getPathFromUri(NormalWebViewActivity.this, data.getData());
                        uploadImage2QiNiu(imagepath);
                    }
                }
                break;
        }
        switch (resultCode) {
            case CodeConstants.ScanOk:
//                T.s("扫描成功");
                assert data != null;
                String qr_code = data.getStringExtra("qr_code");
                if (!CMethod.isEmptyOrZero(qr_code)) {
                    webView.loadUrl("javascript:showQrCode('" + qr_code + "')");
                } else {
                    T.s("扫描失败");
                }
                break;
            case CodeConstants.ScanCancel:
                T.s("扫描失败");
                break;

        }
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_left:
                Finish();
                break;

            case R.id.tv_right:
                switch (right_btn_type) {
                    case 1:
                        webView.loadUrl("javascript:sumbmit()");
                        break;

                    case 2:
                        webView.loadUrl("javascript:sumbmit_click()");
                        break;

                    case 3:
                        webView.loadUrl("javascript:activityShare()");
                        break;

                }
                break;
        }
    }


    private boolean isOpenScanner = true;

    /**
     * 打开二维码扫描
     */
    private void openScanner() {
        if (isOpenScanner) {
            Intent openCameraIntent = new Intent(this, ScannerActivity.class);
            openCameraIntent.putExtra("from_tag", "web");
            startActivityForResult(openCameraIntent, CodeConstants.openScanWithWeb);
            isOpenScanner = false;
        }

    }


    /**
     * 选择照片或图片
     */
    private void uploadPic() {
        DialogUtils.uploadPicDialog(NormalWebViewActivity.this);
    }

    /**
     * 将图片上传至其牛
     *
     * @param imagePath
     */
    private void uploadImage2QiNiu(final String imagePath) {
        loadingDialog.show();
        new Thread(new Runnable() {
            @Override
            public void run() {
                String path = ImageUtils.handleUploadImage(NormalWebViewActivity.this, imagePath, 50);

                if (path != null) {
                    qiNiuUtils.uploadImage(path, new QiNiuUtils.ProgressHandler() {
                        @Override
                        public void progress(double percent) {
                            L.d("上传图片进度 ： " + percent * 100 + "%");
                        }
                    }, new QiNiuUtils.UploadHandler() {
                        @Override
                        public void ok(String filename) {
                            loadingDialog.dismiss();
                            String image_path = AppConstants.QINIU_IMG_DOMAIN + filename;
                            L.d("上传图片进度 ----image_path===： " + image_path);
                            webView.loadUrl("javascript:showImg('" + image_path + "','" + post_image_type + "')");
                        }

                        @Override
                        public void error(String err) {
                            loadingDialog.dismiss();
                        }
                    });
                }
            }
        }).start();

    }

    /**
     * 新浪微博分享
     */
    private void sendMultiMessage(ShareData shareData) {

        Bitmap bitmap = ImageUtils.imageZoom(shareData.getShare_bitmap());

        WeiboMultiMessage weiboMessage = new WeiboMultiMessage();//初始化微博的分享消息
        weiboMessage.textObject = ShareUtil.getTextObj(shareData.getShare_title());
        weiboMessage.imageObject = ShareUtil.getImageObj(bitmap);
        weiboMessage.mediaObject = ShareUtil.getWebpageObj(shareData.getShare_url(), shareData.getShare_title(), shareData.getShare_description(), bitmap);
        SendMultiMessageToWeiboRequest request = new SendMultiMessageToWeiboRequest();
        request.transaction = String.valueOf(System.currentTimeMillis());
        request.multiMessage = weiboMessage;

        AuthInfo authInfo = new AuthInfo(this, AppConstants.SINA_APP_KEY, AppConstants.SINA_REDIRECT_URL, AppConstants.SINA_SCOPE);
        Oauth2AccessToken accessToken = AccessTokenKeeper.readAccessToken(getApplicationContext());
        String token = "";
        if (accessToken != null) {
            token = accessToken.getToken();
        }
        mWeiboShareAPI.sendRequest(this, request, authInfo, token, new WeiboAuthListener() {

            @Override
            public void onWeiboException(WeiboException arg0) {
                L.e("123==arg0.getMessage()===" + arg0.getMessage());

            }

            @Override
            public void onComplete(Bundle bundle) {
                // TODO Auto-generated method stub
                Oauth2AccessToken newToken = Oauth2AccessToken.parseAccessToken(bundle);
                AccessTokenKeeper.writeAccessToken(getApplicationContext(), newToken);
            }

            @Override
            public void onCancel() {
            }
        });
    }

    private void sendShareData(final ShareData shareData) {
        loadingDialog.show();
        ShareUtil.getShareData4DownPic(NormalWebViewActivity.this, shareData, new ShareUtil.ShareDataBacklistener() {
            @Override
            public void downFinish(final ShareData shareData) {

                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        loadingDialog.dismiss();
                        int type = shareData.getShare_type();
                        switch (type) {
                            case 1:
                                ShareFactory.getInstance(NormalWebViewActivity.this).shareWX(shareData);
                                break;
                            case 2:
                                ShareFactory.getInstance(NormalWebViewActivity.this).shareWXFriend(shareData);
                                break;
                            case 3:
                                sendMultiMessage(shareData);
                                break;
                            case 4:
                                new ShareDialog(NormalWebViewActivity.this, new ShareListener() {
                                    @Override
                                    public void shareSina() {
                                        sendMultiMessage(shareData);
                                    }

                                    @Override
                                    public void shareWX() {
                                        ShareFactory.getInstance(NormalWebViewActivity.this).shareWX(shareData);
                                    }

                                    @Override
                                    public void shareWXF() {
                                        ShareFactory.getInstance(NormalWebViewActivity.this).shareWXFriend(shareData);
                                    }
                                }).show();
                                break;

                        }
                    }
                });

            }
        });

    }


    @Override
    public void onResponse(BaseResponse baseResp) {
        if (baseResp != null) {
            switch (baseResp.errCode) {
                case WBConstants.ErrorCode.ERR_OK:
                    T.s(R.string.weibosdk_demo_toast_share_success);
                    break;
                case WBConstants.ErrorCode.ERR_CANCEL:
                    T.s(R.string.weibosdk_demo_toast_share_canceled);
                    break;
                case WBConstants.ErrorCode.ERR_FAIL:
                    T.s(R.string.weibosdk_demo_toast_share_failed);
                    break;
            }
        }

    }

//
//    private void goBack() {
//
////       if (webView.canGoBack()) {
////           webView.goBack();
////       } else {
////           Finish();
////       }
//
//
//        if(netWorkState()==-1){
//            finish();
//        }
//
//        if (CMethod.isNet(NormalWebViewActivity.this)&&!isCanBack) {
//            webView.loadUrl("javascript:redirectURL()");
//        } else {
//            if (webView.canGoBack()) {
//                webView.goBack();
//            } else {
//                Finish();
//            }
//        }
//
//    }

    private void Finish() {
        if (webView.canGoBack()) {
            webView.goBack();
        } else {

            finish();
        }
    }


    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK)) {
            if (webView.canGoBack()) {
                webView.goBack();
            } else {

                finish();
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

}
