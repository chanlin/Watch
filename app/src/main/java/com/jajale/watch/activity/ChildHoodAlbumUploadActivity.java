package com.jajale.watch.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jajale.watch.AppConstants;
import com.jajale.watch.BaseActivity;
import com.jajale.watch.BaseApplication;
import com.jajale.watch.CodeConstants;
import com.jajale.watch.R;
import com.jajale.watch.listener.HttpClientListener;
import com.jajale.watch.utils.CMethod;
import com.jajale.watch.utils.HttpUtils;
import com.jajale.watch.utils.ImageUtils;
import com.jajale.watch.utils.LoadingDialog;
import com.jajale.watch.utils.T;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by llh on 16-4-13.
 */
public class ChildHoodAlbumUploadActivity extends BaseActivity implements View.OnClickListener {
    @InjectView(R.id.iv_left)
    ImageView ivLeft;
    @InjectView(R.id.tv_left_2)
    TextView tvLeft2;
    @InjectView(R.id.tv_middle)
    TextView tvMiddle;
    @InjectView(R.id.tv_right)
    TextView tvRight;
    @InjectView(R.id.iv_right)
    ImageView ivRight;
    @InjectView(R.id.layout_title)
    RelativeLayout layoutTitle;
    @InjectView(R.id.album_iv_pic)
    ImageView albumIvPic;

    @InjectView(R.id.album_rl_side)
    RelativeLayout albumRlSide;

    private LoadingDialog loadingDialog;
    private String image_path;
    private String image_path_local;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_childhood_album_upload);
        ButterKnife.inject(this);
        loadingDialog = new LoadingDialog(this);
        initView();
        image_path = getIntent().getStringExtra("album_image_network_url");
        image_path_local = getIntent().getStringExtra("album_image_local_url");
        albumIvPic.setImageBitmap(ImageUtils.decodeSampledBitmapFromFile(image_path_local));
    }

    private void initView() {

        layoutTitle.setBackgroundResource(R.mipmap.nav_bar);
        tvMiddle.setText(getResources().getString(R.string.childhood_album));
        tvRight.setText(getResources().getString(R.string.upload_album));
        tvRight.setVisibility(View.VISIBLE);
        tvRight.setOnClickListener(this);
        ivLeft.setImageResource(R.drawable.title_goback_selector);
        ivLeft.setOnClickListener(this);

    }


    private void uploadImageToNetwork() {
        try {

            loadingDialog.show();
            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.put("userID", BaseApplication.getUserInfo().userID);
                jsonObject.put("watchID", getIntent().getStringExtra("watch_id"));
                jsonObject.put("imgList", getJsonStringALbumList());
            } catch (JSONException e) {
                e.printStackTrace();
            }
            HttpUtils.PostDataToWeb(AppConstants.JAVA_HAS_CODE_URL, CodeConstants.POST_ALBUM_URL, jsonObject, new HttpClientListener() {
                @Override
                public void onSuccess(String result) {
                    loadingDialog.dismiss();
                    T.s("上传成功");
                    finish();

                }

                @Override
                public void onFailure(String result) {
                    loadingDialog.dismiss();
                    T.s(result);
                }

                @Override
                public void onError() {
                    loadingDialog.dismiss();
                }
            });
        } catch (Exception e) {
            loadingDialog.dismiss();
        }
    }


    private String getJsonStringALbumList() {


        try {
            JSONArray jsonArray = new JSONArray();
            JSONObject obj = new JSONObject();
            obj.put("imgUrl", image_path);
            jsonArray.put(obj);
            return jsonArray.toString();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return "";


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_left://返回键
                finish();
                break;
            case R.id.tv_right:// 发布
                if (CMethod.isNet(ChildHoodAlbumUploadActivity.this)) {
                    uploadImageToNetwork();
                } else {
                    T.s("请检查网络连接");
                }
                break;
        }
    }
}
