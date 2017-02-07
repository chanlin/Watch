package com.jajale.watch.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.jajale.watch.AppConstants;
import com.jajale.watch.BaseActivity;
import com.jajale.watch.R;
import com.jajale.watch.adapter.InfomationListAdapter;
import com.jajale.watch.entity.GetInfomationListData;
import com.jajale.watch.entity.InfomationItemEntity;
import com.jajale.watch.helper.ListViewFooterHelper;
import com.jajale.watch.listener.HttpClientListener;
import com.jajale.watch.utils.CMethod;
import com.jajale.watch.utils.HttpUtils;
import com.jajale.watch.listener.SimpleClickListener;
import com.jajale.watch.utils.DialogUtils;
import com.jajale.watch.utils.DisplayUtil;
import com.jajale.watch.utils.L;
import com.jajale.watch.utils.LoadingDialog;
import com.jajale.watch.utils.T;
import com.jajale.watch.utils.UrlAddressUrils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import in.srain.cube.views.ptr.PtrClassicFrameLayout;
import in.srain.cube.views.ptr.PtrDefaultHandler;
import in.srain.cube.views.ptr.PtrFrameLayout;
import in.srain.cube.views.ptr.PtrHandler;
import in.srain.cube.views.ptr.header.MaterialHeader;

/**
 * Created by athena on 2016/2/18.
 * Email: lizhiqiang@bjjajale.com
 */
public class InfomationListActivity extends BaseActivity {

    private LoadingDialog loadingDialog;
    private ListView mListView;
    private PtrClassicFrameLayout mPtrFrame;
    private InfomationListAdapter adapter;
    private ListViewFooterHelper<InfomationListAdapter> listViewFooterHelper;

    private ImageView ivLeft;

    private View empty_root;

    private boolean isRefreshFoot;
    private int visiableCount = 0;

    private int pagesize = 10;
    private int pageindex = 1;

    private String fromItemType;
    private String titleDes;
    private boolean firstload = true;

    private Gson gson = new Gson();
    private GetInfomationListData fromJson;

