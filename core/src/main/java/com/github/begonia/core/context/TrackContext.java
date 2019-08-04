package com.github.begonia.core.context;

import com.alibaba.ttl.TransmittableThreadLocal;
import java.util.List;
import java.util.UUID;

public class TrackContext extends SysInfo{

    private static TransmittableThreadLocal<TrackContext> hoder = new TransmittableThreadLocal<TrackContext>();

    private volatile String anoId;//当前方法的唯一id

    private TrackContext() {

    }

    /**
     * 获取上下文
     **/
    public static TrackContext getTrackContextNotNull() {
        TrackContext context = hoder.get();
        if (context == null) {
            synchronized (TrackContext.class) {
                if (hoder.get() == null) {
                    context = new TrackContext();
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
    public static void clearContext() {
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

    public String getAnoId() {
        return anoId;
    }

    public void setAnoId(String anoId) {
        this.anoId = anoId;
    }

    public static String findGId(List<MethodNode> methodNodes){
        MethodNode node = methodNodes.stream().filter(s -> s.getNodeType() == MethodNode.NODE_TYPE_START).findFirst().orElseThrow(() -> new RuntimeException("没有找到对应的开始节点"));
        return node.getGid();
    }

    public static String findFromId(List<MethodNode> methodNodes){
        MethodNode node = methodNodes.stream().filter(s -> s.getNodeType() == MethodNode.NODE_TYPE_START).findFirst().orElseThrow(() -> new RuntimeException("没有找到对应的开始节点"));
        return node.getFromId();
    }

}
