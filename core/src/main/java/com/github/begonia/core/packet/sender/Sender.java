package com.github.begonia.core.packet.sender;

import java.io.IOException;
import java.util.concurrent.ExecutionException;

public interface Sender {

    void send(Object object) throws RuntimeException, IOException, ExecutionException, InterruptedException;

}
