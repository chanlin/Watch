package com.jajale.watch.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.jajale.watch.R;
import com.jajale.watch.entity.BookData;
import com.jajale.watch.image.JJLImageLoader;

import java.util.List;


public class EducationBookListAdapter extends BaseAdapter {
    private LayoutInflater mInflater;


    private Context mcontext;
    private ViewHolder holder;
    private List<BookData> mList;


    public EducationBookListAdapter(Context context, List<BookData> lcs) {
        this.mInflater = LayoutInflater.from(context);
        this.mcontext = context;
        this.mList = lcs;
    }

    public int getCount() {
        // TODO Auto-generated method stub
        return mList.size();
    }

    public BookData getItem(int arg0) {
        // TODO Auto-generated method stub
        return mList.get(arg0);
    }

    @SuppressWarnings("deprecation")
    public View getView(int position, View convertView, ViewGroup parent) {

        View view = convertView;
        holder = null;
        if (view == null) {
            holder = new ViewHolder();
            view = mInflater.inflate(R.layout.listview_item_book_list, null);
            holder.book_tv_name = (TextView) view.findViewById(R.id.book_tv_name);
            holder.book_tv_author = (TextView) view.findViewById(R.id.book_tv_author);
            holder.book_tv_brief = (TextView) view.findViewById(R.id.book_tv_brief);
            holder.book_iv = (ImageView) view.findViewById(R.id.book_iv);

            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }
        BookData bookData = mList.get(position);

        holder.book_tv_name.setText(bookData.getBook_name());
        holder.book_tv_author.setText(bookData.getBook_author());
        holder.book_tv_brief.setText(bookData.getBook_brief());

        JJLImageLoader.downloadBig(mcontext, bookData.getCover_img_url(), holder.book_iv );
        JJLImageLoader.downloadBigPlace(mcontext, bookData.getCover_img_url(),R.mipmap.book_img_place, holder.book_iv );



        return view;
    }

    public long getItemId(int arg0) {
        // TODO Auto-generated method stub
        return 0;
    }

    public final class ViewHolder {
        public TextView book_tv_name;
        public TextView book_tv_author;
        public TextView book_tv_brief;
        public ImageView book_iv;

    }
}
