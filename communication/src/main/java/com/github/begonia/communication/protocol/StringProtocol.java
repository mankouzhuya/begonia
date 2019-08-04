package com.github.begonia.communication.protocol;

import lombok.extern.slf4j.Slf4j;
import org.smartboot.socket.Protocol;
import org.smartboot.socket.transport.AioSession;

import java.nio.ByteBuffer;

/**
 * @version V1.0 , 2018/11/23
 */
@Slf4j
public class StringProtocol implements Protocol<String> {
    @Override
    public String decode(ByteBuffer readBuffer, AioSession<String> session) {
        //识别消息长度
        int remaining = readBuffer.remaining();
        if (remaining < Integer.BYTES) {
            return null;
        }
        //判断是否存在半包情况
        readBuffer.mark();
        int length = readBuffer.getInt();
        if (length > readBuffer.remaining()) {
            readBuffer.reset();
            return null;
        }
        byte[] b = new byte[length];
        readBuffer.get(b);
        readBuffer.mark();
        String msg = new String(b);
//        log.info("解码后的msg:{}",msg);
        return msg;
    }
}
