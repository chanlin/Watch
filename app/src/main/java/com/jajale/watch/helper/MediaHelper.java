package com.jajale.watch.helper;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Handler;
import android.text.TextUtils;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.jajale.watch.entitydb.Message;
import com.jajale.watch.entityno.MessageContentType;
import com.jajale.watch.utils.ByteUtil;
import com.jajale.watch.utils.CMethod;
import com.jajale.watch.utils.CacheUtils;
import com.jajale.watch.utils.L;
import com.jajale.watch.utils.Md5Util;
import com.jajale.watch.utils.T;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;

/**
 * Created by athena on 2015/11/28.
 * Email: lizhiqiang@bjjajale.com
 */
public class MediaHelper {

    private MediaRecorder mMediaRecorder;
    private volatile boolean isRecording = false;
    private int second = 0;
    private MediaRecorderListener mListener;
    private long delayMillis = 1000;
    private String filaName;
    private String suffixes = ".amr";
    private Context mContext;
    private String cacheDir;
    private OnPlayStatusListener last_listener = null;
//    Handler handler = new Handler();
    Handler handler = null ;
    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            second = second + 1;
            if (mListener != null) {
                mListener.update(second);
            }
            if (handler!=null){
                handler.postDelayed(this, delayMillis);
            }

        }
    };
    private MediaPlayer mediaPlayer;

    private MicDBCallback mMicCallBack;
    private File mRecAudioFile;
    private final DownloadHelper downloadHelper;


    public MediaHelper(Context context) {
        this.mContext = context;
        this.downloadHelper = new DownloadHelper(context);
        this.cacheDir = CacheUtils.getExternalCacheDir(mContext);
    }

    public interface OnPlayStatusListener {
        void onStart();

        void onEnd(String path);

        void onFail();
    }

    public void setRecorderListener(MediaRecorderListener mListener) {
        this.mListener = mListener;
    }

//    public boolean moveAudio2File(String oldPath, String url) {
//        if (!TextUtils.isEmpty(url) && url.startsWith("http")) {
//            return FileUtils.moveFile(oldPath, cacheDir + Md5Util.string2MD5(url) + suffixes);
//        }
//        return false;
//    }

    public String getNetfilePath(String directoryOrUrl) {
        String md5 = Md5Util.string2MD5(directoryOrUrl);
        return cacheDir + md5 + suffixes;
    }

    private void playOnMainThread(int index ,final String filename, final Message msg,final OnPlayStatusListener listener) {
        try {
            L.e(filename + ">>>>>>>>>>" + index);
//            final String filename1 = "/storage/emulated/0/Android/data/com.jajale.watch/cache/d52bf484a9d3737f02c5b272a9bfce4b.amr";
            if (mediaPlayer != null) {
                mediaPlayer.stop();
                mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                    @Override
                    public void onCompletion(MediaPlayer mp) {

                        listener.onEnd(filename);
                        last_listener = null;
                    }
                });
                mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                    @Override
                    public void onPrepared(MediaPlayer mp) {

                        mediaPlayer.start();
                        listener.onStart();
                    }
                });
                mediaPlayer.reset();
                if (last_listener != null) {
                    last_listener.onEnd(filename);
                    last_listener = null;
                }
//                mediaPlayer.release();
                last_listener = listener;
                mediaPlayer.setDataSource(filename);
                mediaPlayer.prepareAsync();
            } else {
                last_listener = listener;
                mediaPlayer = new MediaPlayer();
                mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                    @Override
                    public void onCompletion(MediaPlayer mp) {
                        listener.onEnd(filename);
                        last_listener = null;
                    }
                });
                mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                    @Override
                    public void onPrepared(MediaPlayer mp) {

                        mediaPlayer.start();
                        listener.onStart();
                    }
                });
                mediaPlayer.reset();
                mediaPlayer.setDataSource(filename);
                mediaPlayer.prepareAsync();
            }


