package com.jajale.watch.entity;

import java.io.Serializable;

/**
 * Created by lilonghui on 2016/1/5.
 * Email:lilonghui@bjjajale.com
 */
public class BookListData implements Serializable {


    /**
     * offset : 2867
     * title : 学而第一
     */

    private int offset;
    private int offset_b;

    public int getOffset_b() {
        return offset_b;
    }

    public void setOffset_b(int offset_b) {
        this.offset_b = offset_b;
    }

    private String title;

    public void setOffset(int offset) {
        this.offset = offset;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getOffset() {
        return offset;
    }

    public String getTitle() {
        return title;
    }


}
