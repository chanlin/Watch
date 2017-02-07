package com.jajale.watch.utils;

import android.content.Context;
import android.text.TextUtils;

import com.qiniu.android.http.ResponseInfo;
import com.qiniu.android.storage.UpCompletionHandler;
import com.qiniu.android.storage.UpProgressHandler;
import com.qiniu.android.storage.UploadManager;
import com.qiniu.android.storage.UploadOptions;

import org.json.JSONObject;

import java.io.File;

/**
 * Created by athena on 2015/11/30.
 * Email: lizhiqiang@bjjajale.com
 */
public class QiNiuUtils {


    private Context mContext;
    private UploadManager uploadManager;
    private String ERR_FILE_NOT_EXIST = "文件不存在";
    /**
     * 测试token，最终从config取
     */
    private String date_img_token = "QvzQ-E3bJiOXQfwaYHJvX6Qk__uDyZse5L1dG70y:9CJRyZBQgStOunXDIKxsSC37h7M=:eyJzY29wZSI6ImltYWdlIiwiZGVhZGxpbmUiOjMzNDI2OTE2MDh9";

//    QvzQ-E3bJiOXQfwaYHJvX6Qk__uDyZse5L1dG70y:NagDu3eGecOh22XUg/3f8GIUrgw=:eyJzY29wZSI6ImltYWdlIiwiZGVhZGxpbmUiOjMzNDI2OTE1NzF9
//    QvzQ-E3bJiOXQfwaYHJvX6Qk__uDyZse5L1dG70y:SOnVHc/3dCG0pXV2ANeaNsDFH2E=:eyJzY29wZSI6ImltYWdlIiwiZGVhZGxpbmUiOjMzNDI2OTE1ODN9
//    QvzQ-E3bJiOXQfwaYHJvX6Qk__uDyZse5L1dG70y:nXHeFg33xNglI1jBh/zJ6QwBoRY=:eyJzY29wZSI6ImltYWdlIiwiZGVhZGxpbmUiOjMzNDI2OTE1OTd9
//    QvzQ-E3bJiOXQfwaYHJvX6Qk__uDyZse5L1dG70y:9CJRyZBQgStOunXDIKxsSC37h7M=:eyJzY29wZSI6ImltYWdlIiwiZGVhZGxpbmUiOjMzNDI2OTE2MDh9
//    QvzQ-E3bJiOXQfwaYHJvX6Qk__uDyZse5L1dG70y:wltThul/cnPRLjwPv++jOmIHjVo=:eyJzY29wZSI6ImltYWdlIiwiZGVhZGxpbmUiOjMzNDI2OTE2MTZ9


    private String date_media_token = "QvzQ-E3bJiOXQfwaYHJvX6Qk__uDyZse5L1dG70y:qbMbFLzGkvhWWbbfzzfCxVLKR90=:eyJzY29wZSI6InZvaWNlIiwiZGVhZGxpbmUiOjMzNDI2OTEwNjh9";
//                                     QvzQ-E3bJiOXQfwaYHJvX6Qk__uDyZse5L1dG70y:DkQlMI3qecGWKZPo+S1Yu2bDmyw=:eyJzY29wZSI6InZvaWNlIiwiZGVhZGxpbmUiOjMzNDI2OTEwNTd9
//                                     QvzQ-E3bJiOXQfwaYHJvX6Qk__uDyZse5L1dG70y:qbMbFLzGkvhWWbbfzzfCxVLKR90=:eyJzY29wZSI6InZvaWNlIiwiZGVhZGxpbmUiOjMzNDI2OTEwNjh9
//                                     QvzQ-E3bJiOXQfwaYHJvX6Qk__uDyZse5L1dG70y:jDS70Mb8Ztu++IT8bAl0p5CeLgA=:eyJzY29wZSI6InZvaWNlIiwiZGVhZGxpbmUiOjMzNDI2OTEwNzZ9
//                                     QvzQ-E3bJiOXQfwaYHJvX6Qk__uDyZse5L1dG70y:MXi4KlQBPbmUQ7w6d7i+VebKQ4k=:eyJzY29wZSI6InZvaWNlIiwiZGVhZGxpbmUiOjMzNDI2OTExODJ9
//                                     QvzQ-E3bJiOXQfwaYHJvX6Qk__uDyZse5L1dG70y:w42bUnjShUlwhNhqU8g6x1tHdbU=:eyJzY29wZSI6InZvaWNlIiwiZGVhZGxpbmUiOjMzNDI2OTEyMDB9