//            mediaPlayer.prepare();
        } catch (Exception e) {
            e.printStackTrace();
            listener.onFail();
        }
    }

    RequestQueue queue;
    String service_ignore_message = "watch.ignore_message";//获取新微聊信息列表
    String user_id;
    String imei;
    String URL = "http://lib.huayinghealth.com/lib-x/?";
    SharedPreferences sp;
    SharedPreferences.Editor editor;
    String filename;
    public void playAudio(final int index ,final Message msg ,final OnPlayStatusListener listener) {

        queue = Volley.newRequestQueue(mContext);
        sp = mContext.getSharedPreferences("NotDisturb", mContext.MODE_PRIVATE);// 创建对象
        editor = sp.edit();
        imei = sp.getString("IMEI", "");
        user_id = sp.getString("Telephone_Number", "");
//        if (imei.equals("")) {
//            imei = "358688000000158";
//        }
//        if (user_id.equals("")) {
//            user_id = "13006616705";
//        }
        try {
           final  String play_url =msg.url;
            L.e("msgurl:::::"+play_url);
            final String play_content =msg.content;
            filename = play_content;
            L.e("filename" +filename);
            if (!CMethod.isEmpty(play_content)){
                //File file = new File(play_url);
                final String filename;
                if (null == play_url || "".equals(play_url)){
                    filename = play_content;
                }else {
                    filename = getNetfilePath(play_url);
                }


                File file = new File(filename);
                if (!file.exists() && file.length() <=0){
                    //ByteUtil.getFile(play_content,play_url);
                        try {
                            downloadHelper.download(play_url, filename, new DownloadHelper.OnProgress() {
                                @Override
                                public void update(int progress) {

                                    if (progress == 100) {
                                        ((Activity) mContext).runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                playOnMainThread(index*10,filename, msg ,listener);
                                            }
                                        });

                                    }
                                    ignoremessage();//取消已下载语音文件
                                }

                                @Override
                                public void onFail() {
                                    ((Activity) mContext).runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            listener.onFail();
                                        }
                                    });
                                }
                            });
                        } catch (Exception e) {
                            e.printStackTrace();

                        }

                } else {
                    ((Activity) mContext).runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            playOnMainThread(index*100,filename, msg,listener);
                        }
                    });
                }
//                ((Activity) mContext).runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
//                        File sdk = new File("/mnt/sdcard/arm/"+play_content);
//                        String temp = play_url;
//                        if(sdk.exists()) {//判断文件目录是否存在
//                            temp = "/mnt/sdcard/arm/"+play_content;
//                        }
//                        playOnMainThread(index * 100, temp, msg, listener);
//                    }
//                });
            }
        } catch (Exception e) {
            e.printStackTrace();
            L.e("123==播放语言错误===e=="+e.toString());
        }
    }

    private void ignoremessage() {
        String sign = Md5Util.stringmd5(filename, imei, service_ignore_message, user_id);
        String url = URL + "service=" + service_ignore_message + "&imei=" + imei + "&user_id=" + user_id + "&filename=" + filename + "&sign=" + sign;
        L.e("sign===" + url);
        StringRequest request = new StringRequest(url, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONObject jsondata = jsonObject.getJSONObject("data");
                    int code = jsondata.getInt("code");
                    if(code == 0){

                    }else if(code == 1){

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
            }
        });
        queue.add(request);
    }


    public void playAudio(final int index , final String directoryOrUrl,final Message msg ,final OnPlayStatusListener listener) {

        try {

            if (!TextUtils.isEmpty(directoryOrUrl)) {




                if (directoryOrUrl.startsWith("http")) {//link

                    String play_url =directoryOrUrl;
//                    String play_url = directoryOrUrl.replace(".amr","");

//                    directoryOrUrl =
//                    String md5 = Md5Util.string2MD5(directoryOrUrl);
//                    final String filename = cacheDir + md5 + suffixes;
                    final String filename = getNetfilePath(play_url);

                    File file = new File(filename);
                        if (file.exists() && file.length() >0) {
                        ((Activity) mContext).runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                playOnMainThread(index*100,filename, msg,listener);
                            }
                        });

                    } else {//下载

                        try {
                            downloadHelper.download(play_url, filename, new DownloadHelper.OnProgress() {
                                @Override
                                public void update(int progress) {

                                    if (progress == 100) {
                                        ((Activity) mContext).runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                playOnMainThread(index*10,filename, msg ,listener);
                                            }
                                        });

                                    }
                                }

                                @Override
                                public void onFail() {
                                    ((Activity) mContext).runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            listener.onFail();
                                        }
                                    });
                                }
                            });
                        } catch (Exception e) {
                            e.printStackTrace();

                        }
                    }
                } else {//file
                    ((Activity) mContext).runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            playOnMainThread(index*1000,directoryOrUrl, msg ,listener);
                        }
                    });
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void stopPlay() {
        if (mediaPlayer != null && mediaPlayer.isPlaying()) {
            mediaPlayer.stop();
            mediaPlayer.release();
            mediaPlayer = null;
            if (last_listener != null) {
                last_listener.onEnd("stop");
                last_listener = null;
            }
        }
    }

    private void mediaRecorderStop() {
        if (mMediaRecorder != null && isRecording) {
            handler.removeCallbacks(runnable);
            second = 0;
//            mMediaRecorder.setOnErrorListener(null);
            try {
                mMediaRecorder.stop();
                handler = null;
            } catch (Exception e) {
                e.printStackTrace();
            }
            isRecording = false;
            releaseMediaRecorder();
        }

    }

    public void startRecord() {
        if (!isRecording) {
//            new Thread(new Runnable() {
//                @Override
//                public void run() {
            try {
                if (prepareMediaRecorder()) {
                    mediaRecorderStart();
                    updateMicStatus();
                } else {
                    releaseMediaRecorder();
                }
            } catch (IOException e) {
                e.printStackTrace();
                releaseMediaRecorder();
            }
//                }
//            }).start();
        } else {
            mediaRecorderStop();
        }
    }

    public Entity stopRecord() {
        mediaRecorderStop();
        return new Entity(filaName, second);
    }

