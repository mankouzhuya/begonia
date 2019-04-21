package com.github.begonia.core.context;

import com.alibaba.ttl.TransmittableThreadLocal;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Data
public class TrackContext {


    private static TransmittableThreadLocal<TrackContext> hoder = new TransmittableThreadLocal<TrackContext>();


    private String trackId = UUID.randomUUID().toString().replace("-","");

    private List<MethodNode> methodNodes = new ArrayList<>();


    public static TrackContext getTrackContext(){
        return hoder.get();
    }

    public static TrackContext getTrackContextNotNull(){
        TrackContext context = new TrackContext();
        hoder.set(context);
        return hoder.get();
    }

    public static void putContext(TrackContext trackContext){
        hoder.set(trackContext);
    }

    public void addMethodNode(MethodNode methodNode){
        methodNodes.add(methodNode);
    }


    @Override
    public String toString() {
        return "TrackContext{" +
                "trackId='" + trackId + '\'' +
                ", methodNodes=" + methodNodes +
                '}';
    }
}
