package com.github.begonia.communication;

import com.alibaba.fastjson.JSON;
import com.github.begonia.communication.protocol.StringProtocol;
import com.github.begonia.communication.protocol.YqnPacket;
import lombok.extern.slf4j.Slf4j;
import org.smartboot.socket.StateMachineEnum;
import org.smartboot.socket.extension.plugins.HeartPlugin;
import org.smartboot.socket.extension.processor.AbstractMessageProcessor;
import org.smartboot.socket.transport.AioQuickServer;
import org.smartboot.socket.transport.AioSession;
import org.smartboot.socket.transport.WriteBuffer;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

import static com.github.begonia.communication.protocol.YqnPacket.TYPE_HEART;
import static com.github.begonia.communication.protocol.YqnPacket.TYPE_TRACK;

@Slf4j
public class Server {


    public static void main(String[] args) throws IOException {
        Server.start("localhost", 8888);
    }

    public static void start(String host, Integer port) throws IOException {
        System.setProperty("smart-socket.server.pageSize", (1024 * 1024 * 16) + "");
        System.setProperty("smart-socket.session.writeChunkSize", "4096");
        AbstractMessageProcessor<String> processor = new AbstractMessageProcessor<String>() {
            @Override
            public void process0(AioSession<String> session, String msg) {
                YqnPacket yqnPacket = JSON.parseObject(msg, YqnPacket.class);
                if (TYPE_TRACK == yqnPacket.getType()) {
//                    List<MethodNode> list = JSON.parseObject(yqnPacket.getBody(), new TypeReference<List<MethodNode>>() {
//                    });
                    log.info("收到客户端发来来的消息{}:{}", Thread.currentThread().getName() + "==>" + Thread.currentThread().getId(), yqnPacket.getBody());
                }

            }

            @Override
            public void stateEvent0(AioSession<String> session, StateMachineEnum stateMachineEnum, Throwable throwable) {
                if (throwable != null) log.error("异常了:{}", throwable);
            }
        };
        processor.addPlugin(new HeartPlugin<String>(3000) {
            /**
             * 自定义心跳消息并发送
             *
             * @param session
             */
            @Override
            public void sendHeartRequest(AioSession<String> session) throws IOException {
                YqnPacket packet = new YqnPacket(TYPE_HEART, "pong");
                String msg = JSON.toJSONString(packet);
                byte[] data = msg.getBytes(StandardCharsets.UTF_8);
                WriteBuffer outputStream = session.writeBuffer();
                outputStream.writeInt(data.length);
                outputStream.write(data);
                outputStream.flush();
            }

            /**
             * 判断当前收到的消息是否为心跳消息。
             * 心跳请求消息与响应消息可能相同，也可能不同，因实际场景而异，故接口定义不做区分。
             *
             * @param session
             * @param msg
             * @return
             */
            @Override
            public boolean isHeartMessage(AioSession<String> session, String msg) {
                YqnPacket yqnPacket = null;
                try{
                    yqnPacket  = JSON.parseObject(msg, YqnPacket.class);
                }catch (Exception e){
                    log.error("报文解析错误:{}",e);
                    session.close();
                }
                if(yqnPacket == null) return false;
                return TYPE_HEART == yqnPacket.getType();
            }
        });
//        processor.addPlugin(new MonitorPlugin(5));
        AioQuickServer<String> server = new AioQuickServer<>(host, port, new StringProtocol(), processor);
        server.setReadBufferSize(1024 * 1024);
//        processor.addPlugin(new BufferPageMonitorPlugin(server, 12));
        server.start();
    }

}