//    public boolean isPlaying() {
//        return mediaPlayer.isPlaying();
//    }

    public boolean isRecording() {
        return isRecording;
    }

    private void releaseMediaRecorder() {
        if (mMediaRecorder != null) {
            // clear recorder configuration
            mMediaRecorder.reset();
            // release the recorder object
            mMediaRecorder.release();
            mMediaRecorder = null;
            // Lock camera for later use i.e taking it back from MediaRecorder.
            // MediaRecorder doesn't need it anymore and we will release it if the activity pauses.
//            mCamera.lock();
        }
    }

    private void mediaRecorderStart() throws IOException {
        /** 开始 */
        L.e("MediaHelper", "mediaRecorderStart");
        handler = new Handler();
        mMediaRecorder.start();
        isRecording = true;
        handler.postDelayed(runnable, delayMillis);//
    }

    private boolean prepareMediaRecorder() throws IOException {
        /** 创建录音文件，第一个参数是文件名前缀，第二个参数是后缀，第三个参数是SD路径 */
        String fileName = System.currentTimeMillis() + "";
        mRecAudioFile = new File(CacheUtils.getExternalCacheDir(mContext) + fileName + suffixes);
        L.e("position  is :"+CacheUtils.getExternalCacheDir(mContext) +"file name is:"+fileName);

        if (mRecAudioFile.exists()) {
            mRecAudioFile.delete();
        }
        mRecAudioFile.createNewFile();
        filaName = mRecAudioFile.getAbsolutePath();

        /** 实例化MediaRecorder对象 */
        mMediaRecorder = new MediaRecorder();
        mMediaRecorder.setAudioEncodingBitRate(16);
        mMediaRecorder.setAudioChannels(1);
        /** 设置麦克风 */
        mMediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        /** 设置输出文件的格式 */
        mMediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.AMR_NB);
        /** 设置音频文件的编码 */
        mMediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
        /** 设置输出文件的路径 */
        mMediaRecorder.setOutputFile(mRecAudioFile.getAbsolutePath());
        /** 准备 */
        mMediaRecorder.prepare();
        return true;
    }

    public interface MediaRecorderListener {
        void update(int second);
    }


    private Runnable mUpdateMicStatusTimer = new Runnable() {
        public void run() {
            updateMicStatus();
        }
    };

    /**
     * 更新话筒状态
     * 返回话筒的音量值
     */
    private int BASE = 1;
    private int SPACE = 100;// 间隔取样时间

    private void updateMicStatus() {
        if (mMediaRecorder != null) {
            double ratio = (double) mMediaRecorder.getMaxAmplitude() / BASE;
            int db = 0;// 分贝
            if (ratio > 1)
                db = (int) (20 * Math.log10(ratio));
            L.d("MediaHelper", "分贝值：" + db);
            if (mMicCallBack != null) {
                mMicCallBack.getDb(db);
            }
            handler.postDelayed(mUpdateMicStatusTimer, SPACE);
        }
    }

    public void registerMicCallBack(MicDBCallback callback) {
        this.mMicCallBack = callback;
    }

    public void releaseMicCallBack() {
        this.mMicCallBack = null;
    }


    public interface MicDBCallback {
        void getDb(int result);
    }

    /**
     * 丢弃此次录音
     */
    public void discardRecording() {
        mediaRecorderStop();
        if (mRecAudioFile != null && mRecAudioFile.exists()) {
            mRecAudioFile.delete();
        }
    }

    /**
     * 录音成功后返回的对象
     */
    public class Entity {
        /**
         * 录音文件完整名字
         */
        public String filename;
        /**
         * 录音文件时长
         */
        public int time;

        public Entity(String filename, int time) {
            this.filename = filename;
//            this.filename = Uri.fromFile(new File(filename)).toString();
            this.time = time;
        }
    }
}
