package com.github.begonia.communication;

import com.alibaba.fastjson.JSON;
import com.github.begonia.cache.DefaultCache;
import com.github.begonia.communication.protocol.StringProtocol;
import com.github.begonia.communication.protocol.YqnPacket;
import lombok.extern.slf4j.Slf4j;
import org.smartboot.socket.StateMachineEnum;
import org.smartboot.socket.buffer.BufferPagePool;
import org.smartboot.socket.extension.plugins.HeartPlugin;
import org.smartboot.socket.extension.processor.AbstractMessageProcessor;
import org.smartboot.socket.transport.AioQuickClient;
import org.smartboot.socket.transport.AioSession;

import java.io.IOException;
import java.net.ConnectException;
import java.nio.channels.AsynchronousChannelGroup;
import java.time.LocalDateTime;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

import static com.github.begonia.communication.protocol.YqnPacket.TYPE_HEART;
import static com.github.begonia.communication.protocol.YqnPacket.TYPE_TRACK;

@Slf4j
public class Client {

    private static AioSession<String> session;

    private static String severHost;

    private static Integer serverPort;

    private static Boolean regFlag = false;

    public static void main(String[] args) throws InterruptedException, ExecutionException, IOException {
        //client.start("zyj562464314.vicp.cc", 17693);
        start("127.0.0.1", 8989);
        for (int i = 0; i < 8; i++) {
            new Thread(() -> {
                while (true) {
                    YqnPacket packet = new YqnPacket(TYPE_TRACK, LocalDateTime.now().toString());
                    try {
                        write(packet, getSession(), true);
                    } catch (IOException e) {
                        e.printStackTrace();
                        break;
                    }
                }
            }).start();
        }

    }

    public static synchronized void reconnection() {
        System.out.println("重新连接" + LocalDateTime.now());
        if (session != null && !session.isInvalid()) return;
        try {
            start(severHost, serverPort);
        } catch (Exception e) {
            log.error("重新连接服务器失败:{}", e);
        }

    }

    public static synchronized void start(String host, Integer port) throws InterruptedException, ExecutionException, IOException {
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

            @Override
            public void sendHeartRequest(AioSession<String> session) throws IOException {
                YqnPacket packet = new YqnPacket(TYPE_HEART, "ping");
                CommunicationUtils.write(packet, session);
            }

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
        severHost = host;
        serverPort = port;
        regFlag = false;
    }

    public static void write(YqnPacket packet, AioSession<String> session, Boolean needReconnection) throws IOException {
        try {
            CommunicationUtils.write(packet, session);
        } catch (ConnectException e) {
            log.error("连接异常:{}", e);
            synchronized (Client.class) {
                if (!regFlag && needReconnection) {
                    regTask();
                }
            }

        }
    }


    public static void regTask() {
        regFlag = true;
        reconnection();
        //重连
        DefaultCache.getTimer().schedule(() -> {
            if (session == null || session.isInvalid()) regTask();
        }, 5, TimeUnit.SECONDS);
    }

    public static AioSession<String> getSession() {
        return session;
    }

}
