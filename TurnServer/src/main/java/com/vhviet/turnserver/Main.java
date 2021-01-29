package com.vhviet.turnserver;

import com.vhviet.turnserver.server.TurnServer;

import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {
        new TurnServer().startServer();
    }
}
