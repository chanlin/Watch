package com.jajale.watch.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.jajale.watch.R;
import com.jajale.watch.entity.BookListData;

import java.util.List;


public class ReaderDetailListAdapter extends BaseAdapter {
    private LayoutInflater mInflater;


    private Context mcontext;
    private ViewHolder holder;
    public List<BookListData> mBookList;


    public ReaderDetailListAdapter(Context context, List<BookListData> bookList) {
        this.mInflater = LayoutInflater.from(context);
        this.mBookList = bookList;
        this.mcontext = context;
    }

    public int getCount() {
        // TODO Auto-generated method stub
        return mBookList.size();
    }

    public Object getItem(int arg0) {
        // TODO Auto-generated method stub
        return mBookList.get(arg0);
    }

    @SuppressWarnings("deprecation")
    public View getView(int position, View convertView, ViewGroup parent) {

        View view = convertView;
        holder = null;
        if (view == null) {
            holder = new ViewHolder();
            view = mInflater.inflate(R.layout.listview_item_read_list, null);
            holder.number = (TextView) view.findViewById(R.id.number);
            holder.name = (TextView) view.findViewById(R.id.name);

            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }

        holder.name.setText(mBookList.get(position).getTitle());



        return view;
    }

    public long getItemId(int arg0) {
        // TODO Auto-generated method stub
        return 0;
    }

    public final class ViewHolder {
        public TextView number;
        public TextView name;

    }
}
