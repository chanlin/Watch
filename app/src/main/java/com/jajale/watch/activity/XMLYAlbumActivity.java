package com.jajale.watch.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.jajale.watch.AppConstants;
import com.jajale.watch.BaseActivity;
import com.jajale.watch.R;
import com.jajale.watch.adapter.XMLYAlbumAdapter;
import com.jajale.watch.cviews.PullRefreshListView;
import com.jajale.watch.utils.CMethod;
import com.jajale.watch.utils.L;
import com.jajale.watch.utils.LoadingDialog;
import com.jajale.watch.utils.T;
import com.ximalaya.ting.android.opensdk.constants.DTransferConstants;
import com.ximalaya.ting.android.opensdk.datatrasfer.CommonRequest;
import com.ximalaya.ting.android.opensdk.datatrasfer.IDataCallBack;
import com.ximalaya.ting.android.opensdk.model.album.Album;
import com.ximalaya.ting.android.opensdk.model.album.AlbumList;

import java.util.HashMap;
import java.util.List;

/**
 * Created by llh on 16-5-23.
 */
public class XMLYAlbumActivity extends BaseActivity implements PullRefreshListView.PullRefreshListener {
    List<Album> albumList;
    XMLYAlbumAdapter listViewAdapter;
    LoadingDialog loadingDialog;
    private String url;
    ImageView ivLeft;
    TextView tvMiddle;
    PullRefreshListView listView;
    HashMap<String, String> map;
    private int mCurrentPagerNum = 1;
    private String mCurrentAlbumName;
    String category_id;
    String tag_name;
    private CommonRequest mXimalaya;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_xmly_album);
        map = new HashMap<String, String>();
        initView();
        initXMLYSDK();
        initData();
    }
    private void initXMLYSDK() {
        mXimalaya = CommonRequest.getInstanse();
        mXimalaya.init(XMLYAlbumActivity.this, AppConstants.mAppSecret);
        mXimalaya.setDefaultPagesize(50);
    }

    private void initData() {
        Intent intent = getIntent();
        category_id = String.valueOf(6);
        tag_name = intent.getStringExtra("tag_name");
        if(tag_name.equals("英文磨耳朵")){
            tvMiddle.setText("少儿英语");
        }else if(tag_name.equals("国学启蒙")){
            tvMiddle.setText("知识启蒙");
        }else if(tag_name.equals("亲子学堂")){
            tvMiddle.setText("亲子教育");
        }else {
            tvMiddle.setText(tag_name);
        }
        if (!loadingDialog.isShowing()) {
            loadingDialog.show();
        }
        getAlbums(tag_name,mCurrentPagerNum );
    }
    private void initView() {
        listViewAdapter = new XMLYAlbumAdapter(XMLYAlbumActivity.this);
        listView = (PullRefreshListView) findViewById(R.id.program_list);
        listView.setCanLoadMore(true);
        listView.setCanRefresh(false);
        listView.setPullRefreshListener(this);
        listView.setAdapter(listViewAdapter);
        listViewAdapter.setOnItemClickListener(new XMLYAlbumAdapter.OnItemClickListener() {
            /**
             * 歌曲列表
             * @param albumID
             * @param albumpicurl
             * @param lastListenID
             */
            @Override
            public void onItemClick(String albumTitle,String albumID, String albumpicurl, long lastListenID) {
                if(!CMethod.isNetWorkEnable(XMLYAlbumActivity.this)){
                    T.s(XMLYAlbumActivity.this.getString(R.string.check_network_connection));
                    return;
                }
                if (CMethod.isFastDoubleClick()) {
                    return;
                }
                Intent intent = new Intent(XMLYAlbumActivity.this, XMLYPlayActivity.class);
                intent.putExtra("albumtitle",albumTitle);
                intent.putExtra("albumID", albumID);
                intent.putExtra("albumpicurl", albumpicurl);
                intent.putExtra("lastListenID", lastListenID);
                startActivity(intent);
            }
        });

        tvMiddle = (TextView) findViewById(R.id.tv_middle);
        ivLeft = (ImageView) findViewById(R.id.iv_left);

        ivLeft.setImageResource(R.drawable.title_goback_selector);
        ivLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        if (loadingDialog == null) {
            loadingDialog = new LoadingDialog(XMLYAlbumActivity.this);
        }
    }
    private void getAlbums(String TagName, final int pageNums) {
/*        if (TagName.equals("童话寓言")){
            TagName="童话故事";
        }else if(TagName.equals("少儿英语")){
            TagName="儿童英语";
        }else if(TagName.equals("儿童童谣")){
            TagName="儿歌大全";
        }else if(TagName.equals("知识启蒙")){
            TagName="儿童科普";
        }else if(TagName.equals("亲子教育")){
            TagName="儿童教育";
        }else if(TagName.equals("少年文学")){
            TagName="绘本故事";
        }*/
/*        if (TagName.equals(TagEnum.少儿英语.toString())) {
            TagName = TagEnum.英文磨耳朵.toString();
        } else if (TagName.equals(TagEnum.知识启蒙.toString())) {
            TagName = TagEnum.国学启蒙.toString();
        } else if (TagName.equals(TagEnum.亲子教育.toString())) {
           TagName = TagEnum.亲子学堂.toString();
        }*/
        L.i("getAlbumsTagName:" + TagName);
        map.put(DTransferConstants.CATEGORY_ID, "6");
        map.put(DTransferConstants.TAG_NAME, TagName);
        map.put(DTransferConstants.PAGE, pageNums + "");
        CommonRequest.getAlbums(map, new IDataCallBack<AlbumList>() {
            @Override
            public void onSuccess(AlbumList object) {
                if (loadingDialog.isShowing()) {
                    loadingDialog.dismiss();
                }
                List<Album> tAlbumList = object.getAlbums();
                L.i("tAlbumList:" + tAlbumList.size());
                if (tAlbumList.size() != 0) {
                    if (albumList != null) {
                        albumList.addAll(tAlbumList);
                    } else {
                        albumList = tAlbumList;
                    }
                    listViewAdapter.setData(tAlbumList);
                    listViewAdapter.notifyDataSetChanged();
                    listView.onLoadMoreComplete();
                }
            }

            @Override
            public void onError(int code, String message) {
                if (loadingDialog.isShowing()) {
                    loadingDialog.dismiss();
                }
                T.s(getString(R.string.net_poor));
//                MyLogger.kLog().i(code + ":" + message);
            }
        });
    }

    @Override
    public void onRefresh() {
        if (mCurrentPagerNum - 1 <= 0) {
            mCurrentPagerNum = 1;
        } else {
            mCurrentPagerNum = mCurrentPagerNum - 1;
        }
        getAlbums( tag_name, mCurrentPagerNum);
    }

    @Override
    public void onLoadMore() {
//        MyLogger.kLog().i("mCurrentPagerNum:"+mCurrentPagerNum);
        mCurrentPagerNum = mCurrentPagerNum + 1;
        getAlbums( tag_name, mCurrentPagerNum);
    }

}
