package com.github.begonia.core.context;

import java.io.Serializable;

/**
 * @author: 张艳军
 * @date: 2019/8/4 10:35
 * @desc:
 * @version: 1.0
 **/
public class SysInfo implements Serializable {

    private String gid;//各个系统透传

    private String fromId;//

    private String currentSysId;

    private String trackId;//当前系统所有方法透传

    public String getTrackId() {
        return trackId;
    }

    public void setTrackId(String trackId) {
        this.trackId = trackId;
    }

    public String getGid() {
        return gid;
    }

    public void setGid(String gid) {
        this.gid = gid;
    }

    public String getFromId() {
        return fromId;
    }

    public void setFromId(String fromId) {
        this.fromId = fromId;
    }

    public String getCurrentSysId() {
        return currentSysId;
    }

    public void setCurrentSysId(String currentSysId) {
        this.currentSysId = currentSysId;
    }
}
