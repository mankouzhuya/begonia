package com.github.begonia.communication;

import com.alibaba.fastjson.JSON;
import com.github.begonia.communication.protocol.YqnPacket;
import org.smartboot.socket.transport.AioSession;
import org.smartboot.socket.transport.WriteBuffer;

import java.io.IOException;
import java.net.ConnectException;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;

/**
 * @author: 张艳军
 * @date: 2019/8/1 10:24
 * @desc:
 * @version: 1.0
 **/
public class CommunicationUtils {

    public static synchronized void write(YqnPacket packet, AioSession<String> session) throws IOException {
        if (packet == null) throw new IllegalArgumentException("参数不能为空");
        if (session == null) throw new ConnectException("未连接服务器");
        WriteBuffer outputStream = session.writeBuffer();
        if (outputStream == null) throw new ConnectException("与服务器连接断开,请重新连接");

        String msg = JSON.toJSONString(packet);
        byte[] data = msg.getBytes(StandardCharsets.UTF_8);
        ByteBuffer buffer = ByteBuffer.allocate(Integer.BYTES + data.length);
        buffer.putInt(data.length);
        buffer.put(data);
        outputStream.write(buffer.array());
        outputStream.flush();

//        synchronized (outputStream) {
//            outputStream.writeInt(data.length);
//            outputStream.write(data);
//            outputStream.flush();
//        }
    }

}
