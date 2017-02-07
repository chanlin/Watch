package com.jajale.watch.utils;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.os.Environment;
import android.widget.ImageView;

import com.google.gson.Gson;
import com.jajale.watch.AppConstants;
import com.jajale.watch.R;
import com.jajale.watch.entity.SplashImageData;
import com.jajale.watch.listener.HttpClientListener;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by lilonghui on 2015/12/11.
 * Email:lilonghui@bjjajale.com
 */
public class SplashPictureUtils {

    private Context mContext;
    private PhoneSPUtils phoneSPUtils;

    public SplashPictureUtils(Context context) {

        this.mContext = context;
        phoneSPUtils = new PhoneSPUtils(mContext);

    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    public void setSplashBg(ImageView view) {
        String sp_path = phoneSPUtils.getString("splash_image");
        boolean sdCardExist = Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED);
        if (sp_path != null) {
            Bitmap bitmap = getBitmaptoFile(sp_path);
            if (bitmap == null) {
                view.setBackgroundResource(R.mipmap.splash);
            } else {
                view.setBackground(new BitmapDrawable(null, bitmap));
                getImagePathFromNetwork();
            }
        } else if (sdCardExist) {
            view.setBackgroundResource(R.mipmap.splash);
            getImagePathFromNetwork();
        } else {
            L.e("sd卡不存在");
        }
    }


    /**
     * 下载图片
     *
     * @param url
     */
    private void downLoadPicture(final String url) {

        try {
            Bitmap bitmap = Picasso.with(mContext).load(url).get();
            saveFile(bitmap, url);
            phoneSPUtils.save("splash_image", url);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 保存文件
     *
     * @param bm
     * @throws IOException
     */
    private void saveFile(Bitmap bm, String urlPath) throws IOException {
        String path = getSDPath() + "/splash/";
        File dirFile = new File(path);
        if (!dirFile.exists()) {
            dirFile.mkdir();
        }
        File myCaptureFile = new File(path + getFileName(urlPath));
        BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(myCaptureFile));
        bm.compress(Bitmap.CompressFormat.JPEG, 80, bos);
        bos.flush();
        bos.close();
    }


    private String getSDPath() {
        File sdDir = null;
        boolean sdCardExist = Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED); //判断sd卡是否存在
        if (sdCardExist) {
            sdDir = Environment.getExternalStorageDirectory();//获取跟目录
        }
        return sdDir.toString();
    }


    /**
     * 获取bitmap
     *
     * @param filePath
     * @return
     */
    public Bitmap getBitmaptoFile(String filePath) {
        String path = getSDPath() + "/splash/" + getFileName(filePath);
        Bitmap bitmap = null;
        if (fileIsExists(filePath)) {
            bitmap = BitmapFactory.decodeFile(path);
        }
        return bitmap;

    }

    /**
     * 文件是否存在
     *
     * @param path
     * @return
     */
    private boolean fileIsExists(String path) {
        try {
            File f = new File(getSDPath() + "/splash/" + getFileName(path));
            if (!f.exists()) {
                return false;
            }

        } catch (Exception e) {
            // TODO: handle exception
            return false;
        }
        return true;
    }


    /**
     * 文件名的获取
     *
     * @param url
     * @return
     */
    private String getFileName(String url) {
        // 从路径中获取
        if (url != null || !"".equals(url)) {
            url = url.substring(url.lastIndexOf("/") + 1);
        }
        return url;
    }


    /**
     * 从网络获取图片地址
     */
    private void getImagePathFromNetwork() {

        if (CMethod.isNet(mContext)) {

            JSONObject jsonObject = new JSONObject();
            HttpUtils.PostDataToWeb(UrlAddressUrils.CODE_API, AppConstants.JAVA_SPLASH_IMG_URL, jsonObject, new HttpClientListener() {
                @Override
                public void onSuccess(String result) {
                    try {
                        Gson gson = new Gson();
                        SplashImageData fromJson = gson.fromJson(result, SplashImageData.class);
                        String path = fromJson.getImgList().get(0).getImgData().get(0).getAd_img();
                        if (path != null && !path.equals("")) {
                            if (phoneSPUtils.getString("splash_image") != null && phoneSPUtils.getString("splash_image").equals(path)) {
                            } else {
                                downLoadPicture(path);
                            }


                        }
                    } catch (Exception e) {

                    }


                }

                @Override
                public void onFailure(String result) {
                }

                @Override
                public void onError() {
                }
            });

        }
    }
}
