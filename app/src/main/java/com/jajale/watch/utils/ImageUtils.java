package com.jajale.watch.utils;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Base64;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by athena on 2015/11/18.
 * Email: lizhiqiang@bjjajale.com
 */
public class ImageUtils {
    private static final int MAX_TEXTURE_SIZE = getOpengl2MaxTextureSize();
    private static final int reqWidth = 720;
    private static final int reqHeight = 720;


    public static int getOpengl2MaxTextureSize() {
        int[] maxTextureSize = new int[1];
        maxTextureSize[0] = 2048;
        android.opengl.GLES20.glGetIntegerv(android.opengl.GLES20.GL_MAX_TEXTURE_SIZE, maxTextureSize, 0);
        return maxTextureSize[0];
    }

    /**
     * Calculate an inSampleSize for use in a
     * {@link android.graphics.BitmapFactory.Options} object when decoding
     * bitmaps using the decode* methods from {@link android.graphics.BitmapFactory}. This
     * implementation calculates the closest inSampleSize that will result in
     * the final decoded bitmap having a width and user_height equal to or larger
     * than the requested width and user_height. This implementation does not ensure
     * a power of 2 is returned for inSampleSize which can be faster when
     * decoding but results in a larger bitmap which isn't as useful for caching
     * purposes.
     *
     * @param options   An options object with out* params already populated (run
     *                  through a decode* method with inJustDecodeBounds==true
     * @param reqWidth  The requested width of the resulting bitmap
     * @param reqHeight The requested user_height of the resulting bitmap
     * @return The value to be used for inSampleSize
     */
    public static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        // Raw user_height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {
            if (width > height) {
                inSampleSize = Math.round((float) height / (float) reqHeight);
            } else {
                inSampleSize = Math.round((float) width / (float) reqWidth);
            }

            // This offers some additional logic in case the image has a strange
            // aspect ratio. For example, a panorama may have a much larger
            // width than user_height. In these cases the total pixels might still
            // end up being too large to fit comfortably in memory, so we should
            // be more aggressive with sample down the image (=larger
            // inSampleSize).

            final float totalPixels = width * height;

            // Anything more than 2x the requested pixels we'll sample down
            // further.
            final float totalReqPixelsCap = reqWidth * reqHeight;