    private String data_file_token = "QvzQ-E3bJiOXQfwaYHJvX6Qk__uDyZse5L1dG70y:HywvLkUDaRcITN2OP5BXIkq4J2M=:eyJzY29wZSI6ImZpbGUiLCJkZWFkbGluZSI6MzM0MjY5MTQxM30=";
//    QvzQ-E3bJiOXQfwaYHJvX6Qk__uDyZse5L1dG70y:HywvLkUDaRcITN2OP5BXIkq4J2M=:eyJzY29wZSI6ImZpbGUiLCJkZWFkbGluZSI6MzM0MjY5MTQxM30=
//    QvzQ-E3bJiOXQfwaYHJvX6Qk__uDyZse5L1dG70y:/Nb6EbYaN+6wtVjvZJ4af+zA5e8=:eyJzY29wZSI6ImZpbGUiLCJkZWFkbGluZSI6MzM0MjY5MTQ3MH0=
//    QvzQ-E3bJiOXQfwaYHJvX6Qk__uDyZse5L1dG70y:46ytOJbIm8N+al9KpNQX09Zfho0=:eyJzY29wZSI6ImZpbGUiLCJkZWFkbGluZSI6MzM0MjY5MTUwM30=
//    QvzQ-E3bJiOXQfwaYHJvX6Qk__uDyZse5L1dG70y:9302XOggHijcex46AcsaviJaNG0=:eyJzY29wZSI6ImZpbGUiLCJkZWFkbGluZSI6MzM0MjY5MTQ4M30=
//    QvzQ-E3bJiOXQfwaYHJvX6Qk__uDyZse5L1dG70y:wo90laJXZdbEPjtyDszHLFt4oEo=:eyJzY29wZSI6ImZpbGUiLCJkZWFkbGluZSI6MzM0MjY5MTUzMH0=

    public QiNiuUtils(Context context) {
        this.mContext = context;
        uploadManager = new UploadManager();
        ConfigPreferencesUtils cpUtils = new ConfigPreferencesUtils(mContext);
        if (!CMethod.isEmpty(cpUtils.getCowPicToken())) {
            date_img_token = cpUtils.getCowPicToken();
        }
        if (!CMethod.isEmpty(cpUtils.getCowMediaToken())) {
            date_media_token = cpUtils.getCowMediaToken();
        }
    }

    /**
     * 回调进度
     */
    public interface ProgressHandler {
        void progress(double percent);
    }

    /**
     * 回调上传结果
     */
    public interface UploadHandler {
        void ok(String filename);

        void error(String err);
    }

    /**
     * 上传图片
     *
     * @param file
     * @param uploadHandler
     */
//    public void uploadImage(File file, final UploadHandler uploadHandler) {
//        if (exists(file)) {
//            upload(file, date_token, null, uploadHandler);
//        } else {
//            uploadHandler.error(ERR_FILE_NOT_EXIST);
//        }
//    }

    /**
     * 上传图片
     *
     * @param filepath
     * @param progressHandler
     * @param uploadHandler
     */
    public void uploadImage(String filepath, final ProgressHandler progressHandler, final UploadHandler uploadHandler) {
        if (!TextUtils.isEmpty(filepath)) {
            File file = new File(filepath);
            if (file.exists()) {
                upload(file, date_img_token, progressHandler, uploadHandler);
                return;
            }
        }
        uploadHandler.error(ERR_FILE_NOT_EXIST);
    }

    /**
     * 上传音视频文件
     *
     * @param file
     * @param uploadHandler
     */
//    public void uploadMedia(File file, final UploadHandler uploadHandler) {
//        if (exists(file)) {
//            upload(file, date_media_token, null, uploadHandler);
//        } else {
//            uploadHandler.error(ERR_FILE_NOT_EXIST);
//        }
//    }

    /**
     * 上传音视频文件
     *
     * @param filepath
     * @param progressHandler
     * @param uploadHandler
     */
    public void uploadMedia(String filepath, final ProgressHandler progressHandler, final UploadHandler uploadHandler) {
        if (!TextUtils.isEmpty(filepath)) {
            File file = new File(filepath);
            if (file.exists()) {
                upload(file, date_media_token, progressHandler, uploadHandler);
                return;
            }
        }
        uploadHandler.error(ERR_FILE_NOT_EXIST);
    }

    private void upload(File file, String token, final ProgressHandler progressHandler, final UploadHandler uploadHandler) {

        UploadOptions opt = null;
        if (progressHandler != null) {

            opt = new UploadOptions(null, null, true, new UpProgressHandler() {
                @Override
                public void progress(String key, double percent) {
                    progressHandler.progress(percent);
                }
            }, null);
        }

        uploadManager.put(file, null, token,
                new UpCompletionHandler() {
                    @Override
                    public void complete(String key, ResponseInfo info, JSONObject response) {
                        try {

                            if (info.isOK()) {//上传成功，更新资料
                                String filename = response.getString("key");//最终文件名
                                uploadHandler.ok(filename);
                            } else {//失败
                                uploadHandler.error(info.error);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            uploadHandler.error(e.getMessage());
                        }
                    }
                }, opt);
    }

//    private boolean exists(File file) {
//        return file != null && file.exists();
//    }


}
