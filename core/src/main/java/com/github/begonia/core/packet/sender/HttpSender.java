package com.github.begonia.core.packet.sender;

import java.io.IOException;

public class HttpSender implements Sender {

    @Override
    public void send(Object object) throws RuntimeException, IOException {
        System.out.println("http发送报文到远程collector:" + object.toString());
    }
}
