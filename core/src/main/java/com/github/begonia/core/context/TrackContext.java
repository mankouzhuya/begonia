package com.github.begonia.core.context;

import com.alibaba.ttl.TransmittableThreadLocal;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class TrackContext {


    private static TransmittableThreadLocal<TrackContext> hoder = new TransmittableThreadLocal<TrackContext>();


    private String trackId;

    private String anoId;

    private List<MethodNode> methodNodes;

    private TrackContext(String trackId, List<MethodNode> methodNodes) {
        this.trackId = trackId;
        this.methodNodes = methodNodes;
    }

    /**
     * 获取上下文
     **/
    public static TrackContext getTrackContextNotNull() {
        TrackContext context = hoder.get();
        if (context == null) {
            synchronized (TrackContext.class) {
                if (hoder.get() == null) {
                    context = new TrackContext(genUid(), new ArrayList<>());
                    hoder.set(context);
                }
                return hoder.get();
            }
        }
        return context;
    }


    /**
     * 清除上下文
     **/
    public static void clearEventContext() {
        TrackContext context = hoder.get();
        if (context != null) {
            synchronized (TrackContext.class) {
                if (hoder.get() != null) {
                    hoder.remove();
                }
            }
        }
    }

    public static String genUid() {
        return UUID.randomUUID().toString().replace("-", "");
    }


    public void addMethodNode(MethodNode methodNode) {
        methodNodes.add(methodNode);
    }

    public String getTrackId() {
        return trackId;
    }

    public void setTrackId(String trackId) {
        this.trackId = trackId;
    }

    public String getAnoId() {
        return anoId;
    }

    public void setAnoId(String anoId) {
        this.anoId = anoId;
    }

    public List<MethodNode> getMethodNodes() {
        return methodNodes;
    }

    public void setMethodNodes(List<MethodNode> methodNodes) {
        this.methodNodes = methodNodes;
    }

    @Override
    public String toString() {
        return "TrackContext{" +
                "trackId='" + trackId + '\'' +
                ", anoId='" + anoId + '\'' +
                ", methodNodes=" + methodNodes +
                '}';
    }
}
