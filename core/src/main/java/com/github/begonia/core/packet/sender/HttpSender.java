package com.github.begonia.core.packet.sender;

import com.github.begonia.core.context.TrackContext;

import java.io.IOException;

public class HttpSender implements Sender{

    @Override
    public void send(Packet packet) throws RuntimeException, IOException {
        System.out.println("http发送报文到远程collector:"+packet.toString());
    }
}
