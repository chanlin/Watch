package com.jajale.watch.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jajale.watch.R;
import com.jajale.watch.entity.InfomationItemEntity;
import com.jajale.watch.image.JJLImageLoader;
import com.jajale.watch.utils.L;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by athena on 2016/2/18.
 * Email: lizhiqiang@bjjajale.com
 */
public class InfomationListAdapter extends BaseAdapter {

    private List<InfomationItemEntity> lists = new ArrayList<InfomationItemEntity>();
    private LayoutInflater mInflater;
    private Context mContext ;

    public InfomationListAdapter(Context context) {
        this.mContext = context ;
        this.mInflater = LayoutInflater.from(mContext);
    }

    @Override
    public int getCount() {
        return lists.size();
    }

    @Override
    public Object getItem(int position) {
        L.e(position+"<======>"+lists.size());

        return position<lists.size() ?lists.get(position):null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder holder = null;

        if (convertView == null) {
            holder = new ViewHolder();

            RelativeLayout target = (RelativeLayout) mInflater.inflate(R.layout.listview_item_infomation,null);
//            android.widget.AbsList
//            android.widget.RelativeLayout.LayoutParams params = new android.widget.RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,Dp2Px(100));
//            target.setLayoutParams(params);

            android.widget.AbsListView.LayoutParams params = new android.widget.AbsListView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,Dp2Px(100));
            target.setLayoutParams(params);
            convertView = target;

            holder.iv_ad = (ImageView) convertView.findViewById(R.id.iv_item_ad_pic);
            holder.tv_title  = (TextView) convertView.findViewById(R.id.tv_item_info_title);
            holder.tv_desc = (TextView) convertView.findViewById(R.id.tv_item_info_content);

            convertView.setTag(holder);

        }else {
            holder = (ViewHolder) convertView.getTag();
        }

        InfomationItemEntity entity = lists.get(position);
        JJLImageLoader.downloadInfomation(mContext, entity.getImgUrl(), holder.iv_ad);

        String title = entity.getTitle() ;
        if (title.length()>12){
            title = title.substring(0,11)+"...";
        }
        holder.tv_title.setText(title);

        String desc = entity.getDetailed() ;
        if (desc.length() > 18){
            desc = desc.substring(0,16)+"...";
        }
        holder.tv_desc.setText(desc);

        return convertView;
    }

    public void refresh(List<InfomationItemEntity> source , boolean reload) {
        if (reload){
            lists.clear();
        }
        lists.addAll(source);
        notifyDataSetChanged();
    }



    private class ViewHolder {
        ImageView iv_ad ;
        TextView tv_title ;
        TextView tv_desc ;


    }


    public int Dp2Px(float dp) {
        final float scale = mContext.getResources().getDisplayMetrics().density;
        return (int) (dp * scale + 0.5f);
    }


}
