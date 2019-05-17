package com.github.begonia.core.context;


import java.io.Serializable;
import java.util.List;

public class MethodNode implements Serializable {

    public static final Integer NODE_TYPE_START = 1;

    public static final Integer NODE_TYPE_NORMAL = 2;

    private String trackId;

    private String methodId;//id

    private String parentMethodId;

    private Integer nodeType;

    private String fullClassName;

    private String methodName;

    private List<String> args;//方法入参

    private String returnInfo;//返回信息

    private long excuteTime;//执行时间

    private String excetpionClass;//异常类

    private String excetpionMsg;//异常信息

    private List<MethodNode> methodNodes;

    public String getTrackId() {
        return trackId;
    }

    public void setTrackId(String trackId) {
        this.trackId = trackId;
    }

    public String getMethodId() {
        return methodId;
    }

    public void setMethodId(String methodId) {
        this.methodId = methodId;
    }

    public String getParentMethodId() {
        return parentMethodId;
    }

    public void setParentMethodId(String parentMethodId) {
        this.parentMethodId = parentMethodId;
    }

    public Integer getNodeType() {
        return nodeType;
    }

    public void setNodeType(Integer nodeType) {
        this.nodeType = nodeType;
    }

    public String getFullClassName() {
        return fullClassName;
    }

    public void setFullClassName(String fullClassName) {
        this.fullClassName = fullClassName;
    }

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public List<String> getArgs() {
        return args;
    }

    public void setArgs(List<String> args) {
        this.args = args;
    }

    public String getReturnInfo() {
        return returnInfo;
    }

    public void setReturnInfo(String returnInfo) {
        this.returnInfo = returnInfo;
    }

    public long getExcuteTime() {
        return excuteTime;
    }

    public void setExcuteTime(long excuteTime) {
        this.excuteTime = excuteTime;
    }

    public String getExcetpionClass() {
        return excetpionClass;
    }

    public void setExcetpionClass(String excetpionClass) {
        this.excetpionClass = excetpionClass;
    }

    public String getExcetpionMsg() {
        return excetpionMsg;
    }

    public void setExcetpionMsg(String excetpionMsg) {
        this.excetpionMsg = excetpionMsg;
    }

    public List<MethodNode> getMethodNodes() {
        return methodNodes;
    }

    public void setMethodNodes(List<MethodNode> methodNodes) {
        this.methodNodes = methodNodes;
    }

    @Override
    public String toString() {
        return "MethodNode{" +
                "trackId='" + trackId + '\'' +
                ", methodId='" + methodId + '\'' +
                ", parentMethodId='" + parentMethodId + '\'' +
                ", nodeType=" + nodeType +
                ", fullClassName='" + fullClassName + '\'' +
                ", methodName='" + methodName + '\'' +
                ", args=" + args +
                ", returnInfo='" + returnInfo + '\'' +
                ", excuteTime=" + excuteTime +
                ", excetpionClass='" + excetpionClass + '\'' +
                ", excetpionMsg='" + excetpionMsg + '\'' +
                ", methodNodes=" + methodNodes +
                '}';
    }
}
