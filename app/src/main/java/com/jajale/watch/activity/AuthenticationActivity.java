package com.jajale.watch.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.jajale.watch.AppConstants;
import com.jajale.watch.BaseActivity;
import com.jajale.watch.BaseApplication;
import com.jajale.watch.R;
import com.jajale.watch.entity.AppInfo;
import com.jajale.watch.entity.RequestCode;
import com.jajale.watch.listener.HttpClientListener;
import com.jajale.watch.utils.CMethod;
import com.jajale.watch.utils.DialogUtils;
import com.jajale.watch.utils.HttpUtils;
import com.jajale.watch.utils.ImageUtils;
import com.jajale.watch.utils.L;
import com.jajale.watch.utils.LoadingDialog;
import com.jajale.watch.utils.QiNiuUtils;
import com.jajale.watch.utils.T;
import com.jajale.watch.utils.UrlAddressUrils;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * 实名认证
 * Created by lilonghui on 2016/1/18.
 * Email:lilonghui@bjjajale.com
 */
public class AuthenticationActivity extends BaseActivity implements OnClickListener {


    @InjectView(R.id.tv_middle)
    TextView tvMiddle;
    @InjectView(R.id.authentication_iv_front)
    ImageView authenticationIvFront;
    @InjectView(R.id.authentication_iv_behind)
    ImageView authenticationIvBehind;
    @InjectView(R.id.authentication_iv_hand)
    ImageView authentication_iv_hand;

    @InjectView(R.id.authentication_btn_commit)
    Button authenticationBtnCommit;

    private LoadingDialog loadingDialog;
    private QiNiuUtils qiNiuUtils;

    private ImageView iv_target ;

    private String url_front = "";
    private String url_back = "";
    private String url_hand = "" ;

    private String uri_front = "";
    private String uri_back = "";
    private String uri_hand = "" ;

