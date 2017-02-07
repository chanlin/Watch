package com.jajale.watch.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.jajale.watch.R;
import com.jajale.watch.utils.L;
import com.jajale.watch.utils.LoadingDialog;

import java.lang.reflect.InvocationTargetException;

/**
 * Created by llh on 16-5-23.
 */
public class AQYPlayActivity extends Activity {
    private FrameLayout videoview;// 全屏时视频加载view
    private WebView videowebview;
    private View xCustomView;
    private xWebChromeClient xwebchromeclient;
    private String url;
    ImageView ivLeft;
    TextView tvMiddle;
    private WebChromeClient.CustomViewCallback 	xCustomViewCallback;
    LoadingDialog dialog;
     String ONRESUME="onResume";
     String ONPAUSE="onPause";
    private Boolean islandport = true;//true表示此时是竖屏，false表示此时横屏。

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//		requestWindowFeature(Window.FEATURE_NO_TITLE);//去掉应用标题
//		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
//	            WindowManager.LayoutParams.FLAG_FULLSCREEN);
        L.i("guokm", "onCreate");
        setContentView(R.layout.aqy_activity);

        initView();
        initData();
        initwidget();


    }

    private void initData() {
        Intent intent = getIntent();
        url = intent.getStringExtra("url");
    }
    private void initView() {
        tvMiddle= (TextView) findViewById(R.id.tv_middle);
        ivLeft= (ImageView) findViewById(R.id.iv_left);
        tvMiddle.setText(getResources().getString(R.string.see_the_word_two));
        ivLeft.setImageResource(R.drawable.title_goback_selector);
        ivLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        videoview = (FrameLayout) findViewById(R.id.video_view);
        videowebview = (WebView) findViewById(R.id.video_webview);
        dialog=new LoadingDialog(AQYPlayActivity.this);
        dialog.Cancelable(true);
    }
    private void initwidget() {
        // TODO Auto-generated method stub
        dialog.show();
        videowebview.loadUrl(url);
        WebSettings ws = videowebview.getSettings();
        ws.setBuiltInZoomControls(true);// 隐藏缩放按钮
        ws.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NARROW_COLUMNS);// 排版适应屏幕
        ws.setUseWideViewPort(true);// 可任意比例缩放
        ws.setLoadWithOverviewMode(true);// setUseWideViewPort方法设置webview推荐使用的窗口。setLoadWithOverviewMode方法是设置webview加载的页面的模式。
        ws.setSavePassword(true);
        ws.setSaveFormData(true);// 保存表单数据
        ws.setJavaScriptEnabled(true);
        ws.setGeolocationEnabled(true);// 启用地理定位
        ws.setGeolocationDatabasePath(this.getFilesDir().getPath()+"/databases/");// 设置定位的数据库路径
        ws.setDomStorageEnabled(true);
        xwebchromeclient = new xWebChromeClient();
        videowebview.setWebChromeClient(xwebchromeclient);
        videowebview.setWebViewClient(new xWebViewClientent());
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (inCustomView()) {
                hideCustomView();
                return true;
            }else if(videowebview.canGoBack()) {
                videowebview.goBack();
            }else {
                    videowebview.loadUrl("about:blank");
//		       		 mTestWebView.loadData("", "text/html; charset=UTF-8", null);
                    AQYPlayActivity.this.finish();
                    L.i("guokm", "===>>>2");
                }
            }
        return false;
    }
    /**
     * 判断是否是全屏
     * @return
     */
    public boolean inCustomView() {
        return (xCustomView != null);
    }
    /**
     * 全屏时按返加键执行退出全屏方法
     */
    public void hideCustomView() {
        xwebchromeclient.onHideCustomView();
    }
    /**
     * 处理Javascript的对话框、网站图标、网站标题以及网页加载进度等
     * @author
     */
    public class xWebChromeClient extends WebChromeClient {
        private Bitmap xdefaltvideo;
        private View xprogressvideo;
        @Override
        //播放网络视频时全屏会被调用的方法
        public void onShowCustomView(View view, WebChromeClient.CustomViewCallback callback)
        {
            if (islandport) {
            }
            else{
				setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
            }
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
            videowebview.setVisibility(View.GONE);
            //如果一个视图已经存在，那么立刻终止并新建一个
            if (xCustomView != null) {
                callback.onCustomViewHidden();
                return;
            }
            videoview.addView(view);
            xCustomView = view;
            xCustomViewCallback = callback;
            videoview.setVisibility(View.VISIBLE);
        }
        @Override
        //视频播放退出全屏会被调用的
        public void onHideCustomView() {
            if (xCustomView == null)//不是全屏播放状态
                return;
            // Hide the custom view.
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
            xCustomView.setVisibility(View.GONE);
            // Remove the custom view from its container.
            videoview.removeView(xCustomView);
            xCustomView = null;
            videoview.setVisibility(View.GONE);
            xCustomViewCallback.onCustomViewHidden();
            videowebview.setVisibility(View.VISIBLE);


        }
        //视频加载添加默认图标
        @Override
        public Bitmap getDefaultVideoPoster() {
            //Log.i(LOGTAG, "here in on getDefaultVideoPoster");
            if (xdefaltvideo == null) {
                xdefaltvideo = BitmapFactory.decodeResource(
                        getResources(), R.mipmap.bg_kanner_default);
            }
            return xdefaltvideo;
        }
        //视频加载时进程loading
        @Override
        public View getVideoLoadingProgressView() {
            //Log.i(LOGTAG, "here in on getVideoLoadingPregressView");

            if (xprogressvideo == null) {
                LayoutInflater inflater = LayoutInflater.from(AQYPlayActivity.this);
                xprogressvideo = inflater.inflate(R.layout.video_loading_progress, null);
            }
            return xprogressvideo;
        }
        //网页标题
        @Override
        public void onReceivedTitle(WebView view, String title) {
//            (MainActivity.this).setTitle(title);
        }

//         @Override
//       //当WebView进度改变时更新窗口进度
//         public void onProgressChanged(WebView view, int newProgress) {
//        	 (MainActivity.this).getWindow().setFeatureInt(Window.FEATURE_PROGRESS, newProgress*100);
//         }
    }

    /**
     * 处理各种通知、请求等事件
     * @author
     */
    public class xWebViewClientent extends WebViewClient {

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url + "?apiKey=a813b5c475d74730b517748d713bab4");
//            L.i("webviewtest", "shouldOverrideUrlLoading: "+url);
            return true;
        }
        @Override
        public void onPageFinished(WebView view, String url) {
            if(dialog.isShowing())
            {
                dialog.dismiss();
            }

        }
    }
    /**
     * 当横竖屏切换时会调用该方法
     * @author
     */
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        L.i("guokm", "=====<<<  onConfigurationChanged  >>>=====");
        super.onConfigurationChanged(newConfig);
        if(newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE){
            L.i("guokm", "   现在是横屏1");
            islandport = false;
        }else if(newConfig.orientation == Configuration.ORIENTATION_PORTRAIT){
            L.i("guokm", "   现在是竖屏1");
            islandport = true;
        }
/*        try {
            videowebview.getClass().getMethod(ONPAUSE).invoke(videowebview, (Object[]) null);
            videowebview.getClass().getMethod(ONRESUME).invoke(videowebview, (Object[]) null);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }*/

    }

    @Override
    protected void onPause() {
//        videowebview.reload();
        try {
            videowebview.getClass().getMethod(ONPAUSE).invoke(videowebview,(Object[])null);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
        super.onPause();

    }

    @Override
    protected void onResume() {
//        videowebview.onResume();
        try {
            videowebview.getClass().getMethod(ONRESUME).invoke(videowebview,(Object[])null);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
        super.onResume();
    }

}