            while (totalPixels / (inSampleSize * inSampleSize) > totalReqPixelsCap) {
                inSampleSize++;
            }
        }
        return inSampleSize;
    }


    public static Bitmap zoomImg(Bitmap bm, int newWidth ,int newHeight){
        // 获得图片的宽高
        int width = bm.getWidth();
        int height = bm.getHeight();
        // 计算缩放比例
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;
        // 取得想要缩放的matrix参数
        Matrix matrix = new Matrix();
        matrix.postScale(scaleWidth, scaleHeight);
        // 得到新的图片
        Bitmap newbm = Bitmap.createBitmap(bm, 0, 0, width, height, matrix, true);
        return newbm;
    }

    private static Bitmap adjustPhotoRotation(Bitmap bm, final int orientationDegree)
    {

        Matrix m = new Matrix();
        m.setRotate(orientationDegree, (float) bm.getWidth() / 2, (float) bm.getHeight() / 2);
        float targetX, targetY;
        if (orientationDegree == 90) {
            targetX = bm.getHeight();
            targetY = 0;
        } else {
            targetX = bm.getHeight();
            targetY = bm.getWidth();
        }

        final float[] values = new float[9];
        m.getValues(values);

        float x1 = values[Matrix.MTRANS_X];
        float y1 = values[Matrix.MTRANS_Y];

        m.postTranslate(targetX - x1, targetY - y1);

        Bitmap bm1 = Bitmap.createBitmap(bm.getHeight(), bm.getWidth(), Bitmap.Config.ARGB_8888);
        Paint paint = new Paint();
        Canvas canvas = new Canvas(bm1);
        canvas.drawBitmap(bm, m, paint);
        return bm1;
    }


    public static Bitmap decodeSampledBitmapFromFile(String filePath,int reqWidth , int reqHeight) {
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(filePath, options);
        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;
        Bitmap bitmap = BitmapFactory.decodeFile(filePath, options);
        // return compressImage(bitmap);
        if (bitmap.getWidth() > bitmap.getHeight()){
            return zoomImg(bitmap,reqWidth,reqHeight);
        }else {
            return zoomImg(adjustPhotoRotation(bitmap,90),reqWidth,reqHeight);
        }


    }

    public static Bitmap decodeSampledBitmapFromFile(String filePath) {
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(filePath, options);

        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;
        Bitmap bitmap = BitmapFactory.decodeFile(filePath, options);
        // return compressImage(bitmap);
        return bitmap;
    }


    /**
     * 保存图片到文件
     */
    public static boolean saveBitmap(File f, Bitmap bitmap, int limite) {
        if (bitmap == null || bitmap.isRecycled())
            return false;

        FileOutputStream fOut = null;
        try {

            if (!f.getParentFile().exists()) {
                f.getParentFile().mkdirs();
            }

            f.createNewFile();

            fOut = new FileOutputStream(f);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);// 质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
            int options = 100;
            while (baos.toByteArray().length / 1024 > limite) { // 循环判断如果压缩后图片是否大于limite
                // kb,大于继续压缩
                baos.reset();// 重置baos即清空baos
                options -= 2;// 每次都减少2
                bitmap.compress(Bitmap.CompressFormat.JPEG, (options < 0 ? 1 : options), baos);// 这里压缩options%，把压缩后的数据存放到baos中
            }
            fOut.write(baos.toByteArray());
            fOut.flush();
            return true;
        } catch (Exception e) {
            L.e(e);
        } finally {
            if (fOut != null) {
                try {
                    fOut.close();
                } catch (IOException e) {
                    L.e(e);
                }
            }
        }
        return false;
    }

    /**
     * @param path
     * @param bitmap
     * @param limite 压缩大小限制
     * @return
     */
    public static boolean saveBitmap(String path, Bitmap bitmap, int limite) {
        return saveBitmap(new File(path), bitmap, limite);
    }


    public static String handleUploadImage(Context context, String src, int limite) {
        if (CMethod.isEmptyOrZero(src)) {
            return src;
        }
        File file = new File(src);
        if (file.exists()) {
            long length = file.length();
            if (length / 1024 > limite) {
                String newpath = CacheUtils.getExternalCacheDir(context) +
                        System.currentTimeMillis() + src.substring(src.indexOf("."), src.length());

//                Bitmap bitmap = BitmapFactory.decodeFile(src);
                Bitmap bitmap = decodeSampledBitmapFromFile(src);
                boolean b = saveBitmap(newpath, bitmap, limite);
                if (b) {
                    return newpath;
                }
            }
        }
        return src;
    }


    public static String getPathFromUri(Context context, Uri uri) {
        String img_path = null;
        if (uri.toString().contains("content")) {
            Cursor cursor = null;
            try {
                String[] proj = {MediaStore.Images.Media.DATA};
                android.support.v4.content.CursorLoader cursorLoader = new android.support.v4.content.CursorLoader(context, uri, proj, null, null, null);
                Cursor actualimagecursor = cursorLoader.loadInBackground();
                int actual_image_column_index = actualimagecursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                actualimagecursor.moveToFirst();
                img_path = actualimagecursor.getString(actual_image_column_index);
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (cursor != null) {
                    try {
                        cursor.close();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        } else {
            img_path = uri.getPath();
        }
        return img_path;
    }

    /**
     * 将base64转换成bitmap图片
     *
     * @param string base64字符串
     * @return bitmap
     */

    public static Bitmap string2Bitmap (String string){
        // 将字符串转换成Bitmap类型
        Bitmap bitmap = null;
        try {
            byte[] bitmapArray;
            bitmapArray = Base64.decode(string, Base64.DEFAULT);
            bitmap = BitmapFactory.decodeByteArray(bitmapArray, 0, bitmapArray.length);
        }catch (Exception e){
            e.printStackTrace();
        }
        return bitmap;
    }

    public static String bitmap2String(Bitmap bitmap, int bitmapQuality){
        // 将Bitmap转换成字符串
        String string = null;
        ByteArrayOutputStream bStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, bitmapQuality, bStream);
        byte[] bytes = bStream.toByteArray();
        string = Base64.encodeToString(bytes, Base64.DEFAULT);
        return string;
    }



    public static Bitmap imageZoom(Bitmap bitMap) {
        //图片允许最大空间   单位：KB
        double maxSize =30.00;
        //将bitmap放至数组中，意在bitmap的大小（与实际读取的原文件要大）
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitMap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] b = baos.toByteArray();
        //将字节换成KB
        double mid = b.length/1024;
        //判断bitmap占用空间是否大于允许最大空间  如果大于则压缩 小于则不压缩
        if (mid > maxSize) {
            //获取bitmap大小 是允许最大大小的多少倍
            double i = mid / maxSize;
            //开始压缩  此处用到平方根 将宽带和高度压缩掉对应的平方根倍 （1.保持刻度和高度和原bitmap比率一致，压缩后也达到了最大大小占用空间的大小）
            bitMap = zoomImage(bitMap, bitMap.getWidth() / Math.sqrt(i),
                    bitMap.getHeight() / Math.sqrt(i));
        }
        return bitMap;
    }

    public static Bitmap zoomImage(Bitmap bgimage, double newWidth,
                                   double newHeight) {
        // 获取这个图片的宽和高
        float width = bgimage.getWidth();
        float height = bgimage.getHeight();
        // 创建操作图片用的matrix对象
        Matrix matrix = new Matrix();
        // 计算宽高缩放率
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;
        // 缩放图片动作
        matrix.postScale(scaleWidth, scaleHeight);
        Bitmap bitmap = Bitmap.createBitmap(bgimage, 0, 0, (int) width,
                (int) height, matrix, true);
        return bitmap;
    }


}