    private String click_source = "" ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_authentication);
        ButterKnife.inject(this);
        initView();
        setOnClickListener();
        setButtonType(false);
    }

    private void initView() {
        loadingDialog = new LoadingDialog(this);
        tvMiddle.setText(getResources().getString(R.string.authentication_title));

        qiNiuUtils = new QiNiuUtils(AuthenticationActivity.this);
    }

    private void setOnClickListener() {
        authenticationBtnCommit.setOnClickListener(this);
        authenticationIvFront.setOnClickListener(this);
        authenticationIvBehind.setOnClickListener(this);
        authentication_iv_hand.setOnClickListener(this);
    }


    /**
     * button是否可点击
     *
     * @param isCanClickable
     */
    private void setButtonType(boolean isCanClickable) {
        if (isCanClickable) {
            authenticationBtnCommit.setBackgroundResource(R.drawable.button_common_on_selector_green);
            authenticationBtnCommit.setTextColor(getResources().getColor(R.color.common_button_middle_text_on_color));
            authenticationBtnCommit.setClickable(true);
        } else {

            authenticationBtnCommit.setBackgroundResource(R.drawable.shape_button_gray_normal);
            authenticationBtnCommit.setTextColor(getResources().getColor(R.color.common_button_middle_text_off_color));
            authenticationBtnCommit.setClickable(false);

        }
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.authentication_btn_commit://提交
                updateProfile();
                break;
            case R.id.authentication_iv_front://正面照片
                click_source = "front";
                uploadPic();

                break;
            case R.id.authentication_iv_behind://反面照片
                click_source = "back";
                uploadPic();
                break;
            case R.id.authentication_iv_hand://手持照片
                click_source = "hand";
                uploadPic();
                break;


        }

    }

    private void uploadPic(){
        DialogUtils.uploadPicDialog(AuthenticationActivity.this);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (!CMethod.isNet(AuthenticationActivity.this)){
            T.s("网路不给力");
            return;
        }
        switch (requestCode) {
            case RequestCode.TAKE_FROM_CAMERA:
                if (resultCode == Activity.RESULT_OK) {
                        uploadImage(AppInfo.getInstace().getCameraTempPath());
                }

                break;
            case RequestCode.TAKE_FROM_PHOTO:
                if (resultCode == Activity.RESULT_OK) {
                    if (data.getData() != null) {
                        String imagepath = ImageUtils.getPathFromUri(AuthenticationActivity.this, data.getData());
                        uploadImage(imagepath);
                    }
                }
                break;
        }
    }

    private void uploadImage(final String imagepath) {
        loadingDialog.show();
        L.e(imagepath);
        if (click_source.equals("front")){
            uri_front = imagepath;
        }else if(click_source.equals("back")){
            uri_back = imagepath;
        }else if(click_source.equals("hand")){
            uri_hand = imagepath;
        }

        new Thread(new Runnable() {
            @Override
            public void run() {
                String path = ImageUtils.handleUploadImage(AuthenticationActivity.this, imagepath, AppConstants.MAX_IMAGE_SIZE);
                if (path != null) {
                    qiNiuUtils.uploadImage(path, new QiNiuUtils.ProgressHandler() {
                        @Override
                        public void progress(double percent) {
                            L.d("上传图片进度 ： " + percent * 100 + "%");
                        }
                    }, new QiNiuUtils.UploadHandler() {
                        @Override
                        public void ok(String filename) {
                            //更新profile
//                                L.e("---881228---" + filename);
                            if (click_source.equals("back")){
                                url_back = AppConstants.QINIU_IMG_DOMAIN + filename;
                                authenticationIvBehind.setImageBitmap(ImageUtils.decodeSampledBitmapFromFile(uri_back,authenticationIvBehind.getWidth(),authenticationIvBehind.getHeight()));
                            }else if(click_source.equals("front")){
                                url_front = AppConstants.QINIU_IMG_DOMAIN + filename;
                                authenticationIvFront.setImageBitmap(ImageUtils.decodeSampledBitmapFromFile(uri_front,authenticationIvFront.getWidth(),authenticationIvFront.getHeight()));
                            }else if(click_source.equals("hand")){
                                url_hand = AppConstants.QINIU_IMG_DOMAIN + filename;
                                authentication_iv_hand.setImageBitmap(ImageUtils.decodeSampledBitmapFromFile(uri_hand,authentication_iv_hand.getWidth(),authentication_iv_hand.getHeight()));
                            }
                            click_source = "";
                            checkBtn();
                        }

                        @Override
                        public void error(String err) {
                            L.e(err);
                            if (click_source.equals("back")){
                                url_back = "";
                            }else if(click_source.equals("front")){
                                url_front = "" ;
                            }else if(click_source.equals("hand")){
                                url_hand = "";
                            }

                            loadingDialog.dismiss();
                        }
                    });
                }
            }
        }).start();

    }

    private void checkBtn(){
        if (CMethod.isEmpty(url_back) || CMethod.isEmpty(url_front)  || CMethod.isEmpty(url_hand) || !url_front.startsWith("http://") || !url_back.startsWith("http://") || !url_hand.startsWith("http://")){
            setButtonType(false);
        }else {
            setButtonType(true);
        }
        loadingDialog.dismiss();

    }


    private void updateProfile() {
        L.e("更新用户资料");
        if (!CMethod.isNet(AuthenticationActivity.this)){
            T.s("网路不给力");
            return;
        }

        loadingDialog.show();
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("user_id", BaseApplication.getUserInfo().userID);
            jsonObject.put("z_image_url", url_front);
            jsonObject.put("f_image_url", url_back);
            jsonObject.put("sc_image_url", url_hand);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        HttpUtils.PostDataToWeb(UrlAddressUrils.CODE_API,AppConstants.JAVA_UPLOAD_CERTIFICATION_DATA_URL, jsonObject, new HttpClientListener() {
            @Override
            public void onSuccess(String result) {
                loadingDialog.dismiss();
                L.e("getRegisterState_onSuccess" + result);
//                HomeSecActivity.isChangeWatchData=true;
                Finish();
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

    }


    private void Finish(){
        finish();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK){
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }


}
