package com.github.begonia.core.packet.sender;

import java.io.IOException;

public interface Sender {

    void send(Object object) throws RuntimeException, IOException;

}
