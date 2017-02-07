package com.jajale.watch.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.jajale.watch.R;
import com.jajale.watch.entity.TagEnum;
import com.ximalaya.ting.android.opensdk.model.tag.Tag;

import java.util.List;


/**
 * Created by guokm on 2016/3/30.
 */
public class ChildTagsGvAdpter extends BaseAdapter {
    private List<Tag> tagList;
    private int position;
    private Context context;

    public ChildTagsGvAdpter(Context context) {
        this.context = context;
    }

    public void setTagData(List<Tag> tagList) {
        if (tagList != null) {
            this.tagList = tagList;
            notifyDataSetChanged();
        }
    }

    public void setTagItemSelect(int position) {
        this.position = position;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return tagList == null ? 0 : tagList.size();
    }

    @Override
    public Object getItem(int i) {
        return tagList == null ? null : tagList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(final int i, View view, ViewGroup viewGroup) {
        ViewHolder viewHolder = null;
        if (view == null) {
            viewHolder = new ViewHolder();
            view = LayoutInflater.from(context).inflate(R.layout.child_tags_item, null);
            viewHolder.tags_item_iv = (ImageView) view.findViewById(R.id.tags_item_iv);
            viewHolder.name_tv = (TextView) view.findViewById(R.id.tags_name_tv);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }


//        Uri uri = Uri.parse(tagList.get(i).getCoverUrlLarge());
//        Picasso.with(context).load(uri.toString())
//        .into(viewHolder.tags_item_iv);

//        JJLImageLoader.downloadRoundImage(context,uri.toString(),viewHolder.tags_item_iv);
//        viewHolder.tags_item_iv.setImageURI(uri);
        final String tagName = tagList.get(i).getTagName();
        if (tagName.equals(TagEnum.英文磨耳朵.toString())) {
            viewHolder.name_tv.setText(TagEnum.少儿英语.toString());
            viewHolder.tags_item_iv.setImageResource(R.mipmap.xmly_child_english);
        } else if (tagName.equals(TagEnum.国学启蒙.toString())) {
            viewHolder.name_tv.setText(TagEnum.知识启蒙.toString());
            viewHolder.tags_item_iv.setImageResource(R.mipmap.xmly_elementary);
        } else if (tagName.equals(TagEnum.亲子学堂.toString())) {
            viewHolder.name_tv.setText(TagEnum.亲子教育.toString());
            viewHolder.tags_item_iv.setImageResource(R.mipmap.xmly_parent_child);
        } else if (tagName.equals(TagEnum.儿歌大全.toString())) {
            viewHolder.name_tv.setText(tagName);
            viewHolder.tags_item_iv.setImageResource(R.mipmap.xmly_childsong);
        } else if (tagName.equals(TagEnum.宝贝SHOW.toString())) {
            viewHolder.name_tv.setText(tagName);
            viewHolder.tags_item_iv.setImageResource(R.mipmap.xmly_babyshow);
        } else if (tagName.equals(TagEnum.卡通动画片.toString())) {
            viewHolder.name_tv.setText(tagName);
            viewHolder.tags_item_iv.setImageResource(R.mipmap.xmly_cartoon);
        }

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tagClickListenr.OnTagClick(tagName);
                setTagItemSelect(i);
            }
        });
        if (i == position) {
            viewHolder.tags_item_iv.setSelected(true);
        } else {
            viewHolder.tags_item_iv.setSelected(false);
        }
        return view;
    }

    private static class ViewHolder {
        public ImageView tags_item_iv;
        public TextView name_tv;
    }

    /**
     * Tag点击
     */
    private OnTagClickListenr tagClickListenr;

    public void setOnTagClickListenr(OnTagClickListenr tagClickListenr) {
        this.tagClickListenr = tagClickListenr;
    }

    public interface OnTagClickListenr {
        void OnTagClick(String tagName);
    }
}
