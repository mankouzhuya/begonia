package com.github.begonia.collectors.support.processor;

import com.alibaba.fastjson.JSON;
import com.github.begonia.communication.protocol.YqnPacket;
import com.github.begonia.core.context.Carrier;
import lombok.extern.slf4j.Slf4j;
import org.smartboot.socket.StateMachineEnum;
import org.smartboot.socket.extension.processor.AbstractMessageProcessor;
import org.smartboot.socket.transport.AioSession;
import org.springframework.stereotype.Component;

import static com.github.begonia.communication.protocol.YqnPacket.TYPE_TRACK;

/**
 * @author: 张艳军
 * @date: 2019/8/3 13:53
 * @desc:
 * @version: 1.0
 **/
@Component
@Slf4j
public class MsgProcessor extends AbstractMessageProcessor<String> {


    @Override
    public void stateEvent0(AioSession aioSession, StateMachineEnum stateMachineEnum, Throwable throwable) {
        if (throwable != null) log.error("异常了:{}", throwable);
    }

    @Override
    public void process0(AioSession<String> aioSession, String msg) {
        YqnPacket yqnPacket = JSON.parseObject(msg, YqnPacket.class);
        if (TYPE_TRACK == yqnPacket.getType()) {
//            List<MethodNode> list = JSON.parseObject(yqnPacket.getBody(), new TypeReference<List<MethodNode>>() {
//            });
            Carrier carrier = JSON.parseObject(yqnPacket.getBody(), Carrier.class);
            log.info("收到客户端发来来的消息{}", msg);

        }
    }
}
