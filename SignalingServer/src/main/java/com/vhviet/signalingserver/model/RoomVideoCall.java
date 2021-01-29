package com.vhviet.signalingserver.model;

import com.vhviet.signalingserver.ultis.CommandManager;

import java.util.ArrayList;
import java.util.Random;

public class RoomVideoCall {
    private int roomId;
    private UserModel owner;
    private ArrayList<UserModel> members;

    public RoomVideoCall(UserModel owner) {
        Random rand = new Random();
        this.roomId = 1 + rand.nextInt(999999);
        this.owner = owner;
    }

    public void checkRoomState() {
        if (owner == null) {
            for (UserModel member : members) {
                member.getInfo().setStatus(0);
                member.getInfo().setRoomId(0);
                member.setRoom(null);
                member.sendCommand(CommandManager.SHARE.roomClosed("Chủ phòng đã ngắt kết nối"));
            }
        } else if (members.size() == 0) {
            owner.getInfo().setStatus(0);
            owner.getInfo().setRoomId(0);
            owner.setRoom(null);
            owner.sendCommand(CommandManager.SHARE.roomClosed("Mọi người đã ngắt kết nối"));
        }
    }

    //get set
    public UserModel getOwner() {
        return owner;
    }

    public void setOwner(UserModel owner) {
        this.owner = owner;
    }

    public ArrayList<UserModel> getMembers() {
        return members;
    }

    public void setMembers(ArrayList<UserModel> members) {
        this.members = members;
    }

    public int getRoomId() {
        return roomId;
    }
}
