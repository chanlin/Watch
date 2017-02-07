package com.jajale.watch.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;

import com.jajale.watch.activity.ImageFactoryActivity;
import com.jajale.watch.entity.AppInfo;
import com.jajale.watch.entity.RequestCode;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.UUID;

/**
 * Created by pig on 3/6/15.
 */
public class PhotoUtils {
    // 图片在SD卡中的缓存路径
    private static final String IMAGE_PATH = Environment
            .getExternalStorageDirectory().toString()
            + File.separator
            + "jajale" + File.separator + "Images" + File.separator;


    /**
     * 根据宽度和长度进行缩放图片
     *
     * @param path
     *            图片的路径
     * @param w
     *            宽度
     * @param h
     *            长度
     * @return
     */
    public static Bitmap createBitmap(String path, int w, int h) {
        try {
            BitmapFactory.Options opts = new BitmapFactory.Options();
            opts.inJustDecodeBounds = true;
            // 这里是整个方法的关键，inJustDecodeBounds设为true时将不为图片分配内存。
            BitmapFactory.decodeFile(path, opts);
            int srcWidth = opts.outWidth;// 获取图片的原始宽度
            int srcHeight = opts.outHeight;// 获取图片原始高度
            int destWidth = 0;
            int destHeight = 0;
            // 缩放的比例
            double ratio = 0.0;
            if (srcWidth < w || srcHeight < h) {
                ratio = 0.0;
                destWidth = srcWidth;
                destHeight = srcHeight;
            } else if (srcWidth > srcHeight) {// 按比例计算缩放后的图片大小，maxLength是长或宽允许的最大长度
                ratio = (double) srcWidth / w;
                destWidth = w;
                destHeight = (int) (srcHeight / ratio);
            } else {
                ratio = (double) srcHeight / h;
                destHeight = h;
                destWidth = (int) (srcWidth / ratio);
            }
            BitmapFactory.Options newOpts = new BitmapFactory.Options();
            // 缩放的比例，缩放是很难按准备的比例进行缩放的，目前我只发现只能通过inSampleSize来进行缩放，其值表明缩放的倍数，SDK中建议其值是2的指数值
            newOpts.inSampleSize = (int) ratio + 1;
            // inJustDecodeBounds设为false表示把图片读进内存中
            newOpts.inJustDecodeBounds = false;
            // 设置大小，这个一般是不准确的，是以inSampleSize的为准，但是如果不设置却不能缩放
            newOpts.outHeight = destHeight;
            newOpts.outWidth = destWidth;
            // 获取缩放后图片
            return BitmapFactory.decodeFile(path, newOpts);
        } catch (Exception e) {
            // TODO: handle exception
            return null;
        }
    }
    /**
     * 从文件中获取图片
     *
     * @param path
     *            图片的路径
     * @return
     */
    public static Bitmap getBitmapFromFile(String path) {
        return BitmapFactory.decodeFile(path);
    }
    public static void toPhoto(Activity activity) {
        try{
            Intent intent = new Intent(Intent.ACTION_PICK, null);
            intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
            activity.startActivityForResult(intent, RequestCode.TAKE_FROM_PHOTO);
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    public static void toCamera(Activity activity) {
        try{
            File file = FileUtils.initFile(AppInfo.getInstace().getCameraTempPath());
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            // 下面这句指定调用相机拍照后的照片存储的路径
            intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(file));
            intent.putExtra("return-data", false);
            activity.startActivityForResult(intent, RequestCode.TAKE_FROM_CAMERA);
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    /**
     * 保存图片到SD卡
     *
     * @param bitmap
     *            图片的bitmap对象
     * @return
     */
    public static String savePhotoToSDCard(Bitmap bitmap) {
        if (!FileUtils.isSdcardExist()) {
            return null;
        }
        FileOutputStream fileOutputStream = null;
        FileUtils.createDirFile(IMAGE_PATH);

        String fileName = UUID.randomUUID().toString() + ".jpg";
        String newFilePath = IMAGE_PATH + fileName;
        File file = FileUtils.createNewFile(newFilePath);
        if (file == null) {
            return null;
        }
        try {
            fileOutputStream = new FileOutputStream(newFilePath);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fileOutputStream);
        } catch (FileNotFoundException e1) {
            return null;
        } finally {
            try {
                fileOutputStream.flush();
                fileOutputStream.close();
            } catch (IOException e) {
                return null;
            }
        }
        return newFilePath;
    }
    /**
     * 裁剪图片
     *
     * @param context
     * @param activity
     * @param path
     *            需要裁剪的图片路径
     */
    public static void cropPhoto(Context context, Activity activity, String path) {
        Intent intent = new Intent(context, ImageFactoryActivity.class);
        if (path != null) {
            intent.putExtra("path", path);
            intent.putExtra(ImageFactoryActivity.TYPE,
                    ImageFactoryActivity.CROP);
        }
        activity.startActivityForResult(intent, RequestCode.INTENT_REQUEST_CODE_CROP);
    }



/*    *//**
     * 判断图片高度和宽度是否过大
     *
     * @param bitmap
     *            图片bitmap对象
     * @return
     */
    public static boolean bitmapIsLarge(Bitmap bitmap) {
        final int MAX_WIDTH = 60;
    final int MAX_HEIGHT = 60;
    Bundle bundle = getBitmapWidthAndHeight(bitmap);
    if (bundle != null) {
        int width = bundle.getInt("width");
        int height = bundle.getInt("height");
        if (width > MAX_WIDTH && height > MAX_HEIGHT) {
            return true;
        }
    }
    return false;
}

    /**
     * 获取图片的长度和宽度
     *
     * @param bitmap
     *            图片bitmap对象
     * @return
     */
    public static Bundle getBitmapWidthAndHeight(Bitmap bitmap) {
        Bundle bundle = null;
        if (bitmap != null) {
            bundle = new Bundle();
            bundle.putInt("width", bitmap.getWidth());
            bundle.putInt("height", bitmap.getHeight());
            return bundle;
        }
        return null;
    }

}
