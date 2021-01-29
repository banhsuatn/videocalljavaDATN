package com.vhviet.turnserver.server;

import com.vhviet.turnserver.model.UserModel;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.util.ArrayList;
import java.util.Date;

public class TurnServer {
    private DatagramSocket socket;
    private ArrayList<UserModel> userModels;
    private boolean isRun = true;

    public TurnServer() {
        userModels = new ArrayList<>();
    }

    public void startServer() throws IOException {
        runThreadCheckConnected();
        byte[] buffer = new byte[20*1024];
        socket = new DatagramSocket(6667);
        System.out.println("Server started");
        while (isRun) {
            try {
                DatagramPacket client = new DatagramPacket(buffer, buffer.length);
                socket.receive(client);
                System.out.println(client.getLength());
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        System.out.println("accept client " + client.getAddress() + " " + client.getPort());
                        UserModel user = new UserModel(client);
                        addNewUser(user);
                        sendCommand(user);
                    }
                }).start();
            } catch (Exception e) {
                e.printStackTrace();
                System.out.println("ERROR -----Turn startServer -----\n" + e.getMessage());
            }
        }
    }

    private void addNewUser(UserModel user) {
        for (UserModel u: userModels) {
            if (u.getClient().getAddress().getHostAddress()
                    .equalsIgnoreCase(user.getClient().getAddress().getHostAddress())
                    && u.getClient().getPort() == user.getClient().getPort()) {
                userModels.remove(u);
                break;
            }
        }
        userModels.add(user);
    }

    private void sendCommand(UserModel user) {
        if (user.getClient().getData()[0] != 0) {
            for (UserModel u: userModels) {
                if (u.getRoomId() == user.getRoomId() && !u.equals(user)) {
                    try {
                        System.out.println(user.getClient().getAddress() + "send to " + u.getClient().getAddress());
                        socket.send(new DatagramPacket(user.getClient().getData(),
                                user.getClient().getLength(),
                                u.getClient().getAddress(),
                                u.getClient().getPort()));
                    } catch (IOException e) {
                        System.out.println("ERROR -----TurnServer sendCommand -----\n" + e.getMessage());
                    }
                }
            }
        }
    }

    private void runThreadCheckConnected() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (isRun) {
                    try {
                        userModels.removeIf(u -> new Date().getTime() - u.getTime() > 10000);
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }
}
