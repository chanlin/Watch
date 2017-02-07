package com.jajale.watch.cviews;

import android.os.Parcel;
import android.text.style.URLSpan;
import android.view.View;

import com.jajale.watch.listener.ClauseSelectListener;

/**
 * Created by athena on 2015/12/10.
 * Email: lizhiqiang@bjjajale.com
 */
public class ClauseURLSpan extends URLSpan {

    private ClauseSelectListener listener ;

    public ClauseURLSpan(String url , ClauseSelectListener listener ) {
        super(url);
        this.listener = listener;
    }

    public ClauseURLSpan(Parcel src) {
        super(src);
    }
    @Override
    public void onClick(View widget) {
//        super.onClick(widget);
        listener.onCheckedLink(this.getURL());
    }


}
