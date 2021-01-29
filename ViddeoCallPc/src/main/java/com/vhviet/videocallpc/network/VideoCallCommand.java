package com.vhviet.videocallpc.network;

import com.google.gson.Gson;

public class VideoCallCommand {
    public static VideoCallCommand SHARE = new VideoCallCommand();

    public String login(String username) {
        return "{\"signalKey\":\"LOGIN\",\"data\":{\"userName\":\"" + username + "\",\"os\":\"PC\",\"isWifi\":\"true\"}}";
    }

    public String callRequest(String[] list) {
        Gson gson = new Gson();
        String str = gson.toJson(list);

        if (str != null && !str.isEmpty()){
            return "{\"signalKey\":\"CALL_REQUEST\",\"data\": " + str + " }\n";
        }
        return "";
    }

    public String callResponse(boolean accept) {
        Gson gson = new Gson();
        String str = gson.toJson(accept);
        if (str != null && !str.isEmpty()){
            return "{\"signalKey\":\"CALL_RESPONSE\",\"data\": " + str + " }\n";
        }
        return "";
    }
}