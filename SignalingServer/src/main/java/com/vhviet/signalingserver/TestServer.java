package com.vhviet.signalingserver;

import com.vhviet.signalingserver.server.SignalingServer;

import java.io.IOException;

public class TestServer {
    public static void main(String[] args) throws IOException {
        new SignalingServer().startServer();
    }
}
