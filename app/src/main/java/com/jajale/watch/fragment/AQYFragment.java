package com.jajale.watch.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import com.jajale.watch.R;
import com.jajale.watch.activity.AQYPlayActivity;
import com.jajale.watch.adapter.AQYListViewAdapter;
import com.jajale.watch.entity.AQYAlbum;
import com.jajale.watch.factory.GsonFactory;
import com.jajale.watch.utils.CMethod;
import com.jajale.watch.utils.L;
import com.jajale.watch.utils.LoadingDialog;
import com.jajale.watch.utils.T;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.apache.http.Header;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by llh on 2016/5/23.
 */
public class AQYFragment extends Fragment implements AdapterView.OnItemClickListener {
    Activity activity;
    AQYListViewAdapter adapter;
    List<AQYAlbum.Album> albumList;
LoadingDialog loadingDialog;

    GridView gridView;

    private List<String> tvidsLists = new ArrayList<String>();


    public static AQYFragment newInstance() {


        AQYFragment fragment = new AQYFragment();
        return fragment;
    }

    @Override
    public void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }


    @Override
    public View onCreateView(LayoutInflater inflater,  ViewGroup container,  Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_aiqiyi, null);
        if(loadingDialog==null){
            loadingDialog=new LoadingDialog(getActivity());
        }
        adapter = new AQYListViewAdapter(getContext());
        gridView = (GridView) view.findViewById(R.id.ly_image_list_grid);
        gridView.setAdapter(adapter);
        gridView.setOnItemClickListener(this);
        getChildCategory();
        return view;
    }

    private void getChildCategory() {
        loadingDialog.show();
        String requestUrl = "http://expand.video.iqiyi.com/api/album/list.json?apiKey=a813b5c475d74730b517748d713bab4d&categoryId=15";



        AsyncHttpClient client = new AsyncHttpClient();
        client.setTimeout(5000);
//        client.addHeader(HTTP.CONTENT_TYPE,
//                "application/x-www-form-urlencoded;charset=UTF-8");
        client.get(requestUrl, null, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers,
                                  byte[] responseBody) {
                // TODO Auto-generated method stub
                loadingDialog.dismiss();
                L.e("http_client____all_result----->" + new String(responseBody));
                try {
                    String result = new String(responseBody);
                    if (result != null) {
                        AQYAlbum aqyAlbum = GsonFactory.newInstance().fromJson(result, AQYAlbum.class);
                        albumList = aqyAlbum.data;
                        if (albumList != null && albumList.size() != 0) {
                            adapter.setData(albumList);
                            adapter.notifyDataSetChanged();
                        }
                    }

                } catch (Exception e) {
                    T.s("访问网络失败，请稍后重试！");
                    loadingDialog.dismiss();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers,
                                  byte[] responseBody, Throwable error) {
                loadingDialog.dismiss();
                // TODO Auto-generated method stub
            }
        });

    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if(!CMethod.isNetWorkEnable(getContext())){
            T.s(getContext().getString(R.string.check_network_connection));
            return;
        }
        if (CMethod.isFastDoubleClick()) {
            return;
        }
//        view.setSelected(true);
        Intent intent = new Intent(getActivity(), AQYPlayActivity.class);
        intent.putExtra("url", albumList.get(position).html5PlayUrl);
        getActivity().startActivity(intent);
    }
}
