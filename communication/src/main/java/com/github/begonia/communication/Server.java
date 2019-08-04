package com.github.begonia.communication;

import com.alibaba.fastjson.JSON;
import com.github.begonia.communication.protocol.StringProtocol;
import com.github.begonia.communication.protocol.YqnPacket;
import lombok.extern.slf4j.Slf4j;
import org.smartboot.socket.extension.plugins.HeartPlugin;
import org.smartboot.socket.extension.processor.AbstractMessageProcessor;
import org.smartboot.socket.transport.AioQuickServer;
import org.smartboot.socket.transport.AioSession;

import java.io.IOException;

import static com.github.begonia.communication.protocol.YqnPacket.TYPE_HEART;

@Slf4j
public class Server {


    public static void main(String[] args) throws IOException {
//        Server.start("127.0.0.1", 8989);
    }

    public static void start(String host, Integer port, AbstractMessageProcessor<String> processor) throws IOException {
        System.setProperty("smart-socket.server.pageSize", (1024 * 1024 * 16) + "");
        System.setProperty("smart-socket.session.writeChunkSize", "4096");
        processor.addPlugin(new HeartPlugin<String>(3000) {

            @Override
            public void sendHeartRequest(AioSession<String> aioSession) throws IOException {
                YqnPacket packet = new YqnPacket(TYPE_HEART, "pong");
                CommunicationUtils.write(packet, aioSession);
            }

            @Override
            public boolean isHeartMessage(AioSession<String> aioSession, String msg) {
                YqnPacket yqnPacket = JSON.parseObject(msg, YqnPacket.class);
                return TYPE_HEART == yqnPacket.getType();
            }
        });
//        processor.addPlugin(new MonitorPlugin(3));
        AioQuickServer<String> server = new AioQuickServer<>(host, port, new StringProtocol(), processor);
        server.setReadBufferSize(1024 * 1024);
//        processor.addPlugin(new BufferPageMonitorPlugin(server, 12));
        server.start();
    }

}
