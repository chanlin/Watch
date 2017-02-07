package com.jajale.watch.utils;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.support.v4.app.NotificationCompat;
import android.widget.RemoteViews;

import com.jajale.watch.R;

/**
 * Created by athena on 2016/1/23.
 * Email: lizhiqiang@bjjajale.com
 */
public class NotificationUtils {
    private NotificationManager manger;
    private Notification notification;
    private NotificationCompat.Builder builder;

    private Context mContext;
    public static final int CHILD_MESSAGE_ID=0;
    public static final int SYSTEM_MESSAGE_ID=1;

    public NotificationUtils(Context context, final PendingIntent intent , final String title ,final String top, final String bottom,final int index){
        this.mContext = context.getApplicationContext();
        builder = new NotificationCompat.Builder(mContext);
        builder.setAutoCancel(true);
        builder.setDefaults(Notification.DEFAULT_SOUND | Notification.DEFAULT_VIBRATE | Notification.DEFAULT_LIGHTS);
        manger = (NotificationManager) mContext.getSystemService(mContext.NOTIFICATION_SERVICE);
        notification = builder.build();
        notification.defaults = Notification.DEFAULT_ALL;
        RemoteViews remoteViews = new RemoteViews(mContext.getPackageName(), R.layout.custom_notification);
        remoteViews.setTextViewText(R.id.tv_top, top);
        remoteViews.setTextViewText(R.id.tv_bottom, bottom);
        remoteViews.setOnClickPendingIntent(R.id.ll_customer_content, intent);
        builder.setContent(remoteViews).setSmallIcon(R.mipmap.ic_launcher)
//                        .setLargeIcon(R.mipmap.ic_launcher)
//                          .setOngoing(true)
                .setTicker(title);
        manger.notify(index, builder.build());
    }


    public static void cancelAll(Context context) {
        context = context.getApplicationContext();
        NotificationManager manger = (NotificationManager) context.getSystemService(context.NOTIFICATION_SERVICE);
        manger.cancelAll();
    }

    public static void cancel(Context context,int id) {
        context = context.getApplicationContext();
        NotificationManager manger = (NotificationManager) context.getSystemService(context.NOTIFICATION_SERVICE);
        manger.cancel(id);
    }

    /**
     * 转换图片成圆形
     *
     * @param bitmap 传入Bitmap对象
     * @return
     */
    public Bitmap toRoundBitmap(Bitmap bitmap) {
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        float roundPx;
        float left, top, right, bottom, dst_left, dst_top, dst_right, dst_bottom;
        if (width <= height) {
            roundPx = width / 2;

            left = 0;
            top = 0;
            right = width;
            bottom = width;

            height = width;

            dst_left = 0;
            dst_top = 0;
            dst_right = width;
            dst_bottom = width;
        } else {
            roundPx = height / 2;

            float clip = (width - height) / 2;

            left = clip;
            right = width - clip;
            top = 0;
            bottom = height;
            width = height;

            dst_left = 0;
            dst_top = 0;
            dst_right = height;
            dst_bottom = height;
        }

        Bitmap output = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_4444);
        Canvas canvas = new Canvas(output);

        final Paint paint = new Paint();
        final Rect src = new Rect((int) left, (int) top, (int) right, (int) bottom);
        final Rect dst = new Rect((int) dst_left, (int) dst_top, (int) dst_right, (int) dst_bottom);
        final RectF rectF = new RectF(dst);

        paint.setAntiAlias(true);// 设置画笔无锯齿

        canvas.drawARGB(0, 0, 0, 0); // 填充整个Canvas

        // 以下有两种方法画圆,drawRounRect和drawCircle
        canvas.drawRoundRect(rectF, roundPx, roundPx, paint);// 画圆角矩形，第一个参数为图形显示区域，第二个参数和第三个参数分别是水平圆角半径和垂直圆角半径。
        // canvas.drawCircle(roundPx, roundPx, roundPx, paint);

        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));// 设置两张图片相交时的模式,参考http://trylovecatch.iteye.com/blog/1189452
        canvas.drawBitmap(bitmap, src, dst, paint); // 以Mode.SRC_IN模式合并bitmap和已经draw了的Circle
        return output;
    }

}
