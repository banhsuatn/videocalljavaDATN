package com.vhviet.signalingserver.ultis;

import com.google.gson.Gson;
import com.vhviet.signalingserver.model.RequestSocketModel;
import com.vhviet.signalingserver.model.RqUserInfo;
import com.vhviet.signalingserver.model.UserModel;

import java.util.ArrayList;

public class CommandManager {
    public static CommandManager SHARE = new CommandManager();
    private Gson gson = new Gson();

    private CommandManager() {
    }

    public String loginRes(String msg) {
        RequestSocketModel requestSocketModel = new RequestSocketModel("LOGIN_RES", msg);
        return gson.toJson(requestSocketModel);
    }

    public String listUser(ArrayList<UserModel> userModels) {
        ArrayList<RqUserInfo.UserInfo> infos = new ArrayList<>();
        for (UserModel userModel: userModels) {
            if (userModel.getInfo() != null) {
                infos.add(userModel.getInfo());
            }
        }
        RequestSocketModel requestSocketModel = new RequestSocketModel("LIST_USER", infos);
        return gson.toJson(requestSocketModel);
    }

    public String roomClosed(String msg) {
        RequestSocketModel requestSocketModel = new RequestSocketModel("ROOM_CLOSED", msg);
        return gson.toJson(requestSocketModel);
    }

    public String requestJoinRoom(ArrayList<UserModel> members) {
        String[] users = new String[members.size()];

        for (int i = 0; i < members.size(); i++) {
            users[i] = members.get(i).getInfo().getUserName();
        }
        return gson.toJson(users);
    }
}
