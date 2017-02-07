package com.jajale.watch.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.jajale.watch.R;
import com.jajale.watch.utils.CacheUtils;

import java.io.File;


public class PDfListAdapter extends BaseAdapter {
    public static String strings[];
    private LayoutInflater mInflater;

    private Context mcontext;
    private ViewHolder holder;
    private String  book_id;


    public PDfListAdapter(Context context,String strings[],String book_id) {
        this.mInflater = LayoutInflater.from(context);
        this.strings = strings;
        this.book_id = book_id;
        this.mcontext = context;
    }

    public int getCount() {
        // TODO Auto-generated method stub
        return strings.length;
    }

    public Object getItem(int arg0) {
        // TODO Auto-generated method stub
        return arg0;
    }

    @SuppressWarnings("deprecation")
    public View getView(final int position, View convertView, ViewGroup parent) {

        View view = convertView;
        holder = null;
        if (view == null) {
            holder = new ViewHolder();
            view = mInflater.inflate(R.layout.pdf_list_item, null);
            holder.text_name = (TextView) view.findViewById(R.id.text_name);
            holder.text_state = (TextView) view.findViewById(R.id.text_state);


            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }

        holder.text_name.setText(strings[position]);

        if (isExistsFile(book_id+"—"+position))
            holder.text_state.setText("已下载");
        else
            holder.text_state.setText("未下载");



        return view;
    }

    public long getItemId(int arg0) {
        // TODO Auto-generated method stub
        return 0;
    }

    public final class ViewHolder {
        public TextView text_name;
        public TextView text_state;
    }

    private boolean isExistsFile(String bookId){
        final String netfilePath = getNetfilePathPdf(bookId);
        File file = new File(netfilePath);
        return file.exists() && file.length() > 0;
    }

    public String getNetfilePathPdf(String bookId) {
        String cacheDir = CacheUtils.getExternalBookCacheDir(mcontext.getApplicationContext());
        String suffixes = ".pdf";
        return cacheDir + bookId + suffixes;
    }

}
