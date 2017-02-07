package com.jajale.watch.entity;

/**
 * Created by lilonghui on 2015/12/8.
 * Email:lilonghui@bjjajale.com
 */
public class VersionUpdateData {

    /**
     * version : 0.2.0
     * describe : 版本更新内容：1.修复某某BUG。2.修复某某某BUG
     * url : https://portal.qiniu.com/bucket/file/jjlfamily0.2.0.apk
     * releaseTime : 2015-12-08
     * forcedState : 1
     */

    private String version;
    private String describe;
    private String url;
    private String releaseTime;
    private String forcedState;

    public void setVersion(String version) {
        this.version = version;
    }

    public void setDescribe(String describe) {
        this.describe = describe;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void setReleaseTime(String releaseTime) {
        this.releaseTime = releaseTime;
    }

    public void setForcedState(String forcedState) {
        this.forcedState = forcedState;
    }

    public String getVersion() {
        return version;
    }

    public String getDescribe() {
        return describe;
    }

    public String getUrl() {
        return url;
    }

    public String getReleaseTime() {
        return releaseTime;
    }

    public String getForcedState() {
        return forcedState;
    }
}
