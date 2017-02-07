package com.jajale.watch.entityno;

import java.util.List;

/**
 * Created by athena on 2015/12/9.
 * Email: lizhiqiang@bjjajale.com
 */
public class getUnReadMsgResult {
    public List<UnReadMsgEntity> watchList ;

    public List<WarningResponseEntity> alarmList ;

    public List<WarningResponseEntity> getAmlList() {
        return alarmList;
    }

    public void setAmlList(List<WarningResponseEntity> amlList) {
        this.alarmList = amlList;
    }

    public List<UnReadMsgEntity> getWatchList() {
        return watchList;
    }

    public void setWatchList(List<UnReadMsgEntity> watchList) {
        this.watchList = watchList;
    }
}
