package com.jajale.watch.image;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

/**
 * Created by athena on 2015/11/18.
 * Email: lizhiqiang@bjjajale.com
 */
public class JJLImageLoader {

    //后期可能会取不同尺寸的图片用
    private static final String BIG_TAIL = "?imageView2/1/w/270/h/330/";
    private static final String BIG_TAIL_WIFI = "?imageView2/1/w/405/h/495/";
    private static final String NORMAL_TAIL_WIFI = "?imageView2/1/w/108/h/120/q/90";
    private static final String SMALL_TAIL_WIFI = "?imageView2/1/w/120/h/120/q/60";
    private static final String INFOMATION_TAIL = "?imageView2/1/w/95/h/80/q/80";

    private static final String AD_TAIL = "?imageView2/1/w/420/h/180/q/90";



    public static void downloadInfomation(Context context, String url, ImageView imageView){
        StringBuilder b = new StringBuilder();
        b.append(url).append(INFOMATION_TAIL);
        Picasso.with(context).load(b.toString())
                .into(imageView);
    }

    public static void downloadCirleImage(Context context, String url, ImageView imageView) {
        StringBuilder b = new StringBuilder();
        b.append(url).append(INFOMATION_TAIL);
        Picasso.with(context).load(b.toString()).transform(new CircleTransform()).into(imageView);
    }

    public static void downloadRoundImage(Context context, String url, ImageView imageView) {
        StringBuilder b = new StringBuilder();
        b.append(url).append(INFOMATION_TAIL);
        Picasso.with(context).load(b.toString()).transform(new RoundTransform()).into(imageView);
    }

    public static void downloadRoundImageResize(Context context, String url, ImageView imageView,int width,int height) {
        StringBuilder b = new StringBuilder();
        b.append(url).append(INFOMATION_TAIL);
        Picasso.with(context).load(b.toString()).transform(new RoundTransform()).resize(width, height).centerCrop().into(imageView);
    }
    public static void downloadRoundImageResizePlace(Context context, String url,int placePicId, ImageView imageView,int width,int height) {
        StringBuilder b = new StringBuilder();
        b.append(url).append(INFOMATION_TAIL);
        Picasso.with(context).load(b.toString()).placeholder(placePicId).transform(new RoundTransform()).resize(width,height).centerCrop().into(imageView);
    }


    public static void downloadSmall(Context context, String url, ImageView imageView) {
        StringBuilder b = new StringBuilder();
        b.append(url).append(SMALL_TAIL_WIFI);
        Picasso.with(context).load(b.toString())
                .into(imageView);
    }

    public static void downloadNormal(Context context, String url, ImageView imageView){
        StringBuilder b = new StringBuilder();
        b.append(url).append(NORMAL_TAIL_WIFI);
        Picasso.with(context).load(b.toString())
                .into(imageView);
    }


    public static void downloadAD(Context context, String url, ImageView imageView){
        StringBuilder b = new StringBuilder();
        b.append(url).append(AD_TAIL);
        Picasso.with(context).load(b.toString())
                .into(imageView);
    }

    public static void downloadBig(Context context, String url, ImageView imageView){
        StringBuilder b = new StringBuilder();
        b.append(url).append(BIG_TAIL);
        Picasso.with(context).load(b.toString())
                .into(imageView);
    }
    public static void downloadBigPlace(Context context, String url,int placePicId, ImageView imageView) {
        StringBuilder b = new StringBuilder();
        b.append(url).append(BIG_TAIL);
        Picasso.with(context).load(b.toString()).placeholder(placePicId)
                .into(imageView);
    }

    public static void download(Context context, String url, ImageView imageView){
        Picasso.with(context).load(url)
                .into(imageView);
    }

    public static void download(Context context, String url,int error_id ,ImageView imageView){
        Picasso.with(context).load(url).error(error_id)
                .into(imageView);
    }


    // 后期下载 相册图片等 将会以方法download***的形式暴露

    /**
     * 加载本地图片
     * http://bbs.3gstdy.com
     * @param url
     * @return
     */
    public static Drawable getLoacalBitmap(String url) {
        try {
            FileInputStream fis = new FileInputStream(url);
            Bitmap b = BitmapFactory.decodeStream(fis);
            return new BitmapDrawable(b);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 圆角矩形
     */
    static class RoundTransform implements Transformation {

        @Override
        public Bitmap transform(Bitmap source) {
            int widthLight = source.getWidth();
            int heightLight = source.getHeight();

            Bitmap output = Bitmap.createBitmap(source.getWidth(), source.getHeight(), Bitmap.Config.ARGB_8888);

            Canvas canvas = new Canvas(output);
            Paint paintColor = new Paint();
            paintColor.setFlags(Paint.ANTI_ALIAS_FLAG);

            RectF rectF = new RectF(new Rect(0, 0, widthLight, heightLight));

            canvas.drawRoundRect(rectF, widthLight / 12, heightLight / 12, paintColor);

            Paint paintImage = new Paint();
            paintImage.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_ATOP));
            canvas.drawBitmap(source, 0, 0, paintImage);
            source.recycle();
            return output;
        }

        @Override
        public String key() {
            return "roundcorner";
        }
    }

    static class CircleTransform implements Transformation {
        @Override
        public Bitmap transform(Bitmap source) {
            int size = Math.min(source.getWidth(), source.getHeight());

            int x = (source.getWidth() - size) / 2;
            int y = (source.getHeight() - size) / 2;

            Bitmap squaredBitmap = Bitmap.createBitmap(source, x, y, size, size);
            if (squaredBitmap != source) {
                source.recycle();
            }

            Bitmap bitmap = Bitmap.createBitmap(size, size, source.getConfig());

            Canvas canvas = new Canvas(bitmap);
            Paint paint = new Paint();
            BitmapShader shader = new BitmapShader(squaredBitmap,
                    BitmapShader.TileMode.CLAMP, BitmapShader.TileMode.CLAMP);
            paint.setShader(shader);
            paint.setAntiAlias(true);

            float r = size / 2f;
            canvas.drawCircle(r, r, r, paint);

            squaredBitmap.recycle();
            return bitmap;
        }


        @Override
        public String key() {
            return "circle";
        }
    }

}
