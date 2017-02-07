package com.jajale.watch.entity;

import android.graphics.Bitmap;
import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by llh on 16-3-14.
 */
public class ShareData implements Parcelable {


    private int share_type;
    private String share_url;
    private String share_description;
    private String share_image_url;
    private Bitmap share_bitmap;
    private String share_title;

    public int getShare_type() {
        return share_type;
    }

    public void setShare_type(int share_type) {
        this.share_type = share_type;
    }

    public String getShare_url() {
        return share_url;
    }

    public void setShare_url(String share_url) {
        this.share_url = share_url;
    }

    public String getShare_description() {
        return share_description;
    }

    public void setShare_description(String share_description) {
        this.share_description = share_description;
    }

    public String getShare_image_url() {
        return share_image_url;
    }

    public void setShare_image_url(String share_image_url) {
        this.share_image_url = share_image_url;
    }

    public Bitmap getShare_bitmap() {
        return share_bitmap;
    }

    public void setShare_bitmap(Bitmap share_bitmap) {
        this.share_bitmap = share_bitmap;
    }

    public String getShare_title() {
        return share_title;
    }

    public void setShare_title(String share_title) {
        this.share_title = share_title;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.share_type);
        dest.writeString(this.share_url);
        dest.writeString(this.share_description);
        dest.writeString(this.share_image_url);
        dest.writeParcelable(this.share_bitmap, 0);
        dest.writeString(this.share_title);
    }

    public ShareData() {
    }

    protected ShareData(Parcel in) {
        this.share_type = in.readInt();
        this.share_url = in.readString();
        this.share_description = in.readString();
        this.share_image_url = in.readString();
        this.share_bitmap = in.readParcelable(Bitmap.class.getClassLoader());
        this.share_title = in.readString();
    }

    public static final Parcelable.Creator<ShareData> CREATOR = new Parcelable.Creator<ShareData>() {
        public ShareData createFromParcel(Parcel source) {
            return new ShareData(source);
        }

        public ShareData[] newArray(int size) {
            return new ShareData[size];
        }
    };
}
