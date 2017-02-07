package com.jajale.watch.fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.drawable.AnimationDrawable;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.j256.ormlite.dao.Dao;
import com.jajale.watch.R;
import com.jajale.watch.dao.QASqliteHelper;
import com.jajale.watch.entity.QaEnum;
import com.jajale.watch.entitydb.QAEntity;
import com.jajale.watch.image.JJLImageLoader;
import com.jajale.watch.utils.FileUtils;
import com.jajale.watch.utils.L;
import com.jajale.watch.utils.LoadingDialog;
import com.jajale.watch.utils.T;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class QADetailsFragment extends BaseFragment implements View.OnClickListener {
    Button answer_btn;
    Button next_ques_btn;
    QASqliteHelper qaSqliteHelper;
    TextView ques_tv;
    TextView answer_tv;
    QAEntity qaEntity;
    String fragmentType;
    ImageView pic_iv;
    MediaPlayer mediaPlayer;
    private AnimationDrawable animationDrawable;
    LoadingDialog loadingDialog;
    View anim_view;
    Dao<QAEntity, String> dao;
    AnimationDrawable drawable;

//    public static final String mp3_DIR = Environment.getExternalStorageDirectory().getAbsolutePath() + "/jajalewatch/mp3/";
public   String mp3_DIR;

    private Handler handler;

    public static QADetailsFragment newInstance(String fragmentType) {
        QADetailsFragment fragment = new QADetailsFragment();
        fragment.fragmentType = fragmentType;
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mp3_DIR= FileUtils.getDiskCacheDir(getContext())+"/QAVoiceCache/";
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

//        Bundle bundle = getArguments();
//        fragmentType = bundle.getString("arg");
//        L.i("guokm", bundle.getString("arg"));
        View view = inflater.inflate(R.layout.fragment_math, null);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        qaSqliteHelper = QASqliteHelper.getInstance(getActivity());
        loadingDialog = new LoadingDialog(getActivity());
        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case 0:
                        //网络错误
                        T.s("网络不给力");
                        loadingDialog.dismiss();
                        break;
                    case 1:
                        //成功下载
                        L.i("guokm", "1111111-msg");
                        loadingDialog.dismiss();
                        playOrStopMusic();
                        break;
                    case 2:
                        //进度跟新
                        break;
                }
                super.handleMessage(msg);
            }
        };
        ques_tv = (TextView) view.findViewById(R.id.ques_tv);
        answer_tv = (TextView) view.findViewById(R.id.answer_tv);
        answer_btn = (Button) view.findViewById(R.id.answer_btn);
        next_ques_btn = (Button) view.findViewById(R.id.next_ques_btn);
        pic_iv = (ImageView) view.findViewById(R.id.pic_iv);
        anim_view = view.findViewById(R.id.anim_view);

        anim_view.setBackgroundResource(R.drawable.voice_animation);
        drawable = (AnimationDrawable) anim_view.getBackground();

        answer_btn.setOnClickListener(this);
        next_ques_btn.setOnClickListener(this);
        anim_view.setOnClickListener(this);
        getQuesByType();
        initView();
    }

    private void initView() {
        L.i("guokm", "initView-------:::::" + qaEntity.toString());
        /**
         * 当前fragmentType如果还有其他也是需要图，只需要在下面添加fragmentType.equals(QaEnum.XX + "")
         */
        if (qaEntity.q_url != null && (!qaEntity.q_url.equals("")) && (fragmentType.equals(QaEnum.look + "") || fragmentType.equals(QaEnum.math + ""))) {
            //ImageView显示（看图识字）
//            L.i("guokm","111111111111111111111111111111111111323333333331313");
            pic_iv.setVisibility(View.VISIBLE);
            JJLImageLoader.download(getActivity(), qaEntity.getQ_url(), pic_iv);
            if (drawable.isRunning()) {
                drawable.stop();
            }
        } else {
            //ImageView不显示（看图识字）
            pic_iv.setVisibility(View.INVISIBLE);
        }
        if ((qaEntity.q_url != "") && (fragmentType.equals(QaEnum.voice + ""))) {
//            设置默认的喇叭图标
            anim_view.setVisibility(View.VISIBLE);
            if (drawable.isRunning()) {
                drawable.stop();
            }
        } else {
            anim_view.setVisibility(View.INVISIBLE);
        }

        ques_tv.setText(qaEntity.getQ_txt());
        answer_tv.setVisibility(View.INVISIBLE);
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_left:
                getActivity().finish();
                break;
            case R.id.answer_btn:
                //显示答案
                getRightAnwser();
                break;
            case R.id.next_ques_btn:
                //下一题(随机)
                if (drawable.isRunning()) {
                    drawable.stop();
                }
                getQuesByType();
                initView();
                if (mediaPlayer != null && mediaPlayer.isPlaying()) {
                    mediaPlayer.reset();
                }
                //
                //自动播放声音
                //
                if (qaEntity.q_url != null) {
                    L.i("guokm", "auto 播放");
                    playOrDownLoad();
                }
                //
                break;
            case R.id.anim_view:
                playOrDownLoad();

                break;
        }
    }

    public void playOrDownLoad() {
        //播放/暂停声音，切换图片（如果本地路径不存在改声音文件则下载，如果存在则读取-----然后播放）;
        if (!isLocalVoice(hashKeyForDisk(qaEntity.getQ_url()))) {
            // 判断下网络这里
            loadingDialog.show();
            new Download().to(qaEntity.getQ_url(), mp3_DIR + hashKeyForDisk(qaEntity.getQ_url()), handler);
            //下載就播放
            L.i("guokm", "wangluo");
        } else {
            L.i("guokm", "bendi");

            playOrStopMusic();
        }
    }

    public void playOrStopMusic() {
        if (mediaPlayer != null && mediaPlayer.isPlaying()) {
            drawable.stop();
            mediaPlayer.reset();
        } else {
            startmusic(mp3_DIR + hashKeyForDisk(qaEntity.getQ_url()));
        }
    }


    public void log(){
        L.e("234===mediaPlayer=="+mediaPlayer);
        L.e("234===");
        L.e("234===");
        L.e("234===");
        L.e("234===");



    }

    private void startmusic(String currentMp3Path) {
        if (mediaPlayer == null) {
            mediaPlayer = new MediaPlayer();
            mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mediaPlayer) {
                    if (mediaPlayer != null) {
                        drawable.stop();
                        mediaPlayer.reset();
                    }
                }
            });
        }
        try {
            mediaPlayer.setDataSource(currentMp3Path);
            mediaPlayer.prepare();
            mediaPlayer.start();
            mediaPlayer.seekTo(0);
            //播放动画
            drawable.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private boolean isLocalVoice(String strMd5) {
        File file = new File(mp3_DIR + strMd5);
        if (file.exists()) {
            return true;
        }
        File fileVoice = new File((mp3_DIR));

        fileVoice.mkdirs();


        return false;
    }

    private void getQuesByType() {
        if (fragmentType.equals(QaEnum.math + "")) {
            getNextQues(getPosition(8), 8 + "");
        } else if (fragmentType.equals(QaEnum.grown + "")) {
            getNextQues(getPosition(5), 5 + "");
        } else if (fragmentType.equals(QaEnum.look + "")) {
            getNextQues(getPosition(6), 6 + "");
        } else if (fragmentType.equals(QaEnum.voice + "")) {
            getNextQues(getPosition(9), 9 + "");
        } else if (fragmentType.equals(QaEnum.culture + "")) {
            getNextQues(getPosition(1), 1 + "");
        } else if (fragmentType.equals(QaEnum.earth + "")) {
            getNextQues(getPosition(2), 2 + "");
        } else if (fragmentType.equals(QaEnum.technology + "")) {
            getNextQues(getPosition(3), 3 + "");
        } else {
            getNextQues(getPosition(4), 4 + "");
        }

    }

    private int getPosition(int category) {
        int currentPosition = 0;
        try {
            int minPosition = getCategoryCount(category, "min");
            int maxPosition = getCategoryCount(category, "max");
/*            SharedPreferences.Editor editor=getActivity().getSharedPreferences("qa_sp", Context.MODE_PRIVATE).edit();
                    editor.putInt(category+"_min",minPosition);
            editor.putInt(category+"_min",maxPosition);
            editor.commit();*/
            SharedPreferences sp = getActivity().getSharedPreferences("qa_sp", Context.MODE_PRIVATE);
            //-1则为没有存过，首次进入创建

            if (-1 == sp.getInt(category + "", -1)) {
                //从最小题开始做
                currentPosition = minPosition;
            } else {
                //存了的，currentPosition+1开始做
                currentPosition = sp.getInt(category + "", minPosition) + 1;
                L.e("guokm", "--category111:" + category + "----minp111:" + minPosition + "---maxp111:" + maxPosition);
            }
            if (currentPosition > maxPosition) {
                L.e("guokm", "--category:" + category + "----minp:" + minPosition + "---maxp:" + maxPosition);
                currentPosition = minPosition;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        L.e("guokm", "--currentPosition:" + currentPosition);
        return currentPosition;
    }

    /**
     *
     */
    private int getCategoryCount(int category, String minOrMax) throws Exception {
        if (dao == null) {
            dao = qaSqliteHelper.getDao(QAEntity.class);
        }
        Long position;
        if (minOrMax.equals("min")) {
            position = dao.queryRawValue("select min(qa_id) from QAEntity where category=" + category);
            return position.intValue();
        }
        if (minOrMax.equals("max")) {
            position = dao.queryRawValue("select max(qa_id) from QAEntity where category=" + category);
            return position.intValue();
        }
        return 1;
    }

    /**
     * 获取题
     *
     * @param ques_position 题号
     */
    private void getNextQues(int ques_position, String category) {
        try {
            Dao<QAEntity, String> dao = qaSqliteHelper.getDao(QAEntity.class);
            qaEntity = dao.queryForId(ques_position + "");
            //做完该题，保存当前position
            SharedPreferences.Editor editor = getActivity().getSharedPreferences("qa_sp", Context.MODE_PRIVATE).edit();
            editor.putInt(category + "", ques_position);
            editor.commit();
            L.i("guokm", qaEntity.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 展示答案
     */
    private void getRightAnwser() {
        answer_tv.setText(qaEntity.getA_txt());
        answer_tv.setVisibility(View.VISIBLE);
        L.d("guokm", qaEntity.getA_txt());
    }


    /**
     * MD5加密音乐路径
     *
     * @param key
     * @return
     */
    public String hashKeyForDisk(String key) {
        String cacheKey;
        try {
            final MessageDigest mDigest = MessageDigest.getInstance("MD5");
            mDigest.update(key.getBytes());
            cacheKey = bytesToHexString(mDigest.digest());
        } catch (NoSuchAlgorithmException e) {
            cacheKey = String.valueOf(key.hashCode());
        }
        return cacheKey;
    }

    private String bytesToHexString(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < bytes.length; i++) {
            String hex = Integer.toHexString(0xFF & bytes[i]);
            if (hex.length() == 1) {
                sb.append('0');
            }
            sb.append(hex);
        }
        return sb.toString();
    }


    /**
     * 下载工具类
     *
     * @author lu
     */
    public class Download {

        /**
         * 进度这里固定为100
         */
        public static final int jd = 100;

        /**
         * @param urlStr   http://f.txt
         * @param path     /mnt/sdcard/f.txt
         * @param mHandler what=0失败1成功2进度：arg1(1~100)
         */
        public void to(final String urlStr, final String path, final Handler mHandler) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    File f = new File(path);
                    try {
                        f.createNewFile();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    InputStream is = null;
                    OutputStream os = null;
                    try {
                        // 构造URL
                        URL url = new URL(urlStr);
                        // 打开连接
                        URLConnection con = url.openConnection();
                        //获得文件的长度
                        int contentLength = con.getContentLength();
                        int cd = 0;
                        if (contentLength >= jd) {
                            cd = contentLength / jd;//计算出文件长度的1/100用于进度
                        }
                        // 输入流
                        is = con.getInputStream();
                        // 1K的数据缓冲
                        byte[] bs = new byte[1024];
                        // 读取到的数据长度
                        int len;
                        // 输出的文件流
                        os = new FileOutputStream(path);
                        // 开始读取
                        int i = 0;
                        int arg = 0;
                        while ((len = is.read(bs)) != -1) {
                            os.write(bs, 0, len);
                            i += len;
                            if (i >= cd) {
                                Message msg = Message.obtain();
                                msg.arg1 = ++arg;
                                msg.what = 2;
                                mHandler.sendMessage(msg);
                                i = 0;
                            }
                        }
                        // 完毕，关闭所有链接
                        os.close();
                        is.close();
                        mHandler.sendEmptyMessage(1);
                    } catch (Exception e) {
                        e.printStackTrace();
                        mHandler.sendEmptyMessage(0);
                    }
                }
            }).start();
        }

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
 /*       if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.release();
        }*/
        if (drawable.isRunning()) {
            drawable.stop();
        }
        L.i("guokm", "我切换了1");

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.reset();
            L.i("guokm", "我执行了停止音乐2");
        }
        if (drawable.isRunning()) {
            drawable.stop();

            L.i("guokm", "我执行了停止动画2");
        }
        L.i("guokm", "我切换了2");
    }

    @Override
    public void onResume() {
        super.onResume();
        L.i("guokm", "我执行了停止动画onResume");
    }

    @Override
    public void onPause() {
        super.onPause();
        if (drawable.isRunning()) {
            drawable.stop();

            L.i("guokm", "我执行了停止动画3");
        }
        L.i("guokm", "我切换了3");
    }

    /**
     * 设置改fg的UI是否被当前用户可见
     * 作用：避免预加载（vp设置只能至少预加载一页，实际内存中加载了两页）
     *
     * @param isVisibleToUser
     */
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (!isVisibleToUser) {
            if (mediaPlayer != null) {
                mediaPlayer.reset();
            }
            if (drawable != null && drawable.isRunning()) {
                drawable.stop();
                L.i("guokm", "setUserVisibleHint执行");
            }
        }
    }
}
