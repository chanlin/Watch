package com.jajale.watch.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.jajale.watch.R;
import com.jajale.watch.entity.AQYAlbum;
import com.jajale.watch.image.JJLImageLoader;

import java.util.List;

/**
 * Created by Sunzhigang on 2016/3/11.
 */
public class AQYListViewAdapter extends BaseAdapter {

    private List<AQYAlbum.Album> albumList;
    private Context context;

    public void setData(List<AQYAlbum.Album> albumList) {
        this.albumList = albumList;
        notifyDataSetChanged();
    }

    public AQYListViewAdapter(Context context) {
        this.context = context;
    }

    @Override
    public int getCount() {
        return albumList != null ? albumList.size() : 0;
    }

    @Override
    public Object getItem(int position) {
        return albumList != null ? albumList.get(position) : null;
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
            convertView = LayoutInflater.from(context).inflate(R.layout.aqi_gridview_item, null);
            holder.album_iv = (ImageView) convertView.findViewById(R.id.image_view);
            holder.album_title_tv = (TextView) convertView.findViewById(R.id.gv_title_tv);
            holder.album_describe_tv = (TextView) convertView.findViewById(R.id.set_tv);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        String imageurl = albumList.get(position).posterPicUrl;
        if (imageurl != null) {
//            JJLImageLoader.downloadNormal(context, imageurl,  holder.album_iv);
            JJLImageLoader.downloadRoundImageResize(context, imageurl, holder.album_iv,154,120);
//            JJLImageLoader.downloadRoundImageResizePlace(context, imageurl,R.mipmap.book_img_place, holder.album_iv,154,120);

        }

        holder.album_title_tv.setText(albumList.get(position).albumName);
        holder.album_describe_tv.setText("第"+albumList.get(position).tvYear+"期");
//        convertView.setSelected(false);
        return convertView;
    }

    static class ViewHolder {
        ImageView album_iv;
        TextView album_title_tv;
        TextView album_describe_tv;
    }
}
