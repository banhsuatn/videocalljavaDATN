package com.vhviet.signalingserver.model;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;

public class UserModel {
    private RqUserInfo.UserInfo info;
    private Socket socket;
    private RoomVideoCall room;

    public UserModel(Socket socket) {
        this.socket = socket;
    }

    public void sendCommand(String message) {
        try {
            String msg = message;
            OutputStream output = socket.getOutputStream();
            PrintWriter writer = new PrintWriter(output, true);
            writer.println(msg);
        } catch (IOException e) {
            System.out.println("ERROR -----UserModel sendCommand -----\n" + e.getMessage());
        }
    }

    public Socket getSocket() {
        return socket;
    }

    public RqUserInfo.UserInfo getInfo() {
        return info;
    }

    public void setInfo(RqUserInfo.UserInfo info) {
        this.info = info;
    }

    public RoomVideoCall getRoom() {
        return room;
    }

    public void setRoom(RoomVideoCall room) {
        if (room == null) {
            info.setRoomId(0);
        } else {
            info.setRoomId(room.getRoomId());
        }
        this.room = room;
    }
}

