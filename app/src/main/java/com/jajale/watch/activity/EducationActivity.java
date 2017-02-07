package com.jajale.watch.activity;

import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.jajale.watch.R;
import com.jajale.watch.adapter.EducationBookListAdapter;
import com.jajale.watch.dialog.EducationPdfListDialog;
import com.jajale.watch.entity.BookData;
import com.jajale.watch.helper.DownloadHelper;
import com.jajale.watch.listener.HttpClientListener;
import com.jajale.watch.listener.SimpleClickListener;
import com.jajale.watch.utils.CMethod;
import com.jajale.watch.utils.CacheUtils;
import com.jajale.watch.utils.DialogUtils;
import com.jajale.watch.utils.HttpUtils;
import com.jajale.watch.utils.L;
import com.jajale.watch.utils.LoadingDialog;
import com.jajale.watch.utils.T;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * 教育首页，现为教育图书列表,修改为gridView布局
 * <p/>
 * modify by guokaimin on 2015/12/31.
 * Email: guokaimin@bjjajale.com
 */
public class EducationActivity extends NoNetworkActivity implements View.OnClickListener {

//    private String path = "http://192.168.1.125:8082/cms/books/book_list.do";//书籍测试地址
//    private String path="http://cms.bjjajale.com/cms/books/book_list.do";//书籍线上地址

    private EducationBookListAdapter adapter;
    public static EducationActivity educationActivity;

    public static boolean isDestroy = false;
    @InjectView(R.id.iv_left)
    ImageView ivLeft;
    @InjectView(R.id.tv_left_2)
    TextView tvLeft2;
    @InjectView(R.id.tv_middle)
    TextView tvMiddle;
    @InjectView(R.id.tv_right)
    TextView tvRight;
    @InjectView(R.id.iv_right)
    ImageView ivRight;
    @InjectView(R.id.layout_title)
    RelativeLayout layoutTitle;
   /* @InjectView(R.id.book_lv_content)
    ListView bookLvContent;*/

    @InjectView(R.id.book_lv_content)
    GridView bookLvContent;

    private LoadingDialog loadingDialog;
    private BookData bookData;
    private EducationPdfListDialog pdfdialog;

    @Override
    protected void onDestroy() {
        super.onDestroy();
        isDestroy = true;
    }

    @Override
    public void refreshView() {
        super.refreshView();
        setBookListFromNetWork();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        educationActivity = this;
        setContentView(R.layout.activity_education);
        ButterKnife.inject(this);
        showNotworkView();
        loadingDialog = new LoadingDialog(this);
        initView();
//        test();
        setBookListFromNetWork();
    }

    private String getPdfurl(String url, int position) {
        try {
            String path = url.split("\\.pdf")[0] + "-" + (position + 1) + ".pdf";
            return path;
        } catch (Exception e) {

        }
        return "";
    }

