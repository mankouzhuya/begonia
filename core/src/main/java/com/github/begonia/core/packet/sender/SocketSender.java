package com.github.begonia.core.packet.sender;

import com.alibaba.fastjson.JSON;
import com.github.begonia.communication.Client;
import com.github.begonia.communication.protocol.YqnPacket;

import java.io.IOException;
import java.util.concurrent.ExecutionException;

import static com.github.begonia.communication.protocol.YqnPacket.TYPE_TRACK;

public class SocketSender implements Sender {

    @Override
    public void send(Object object) throws RuntimeException, IOException, ExecutionException, InterruptedException {
        Client.write(new YqnPacket(TYPE_TRACK, JSON.toJSONString(object)));
    }

}
