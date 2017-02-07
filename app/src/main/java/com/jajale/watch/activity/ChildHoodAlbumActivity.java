package com.jajale.watch.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.jajale.watch.AppConstants;
import com.jajale.watch.BaseApplication;
import com.jajale.watch.CodeConstants;
import com.jajale.watch.R;
import com.jajale.watch.adapter.ChildHoodAlbumListAdapter;
import com.jajale.watch.entity.AlbumData;
import com.jajale.watch.entity.AppInfo;
import com.jajale.watch.entity.RequestCode;
import com.jajale.watch.helper.GetMessageHelper;
import com.jajale.watch.listener.GetMessageListener;
import com.jajale.watch.listener.HttpClientListener;
import com.jajale.watch.utils.CMethod;
import com.jajale.watch.utils.DialogUtils;
import com.jajale.watch.utils.HttpUtils;
import com.jajale.watch.utils.DisplayUtil;
import com.jajale.watch.utils.ImageUtils;
import com.jajale.watch.utils.L;
import com.jajale.watch.utils.LoadingDialog;
import com.jajale.watch.utils.QiNiuUtils;
import com.jajale.watch.utils.T;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.ButterKnife;
import butterknife.InjectView;
import in.srain.cube.views.ptr.PtrClassicFrameLayout;
import in.srain.cube.views.ptr.PtrDefaultHandler;
import in.srain.cube.views.ptr.PtrFrameLayout;
import in.srain.cube.views.ptr.PtrHandler;
import in.srain.cube.views.ptr.header.MaterialHeader;

/**
 * Created by llh on 16-4-13.
 */
public class ChildHoodAlbumActivity extends NoNetworkActivity implements View.OnClickListener {
    @InjectView(R.id.iv_left)
    ImageView ivLeft;
    @InjectView(R.id.tv_left_2)
    TextView tvLeft2;
    @InjectView(R.id.tv_middle)
    TextView tvMiddle;
    @InjectView(R.id.tv_right)
    TextView tvRight;
    @InjectView(R.id.album_tv_no_data)
    TextView albumTvNoData;
    @InjectView(R.id.iv_right)
    ImageView ivRight;
    @InjectView(R.id.layout_title)
    RelativeLayout layoutTitle;
    @InjectView(R.id.album_lv_content)
    ListView albumLvContent;

    private LoadingDialog loadingDialog;
    private QiNiuUtils qiNiuUtils;
    private String imagepath;
    private ChildHoodAlbumListAdapter adapter;
    private PtrClassicFrameLayout mPtrFrame;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_childhood_album);
        ButterKnife.inject(this);
        showNotworkView();
        loadingDialog = new LoadingDialog(this);
        qiNiuUtils = new QiNiuUtils(this);
        initView();
        setAlbumListFromNetWork();
    }

    private void initView() {
//        layoutTitle.setBackgroundColor(getResources().getColor(R.color.edu_title_bg));
        layoutTitle.setBackgroundResource(R.mipmap.nav_bar);
        tvMiddle.setText(getResources().getString(R.string.childhood_album));
        ivLeft.setImageResource(R.drawable.title_goback_selector);
        ivLeft.setOnClickListener(this);
        ivRight.setImageResource(R.mipmap.icon_title_right_album);
        ivRight.setOnClickListener(this);
        albumTvNoData.setOnClickListener(this);
        albumLvContent.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                //获取状态栏高度
                if (CMethod.isFastDoubleClick())
                    return;
                if (adapter != null) {
                    Intent intent = new Intent(ChildHoodAlbumActivity.this, TouchImageViewActivity.class);
                    intent.putExtra("image_url_path", adapter.getItem(position).getImgUrl());
                    startActivity(intent);
                }

            }
        });

        mPtrFrame = (PtrClassicFrameLayout) findViewById(R.id.rotate_header_list_view_frame);
