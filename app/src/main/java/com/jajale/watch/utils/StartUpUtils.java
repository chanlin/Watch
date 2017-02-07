package com.jajale.watch.utils;

import android.content.Context;

import com.jajale.watch.R;
import com.jajale.watch.helper.DownloadHelper;

import java.io.File;

/**
 * 处理启动图
 * Created by chunlongyuan on 12/8/15.
 */
public class StartUpUtils {

    private Context context;

    public StartUpUtils(Context context) {
        this.context = context;
    }

    /*
    启动图放在卡上，有更新就下载
     */


    /**
     * 如果该文件名没下载过 就去下载
     *
     * @param imageUrl
     */
    public void handle(String imageUrl) {
        String md5 = Md5Util.string2MD5(imageUrl);
        String packageName = context.getPackageName();
        final String filename = "/data/data/" + packageName + "/" + md5 + "";
        File file = new File(filename);
        if (!file.exists()) {
            DownloadHelper downloadHelper = new DownloadHelper(context);
            try {
                downloadHelper.download(imageUrl, filename, new DownloadHelper.OnProgress() {
                    @Override
                    public void update(int progress) {
                        if (progress == 100) {
                            setImageName(filename);
                        }
                    }

                    @Override
                    public void onFail() {

                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void setImageName(String filename) {
    }

    /**
     * 如果返回数字 就是资源图片
     * 如果返回文件名 就是本地图片
     *
     * @return
     */
    public String getImageName() {
        return R.mipmap.splash + "";
    }
}
