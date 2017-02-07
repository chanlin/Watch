package com.jajale.watch.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.jajale.watch.R;
import com.jajale.watch.utils.ApkUtil;
import com.jajale.watch.utils.L;
import com.jajale.watch.utils.T;

import org.wlf.filedownloader.DownloadFileInfo;
import org.wlf.filedownloader.FileDownloader;
import org.wlf.filedownloader.listener.OnFileDownloadStatusListener;

/**
 * Created by athena on 2015/11/20.
 * Email: lizhiqiang@bjjajale.com
 */
public class DownLoadDialog extends Dialog implements View.OnClickListener, OnFileDownloadStatusListener {


    private String mPath;
    private Context mContext;
    private ProgressBar progressBar;
    private TextView textView;
    private TextView tv_dialog_title;


    public DownLoadDialog(Context context, String path) {
        super(context);

        this.mContext = context;
        this.mPath = path;
        int layoutId = R.layout.dialog_download;
        setContentView(layoutId);
        Window window = getWindow();
        WindowManager.LayoutParams params = window.getAttributes();
        params.gravity = Gravity.CENTER;
        window.setBackgroundDrawableResource(android.R.color.transparent);
        window.setAttributes(params);
        setCanceledOnTouchOutside(false);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        progressBar = (ProgressBar) findViewById(R.id.dialog_download_pb);
        textView = (TextView) findViewById(R.id.dialog_download_tv_state);
        tv_dialog_title = (TextView) findViewById(R.id.tv_dialog_title);
        tv_dialog_title.setText("正在更新");
        FileDownloader.registerDownloadStatusListener(this);
        FileDownloader.start(mPath);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_cancel:
                dismiss();
            case R.id.btn_ok:
                dismiss();

                break;
        }
    }


    @Override
    public void onFileDownloadStatusWaiting(DownloadFileInfo downloadFileInfo) {
        // 等待下载（等待其它任务执行完成，或者FileDownloader在忙别的操作）
        L.e("123==OnFileDownloadStatusListener===等待下载");
    }

    @Override
    public void onFileDownloadStatusPreparing(DownloadFileInfo downloadFileInfo) {
        // 准备中（即，正在连接资源）
        L.e("123==OnFileDownloadStatusListener===正在连接资源");
    }

    @Override
    public void onFileDownloadStatusPrepared(DownloadFileInfo downloadFileInfo) {
        // 已准备好（即，已经连接到了资源）
        L.e("123==OnFileDownloadStatusListener===已经连接到了资源");
    }

    @Override
    public void onFileDownloadStatusDownloading(DownloadFileInfo downloadFileInfo, float downloadSpeed, long
            remainingTime) {
        // 正在下载，downloadSpeed为当前下载速度，单位KB/s，remainingTime为预估的剩余时间，单位秒
        L.e("123==OnFileDownloadStatusListener===正在下载");
        int totalSize = (int) downloadFileInfo.getFileSizeLong();
        int downloaded = (int) downloadFileInfo.getDownloadedSizeLong();
        double rate = (double) totalSize / Integer.MAX_VALUE;
        if (rate > 1) {
            totalSize = Integer.MAX_VALUE;
            downloaded = (int) (downloaded / rate);
        }
        L.e("123==OnFileDownloadStatusListener===downloaded==" + downloaded);
        progressBar.setMax(totalSize);
        progressBar.setProgress(downloaded);

        double downloadSize = downloadFileInfo.getDownloadedSizeLong() / 1024f / 1024;
        double fileSize = downloadFileInfo.getFileSizeLong() / 1024f / 1024;


        String tvDownloadSize = ((float) (Math.round(downloadSize * 100)) / 100) + "M/";
        String tvTotalSize = ((float) (Math.round(fileSize * 100)) / 100) + "M";


        textView.setText(tvDownloadSize + tvTotalSize);


    }

    @Override
    public void onFileDownloadStatusPaused(DownloadFileInfo downloadFileInfo) {
        // 下载已被暂停
        L.e("123==OnFileDownloadStatusListener===下载已被暂停");
    }

    @Override
    public void onFileDownloadStatusCompleted(DownloadFileInfo downloadFileInfo) {
        // 下载完成（整个文件已经全部下载完成）
        L.e("123==OnFileDownloadStatusListener=== 下载完成（整个文件已经全部下载完成）");
        tv_dialog_title.setText("更新完成");

        double downloadSize = downloadFileInfo.getDownloadedSizeLong() / 1024f / 1024;
        L.e("123==OnFileDownloadStatusListener==downloadSize==" + downloadSize);

        double fileSize = downloadFileInfo.getFileSizeLong() / 1024f / 1024;
        L.e("123==OnFileDownloadStatusListener==fileSize==" + fileSize);
        if (downloadFileInfo.getFileName().contains("apk")) {// apk

            final String packageName = ApkUtil.getUnInstallApkPackageName(mContext,
                    downloadFileInfo.getFilePath());

            if (packageName != null) {
                this.dismiss();
                boolean isInstall = ApkUtil.checkAppInstalled(mContext, packageName);
                L.e("123==OnFileDownloadStatusListener==isInstall==" + isInstall);
                L.e("123==OnFileDownloadStatusListener==packageName==" + packageName);
                ApkUtil.installApk(mContext, downloadFileInfo.getFilePath());
            } else {
                FileDownloader.reStart(mPath);
            }

        }
    }

    @Override
    public void onFileDownloadStatusFailed(String url, DownloadFileInfo downloadFileInfo, FileDownloadStatusFailReason failReason) {
        // 下载失败了，详细查看失败原因failReason，有些失败原因你可能必须关心

        DownLoadDialog.this.dismiss();

        String failType = failReason.getType();
        String failUrl = failReason.getUrl();// 或：failUrl = url，url和failReason.getUrl()会是一样的

        if (FileDownloadStatusFailReason.TYPE_URL_ILLEGAL.equals(failType)) {
            // 下载failUrl时出现url错误
            L.e("123==OnFileDownloadStatusListener===" + "下载失败了==" + 1);
        } else if (FileDownloadStatusFailReason.TYPE_STORAGE_SPACE_IS_FULL.equals(failType)) {
            // 下载failUrl时出现本地存储空间不足
            L.e("123==OnFileDownloadStatusListener===" + "下载失败了==" + 2);
            T.s("空间不足或下载文件夹已被删除");
        } else if (FileDownloadStatusFailReason.TYPE_NETWORK_DENIED.equals(failType)) {
            // 下载failUrl时出现无法访问网络
            L.e("123==OnFileDownloadStatusListener===" + "下载失败了==" + 3);
        } else if (FileDownloadStatusFailReason.TYPE_NETWORK_TIMEOUT.equals(failType)) {
            // 下载failUrl时出现连接超时
            L.e("123==OnFileDownloadStatusListener===" + "下载失败了==" + 4);
        } else {
            // 更多错误....
            L.e("123==OnFileDownloadStatusListener===" + "下载失败了==" + 5);
        }

        // 查看详细异常信息
        Throwable failCause = failReason.getCause();// 或：failReason.getOriginalCause()

        // 查看异常描述信息
        String failMsg = failReason.getMessage();// 或：failReason.getOriginalCause().getMessage()

        L.e("123==OnFileDownloadStatusListener===" + "下载失败了==" + failMsg);
        L.e("123==OnFileDownloadStatusListener===" + "下载失败了==" + failType);
    }
}
