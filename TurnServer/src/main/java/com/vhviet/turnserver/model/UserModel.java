package com.vhviet.turnserver.model;

import com.vhviet.turnserver.utils.Utilitys;

import java.net.DatagramPacket;
import java.util.Arrays;
import java.util.Date;

public class UserModel {
    private long time;
    private DatagramPacket client;
    private int roomId;

    public UserModel(DatagramPacket client) {
        this.client = client;
        time = new Date().getTime();
        roomId = Utilitys.SHARE.fromByteArray(Arrays.copyOfRange(client.getData(), 1, 5));
    }

    public DatagramPacket getClient() {
        return client;
    }

    public long getTime() {
        return time;
    }

    public int getRoomId() {
        return roomId;
    }
}
