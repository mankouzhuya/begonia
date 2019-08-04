package com.github.begonia.core.context;

import lombok.extern.slf4j.Slf4j;

import java.io.Serializable;
import java.util.List;

/**
 * @author: 张艳军
 * @date: 2019/8/3 14:32
 * @desc:
 * @version: 1.0
 **/
@Slf4j
public class Carrier implements Serializable {

    private String gid;

    private String fromId;

    private String sysName;

    private List<MethodNode> methodNodes;

    public String getSysName() {
        return sysName;
    }

    public void setSysName(String sysName) {
        this.sysName = sysName;
    }

    public List<MethodNode> getMethodNodes() {
        return methodNodes;
    }

    public void setMethodNodes(List<MethodNode> methodNodes) {
        this.methodNodes = methodNodes;
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
}
