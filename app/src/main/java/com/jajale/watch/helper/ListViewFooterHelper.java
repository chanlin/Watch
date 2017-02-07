package com.jajale.watch.helper;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.Adapter;
import android.widget.HeaderViewListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.jajale.watch.R;

/**
 * Created by lizhiqiang on 18/2/16.
 */
public class ListViewFooterHelper<T> {

    private ListView mListView;
    private View footer_progressbar;
    private TextView footer_hint_text;


    public ListViewFooterHelper(ListView listView) {
        if (listView == null) {
            throw new NullPointerException("listview 不能是 NULL");
        }
        if (listView.getAdapter() != null) {
            throw new RuntimeException("必须在setAdapter()之前实例化");
        }
        this.mListView = listView;
        this.addFooter();
    }

    private void addFooter() {
        View bottomV = LayoutInflater.from(mListView.getContext()).inflate(R.layout.pull_refresh_footer, null);
        if (mListView != null && mListView.getFooterViewsCount() == 0) {
            footer_progressbar = bottomV.findViewById(R.id.footer_progressbar);
            footer_hint_text = (TextView) bottomV.findViewById(R.id.footer_hint_text);
            mListView.addFooterView(bottomV);
        }
    }

    /**
     * 显示点击加载更多
     */
    public void showClickLoadMore(final ClickListener clickListener) {
        footer_progressbar.setVisibility(View.INVISIBLE);
        footer_hint_text.setVisibility(View.VISIBLE);
        footer_hint_text.setText(mListView.getContext().getString(R.string.footer_hint_load_normal));
        footer_hint_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (clickListener != null) {
                    clickListener.click(v);
                }
            }
        });
    }

    /**
     * 显示正在加载中
     */
    public void showLoading() {
        footer_progressbar.setVisibility(View.VISIBLE);
        footer_hint_text.setVisibility(View.INVISIBLE);
        footer_hint_text.setOnClickListener(null);
    }

    public void cancelLoading() {
        footer_progressbar.setVisibility(View.INVISIBLE);
        footer_hint_text.setVisibility(View.INVISIBLE);
        footer_hint_text.setOnClickListener(null);
    }

    /**
     * 是否显示正在加载中
     * @return
     */
    public boolean isLoading() {

        return footer_progressbar.getVisibility() == View.VISIBLE;
    }

    /**
     * 显示没有数据了
     */
    public void showNoMoreData() {
        footer_progressbar.setVisibility(View.INVISIBLE);
        footer_hint_text.setVisibility(View.VISIBLE);
        footer_hint_text.setText(mListView.getContext().getString(R.string.footer_hint_load_no_more));
        footer_hint_text.setOnClickListener(null);
    }

    public T getAdapter() {
        T t = null;
        if (mListView != null && mListView.getAdapter() != null) {
            Adapter adapter = mListView.getAdapter();
            if (HeaderViewListAdapter.class.isInstance(adapter)) {
                t = (T) ((HeaderViewListAdapter) adapter).getWrappedAdapter();
            } else {
                t = (T) adapter;
            }
        }
        return t;
    }

    public interface ClickListener {
        void click(View view);
    }

}
