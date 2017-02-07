package com.jajale.watch.entity;

/**
 * Created by lilonghui on 2016/1/5.
 * Email:lilonghui@bjjajale.com
 */
public class BookData {


    /**
     * book_author : dsfd
     * book_brief : dgd
     * book_id : 1
     * book_name : sf
     * book_url : http://www.baidu.com
     * cover_img_url : sf
     * create_time : 2016-03-02 00:00:00.0
     * end : 0
     * limit : 10
     * offset : 0
     * start : 0
     */

    private String book_author;
    private String book_brief;
    private int book_id;
    private String book_name;
    private String book_url;
    private String cover_img_url;
    private String create_time;
    private int end;
    private int limit;
    private int offset;
    private int start;

    public void setBook_author(String book_author) {
        this.book_author = book_author;
    }

    public void setBook_brief(String book_brief) {
        this.book_brief = book_brief;
    }

    public void setBook_id(int book_id) {
        this.book_id = book_id;
    }

    public void setBook_name(String book_name) {
        this.book_name = book_name;
    }

    public void setBook_url(String book_url) {
        this.book_url = book_url;
    }

    public void setCover_img_url(String cover_img_url) {
        this.cover_img_url = cover_img_url;
    }

    public void setCreate_time(String create_time) {
        this.create_time = create_time;
    }

    public void setEnd(int end) {
        this.end = end;
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }

    public void setOffset(int offset) {
        this.offset = offset;
    }

    public void setStart(int start) {
        this.start = start;
    }

    public String getBook_author() {
        return book_author;
    }

    public String getBook_brief() {
        return book_brief;
    }

    public int getBook_id() {
        return book_id;
    }

    public String getBook_name() {
        return book_name;
    }

    public String getBook_url() {
        return book_url;
    }

    public String getCover_img_url() {
        return cover_img_url;
    }

    public String getCreate_time() {
        return create_time;
    }

    public int getEnd() {
        return end;
    }

    public int getLimit() {
        return limit;
    }

    public int getOffset() {
        return offset;
    }

    public int getStart() {
        return start;
    }
}
