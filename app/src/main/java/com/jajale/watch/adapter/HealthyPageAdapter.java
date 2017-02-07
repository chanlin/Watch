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
import com.jajale.watch.listener.HealthyItemSelectedListener;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by lizhiqiang on 18/2/16.
 */
public class HealthyPageAdapter extends BaseAdapter implements View.OnClickListener {

    private LayoutInflater mInflater;
    private Context mContext ;
    private HealthyItemSelectedListener selectedListener ;
    public  List<InfomationItemEntity> lists = new ArrayList<InfomationItemEntity>();

    public HealthyPageAdapter(Context context , HealthyItemSelectedListener listener) {

        this.mContext = context ;
        this.mInflater = LayoutInflater.from(mContext);
        this.selectedListener = listener;



    }

    @Override
    public int getCount() {
        return lists.size()+2;
    }

    @Override
    public InfomationItemEntity getItem(int position) {
        return lists.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder holder = null;
        if (convertView == null) {

            holder = new ViewHolder();
            convertView = createViewByMessage(position);

            holder.grows_root = (RelativeLayout)convertView.findViewById(R.id.rl_item_grows_root);
            holder.vaccine_root = (RelativeLayout) convertView.findViewById(R.id.rl_item_vaccine_root);
            holder.expect_root = (RelativeLayout) convertView.findViewById(R.id.rl_item_expect_root);

            holder.iv_ad = (ImageView) convertView.findViewById(R.id.iv_item_ad_pic);
            holder.tv_title  = (TextView) convertView.findViewById(R.id.tv_item_info_title);
            holder.tv_desc = (TextView) convertView.findViewById(R.id.tv_item_info_content);

            convertView.setTag(holder);


        }else {
            holder = (ViewHolder) convertView.getTag();
        }



        if (position == 0 ){
            holder.grows_root.setOnClickListener(this);
            holder.expect_root.setOnClickListener(this);
            holder.vaccine_root.setOnClickListener(this);

        }else if(position == 1){


        }else{
            InfomationItemEntity entity = lists.get(position-2);
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

        }

        return convertView;
    }



    private View createViewByMessage(final int position){

        int resId = 0 ;
        int targetDp = 100 ;

        switch (position){


            case 0:
                resId = R.layout.item_healthy_row_mode ;
                targetDp = 145 ;
                break;

            case 1:
                resId = R.layout.listview_item_separate;
                targetDp = 45 ;
                break;

            default:
                resId = R.layout.listview_item_infomation;
                break;

        }
        RelativeLayout target = (RelativeLayout) mInflater.inflate(resId,null);
//        android.widget.RelativeLayout.LayoutParams params = new android.widget.RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,Dp2Px(targetDp));
//        target.setLayoutParams(params);
        android.widget.AbsListView.LayoutParams params = new android.widget.AbsListView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,Dp2Px(targetDp));
        target.setLayoutParams(params);
//        if (position >1 ){
//            target.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    selectedListener.onInfoItemSelected(lists.get(position-2));
//                }
//            });
//
//        }

        return target ;



    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.rl_item_grows_root:
                if (selectedListener != null){
                    selectedListener.onGrowsSelected();
                }
                break;

            case R.id.rl_item_vaccine_root:
                if (selectedListener != null){
                    selectedListener.onVaccineSelecterd();
                }
                break;

            case R.id.rl_item_expect_root:
                if (selectedListener != null){
                    selectedListener.onExpectSelected();
                }
                break;


        }
    }


    @Override
    public int getItemViewType(int position) {
//        if (conversation.getAllMessage().size()>0){
//            Message message = conversation.getMessage(position);
//
//            switch (message.getContent_type()){
//                case MessageContentType.TEXT:
//                    return message.getDrict() ? 0 : 1;
//                case MessageContentType.VOICE:
//                    return message.getDrict() ? 2 : 3;
//            }
//        }
        if (position == 0){
            return 0 ;
        }else if(position == 1){
            return 1 ;
        }else {
            return 2 ;
        }
    }

    @Override
    public int getViewTypeCount() {
        return 3;
    }

    private class ViewHolder {
        RelativeLayout grows_root ;
        RelativeLayout vaccine_root ;
        RelativeLayout expect_root ;

        ImageView iv_ad ;
        TextView tv_title ;
        TextView tv_desc ;



    }

    public int Dp2Px(float dp) {
        final float scale = mContext.getResources().getDisplayMetrics().density;
        return (int) (dp * scale + 0.5f);
    }

    public void refresh(List<InfomationItemEntity> source , boolean reload) {
        if (reload){
            lists.clear();
        }
        lists.addAll(source);
        notifyDataSetChanged();
    }


}