    private String item_video_json = "{\"contentType\":\"1\",\"msgCount\":\"20\",\"msgList\":[{\"detailed\":\"十个搞笑萌宝\",\"imgUrl\":\"http://7xosdd.com2.z0.glb.qiniucdn.com/011.png\",\"title\":\"十个搞笑萌宝\",\"url\":\"http://yuntv.letv.com/bcloud.html?uu\\u003dpyo48zevyh\\u0026vu\\u003d95667e9e26\\u0026auto_play\\u003d1\\u0026gpcflag\\u003d1\\u0026width\\u003d\\u003c#W#\\u003e\\u0026height\\u003d\\u003c#H#\\u003e\"},{\"detailed\":\"机智的小男孩\",\"imgUrl\":\"http://7xosdd.com2.z0.glb.qiniucdn.com/007.png\",\"title\":\"机智的小男孩\",\"url\":\"http://yuntv.letv.com/bcloud.html?uu\\u003dpyo48zevyh\\u0026vu\\u003d9c5aa8bd8b\\u0026auto_play\\u003d1\\u0026gpcflag\\u003d1\\u0026width\\u003d\\u003c#W#\\u003e\\u0026height\\u003d\\u003c#H#\\u003e\"},{\"detailed\":\"天生麦霸\",\"imgUrl\":\"http://7xosdd.com2.z0.glb.qiniucdn.com/012.png\",\"title\":\"天生麦霸\",\"url\":\"http://yuntv.letv.com/bcloud.html?uu\\u003dpyo48zevyh\\u0026vu\\u003d3d22cbb49e\\u0026auto_play\\u003d1\\u0026gpcflag\\u003d1\\u0026width\\u003d\\u003c#W#\\u003e\\u0026height\\u003d\\u003c#H#\\u003e\"},{\"detailed\":\"萌娃吃相比拼\",\"imgUrl\":\"http://7xosdd.com2.z0.glb.qiniucdn.com/009.png\",\"title\":\"萌娃吃相比拼\",\"url\":\"http://yuntv.letv.com/bcloud.html?uu\\u003dpyo48zevyh\\u0026vu\\u003d9438432829\\u0026auto_play\\u003d1\\u0026gpcflag\\u003d1\\u0026width\\u003d\\u003c#W#\\u003e\\u0026height\\u003d\\u003c#H#\\u003e\"},{\"detailed\":\"宝宝爆笑失误合集\",\"imgUrl\":\"http://7xosdd.com2.z0.glb.qiniucdn.com/003.png\",\"title\":\"宝宝爆笑失误合集\",\"url\":\"http://yuntv.letv.com/bcloud.html?uu\\u003dpyo48zevyh\\u0026vu\\u003daca510ecc8\\u0026auto_play\\u003d1\\u0026gpcflag\\u003d1\\u0026width\\u003d\\u003c#W#\\u003e\\u0026height\\u003d\\u003c#H#\\u003e\"},{\"detailed\":\"甜馨合集\",\"imgUrl\":\"http://7xosdd.com2.z0.glb.qiniucdn.com/013.png\",\"title\":\"甜馨合集\",\"url\":\"http://yuntv.letv.com/bcloud.html?uu\\u003dpyo48zevyh\\u0026vu\\u003d1909a50931\\u0026auto_play\\u003d1\\u0026gpcflag\\u003d1\\u0026width\\u003d\\u003c#W#\\u003e\\u0026height\\u003d\\u003c#H#\\u003e\"},{\"detailed\":\"让宝宝不哭的新招\",\"imgUrl\":\"http://7xosdd.com2.z0.glb.qiniucdn.com/010.png\",\"title\":\"让宝宝不哭的新招\",\"url\":\"http://yuntv.letv.com/bcloud.html?uu\\u003dpyo48zevyh\\u0026vu\\u003da99dfac532\\u0026auto_play\\u003d1\\u0026gpcflag\\u003d1\\u0026width\\u003d\\u003c#W#\\u003e\\u0026height\\u003d\\u003c#H#\\u003e\"},{\"detailed\":\"当得知糖果被爸妈吃光孩子的反应合集\",\"imgUrl\":\"http://7xosdd.com2.z0.glb.qiniucdn.com/006.png\",\"title\":\"吃光糖果后\",\"url\":\"http://yuntv.letv.com/bcloud.html?uu\\u003dpyo48zevyh\\u0026vu\\u003d87bd1b43c7\\u0026auto_play\\u003d1\\u0026gpcflag\\u003d1\\u0026width\\u003d\\u003c#W#\\u003e\\u0026height\\u003d\\u003c#H#\\u003e\"},{\"detailed\":\"萌宝来袭专治各种不开心\",\"imgUrl\":\"http://7xosdd.com2.z0.glb.qiniucdn.com/008.png\",\"title\":\"专治不开心\",\"url\":\"http://yuntv.letv.com/bcloud.html?uu\\u003dpyo48zevyh\\u0026vu\\u003dfc1d265088\\u0026auto_play\\u003d1\\u0026gpcflag\\u003d1\\u0026width\\u003d\\u003c#W#\\u003e\\u0026height\\u003d\\u003c#H#\\u003e\"},{\"detailed\":\"宝宝成长记录\",\"imgUrl\":\"http://7xosdd.com2.z0.glb.qiniucdn.com/004.png\",\"title\":\"宝宝成长记录\",\"url\":\"http://yuntv.letv.com/bcloud.html?uu\\u003dpyo48zevyh\\u0026vu\\u003db710b45b1d\\u0026auto_play\\u003d1\\u0026gpcflag\\u003d1\\u0026width\\u003d\\u003c#W#\\u003e\\u0026height\\u003d\\u003c#H#\\u003e\"},{\"detailed\":\"不是说好的就一口么\",\"imgUrl\":\"http://7xosdd.com2.z0.glb.qiniucdn.com/005.png\",\"title\":\"说好一口\",\"url\":\"http://yuntv.letv.com/bcloud.html?uu\\u003dpyo48zevyh\\u0026vu\\u003d8788903514\\u0026auto_play\\u003d1\\u0026gpcflag\\u003d1\\u0026width\\u003d\\u003c#W#\\u003e\\u0026height\\u003d\\u003c#H#\\u003e\"},{\"detailed\":\"留守儿童公益歌曲\",\"imgUrl\":\"http://7xosde.com2.z0.glb.qiniucdn.com/%E6%83%B3%E7%88%B8%E5%A6%88.jpg\",\"title\":\"想爸妈\",\"url\":\"http://yuntv.letv.com/bcloud.html?uu\\u003dpyo48zevyh\\u0026vu\\u003da18b5fab93\\u0026auto_play\\u003d1\\u0026gpcflag\\u003d1\\u0026width\\u003d\\u003c#W#\\u003e\\u0026height\\u003d\\u003c#H#\\u003e\"},{\"detailed\":\"陪伴孩子成长才是最该珍惜的时光\",\"imgUrl\":\"http://7xosde.com2.z0.glb.qiniucdn.com/%E9%99%AA%E4%BC%B4.jpg\",\"title\":\"陪伴\",\"url\":\"http://yuntv.letv.com/bcloud.html?uu\\u003dpyo48zevyh\\u0026vu\\u003dcd816f3209\\u0026auto_play\\u003d1\\u0026gpcflag\\u003d1\\u0026width\\u003d\\u003c#W#\\u003e\\u0026height\\u003d\\u003c#H#\\u003e\"},{\"detailed\":\"陪伴是长情的告白\",\"imgUrl\":\"http://7xosde.com2.z0.glb.qiniucdn.com/%E9%95%BF%E6%83%85%E7%9A%84%E5%91%8A%E7%99%BD.jpg\",\"title\":\"长情的告白\",\"url\":\"http://yuntv.letv.com/bcloud.html?uu\\u003dpyo48zevyh\\u0026vu\\u003d8163720c8b\\u0026auto_play\\u003d1\\u0026gpcflag\\u003d1\\u0026width\\u003d\\u003c#W#\\u003e\\u0026height\\u003d\\u003c#H#\\u003e\"},{\"detailed\":\"七岁男孩独自返乡\",\"imgUrl\":\"http://7xosde.com2.z0.glb.qiniucdn.com/%E7%8B%AC%E8%87%AA%E8%BF%94%E4%B9%A1.jpg\",\"title\":\"独自返乡\",\"url\":\"http://yuntv.letv.com/bcloud.html?uu\\u003dpyo48zevyh\\u0026vu\\u003d95965da4c6\\u0026auto_play\\u003d1\\u0026gpcflag\\u003d1\\u0026width\\u003d\\u003c#W#\\u003e\\u0026height\\u003d\\u003c#H#\\u003e\"},{\"detailed\":\"其实幸福就在身边\",\"imgUrl\":\"http://7xosde.com2.z0.glb.qiniucdn.com/%E5%B9%B8%E7%A6%8F%E5%9C%A8%E8%BA%AB%E8%BE%B9.jpg\",\"title\":\"幸福在身边\",\"url\":\"http://yuntv.letv.com/bcloud.html?uu\\u003dpyo48zevyh\\u0026vu\\u003dd4a1f51b35\\u0026auto_play\\u003d1\\u0026gpcflag\\u003d1\\u0026width\\u003d\\u003c#W#\\u003e\\u0026height\\u003d\\u003c#H#\\u003e\"},{\"detailed\":\"是金子总会发光\",\"imgUrl\":\"http://7xosde.com2.z0.glb.qiniucdn.com/%E6%98%AF%E9%87%91%E5%AD%90%E6%80%BB%E4%BC%9A%E5%8F%91%E5%85%89.jpg\",\"title\":\"是金子总会发光\",\"url\":\"http://yuntv.letv.com/bcloud.html?uu\\u003dpyo48zevyh\\u0026vu\\u003d8c552be954\\u0026auto_play\\u003d1\\u0026gpcflag\\u003d1\\u0026width\\u003d\\u003c#W#\\u003e\\u0026height\\u003d\\u003c#H#\\u003e\"},{\"detailed\":\"我只给你做了一顿饭而那个人把你养大\",\"imgUrl\":\"http://7xosde.com2.z0.glb.qiniucdn.com/%E4%BE%9B%E5%85%BB.jpg\",\"title\":\"供养\",\"url\":\"http://yuntv.letv.com/bcloud.html?uu\\u003dpyo48zevyh\\u0026vu\\u003d1952e3ab35\\u0026auto_play\\u003d1\\u0026gpcflag\\u003d1\\u0026width\\u003d\\u003c#W#\\u003e\\u0026height\\u003d\\u003c#H#\\u003e\"},{\"detailed\":\"在街上看到霸凌你会怎么做\",\"imgUrl\":\"http://7xosde.com2.z0.glb.qiniucdn.com/%E8%BA%AB%E5%A4%84%E5%9B%B0%E5%A2%83.jpg\",\"title\":\"身处困境\",\"url\":\"http://yuntv.letv.com/bcloud.html?uu\\u003dpyo48zevyh\\u0026vu\\u003d27d0e31433\\u0026auto_play\\u003d1\\u0026gpcflag\\u003d1\\u0026width\\u003d\\u003c#W#\\u003e\\u0026height\\u003d\\u003c#H#\\u003e\"},{\"detailed\":\"在圣诞老公公面前测谎\",\"imgUrl\":\"http://7xosde.com2.z0.glb.qiniucdn.com/%E6%B5%8B%E8%B0%8E.jpg\",\"title\":\"测谎\",\"url\":\"http://yuntv.letv.com/bcloud.html?uu\\u003dpyo48zevyh\\u0026vu\\u003dc25bb53e1c\\u0026auto_play\\u003d1\\u0026gpcflag\\u003d1\\u0026width\\u003d\\u003c#W#\\u003e\\u0026height\\u003d\\u003c#H#\\u003e\"}],\"msgType\":\"8\"}";
    private String item_cec_json = "{\"contentType\":\"1\",\"msgCount\":\"12\",\"msgList\":[{\"detailed\":\"教育人生\",\"imgUrl\":\"http://7xosdd.com2.z0.glb.qiniucdn.com/001.png\",\"title\":\"教育人生\",\"url\":\"http://yuntv.letv.com/bcloud.html?uu\\u003dpyo48zevyh\\u0026vu\\u003dd7962ca1df\\u0026auto_play\\u003d1\\u0026gpcflag\\u003d1\\u0026width\\u003d\\u003c#W#\\u003e\\u0026height\\u003d\\u003c#H#\\u003e\"},{\"detailed\":\"草本课堂 4\",\"imgUrl\":\"http://7xosdd.com2.z0.glb.qiniucdn.com/002.png\",\"title\":\"草本课堂 4\",\"url\":\"http://yuntv.letv.com/bcloud.html?uu\\u003dpyo48zevyh\\u0026vu\\u003d3b601d158e\\u0026auto_play\\u003d1\\u0026gpcflag\\u003d1\\u0026width\\u003d\\u003c#W#\\u003e\\u0026height\\u003d\\u003c#H#\\u003e\"},{\"detailed\":\"《中国课堂》节目以课堂实录的...\",\"imgUrl\":\"http://7xosde.com2.z0.glb.qiniucdn.com/%E4%B8%AD%E5%9B%BD%E8%AF%BE%E5%A0%82.jpg\",\"title\":\"中国课堂 1\",\"url\":\"http://yuntv.letv.com/bcloud.html?uu\\u003dpyo48zevyh\\u0026vu\\u003d44dcaf8c83\\u0026auto_play\\u003d1\\u0026gpcflag\\u003d1\\u0026width\\u003d\\u003c#W#\\u003e\\u0026height\\u003d\\u003c#H#\\u003e\"},{\"detailed\":\"《中国课堂》节目以课堂实录的...\",\"imgUrl\":\"http://7xosde.com2.z0.glb.qiniucdn.com/%E4%B8%AD%E5%9B%BD%E8%AF%BE%E5%A0%82.jpg\",\"title\":\"中国课堂 2\",\"url\":\"http://yuntv.letv.com/bcloud.html?uu\\u003dpyo48zevyh\\u0026vu\\u003dbd77f6b5e7\\u0026auto_play\\u003d1\\u0026gpcflag\\u003d1\\u0026width\\u003d\\u003c#W#\\u003e\\u0026height\\u003d\\u003c#H#\\u003e\"},{\"detailed\":\"《中国课堂》节目以课堂实录的...\",\"imgUrl\":\"http://7xosde.com2.z0.glb.qiniucdn.com/%E4%B8%AD%E5%9B%BD%E8%AF%BE%E5%A0%82.jpg\",\"title\":\"中国课堂 3\",\"url\":\"http://yuntv.letv.com/bcloud.html?uu\\u003dpyo48zevyh\\u0026vu\\u003da4f8e71a29\\u0026auto_play\\u003d1\\u0026gpcflag\\u003d1\\u0026width\\u003d\\u003c#W#\\u003e\\u0026height\\u003d\\u003c#H#\\u003e\"},{\"detailed\":\"《中国课堂》节目以课堂实录的...\",\"imgUrl\":\"http://7xosde.com2.z0.glb.qiniucdn.com/%E4%B8%AD%E5%9B%BD%E8%AF%BE%E5%A0%82.jpg\",\"title\":\"中国课堂 4\",\"url\":\"http://yuntv.letv.com/bcloud.html?uu\\u003dpyo48zevyh\\u0026vu\\u003dc067aff2df\\u0026auto_play\\u003d1\\u0026gpcflag\\u003d1\\u0026width\\u003d\\u003c#W#\\u003e\\u0026height\\u003d\\u003c#H#\\u003e\"},{\"detailed\":\"《中国课堂》节目以课堂实录的...\",\"imgUrl\":\"http://7xosde.com2.z0.glb.qiniucdn.com/%E4%B8%AD%E5%9B%BD%E8%AF%BE%E5%A0%82.jpg\",\"title\":\"中国课堂 5\",\"url\":\"http://yuntv.letv.com/bcloud.html?uu\\u003dpyo48zevyh\\u0026vu\\u003df2530abb79\\u0026auto_play\\u003d1\\u0026gpcflag\\u003d1\\u0026width\\u003d\\u003c#W#\\u003e\\u0026height\\u003d\\u003c#H#\\u003e\"},{\"detailed\":\"《中国课堂》节目以课堂实录的...\",\"imgUrl\":\"http://7xosde.com2.z0.glb.qiniucdn.com/%E4%B8%AD%E5%9B%BD%E8%AF%BE%E5%A0%82.jpg\",\"title\":\"中国课堂 6\",\"url\":\"http://yuntv.letv.com/bcloud.html?uu\\u003dpyo48zevyh\\u0026vu\\u003d057b34540b\\u0026auto_play\\u003d1\\u0026gpcflag\\u003d1\\u0026width\\u003d\\u003c#W#\\u003e\\u0026height\\u003d\\u003c#H#\\u003e\"},{\"detailed\":\"《中国课堂》节目以课堂实录的...\",\"imgUrl\":\"http://7xosde.com2.z0.glb.qiniucdn.com/%E4%B8%AD%E5%9B%BD%E8%AF%BE%E5%A0%82.jpg\",\"title\":\"中国课堂 7\",\"url\":\"http://yuntv.letv.com/bcloud.html?uu\\u003dpyo48zevyh\\u0026vu\\u003d6bb93bbfed\\u0026auto_play\\u003d1\\u0026gpcflag\\u003d1\\u0026width\\u003d\\u003c#W#\\u003e\\u0026height\\u003d\\u003c#H#\\u003e\"},{\"detailed\":\"《中国课堂》节目以课堂实录的...\",\"imgUrl\":\"http://7xosde.com2.z0.glb.qiniucdn.com/%E4%B8%AD%E5%9B%BD%E8%AF%BE%E5%A0%82.jpg\",\"title\":\"中国课堂 8\",\"url\":\"http://yuntv.letv.com/bcloud.html?uu\\u003dpyo48zevyh\\u0026vu\\u003d75ef51e83f\\u0026auto_play\\u003d1\\u0026gpcflag\\u003d1\\u0026width\\u003d\\u003c#W#\\u003e\\u0026height\\u003d\\u003c#H#\\u003e\"},{\"detailed\":\"《中国课堂》节目以课堂实录的...\",\"imgUrl\":\"http://7xosde.com2.z0.glb.qiniucdn.com/%E4%B8%AD%E5%9B%BD%E8%AF%BE%E5%A0%82.jpg\",\"title\":\"中国课堂 9\",\"url\":\"http://yuntv.letv.com/bcloud.html?uu\\u003dpyo48zevyh\\u0026vu\\u003d600a863b3c\\u0026auto_play\\u003d1\\u0026gpcflag\\u003d1\\u0026width\\u003d\\u003c#W#\\u003e\\u0026height\\u003d\\u003c#H#\\u003e\"},{\"detailed\":\"《中国课堂》节目以课堂实录的...\",\"imgUrl\":\"http://7xosde.com2.z0.glb.qiniucdn.com/%E4%B8%AD%E5%9B%BD%E8%AF%BE%E5%A0%82.jpg\",\"title\":\"中国课堂 10\",\"url\":\"http://yuntv.letv.com/bcloud.html?uu\\u003dpyo48zevyh\\u0026vu\\u003d08a22698ee\\u0026auto_play\\u003d1\\u0026gpcflag\\u003d1\\u0026width\\u003d\\u003c#W#\\u003e\\u0026height\\u003d\\u003c#H#\\u003e\"}],\"msgType\":\"10\"}";
    boolean isloadFoot = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_healthy);
        getBundle();
        initView();

