package com.jajale.watch.entity;

/**
 * Created by llh on 16-4-29.
 */
public class DHBUserData {

    /**
     * user_id : 158
     * user_name : 王斐
     * level : 见习小记者
     * score : 10
     * activity_count : 10
     */

    private String user_id;
    private String user_name;
    private String level;
    private String score;
    private String activity_count;
    private String header;

    public String getHeader() {
        return header;
    }

    public void setHeader(String header) {
        this.header = header;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public void setScore(String score) {
        this.score = score;
    }

    public void setActivity_count(String activity_count) {
        this.activity_count = activity_count;
    }

    public String getUser_id() {
        return user_id;
    }

    public String getUser_name() {
        return user_name;
    }

    public String getLevel() {
        return level;
    }

    public String getScore() {
        return score;
    }

    public String getActivity_count() {
        return activity_count;
    }
}
