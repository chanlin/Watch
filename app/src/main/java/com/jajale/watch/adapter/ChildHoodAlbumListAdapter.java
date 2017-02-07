package com.jajale.watch.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.jajale.watch.R;
import com.jajale.watch.entity.AlbumData;
import com.jajale.watch.utils.L;
import com.squareup.picasso.Picasso;

import java.util.List;


public class ChildHoodAlbumListAdapter extends BaseAdapter {
    private LayoutInflater mInflater;


    private Context mcontext;
    private ViewHolder holder;
    public List<AlbumData.ImgListEntity> mList;
    private int width;
    private int height;


    public ChildHoodAlbumListAdapter(Context context, List<AlbumData.ImgListEntity> lcs) {
        this.mInflater = LayoutInflater.from(context);
        this.mcontext = context;
        this.mList = lcs;

        WindowManager wm = (WindowManager) mcontext
                .getSystemService(Context.WINDOW_SERVICE);

        this.width = wm.getDefaultDisplay().getWidth()-50;
        this.height = width*2/3;
    }

    public int getCount() {
        // TODO Auto-generated method stub
        return mList.size();
    }

    public AlbumData.ImgListEntity getItem(int arg0) {
        // TODO Auto-generated method stub
        return mList.get(arg0);
    }

    @SuppressWarnings("deprecation")
    public View getView(int position, View convertView, ViewGroup parent) {

        View view = convertView;
        holder = null;
        if (view == null) {
            holder = new ViewHolder();
            view = mInflater.inflate(R.layout.listview_item_album_list, null);
            holder.album_tv_time = (TextView) view.findViewById(R.id.album_tv_time);
            holder.album_tv_name = (TextView) view.findViewById(R.id.album_tv_name);
            holder.album_iv_pic = (ImageView) view.findViewById(R.id.album_iv_pic);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }
        AlbumData.ImgListEntity data = mList.get(position);

        holder.album_tv_time.setText(data.getCreateTime());
        holder.album_tv_name.setText(data.getNicName());

//        JJLImageLoader.downloadBigAll(mcontext, data.getImgUrl(), holder.album_iv_pic);
        L.e("123=======data.getImgUrl()=="+data.getImgUrl());
        Picasso.with(mcontext)
                .load(data.getImgUrl())
                .resize(width,height)
                .centerCrop()
                .into(holder.album_iv_pic);

        return view;
    }

    public long getItemId(int arg0) {
        // TODO Auto-generated method stub
        return 0;
    }

    public final class ViewHolder {
        public TextView album_tv_time;
        public TextView album_tv_name;
        public ImageView album_iv_pic;

    }
}