//        mPtrFrame.setLastUpdateTimeRelateObject(this);
        final MaterialHeader header=new MaterialHeader(ChildHoodAlbumActivity.this);
        header.setPtrFrameLayout(mPtrFrame);
        int[] colors = getResources().getIntArray(R.array.material_colors);
        header.setColorSchemeColors(colors);
        header.setLayoutParams(new PtrFrameLayout.LayoutParams(-1, -2));
        header.setPadding(0, DisplayUtil.designedDP2px(15),0,DisplayUtil.designedDP2px(10));
        mPtrFrame.setDurationToClose(100);
        mPtrFrame.setPinContent(true);
        mPtrFrame.setHeaderView(header);
        mPtrFrame.addPtrUIHandler(header);

        mPtrFrame.setPtrHandler(new PtrHandler() {
            @Override
            public boolean checkCanDoRefresh(PtrFrameLayout frame, View content, View header) {
                return PtrDefaultHandler.checkContentCanBePulledDown(frame, content, header);
            }

            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                GetMessageHelper helper = new GetMessageHelper(ChildHoodAlbumActivity.this, new GetMessageListener() {
                    @Override
                    public void onSuccess(String userinfoJson) {

                        if (!CMethod.isNet(ChildHoodAlbumActivity.this)){
                            T.s("请检查网络连接");
                            mPtrFrame.refreshComplete();
                            return;
                        }
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                mPtrFrame.postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        setAlbumListFromNetWork();
                                        mPtrFrame.refreshComplete();
                                    }
                                }, 2000);
                            }
                        }).start();

                    }

                    @Override
                    public void onFailure() {
                        mPtrFrame.refreshComplete();
                    }
                });
                helper.getMessage();
            }
        });
        // the following are default settings
        mPtrFrame.setResistance(2.7f);
        mPtrFrame.setRatioOfHeaderHeightToRefresh(1.2f);
        mPtrFrame.setDurationToClose(200);
        mPtrFrame.setDurationToCloseHeader(100);
        // default is false
        mPtrFrame.setPullToRefresh(false);
        // default is true
        mPtrFrame.setKeepHeaderWhenRefresh(true);


    }
    private void setAlbumListFromNetWork(){

        loadingDialog.show();
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("userID", BaseApplication.getUserInfo().userID);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        HttpUtils.PostDataToWeb(AppConstants.JAVA_HAS_CODE_URL, CodeConstants.GET_ALBUM_URL, jsonObject, new HttpClientListener() {
            @Override
            public void onSuccess(String result) {
                loadingDialog.dismiss();
                Gson gson = new Gson();
                AlbumData response = gson.fromJson(result, AlbumData.class);
                if (response != null && response.getImgList() != null && response.getImgList().size() != 0) {
                    if (adapter == null) {
                        adapter = new ChildHoodAlbumListAdapter(ChildHoodAlbumActivity.this, response.getImgList());
                        albumLvContent.setAdapter(adapter);
                    } else {
                        adapter.mList = response.getImgList();
                        adapter.notifyDataSetChanged();
                    }
                    albumLvContent.setVisibility(View.VISIBLE);
                    albumTvNoData.setVisibility(View.GONE);
                } else {
                    albumLvContent.setVisibility(View.GONE);
                    albumTvNoData.setVisibility(View.VISIBLE);

                }

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


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case RequestCode.TAKE_FROM_CAMERA://照相
                if (resultCode == Activity.RESULT_OK) {
                    imagepath = AppInfo.getInstace().getCameraTempPath();
                        uploadImage2QiNiu(imagepath);

                }
                break;
            case RequestCode.TAKE_FROM_PHOTO://图片
                if (resultCode == Activity.RESULT_OK) {
                    if (data.getData() != null) {
                        imagepath = ImageUtils.getPathFromUri(ChildHoodAlbumActivity.this, data.getData());
                        uploadImage2QiNiu(imagepath);
                    }
                }
                break;
        }

    }


    /**
     * 选择照片或图片
     */
    private void uploadPic() {
        DialogUtils.uploadPicDialog(ChildHoodAlbumActivity.this);
    }

    /**
     * 将图片上传至其牛
     *
     * @param imagePath
     */
    private void uploadImage2QiNiu(final String imagePath) {

        if (!CMethod.isNet(ChildHoodAlbumActivity.this)) {
            T.s("请检查网络连接");
            return;
        }

        loadingDialog.show();
        new Thread(new Runnable() {
            @Override
            public void run() {
                String path = ImageUtils.handleUploadImage(ChildHoodAlbumActivity.this, imagePath, 50);

                if (path != null) {
                    qiNiuUtils.uploadImage(path, new QiNiuUtils.ProgressHandler() {
                        @Override
                        public void progress(double percent) {
                            L.d("上传图片进度 ： " + percent * 100 + "%");
                        }
                    }, new QiNiuUtils.UploadHandler() {
                        @Override
                        public void ok(String filename) {
                            loadingDialog.dismiss();
                            String image_path = AppConstants.QINIU_IMG_DOMAIN + filename;
                            L.d("上传图片进度 ----image_path===： " + image_path);
                            openUploadActivity(image_path);
                        }

                        @Override
                        public void error(String err) {
                            loadingDialog.dismiss();
                        }
                    });
                }
            }
        }).start();

    }


    private void openUploadActivity(String path) {

        Intent intent = new Intent(ChildHoodAlbumActivity.this, ChildHoodAlbumUploadActivity.class);
        intent.putExtra("watch_id", getIntent().getStringExtra("watch_id"));
        intent.putExtra("album_image_network_url", path);
        intent.putExtra("album_image_local_url", imagepath);
        startActivity(intent);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_left://返回键
                finish();
                break;
            case R.id.iv_right:// 上传图片
                uploadPic();

                break;
            case R.id.album_tv_no_data:// 上传图片

                uploadPic();

                break;
        }
    }
}
