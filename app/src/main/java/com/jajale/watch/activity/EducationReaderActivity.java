package com.jajale.watch.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.PopupWindow;

import com.jajale.watch.BaseActivity;
import com.jajale.watch.R;
import com.jajale.watch.cviews.ReadView;
import com.jajale.watch.cviews.ReaderGuidePopupWindow;
import com.jajale.watch.cviews.ReaderTitlePopupWindow;
import com.jajale.watch.entity.BookListData;
import com.jajale.watch.entity.SPKeyConstants;
import com.jajale.watch.utils.CMethod;
import com.jajale.watch.utils.PhoneSPUtils;

import java.io.File;
import java.io.Serializable;
import java.util.List;

/**
 * 图书阅读页面
 * <p/>
 * Created by athena on 2015/12/31.
 * Email: lizhiqiang@bjjajale.com
 */
public class EducationReaderActivity extends BaseActivity implements View.OnClickListener, ReadView.OnPopupWindowListener {


    private ReadView readView;
    private ReaderGuidePopupWindow guideMenuWindow;
    private ReaderTitlePopupWindow titleMenuWindow;
    private PhoneSPUtils phoneSPUtils;
    private String book_path;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //当前一个页面被销毁时，退出
        if (EducationActivity.educationActivity.isFinishing()) {
            finish();
            return;
        }
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);

        initReadView();

        phoneSPUtils = new PhoneSPUtils(this);

        //是否弹出引导图，弹出后记录为已弹出，以后将不会弹出
        boolean isGuide = phoneSPUtils.getBoolean(SPKeyConstants.BOOK_GUIDE, true);
        if (isGuide) {
            readView.post(new Runnable() {
                @Override
                public void run() {
                    ShowGuideView();
                }
            });
        } else {
            hideStatusBar();//反之，全屏
        }
    }


    /**
     * 初始化readview
     */
    private void initReadView() {
        book_path = getIntent().getStringExtra("filePath");
        if (!CMethod.isEmpty(book_path)) {
            File dir = null;
            Uri fileUri = Uri.parse(book_path);
            if (fileUri != null) {
                dir = new File(fileUri.getPath());
            }
            readView = null;
            if (dir != null) {
                readView = new ReadView(this, dir.getPath());
                setContentView(readView);
            } else {
                finish();
            }

        } else {
            finish();
            return;
        }

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_left://返回键
                finish();
                break;
            case R.id.iv_right://目录
                if (guideMenuWindow != null)
                    guideMenuWindow.dismiss();

                List<BookListData> list = readView.getBookList();
                int list_length = readView.getListLength();
                if (list != null) {
                    Intent intent = new Intent(this, EducationListActivity.class);
                    intent.putExtra("bookListData", (Serializable) list);//传递目录列表数据
                    intent.putExtra("list_length", list_length);//传递json目录长度
                    intent.putExtra("filePath", book_path);//传递图书路径作为记录保存Key
                    String title = getIntent().getStringExtra("book_title");
                    title = CMethod.isEmpty(title) ? "教育" : title;
                    intent.putExtra("book_title", title);//传递图书路径作为记录保存Key
                    startActivityForResult(intent, 1);
                }
                break;
            case R.id.rl_all://点击其他控件，dismiss引导图及title栏及状态栏
                guideMenuWindow.dismiss();
                titleMenuWindow.dismiss();
                hideStatusBar();
                break;
        }
    }


    /**
     * 目录返回操作
     *
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == 1) {
            titleMenuWindow.dismiss();
            hideStatusBar();
            initReadView();

        }
    }


    /**
     * 展示引导图
     */
    public void ShowGuideView() {
        onShowPopupWindow();
        new WaitTask().execute();
    }


    private class WaitTask extends AsyncTask<Void, Void, String[]> {
        @Override
        protected String[] doInBackground(Void... params) {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
            }
            return null;
        }

        @Override
        protected void onPostExecute(String[] result) {
            super.onPostExecute(result);
            phoneSPUtils.save(SPKeyConstants.BOOK_GUIDE, false);
            guideMenuWindow = new ReaderGuidePopupWindow(EducationReaderActivity.this);
            try {
                guideMenuWindow.showAtLocation(readView, Gravity.TOP, 0, getIntent().getIntExtra("statusBarHeight", 50));
            } catch (Exception e) {

            }
            guideMenuWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
                @Override
                public void onDismiss() {
                    //隐藏状态栏
                    if (titleMenuWindow != null) {
                        titleMenuWindow.dismiss();
                    }
                    hideStatusBar();
                }
            });
        }
    }

    /**
     * 展示title栏
     */
    @Override
    public void onShowPopupWindow() {
        showStatusBar();
        //显示状态栏
        String title = getIntent().getStringExtra("book_title");
        title = CMethod.isEmpty(title) ? "教育" : title;

        titleMenuWindow = new ReaderTitlePopupWindow(EducationReaderActivity.this, this, title);
        titleMenuWindow.showAtLocation(readView, Gravity.TOP, 0, getIntent().getIntExtra("statusBarHeight", 50));
        titleMenuWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                //隐藏状态栏
                if (guideMenuWindow != null) {
                    guideMenuWindow.dismiss();
                }
                hideStatusBar();
            }
        });
    }

    /**
     * onPause阶段保存书籍position
     */
    @Override
    protected void onPause() {
        super.onPause();
        readView.setOnPause();
    }

    /**
     * 隐藏状态栏
     */
    private void hideStatusBar() {
        WindowManager.LayoutParams attrs = getWindow().getAttributes();
        attrs.flags |= WindowManager.LayoutParams.FLAG_FULLSCREEN;
        getWindow().setAttributes(attrs);
    }

    /**
     * 显示状态栏
     */
    private void showStatusBar() {
        WindowManager.LayoutParams attrs = getWindow().getAttributes();
        attrs.flags &= ~WindowManager.LayoutParams.FLAG_FULLSCREEN;
        getWindow().setAttributes(attrs);
    }


}
