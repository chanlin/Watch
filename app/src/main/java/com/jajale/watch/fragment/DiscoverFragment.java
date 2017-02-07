package com.jajale.watch.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.jajale.watch.AppConstants;
import com.jajale.watch.BaseApplication;
import com.jajale.watch.CodeConstants;
import com.jajale.watch.R;
import com.jajale.watch.activity.ChildHoodAlbumActivity;
import com.jajale.watch.activity.ChildQAActivity;
import com.jajale.watch.activity.EducationActivity;
import com.jajale.watch.activity.HealthyActivity;
import com.jajale.watch.activity.InfomationActivity;
import com.jajale.watch.activity.InfomationListActivity;
import com.jajale.watch.activity.InsuranceActivity;
import com.jajale.watch.activity.NewsPaperActivityV2;
import com.jajale.watch.activity.SeeTheWordActivity;
import com.jajale.watch.adapter.DiscoverGirdViewAdapter;
import com.jajale.watch.cviews.BabyChoosePopupWindow;
import com.jajale.watch.cviews.FixRequestDisallowTouchEventPtrFrameLayout;
import com.jajale.watch.cviews.Kanner;
import com.jajale.watch.cviews.MyGridView;
import com.jajale.watch.entity.ADListData;
import com.jajale.watch.entity.ADListEntity;
import com.jajale.watch.entity.DiscoverData;
import com.jajale.watch.entity.SPKeyConstants;
import com.jajale.watch.entity.SmartWatchListData;
import com.jajale.watch.entitydb.SmartWatch;
import com.jajale.watch.entityno.UMeventId;
import com.jajale.watch.helper.GetMessageHelper;
import com.jajale.watch.interfaces.CreateSuccessInterface;
import com.jajale.watch.listener.ADItemClickListener;
import com.jajale.watch.listener.GetMessageListener;
import com.jajale.watch.listener.HttpClientListener;
import com.jajale.watch.utils.CMethod;
import com.jajale.watch.utils.DisplayUtil;
import com.jajale.watch.utils.HttpUtils;
import com.jajale.watch.utils.L;
import com.jajale.watch.utils.LoadingDialog;
import com.jajale.watch.utils.NotificationUtils;
import com.jajale.watch.utils.PhoneSPUtils;
import com.jajale.watch.utils.SmartWatchUtils;
import com.jajale.watch.utils.T;
import com.jajale.watch.utils.UrlAddressUrils;
import com.umeng.analytics.MobclickAgent;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import in.srain.cube.views.ptr.PtrDefaultHandler;
import in.srain.cube.views.ptr.PtrFrameLayout;
import in.srain.cube.views.ptr.PtrHandler;
import in.srain.cube.views.ptr.header.MaterialHeader;

/**
 * Created by athena on 2016/2/17.
 * Email: lizhiqiang@bjjajale.com
 */
public class DiscoverFragment extends BaseFragment implements View.OnClickListener{
    public static final String TAG = "DiscoverFragment";
    private LoadingDialog loadingDialog;

    private CreateSuccessInterface createSuccessInterface;

    private Kanner kanner;

    private MyGridView gv_content;
    private List<SmartWatch> watches;
    private SmartWatch watch;
    private int present_position = 0;
    private int ever_position = -1;

    private ImageView iv_left;
    private BabyChoosePopupWindow menuWindow;

//    private String [] item_descs = {"健康","教育","旅游","安全","保险","通讯","公益","视频","大河报","CEC-TV"};
//    private int [] icon_res = {R.drawable.selector_discover_healthy_bg,R.drawable.selector_discover_education_bg,R.drawable.selector_discover_tourism_bg,R.drawable.selector_discover_safe_bg,R.drawable.selector_discover_insurance_bg,R.drawable.selector_discover_communication_bg,R.drawable.selector_discover_welfare_bg,R.drawable.selector_discover_video_bg,R.drawable.selector_discover_paper_bg,R.mipmap.icon_discover_item_cec_default};

    private String[] adList;
    private boolean getAdLock = false;


    //----------------single instance start-----------------//
    private static DiscoverFragment instance = null;
    private PhoneSPUtils phoneSPUtils;
    private DiscoverGirdViewAdapter adapter;
    private ImageView title_arrow_iv;
    private LinearLayout title_ll_middle;
    private TextView midTitle;
    private View view_title;
    private AlphaAnimation mShowAction;
    private View view_background;
    private BaseApplication baseApplication;
    private FixRequestDisallowTouchEventPtrFrameLayout mPtrFrame;
    boolean isChange = true;

    public DiscoverFragment() {
    }

