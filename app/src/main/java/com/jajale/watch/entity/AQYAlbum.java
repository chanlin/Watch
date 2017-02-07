package com.jajale.watch.entity;

import java.util.List;

/**
 * Created by Sunzhigang on 2016/3/11.
 */
public class AQYAlbum {

    public List<Album> data;
    public static class Album {
        public String albumId; //专辑ID
        public String albumName; //专辑名称
        public String html5PlayUrl;  //专辑播放页面
        public String posterPicUrl;   //图片地址
        public String focus;  //说明
        public String subTitle;
        public String tvYear;

        public String getalbumName(){
            return albumName;
        }
    }

}
