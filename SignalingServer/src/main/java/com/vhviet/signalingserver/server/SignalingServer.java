package com.vhviet.signalingserver.server;

import com.google.gson.Gson;
import com.vhviet.signalingserver.model.*;
import com.vhviet.signalingserver.ultis.CommandManager;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class SignalingServer implements ServerThread.ServerThreadCallBack {

    private ArrayList<UserModel> userModels;
    private ArrayList<RoomVideoCall> roomVideoCalls;
    private boolean isRun = true;
    private ServerSocket serverSocket;

    public SignalingServer() {
        userModels = new ArrayList<>();
        roomVideoCalls = new ArrayList<>();
    }

    public void startServer() throws IOException {
        serverSocket = new ServerSocket(6666);
        while (isRun) {
            Socket socket = serverSocket.accept();
            System.out.println("accept client " +socket.getInetAddress());
            try {
                UserModel user = new UserModel(socket);
                userModels.add(user);
                new Thread(new ServerThread(user, this)).start();
            } catch (Exception e) {
                e.printStackTrace();
                System.out.println("ERROR -----SignalingServer startServer -----\n" + e.getMessage());
            }
        }
    }

    private void sendCommand(String message, ArrayList<UserModel> userModels) {
        for (UserModel user: userModels) {
            user.sendCommand(message);
        }
    }

    @Override
    public void clientSendCommand(UserModel user, String message) {
        Gson gson = new Gson();
        System.out.println("--------------------client send--------------------\n" + message + "\n----------------------------------------");
        RequestSocketModel req = gson.fromJson(message, RequestSocketModel.class);
        if (req == null) {
            return;
        }
        switch (req.getSignalKey()) {
            case "LOGIN":
                RqUserInfo info = gson.fromJson(message, RqUserInfo.class);
                user.setInfo(info.getData());
                // response
                ArrayList<UserModel> userLogin = new ArrayList<>();
                userLogin.add(user);
                sendCommand(CommandManager.SHARE.loginRes(""), userLogin);
                // update list user online for client
                sendCommand(CommandManager.SHARE.listUser(userModels), userModels);
                break;
            case "CALL_REQUEST":
                RqCallVideo rqCallVideo = gson.fromJson(message, RqCallVideo.class);
                // create room
                RoomVideoCall room = new RoomVideoCall(user);
                user.getInfo().setStatus(2);
                user.setRoom(room);
                //add member to room
                ArrayList<UserModel> members = new ArrayList<>();
                for (UserModel userOnl: userModels) {
                    for (String reqUser: rqCallVideo.getData()) {
                        if (reqUser.equals(userOnl.getInfo().getUserName())
                                && !userOnl.getInfo().getUserName().equalsIgnoreCase(user.getInfo().getUserName())) {
                            userOnl.setRoom(room);
                            userOnl.getInfo().setStatus(1);
                            members.add(userOnl);
                        }
                    }
                }
                room.setMembers(members);
                roomVideoCalls.add(room);

                // send command join room
                sendCommand(message, members);
                // update list user online for client
                sendCommand(CommandManager.SHARE.listUser(userModels), userModels);
                break;
            case "CALL_RESPONSE":
                user.getRoom().getOwner().sendCommand(message);
                for (UserModel memb: user.getRoom().getMembers()) {
                    memb.sendCommand(message);
                }
                ResCallVideo resCallVideo = gson.fromJson(message, ResCallVideo.class);
                if (resCallVideo.getData()) {
                    user.getInfo().setStatus(2);
                } else {
                    user.getInfo().setStatus(0);
                    user.getInfo().setRoomId(0);
                    user.getRoom().getMembers().remove(user);
                    user.setRoom(null);
                }
                // update list user online for client
                sendCommand(CommandManager.SHARE.listUser(userModels), userModels);
                break;
            default:
                break;
        }
    }

    @Override
    public void clientDisconnected(UserModel userModel) {
        System.out.println("--------------------client disconnted--------------------\n"
                + userModel.getInfo().getUserName() +
                "\n----------------------------------------");
        if (userModel.getRoom() != null) {
            RoomVideoCall roomVideoCall = userModel.getRoom();
            if (userModel.getRoom().getOwner() == userModel) {
                userModel.getRoom().setOwner(null);
            } else {
                userModel.getRoom().getMembers().remove(userModel);
            }
            userModel.setRoom(null);
            roomVideoCall.checkRoomState();
        }
        userModels.remove(userModel);
        // update list user online for client
        sendCommand(CommandManager.SHARE.listUser(userModels), userModels);
    }
}
