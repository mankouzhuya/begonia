package com.github.begonia.core.context;

import com.alibaba.ttl.TransmittableThreadLocal;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Data
public class TrackContext {


    private static TransmittableThreadLocal<TrackContext> hoder = new TransmittableThreadLocal<TrackContext>();


    private String trackId;

    private String anoId;

    private List<MethodNode> methodNodes;

    private TrackContext(String trackId,List<MethodNode> methodNodes){
        this.trackId = trackId;
        this.methodNodes = methodNodes;
    }

    public static TrackContext getTrackContextNotNull(){
        TrackContext context = hoder.get();
        if(context == null){
            context = new TrackContext(genUid(),new ArrayList<>());
            hoder.set(context);
        }
        return context;
    }

    public static String genUid(){
        return UUID.randomUUID().toString().replace("-","");
    }


    public void addMethodNode(MethodNode methodNode){
        methodNodes.add(methodNode);
    }


}
