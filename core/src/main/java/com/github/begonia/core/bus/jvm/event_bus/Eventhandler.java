package com.github.begonia.core.bus.jvm.event_bus;

import com.github.begonia.core.bus.jvm.msg.Msg;
import com.github.begonia.core.cache.DefaultCache;
import com.github.begonia.core.context.MethodNode;
import com.github.begonia.core.packet.sender.HttpSender;
import com.github.begonia.core.packet.sender.Packet;
import com.google.common.eventbus.Subscribe;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static com.github.begonia.core.bus.jvm.msg.Msg.*;
import static com.github.begonia.core.context.MethodNode.NODE_TYPE_START;

@Slf4j
public class Eventhandler {


    /**
     * 只有通过@Subscribe注解的方法才会被注册进EventBus
     * 而且方法有且只能有1个参数
     *
     * @param msg
     */
   // @AllowConcurrentEvents//线程安全
    @Subscribe
    public void process(final Msg msg) throws IOException {
        if(msg.getType() == null)return;
        if(MSG_TYPE_METHOD_NODE == msg.getType()){
            processMethodNode(msg);
            return ;
        }

    }

    private void processMethodNode(Msg msg) throws IOException {
        MethodNode node = (MethodNode) msg.getMsgBody();
        Object object = DefaultCache.getInstance().get(node.getTrackId());

        if(object != null){
            List<MethodNode> arrayList = (List<MethodNode>) object;
            arrayList.add(node);
            DefaultCache.getInstance().put(node.getTrackId(),arrayList);
            //check node type
            if(NODE_TYPE_START == node.getNodeType()) sendPacket(node.getTrackId());
            return ;
        }
        List<MethodNode> arrayList = new ArrayList<>();
        arrayList.add(node);
        DefaultCache.getInstance().put(node.getTrackId(),arrayList);
    }

    private void sendPacket(String trackId) throws IOException {
        Object object = DefaultCache.getInstance().get(trackId);
        Packet packet = new Packet(trackId, (List<MethodNode>) object);
        new HttpSender().send(packet);
        DefaultCache.getInstance().remove(trackId);
    }
}
