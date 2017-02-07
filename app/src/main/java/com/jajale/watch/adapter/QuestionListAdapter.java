package com.jajale.watch.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.jajale.watch.R;
import com.jajale.watch.entity.QuestionData;

import java.util.List;


public class QuestionListAdapter extends BaseAdapter {
    private LayoutInflater mInflater;


    private Context mcontext;
    private ViewHolder holder;


    public List<QuestionData.ProblemListEntity> list;

    public QuestionListAdapter(Context context, List<QuestionData.ProblemListEntity> list) {
        this.mInflater = LayoutInflater.from(context);
        this.list = list;
        this.mcontext = context;
    }

    public int getCount() {
        // TODO Auto-generated method stub
        return list.size();
    }

    public Object getItem(int arg0) {
        // TODO Auto-generated method stub
        return list.get(arg0);
    }

    @SuppressWarnings("deprecation")
    public View getView(final int position, View convertView, ViewGroup parent) {

        View view = convertView;
        holder = null;
        if (view == null) {
            holder = new ViewHolder();
            view = mInflater.inflate(R.layout.listview_item_question, null);
            holder.question_tv_title = (TextView) view.findViewById(R.id.question_tv_title);

            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }
        holder.question_tv_title.setText(list.get(position).getTitle());

        return view;
    }

    public long getItemId(int arg0) {
        // TODO Auto-generated method stub
        return 0;
    }

    public final class ViewHolder {
        public TextView question_tv_title;


    }
}
