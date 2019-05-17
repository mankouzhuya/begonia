package com.github.begonia.core.packet.sender;

import com.github.begonia.core.context.TrackContext;

import java.io.IOException;

public interface Sender {

    void send(Packet packet) throws RuntimeException, IOException;

}
