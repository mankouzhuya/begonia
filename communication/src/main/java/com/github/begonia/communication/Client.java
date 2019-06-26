package com.github.begonia.communication;

import com.alibaba.fastjson.JSON;
import com.github.begonia.communication.protocol.StringProtocol;
import com.github.begonia.communication.protocol.YqnPacket;
import lombok.extern.slf4j.Slf4j;
import org.smartboot.socket.StateMachineEnum;
import org.smartboot.socket.buffer.BufferPagePool;
import org.smartboot.socket.extension.plugins.HeartPlugin;
import org.smartboot.socket.extension.processor.AbstractMessageProcessor;
import org.smartboot.socket.transport.AioQuickClient;
import org.smartboot.socket.transport.AioSession;
import org.smartboot.socket.transport.WriteBuffer;

import java.io.IOException;
import java.nio.channels.AsynchronousChannelGroup;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.ExecutionException;

import static com.github.begonia.communication.protocol.YqnPacket.TYPE_HEART;

@Slf4j
public class Client {

    private static AioSession<String> session;

    public static void main(String[] args) throws InterruptedException, ExecutionException, IOException {
        Client client = new Client();
        client.start("localhost", 8888);
    }

    public static void start(String host, Integer port) throws InterruptedException, ExecutionException, IOException {
        System.setProperty("smart-socket.client.pageSize", (1024 * 1024 * 64) + "");
        System.setProperty("smart-socket.session.writeChunkSize", "" + (1024 * 1024));
        BufferPagePool bufferPagePool = new BufferPagePool(1024 * 1024 * 32, 10, true);
        AsynchronousChannelGroup asynchronousChannelGroup = AsynchronousChannelGroup.withFixedThreadPool(Runtime.getRuntime().availableProcessors(), (r) -> new Thread(r, "ClientGroup"));
        AbstractMessageProcessor<String> processor = new AbstractMessageProcessor() {
            @Override
            public void process0(AioSession session, Object msg) {
                log.info("收到服务器信息:{}", msg);
            }

            @Override
            public void stateEvent0(AioSession session, StateMachineEnum stateMachineEnum, Throwable throwable) {
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
                YqnPacket packet = new YqnPacket(TYPE_HEART, "ping");
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
                YqnPacket yqnPacket = JSON.parseObject(msg, YqnPacket.class);
                return TYPE_HEART == yqnPacket.getType();
            }
        });
        AioQuickClient<String> client = new AioQuickClient<>(host, port, new StringProtocol(), processor);
        client.setBufferPagePool(bufferPagePool);
        client.setWriteQueueCapacity(10);
        session = client.start(asynchronousChannelGroup);
    }

    public static void write(YqnPacket packet) throws IOException {
        if (packet == null) throw new IllegalArgumentException("参数不能为空");
        if (session == null) throw new RuntimeException("未连接服务器");
        String msg = JSON.toJSONString(packet);
        byte[] data = msg.getBytes(StandardCharsets.UTF_8);
        WriteBuffer outputStream = session.writeBuffer();
        outputStream.writeInt(data.length);
        outputStream.write(data);
        outputStream.flush();
    }
}