    private void initView() {
//        layoutTitle.setBackgroundColor(getResources().getColor(R.color.edu_title_bg));
        layoutTitle.setBackgroundResource(R.mipmap.nav_bar);
        tvMiddle.setText(getResources().getString(R.string.education));
        ivLeft.setImageResource(R.drawable.title_goback_selector);
        ivLeft.setOnClickListener(this);
        bookLvContent.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                //获取状态栏高度
                if (CMethod.isFastDoubleClick())
                    return;

                bookData = adapter.getItem(position);
                String prefix = getPrefixName(bookData.getBook_url());
                if (prefix.equals("txt")) {
                    downloadBook(bookData.getBook_url(), bookData.getBook_id() + "");
                } else if (prefix.equals("pdf")) {
                    pdfdialog = new EducationPdfListDialog(EducationActivity.this, bookData.getBook_name(), bookData.getBook_url(), bookData.getBook_id(), pdfOnItemClickListener);
                    pdfdialog.show();
                }

            }
        });
    }

    AdapterView.OnItemClickListener pdfOnItemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, final View view, final int position, long id) {

            if (CMethod.isFastDoubleClick())
                return;
            downloadBookPdf(getPdfurl(bookData.getBook_url(), position), bookData.getBook_id() + "—" + position, view);

        }

    };

    private String getPrefixName(String fileName) {
        String prefix = fileName.substring(fileName.lastIndexOf(".") + 1);
        return prefix;
    }

    private void openReader(String filePath) {
        Rect frame = new Rect();
        getWindow().getDecorView().getWindowVisibleDisplayFrame(frame);
        int statusBarHeight = frame.top;
        L.e("reader___statusBarHeight==" + statusBarHeight);
        loadingDialog.dismiss();
        Intent intent_education_reader = new Intent(EducationActivity.this, EducationReaderActivity.class);
        intent_education_reader.putExtra("statusBarHeight", statusBarHeight);//传递状态栏高度
        intent_education_reader.putExtra("book_title", bookData.getBook_name());//传递书名作为阅读器title
        intent_education_reader.putExtra("filePath", filePath);//传递图书路径
        startActivity(intent_education_reader);
    }


    public String getNetfilePath(String bookId) {
        String cacheDir = CacheUtils.getExternalBookCacheDir(getApplicationContext());
        String suffixes = ".txt";
        return cacheDir + bookId + suffixes;
    }

    public String getNetfilePathPdf(String bookId) {
        String cacheDir = CacheUtils.getExternalBookCacheDir(getApplicationContext());
        String suffixes = ".pdf";
        return cacheDir + bookId + suffixes;
    }


    /**
     * 下载书籍
     *
     * @param url
     * @param bookId
     */
    private void downloadBook(String url, String bookId) {
        loadingDialog.show();
        final String netfilePath = getNetfilePath(bookId);
        File file = new File(netfilePath);
        if (file.exists() && file.length() > 0) {
            //有本地数据
            openReader(netfilePath);
        } else {
            //没有本地数据

            if (CMethod.isNet(EducationActivity.this)) {
                DownloadHelper downloadHelper = new DownloadHelper();
                try {
                    downloadHelper.download(url, netfilePath, new DownloadHelper.OnProgress() {
                        @Override
                        public void update(int progress) {

                            if (progress == 100) {
                                openReader(netfilePath);
                            }
                        }

                        @Override
                        public void onFail() {

                            EducationActivity.this.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    loadingDialog.dismiss();
                                    T.s("图书打开失败！");
                                }
                            });
                        }
                    });
                } catch (Exception e) {

                    EducationActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            loadingDialog.dismiss();
                            T.s("图书打开失败！");
                        }
                    });

                }
            } else {
                loadingDialog.dismiss();
                T.s("网络不给力");
            }
        }

    }


    /**
     * 下载书籍pdf
     *
     * @param url
     * @param bookId
     */
    private void downloadBookPdf(final String url, String bookId, final View view) {
        if (CMethod.isEmpty(url))
            return;

        final TextView textView_state = (TextView) view.findViewById(R.id.text_state);
        if (textView_state.getText().toString().contains("%"))
            return;
        final String netfilePath = getNetfilePathPdf(bookId);
        File file = new File(netfilePath);
        if (file.exists() && file.length() > 0) {
            //有本地数据
            Intent intent = new Intent(EducationActivity.this, PDFReaderActivity.class);
            intent.putExtra("filepath", netfilePath);
            startActivity(intent);
        } else {
            //没有本地数据
            if (CMethod.isNet(EducationActivity.this)) {
                if (!CMethod.getCurrentNetworkType(EducationActivity.this).equals("Wi-Fi")) {
                    DialogUtils.loadingBookDialog(EducationActivity.this, new SimpleClickListener() {
                        @Override
                        public void ok() {
                            loadPdfBook(url, netfilePath, textView_state);
                        }

                        @Override
                        public void cancle() {
                        }
                    });
                } else {
                    loadPdfBook(url, netfilePath, textView_state);
                }
            } else {
                T.s("网络不给力");
            }
        }

    }

    /**
     * 下载pdf书籍
     *
     * @param url
     * @param netfilePath
     * @param textView_state
     */
    private void loadPdfBook(final String url, final String netfilePath, final TextView textView_state) {
        pdfdialog.setCancelable(false);
        DownloadHelper downloadHelper = new DownloadHelper();
        try {
            downloadHelper.download(url, netfilePath, new DownloadHelper.OnProgress() {
                @Override
                public void update(final int progress) {
                    EducationActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            pdfdialog.setCancelable(false);
                            textView_state.setText(progress + "%");
                        }
                    });
                    if (progress == 100) {
                        EducationActivity.this.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                textView_state.setText("已下载");
                                pdfdialog.setCancelable(true);
                            }
                        });

                    }
                }

                @Override
                public void onFail() {
                    EducationActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            pdfdialog.setCancelable(true);
                            textView_state.setText("下载失败");
                            delFile(netfilePath);
                            T.s("图书下载失败！");
                        }
                    });
                }
            });
        } catch (Exception e) {

            EducationActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    pdfdialog.setCancelable(true);
                    textView_state.setText("下载失败");
                    delFile(netfilePath);
                    T.s("图书下载失败！");
                }
            });

        }
    }

    public void delFile(String fileName) {
        File file = new File(fileName);
        if (file.isFile()) {
            file.delete();
        }
        file.exists();
    }

    /**
     * 从网络获取书籍数据
     */
    private void setBookListFromNetWork() {
        loadingDialog.show();
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("book_type", "-1");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        HttpUtils.PostDataToWebBook(jsonObject, new HttpClientListener() {
            @Override
            public void onSuccess(String result) {
                loadingDialog.dismiss();
                Gson gson = new Gson();
                JsonParser parser = new JsonParser();
                JsonArray jsonArray = parser.parse(result).getAsJsonArray();
                List<BookData> lcs = new ArrayList<BookData>();
                for (JsonElement obj : jsonArray) {
                    BookData cse = gson.fromJson(obj, BookData.class);
                    lcs.add(cse);
                }

                adapter = new EducationBookListAdapter(EducationActivity.this, lcs);
                bookLvContent.setAdapter(adapter);
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


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_left://返回键
                finish();
                break;
        }
    }


}
