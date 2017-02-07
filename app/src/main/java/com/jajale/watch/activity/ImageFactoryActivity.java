package com.jajale.watch.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ViewFlipper;

import com.jajale.watch.BaseActivity;
import com.jajale.watch.BaseApplication;
import com.jajale.watch.R;
import com.jajale.watch.cviews.ImageFactoryCrop;
import com.jajale.watch.utils.PhotoUtils;

public class ImageFactoryActivity extends BaseActivity implements  OnClickListener {
    private ViewFlipper mVfFlipper;
    private Button mBtnLeft;
    private Button mBtnRight;
    private ImageView ivLeft;
    private ImageView ivRight;
    private  TextView tvMiddle;
    private ImageFactoryCrop mImageFactoryCrop;
    private String mPath;
    private String mNewPath;
    private int mIndex = 0;
    private String mType;
    public static final String TYPE = "type";
    public static final String CROP = "crop";
    public static final String FLITER = "fliter";

    protected BaseApplication mApplication;

    /**
     * 屏幕的宽度、高度、密度
     */
    protected int mScreenWidth;
    protected int mScreenHeight;
    protected float mDensity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_imagefactory);
        mApplication = (BaseApplication) getApplication();
        DisplayMetrics metric = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metric);
        mScreenWidth = metric.widthPixels;
        mScreenHeight = metric.heightPixels;
        mDensity = metric.density;
        initViews();
        setListener();
        init();
    }

    protected void initViews() {
        tvMiddle= (TextView) findViewById(R.id.tv_middle);
        ivLeft= (ImageView) findViewById(R.id.iv_left);
        ivRight= (ImageView) findViewById(R.id.iv_right);
        tvMiddle.setText(getResources().getString(R.string.cut_photo));
        ivLeft.setImageResource(R.drawable.title_goback_selector);
        ivRight.setImageResource(R.mipmap.ic_topbar_rotation);
        mVfFlipper = (ViewFlipper) findViewById(R.id.imagefactory_vf_viewflipper);
        mBtnLeft = (Button) findViewById(R.id.imagefactory_btn_left);
        mBtnRight = (Button) findViewById(R.id.imagefactory_btn_right);
    }

    private void setListener() {
        ivRight.setOnClickListener(this);
        ivLeft.setOnClickListener(this);
        mBtnLeft.setOnClickListener(this);
        mBtnRight.setOnClickListener(this);
    }


    @Override
    public void onBackPressed() {
        if (mIndex == 0) {
            setResult(RESULT_CANCELED);
            finish();
        } else {
            if (FLITER.equals(mType)) {
                setResult(RESULT_CANCELED);
                finish();
            } else {
                mIndex = 0;
                initImageFactory();
                mVfFlipper.setInAnimation(ImageFactoryActivity.this,
                        R.anim.push_right_in);
                mVfFlipper.setOutAnimation(ImageFactoryActivity.this,
                        R.anim.push_right_out);
                mVfFlipper.showPrevious();
            }
        }
    }

    private void init() {
        mPath = getIntent().getStringExtra("path");
        mType = getIntent().getStringExtra(TYPE);
        mNewPath = new String(mPath);
        if (CROP.equals(mType)) {
            mIndex = 0;
        }
        initImageFactory();
    }

    private void initImageFactory() {
        switch (mIndex) {
            case 0:
                if (mImageFactoryCrop == null) {
                    mImageFactoryCrop = new ImageFactoryCrop(this,
                            mVfFlipper.getChildAt(0));
                }
                mImageFactoryCrop.init(mPath, mScreenWidth, mScreenHeight);
                mBtnLeft.setText(getResources().getString(R.string.quite));
                mBtnRight.setText(getResources().getString(R.string.ok));

                break;

        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.iv_left:
                if (mIndex == 0) {
                    setResult(RESULT_CANCELED);
                    finish();
                } else {
                    if (FLITER.equals(mType)) {
                        setResult(RESULT_CANCELED);
                        finish();
                    } else {
                        mIndex = 0;
                        initImageFactory();
                        mVfFlipper.setInAnimation(ImageFactoryActivity.this,
                                R.anim.push_right_in);
                        mVfFlipper.setOutAnimation(ImageFactoryActivity.this,
                                R.anim.push_right_out);
                        mVfFlipper.showPrevious();
                    }
                }
                break;
            case R.id.iv_right:
                //旋转
                if (mImageFactoryCrop != null) {
                    mImageFactoryCrop.Rotate();
                }
                break;
            case R.id.imagefactory_btn_left:
                //取消
                if (mIndex == 0) {
                    setResult(RESULT_CANCELED);
                    finish();
                } else {
                    if (FLITER.equals(mType)) {
                        setResult(RESULT_CANCELED);
                        finish();
                    } else {
                        mIndex = 0;
                        initImageFactory();
                        mVfFlipper.setInAnimation(ImageFactoryActivity.this,
                                R.anim.push_right_in);
                        mVfFlipper.setOutAnimation(ImageFactoryActivity.this,
                                R.anim.push_right_out);
                        mVfFlipper.showPrevious();
                    }
                }
                break;
            case R.id.imagefactory_btn_right:
                //剪裁完成，上传
                if (mIndex == 0) {
                    mNewPath = PhotoUtils.savePhotoToSDCard(mImageFactoryCrop
                            .cropAndSave());
                    Intent intent = new Intent();
                    intent.putExtra("path", mNewPath);
                    setResult(RESULT_OK, intent);
                    finish();
                }
                break;
        }


    }

}