//        doSearch(true);

    }


    private void getBundle() {
        fromItemType = getIntent().getStringExtra("itemType");
        titleDes = getIntent().getStringExtra("itemTitle");


        L.e("itemType" + fromItemType);

        L.e("itemTitle" + titleDes);


    }

    private void initView() {
        empty_root = findViewById(R.id.rl_empty_root);
        loadingDialog = new LoadingDialog(InfomationListActivity.this);
        final View title = findViewById(R.id.title);
        TextView midTitle = (TextView) title.findViewById(R.id.tv_middle);
        midTitle.setText(titleDes);
        ivLeft = (ImageView) findViewById(R.id.iv_left);
        ivLeft.setImageResource(R.drawable.title_goback_selector);
        ivLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();

            }
        });

        mListView = (ListView) findViewById(R.id.lv_healthy_content);
        listViewFooterHelper = new ListViewFooterHelper<InfomationListAdapter>(mListView);

        mPtrFrame = (PtrClassicFrameLayout) this.findViewById(R.id.rotate_header_list_view_frame);
//        mPtrFrame.setLastUpdateTimeRelateObject(this);
        final MaterialHeader header=new MaterialHeader(this);
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
            public void onRefreshBegin(PtrFrameLayout frame) {
                doSearch(true);
                listViewFooterHelper.showLoading();
            }

            @Override
            public boolean checkCanDoRefresh(PtrFrameLayout frame, View content, View header) {
                return PtrDefaultHandler.checkContentCanBePulledDown(frame, content, header);
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
        mPtrFrame.postDelayed(new Runnable() {
            @Override
            public void run() {
                mPtrFrame.autoRefresh();
            }
        },100);

        adapter = new InfomationListAdapter(InfomationListActivity.this);
        mListView.setAdapter(adapter);

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (!CMethod.isNet(InfomationListActivity.this)) {
                    T.s("请检查网络连接");
                    return;

                }


                final InfomationItemEntity entity = (InfomationItemEntity) adapter.getItem(position);

                if (fromItemType.equals("8") || fromItemType.equals("10")) {
                    if (CMethod.isWifi(InfomationListActivity.this)) {
                        if (entity != null) {
                            Intent i = new Intent(InfomationListActivity.this, VideoActivity.class);
//                            i.putExtra("info_url",entity.getUrl());
                            i.putExtra("info_msg_id", entity.getMsgID());
                            startActivity(i);
                        }
                    } else {
                        DialogUtils.WIFISelectedDialog(InfomationListActivity.this, new SimpleClickListener() {
                            @Override
                            public void ok() {
                                if (!CMethod.isNet(InfomationListActivity.this)) {
                                    T.s("请检查网络连接");
                                    return;

                                }

                                Intent i = new Intent(InfomationListActivity.this, VideoActivity.class);
//                                i.putExtra("info_url",entity.getUrl());
                                i.putExtra("info_msg_id", entity.getMsgID());
                                startActivity(i);
                            }

                            @Override
                            public void cancle() {

                            }
                        });


                    }


                } else if (CMethod.isNet(InfomationListActivity.this)) {

                    if (entity != null) {
                        Intent i = new Intent(InfomationListActivity.this, InfomationActivity.class);
                        i.putExtra("info_msg_id", entity.getMsgID());
                        startActivity(i);
                    }
                } else {
                    T.s("请检查网络连接");
                }
            }
        });

        mListView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                if (scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE && isRefreshFoot) {
                    if (listViewFooterHelper.isLoading()) {//滑动到底部了，并且底部显示正在加载
                        //没有数据了显示没有数据了，或者隐藏
                        listViewFooterHelper.showLoading();
                        if (isloadFoot) {
                            isloadFoot = false;
                            doSearch(false);
                        }
                    }
                }
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                visiableCount = visibleItemCount;

                // 判断是否滑动到底部
                if (firstVisibleItem + visibleItemCount == totalItemCount) {
                    isRefreshFoot = true;
                } else {
                    isRefreshFoot = false;
                }
            }
        });
    }


    //    资讯类型 1.健康、2.教育、3.旅游、4.安全、5.保险、6.通讯、7.公益
    private void doSearch(final boolean reload) {
        L.e("doSearch");
//        if (CMethod.isNet(InfomationListActivity.this)) {

//        独立出  视频和 CEC_TV  (item_type = 8  item_type = 10 );
        if (CMethod.isNet(InfomationListActivity.this)) {
            if (reload) {
                pageindex = 1;
            }
            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.put("type", fromItemType);
                jsonObject.put("pageIndex", pageindex);
                jsonObject.put("pageSize", pagesize);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            HttpUtils.PostDataToWeb(UrlAddressUrils.CODE_API, AppConstants.JAVA_INFOR_MATION_LIST_URL, jsonObject, new HttpClientListener() {
                @Override
                public void onSuccess(String result) {
                    isloadFoot = true;
                    Gson gson = new Gson();
                    GetInfomationListData fromJson = gson.fromJson(result, GetInfomationListData.class);
                    List<InfomationItemEntity> lists = fromJson.getMsgList();

                    if (lists.size() > 0) {
                        adapter.refresh(lists, reload);
                        pageindex++;
                        L.e(lists.size() + "");


                        if (lists.size() < pagesize) {//本次返回不够一页，说明没有数据了
                            listViewFooterHelper.showNoMoreData();
                        }
                        mPtrFrame.refreshComplete();
                    } else if (firstload) {


                        mPtrFrame.refreshComplete();
                        empty_root.setVisibility(View.VISIBLE);
                        mListView.setVisibility(View.INVISIBLE);
                        mPtrFrame.setVisibility(View.INVISIBLE);
                    } else {
                        mPtrFrame.refreshComplete();
                        listViewFooterHelper.showNoMoreData();
                    }

                    firstload = false;

                }

                @Override
                public void onFailure(String result) {
                    loadingDialog.dismiss();
                    mPtrFrame.refreshComplete();
                    listViewFooterHelper.showClickLoadMore(new ListViewFooterHelper.ClickListener() {
                        @Override
                        public void click(View view) {

                        }
                    });
                    empty_root.setVisibility(View.VISIBLE);
                    mListView.setVisibility(View.INVISIBLE);
                    mPtrFrame.setVisibility(View.INVISIBLE);
                }

                @Override
                public void onError() {
                    L.e("SendMessageCodeUtils_onError" + "error result ");
                    loadingDialog.dismiss();
                    mPtrFrame.refreshComplete();
                    listViewFooterHelper.showClickLoadMore(new ListViewFooterHelper.ClickListener() {
                        @Override
                        public void click(View view) {

                        }
                    });
                    empty_root.setVisibility(View.VISIBLE);
                    mListView.setVisibility(View.INVISIBLE);
                    mPtrFrame.setVisibility(View.INVISIBLE);
                }
            });

        } else {
            this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    loadingDialog.dismiss();
                    T.s("网络不给力");
                    mPtrFrame.refreshComplete();
//                    empty_root.setVisibility(View.VISIBLE);
                    if (fromItemType.equals("8") || fromItemType.equals("10")) {

                    } else {
                        mListView.setVisibility(View.INVISIBLE);
                        mPtrFrame.setVisibility(View.INVISIBLE);
                    }


                }
            });
        }
    }

}