    public static DiscoverFragment getInstance() {
        DiscoverFragment fragment = new DiscoverFragment();
        return fragment;
    }
    //----------------single instance end  -----------------//

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        L.e(TAG + "---onCreate");
        if (getArguments() != null) {
        }
    }


    @Override
    public void refreshWatchList(List<SmartWatch> watches) {
        super.refreshWatchList(watches);
        setBabyData(watches);
        getDiscoverListFromNetWork();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_discover, container, false);

        L.e(TAG + "---onCreateView");
        if (createSuccessInterface != null) {
            createSuccessInterface.createSuccess();
        }

        return rootView;
    }


    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        baseApplication = (BaseApplication) getActivity().getApplication();
        present_position = baseApplication.getPresent_position();
        loadingDialog = new LoadingDialog(getActivity());
        phoneSPUtils = new PhoneSPUtils(getActivity());
        setBabyData(null);
        initView(view);
        L.e(TAG + "---onViewCreated");
//        loadingDialog.show();
//        getDiscoverListFromNetWork();

    }

    private void changeHeadIcon(boolean needReload) {

        baseApplication.setPresent_position(present_position);
        if (needReload) {
            watches = SmartWatchUtils.getWatchList();
            BaseApplication.getHomeActivity().watchDataHasChanged = false;
        }
        midTitle.setText(getResources().getString(R.string.function_name_discover));

        if (watches.size() == 1) {
            midTitle.setText(getResources().getString(R.string.function_name_discover));
            title_arrow_iv.setVisibility(View.GONE);
        } else {
            if (present_position < watches.size())
                midTitle.setText(watches.get(present_position).getNick_name());
            title_arrow_iv.setVisibility(View.VISIBLE);
        }

        title_ll_middle.setOnClickListener(this);
    }

    @Override
    public void onResume() {
        super.onResume();

        if (present_position != baseApplication.getPresent_position()) {
            isChange = true;
        }

        L.e("111111111discover===isChange===" + isChange);
        present_position = baseApplication.getPresent_position();
        L.e("111111111discover===onResume===position=========" + present_position);
        setBabyData(watches);
        if (isChange) {
            isChange = false;
//            getDiscoverListFromNetWork();
            mPtrFrame.postDelayed(new Runnable() {
                @Override
                public void run() {
                    mPtrFrame.autoRefresh();
                }
            }, 100);
        }
        changeHeadIcon(BaseApplication.getHomeActivity().watchDataHasChanged);

        if (adList == null || adList.length == 0) {
            getADList();
        }
    }

    private void getADList() {

        if (getAdLock) {
            return;
        }
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("user_id", BaseApplication.getUserInfo().userID);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        HttpUtils.PostDataToWeb(UrlAddressUrils.CODE_API, AppConstants.JAVA_INFOR_MATION_IMG_URL, jsonObject, new HttpClientListener() {
                    @Override
                    public void onSuccess(String result) {
                        L.e(result);
                        Gson gson = new Gson();
                        final ADListData data = gson.fromJson(result, ADListData.class);

                        adList = new String[data.getAdList().size()];
                        for (int i = 0; i < adList.length; i++) {
                            adList[i] = data.getAdList().get(i).getImgUrl();
                        }
                        L.e("123===adList===" + adList.length);
                        if (adList.length > 0)

                            kanner.setImagesUrl(adList, new ADItemClickListener() {
                                @Override
                                public void OnSelected(int index) {
                                    L.e(index + "<===");
                                    if (CMethod.isNet(getActivity())) {
                                        ADListEntity entity = data.getAdList().get(index - 1);
                                        switch (entity.getItem_type()){

                                            case 1://文章
                                                Intent i = new Intent(getActivity(), InfomationActivity.class);
                                                i.putExtra("info_msg_id", entity.getMsgID());
                                                startActivity(i);
                                                break;
                                            case 2://大河报
                                                openNewsActivityV2("大河报", entity.getReq_url(), watch.getWatch_imei_binded());
                                                break;
                                            case 3://看世界
                                                startActivity(new Intent(getActivity(), SeeTheWordActivity.class));
                                                break;
                                            case 4://趣味问答
                                                startActivity(new Intent(getActivity(), ChildQAActivity.class));
                                                break;

                                        }
                                    } else {
                                        T.s("请检查网络连接");
                                    }


                                }
                            });
                        getAdLock = false;

                    }

                    @Override
                    public void onFailure(String result) {
                        T.s(result);
                        getAdLock = false;
                    }

                    @Override
                    public void onError() {
                        getAdLock = false;
                    }
                }

        );
    }


    private void getDiscoverListFromNetWork() {

        if (watch == null)
            return;

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("userID", BaseApplication.getUserInfo().userID);
            jsonObject.put("watchID", watch.getUser_id());
            jsonObject.put("imei", watch.getWatch_imei_binded());
            jsonObject.put("net", netWorkState());
            L.i("userID:"+BaseApplication.getUserInfo().userID+",watchID:"+watch.getUser_id()+",imei:"+watch.getWatch_imei_binded()+",net:"+netWorkState());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        HttpUtils.PostDataToWeb(AppConstants.JAVA_HAS_CODE_URL, CodeConstants.GET_DISCOVER_LIST, jsonObject, new HttpClientListener() {
            @Override
            public void onSuccess(String result) {
//                loadingDialog.dismiss();
                L.i("guokm",result);
                mPtrFrame.refreshComplete();
                Gson gson = new Gson();
                final DiscoverData data = gson.fromJson(result, DiscoverData.class);


                if (data == null || data.getModeList() == null || data.getModeList().size() == 0)
                    return;
                setView(data);
            }

            @Override
            public void onFailure(String result) {
//                loadingDialog.dismiss();
                mPtrFrame.refreshComplete();
                T.s(result);
            }

            @Override
            public void onError() {
//                loadingDialog.dismiss();
                mPtrFrame.refreshComplete();
            }
        });

    }


    private void initView(View view) {
//        view.getParent().requestDisallowInterceptTouchEvent(true);
        // Title 文字
        View title = view.findViewById(R.id.title);
        midTitle = (TextView) title.findViewById(R.id.tv_middle);

        view_title = view.findViewById(R.id.title);
        title_arrow_iv = (ImageView) title.findViewById(R.id.title_arrow_iv);
        title_ll_middle = (LinearLayout) title.findViewById(R.id.title_ll_middle);
        view_background = getActivity().findViewById(R.id.view_background);
        mPtrFrame = (FixRequestDisallowTouchEventPtrFrameLayout) view.findViewById(R.id.rotate_header_list_view_frame);
//        mPtrFrame.setLastUpdateTimeRelateObject(this);
        final MaterialHeader header = new MaterialHeader(getActivity());

        int[] colors = getResources().getIntArray(R.array.material_colors);
        header.setColorSchemeColors(colors);
        header.setLayoutParams(new PtrFrameLayout.LayoutParams(-1, -2));
        header.setPadding(0, DisplayUtil.designedDP2px(15), 0, DisplayUtil.designedDP2px(10));
        header.setPtrFrameLayout(mPtrFrame);
        mPtrFrame.setDurationToClose(100);
        mPtrFrame.setPinContent(true);
//        mPtrFrame.autoRefresh(true);
        mPtrFrame.setHeaderView(header);
        mPtrFrame.addPtrUIHandler(header);
        //自动刷新一次

        mPtrFrame.setPtrHandler(new PtrHandler() {
            @Override
            public boolean checkCanDoRefresh(PtrFrameLayout frame, View content, View header) {
                return PtrDefaultHandler.checkContentCanBePulledDown(frame, content, header);
            }

            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                GetMessageHelper helper = new GetMessageHelper(getActivity(), new GetMessageListener() {
                    @Override
                    public void onSuccess(String userinfoJson) {
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                mPtrFrame.postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        mPtrFrame.refreshComplete();
                                        getPullRefreshDate();
/*                                        setBabyData(null);
                                        getDiscoveListFromNetWork();*/

                                    }
                                }, 2000);
                            }
                        }).start();

                    }

                    @Override
                    public void onFailure() {
                        mPtrFrame.refreshComplete();
                        getPullRefreshDate();
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
        kanner = (Kanner) view.findViewById(R.id.kanner);
        kanner.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()){
                    case MotionEvent.ACTION_MOVE:
//                        mPtrFrame.setEnabled(false);
                        mPtrFrame.setPullToRefresh(false);
                        break;
                    case MotionEvent.ACTION_UP:
                    case MotionEvent.ACTION_CANCEL:

//                        mPtrFrame.setEnabled(true);
                        mPtrFrame.setPullToRefresh(true);

                        break;
                }
                return false;
            }
        });
        gv_content = (MyGridView) getView().findViewById(R.id.gv_discover_content);
        mShowAction = new AlphaAnimation(0.1f, 2.0f);
        mShowAction.setDuration(300);
        L.e("123==="+TAG + "===initView");
    }

    private void getPullRefreshDate() {

        L.e("111111111discover===isChange===" + isChange);
        present_position = baseApplication.getPresent_position();
        L.e("111111111discover===onResume===position=========" + present_position);
        setBabyData(watches);

        getDiscoverListFromNetWork();

        changeHeadIcon(BaseApplication.getHomeActivity().watchDataHasChanged);

        if (adList == null || adList.length == 0) {
            getADList();
        }
    }


    private void setView(final DiscoverData discoverData) {

        adapter = new DiscoverGirdViewAdapter(discoverData, getActivity());
        gv_content.setAdapter(adapter);
        gv_content.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (CMethod.isFastDoubleClick(800)) {
                    return;
                }
//                mode_type 模式类型  为0时 取 req_url 大于0时取具体值 1.健康、2.教育、3.旅游、4.安全、
//                                                                  5.保险、6.通讯、7.公益、8.视频、10.CEC-TV
//                req_url    访问url

                int mode_type = discoverData.getModeList().get(position).getMode_type();
                String req_url = discoverData.getModeList().get(position).getReq_url();
                String name = discoverData.getModeList().get(position).getMo_name();

                Intent i = new Intent();
                switch (mode_type) {
                    case 0://url链接
                        MobclickAgent.onEvent(getActivity(), UMeventId.UMENG_NEWSPAPER_CLICK_TIMES);
                        if (CMethod.isNet(getActivity())) {
                            //未报名
                            phoneSPUtils.save(SPKeyConstants.IS_SHOW_DHB_DOT, false);
                            NotificationUtils.cancelAll(getActivity());
                            openNewsActivityV2(name, req_url, watch.getWatch_imei_binded());

                        } else {
                            T.s("请检查网络连接");
                        }
                        break;

                    case 1://健康
                        MobclickAgent.onEvent(getActivity(), UMeventId.UMENG_HEALTHY_CLICK_TIMES);
                        if (CMethod.isNet(getActivity())) {

                            i.setClass(getActivity(), HealthyActivity.class);
                            Bundle mBundle_tel = new Bundle();
                            mBundle_tel.putParcelable(SmartWatchListData.KEY, watch);//传递宝贝信息
                            i.putExtras(mBundle_tel);
                            startActivity(i);

                        } else {
                            T.s("请检查网络连接");
                        }

                        break;

                    case 2://教育
                        MobclickAgent.onEvent(getActivity(), UMeventId.UMENG_EDUCATION_CLICK_TIMES);
                        if (CMethod.isNet(getActivity())) {

                            i.setClass(getActivity(), EducationActivity.class);
                            startActivity(i);
                        } else {
                            T.s("请检查网络连接");
                        }
                        break;

                    case 3://旅游
                        MobclickAgent.onEvent(getActivity(), UMeventId.UMENG_TOURISM_CLICK_TIMES);
                        if (CMethod.isNet(getActivity())) {
                            L.e("旅游");
                            openInfoListActivity(name, "3");
                        } else {
                            T.s("请检查网络连接");
                        }
                        break;

                    case 4://安全
                        MobclickAgent.onEvent(getActivity(), UMeventId.UMENG_SECURITY_CLICK_TIMES);
                        if (CMethod.isNet(getActivity())) {
                            openInfoListActivity(name, "4");
                        } else {
                            T.s("请检查网络连接");
                        }
                        break;
                    case 5://保险
//                        MobclickAgent.onEvent(getActivity(), UMeventId.UMENG_INSURANCE_CLICK_TIMES);
//                        if (CMethod.isNet(getActivity())) {
//
//                            if (watch.getIs_manage() == 0) {
////                                checkInsurance();
//                            } else {
//                                //非管理员点击弹窗
//                                openInsurance(true);
//
//                            }
//                        } else {
//                            T.s("请检查网络连接");
//                        }
//

                        break;
                    case 6: //通讯
                        MobclickAgent.onEvent(getActivity(), UMeventId.UMENG_COMMUNICATION_CLICK_TIMES);
                        if (CMethod.isNet(getActivity())) {
                            openInfoListActivity(name, "6");
                        } else {
                            T.s("请检查网络连接");
                        }
                        break;
                    case 7://公益
                        MobclickAgent.onEvent(getActivity(), UMeventId.UMENG_WELFARE_CLICK_TIMES);
                        if (CMethod.isNet(getActivity())) {
                            openInfoListActivity(name, "7");
                        } else {
                            T.s("请检查网络连接");
                        }
                        break;

                    case 8://视频
                        MobclickAgent.onEvent(getActivity(), UMeventId.UMENG_VIDEO_CLICK_TIMES);
                        if (CMethod.isNet(getActivity())) {
                            openInfoListActivity(name, "8");
                            break;
                        } else {
                            T.s("请检查网络连接");
                        }

                        break;

                    case 10://CEC-TV
                        MobclickAgent.onEvent(getActivity(), UMeventId.UMENG_CEC_TV_CLICK_TIMES);
                        if (CMethod.isNet(getActivity())) {
                            openInfoListActivity(name, "10");
                        } else {
                            T.s("请检查网络连接");
                        }
                        break;
                    case 9://童年相册
                        if (CMethod.isNet(getActivity())) {
                            openChildhoodAlbumActivity();
                        } else {
                            T.s("请检查网络连接");
                        }
                        break;
                    case 11://看世界
                        if (CMethod.isNet(getActivity())) {
                            Intent intent=new Intent(getActivity(), SeeTheWordActivity.class);
                            intent.putExtra("itemTitle",name);
                            startActivity(intent);
                        } else {
                            T.s("请检查网络连接");
                        }
                        break;
                    case 12://趣味问答
                        if (CMethod.isNet(getActivity())) {

                            Intent intent=new Intent(getActivity(), ChildQAActivity.class);
                            intent.putExtra("itemTitle",name);
                            startActivity(intent);

                        } else {
                            T.s("请检查网络连接");
                        }
                        break;

                }


            }
        });


    }


    private void openChildhoodAlbumActivity() {
        if (watch == null)
            return;
        Intent intent = new Intent();
        intent.setClass(getActivity(), ChildHoodAlbumActivity.class);
        intent.putExtra("watch_id", watch.getUser_id());
        startActivity(intent);
    }

    private void openInsurance(boolean result) {
        Intent intent = new Intent();
        intent.setClass(getActivity(), InsuranceActivity.class);
        intent.putExtra("watchID", watch.getUser_id());
        intent.putExtra("IMEI", watch.getWatch_imei_binded());
        intent.putExtra("changebtn", result);
        startActivity(intent);
    }


    private void openInfoListActivity(String itemTitle, String itemType) {

        Intent i = new Intent();
        i.setClass(getActivity(), InfomationListActivity.class);
        i.putExtra("itemType", itemType);
        i.putExtra("itemTitle", itemTitle);
        startActivity(i);

    }


    private void openNewsActivityV2(String title, String url, String imei) {
        Intent i = new Intent();
        i.setClass(getActivity(), NewsPaperActivityV2.class);
        i.putExtra("info_url", url);

        title = "保险".equals(title) ? "领取儿童意外、重疾综合保险" : title;
        i.putExtra("info_title", title);
        startActivity(i);
    }

    private int netWorkState() {
        if (CMethod.isNet(getActivity())) {
            String type = CMethod.getCurrentNetworkType(getActivity());
            if ("2G".equals(type)) {
                return 0;
            } else if ("无".equals(type) || "未知".equals(type)) {
                return -1;
            } else {
                return 1;
            }
        } else {
            return -1;
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        L.e(TAG + "---onDetach");
    }

    @Override
    public void onPause() {
        super.onPause();
        L.e(TAG + "---onPause");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        L.e(TAG + "---onDestroy");
    }

    public void setCreateSuccessInterface(CreateSuccessInterface successInterface) {
        this.createSuccessInterface = successInterface;
    }

    @Override
    public boolean fragmentOnActivityResult(int requestCode, int resultCode, Intent data) {
        return super.fragmentOnActivityResult(requestCode, resultCode, data);
    }

    private void setBabyData(List<SmartWatch> mWatches) {
        if (mWatches != null) {
            watches = mWatches;
        } else {
            watches = SmartWatchUtils.getWatchList();
        }
        if (watches != null && watches.size() != 0) {
            if (present_position < watches.size()) {
                watch = watches.get(present_position);
            } else {
                L.e("dearDetail_____present_position  错误");
            }
        }

    }


    private View.OnClickListener onClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.btn_cancel:
                    menuWindow.dismiss();
                    break;
            }
        }
    };
    private AdapterView.OnItemClickListener itemsOnClick = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            menuWindow.dismiss();
            if (position < watches.size()) {
//                loadingDialog.show();
/*                mPtrFrame.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                       mPtrFrame.autoRefresh();
                    }
                },100);*/
//                mPtrFrame.autoRefresh();
                present_position = position;
                setBabyData(watches);
                getDiscoverListFromNetWork();
                changeHeadIcon(false);


            }
        }
    };


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.title_ll_middle:
                if (watches.size() > 1) {
                    title_arrow_iv.setImageResource(R.mipmap.title_arrow_up);
                    view_background.startAnimation(mShowAction);
                    view_background.setVisibility(View.VISIBLE);
                    menuWindow = new BabyChoosePopupWindow(getActivity(), present_position, watches, itemsOnClick, title_arrow_iv, view_background);
                    menuWindow.showAsDropDown(view_title, 0, 0);
                }


                break;
        }
    }



}
