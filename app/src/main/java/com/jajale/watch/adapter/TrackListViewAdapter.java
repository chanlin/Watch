package com.jajale.watch.adapter;

import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.jajale.watch.R;
import com.jajale.watch.image.JJLImageLoader;
import com.jajale.watch.utils.CMethod;
import com.jajale.watch.utils.L;
import com.jajale.watch.utils.MyTimerUtil;
import com.ximalaya.ting.android.opensdk.model.track.Track;

import java.util.List;


public class TrackListViewAdapter extends BaseAdapter {
    private List<Track> trackList;
    private Context context;
    private int playPosition = 0;
    private String aobumurl;


    public TrackListViewAdapter(Context context, String aobumurl) {
        this.context = context;
        this.aobumurl=aobumurl;
    }

    public void setData(List<Track> trackList) {
        if (this.trackList != null) {
            this.trackList.addAll(trackList);
        } else {
            this.trackList = trackList;
        }
    }

    @Override
    public int getCount() {
        return trackList != null ? trackList.size() : 0;
    }

    @Override
    public Object getItem(int position) {
        return trackList != null ? trackList.get(position) : null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.track_list_item, parent,false);
            viewHolder.trackTitle = (TextView) convertView.findViewById(R.id.track_title_tv);
            viewHolder.alltime_tv = (TextView) convertView.findViewById(R.id.alltime_tv);
            viewHolder.track_info_tv = (TextView) convertView.findViewById(R.id.track_info_tv);
            viewHolder.play_times_tv = (TextView) convertView.findViewById(R.id.play_times_tv);
            viewHolder.image_view = (ImageView) convertView.findViewById(R.id.image_view);
//            viewHolder.devide_line = convertView.findViewById(R.id.devide_line);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

       /* if (position == 0) {
            viewHolder.devide_line.setVisibility(View.GONE);
        }*/
//设置专辑的标题

        String titleStr = trackList.get(position).getTrackTitle();
//        if (titleStr.length() > 12) {
//            titleStr = titleStr.substring(0, 12) + "...";
//        }
        viewHolder.trackTitle.setText(titleStr);
//设置播放次数

        int playCountNums = trackList.get(position).getPlayCount();
        viewHolder.play_times_tv.setText(playCountNums + "次");
//设置播放时长

        String alltime = MyTimerUtil.formatTime(1000 * trackList.get(position).getDuration());
        L.i("guokm","播放时长："+trackList.get(position).getDuration());
        viewHolder.alltime_tv.setText(alltime);
//设置专辑的介绍信息

        String trackInfo = trackList.get(position).getTrackIntro();
        viewHolder.track_info_tv.setText(trackInfo);
//设置专辑图片

        Uri uri = Uri.parse(trackList.get(position).getCoverUrlSmall());
        if(CMethod.isEmpty(uri.toString())){
            JJLImageLoader.downloadRoundImage(context,aobumurl,viewHolder.image_view);
        }else{

            JJLImageLoader.downloadRoundImage(context,uri.toString(),viewHolder.image_view);
        }
//        viewHolder.image_view.setImageURI(uri);

        if (playPosition == position) {
//            convertView.setBackgroundResource(R.drawable.kan_select_bg);
            convertView.setBackgroundColor(context.getResources().getColor(R.color.gray_bg));
        } else {
            convertView.setBackgroundColor(Color.argb(0, 0, 0, 0));
        }
        return convertView;
    }

    public void setViewSelect(int position) {
        playPosition = position;
        notifyDataSetChanged();
    }


    static class ViewHolder {
        public TextView trackTitle;
        //        public TextView like_times_tv;
        public TextView alltime_tv;
        public TextView play_times_tv;
        public TextView track_info_tv;
        public ImageView image_view;

//        public View devide_line;
    }
}
