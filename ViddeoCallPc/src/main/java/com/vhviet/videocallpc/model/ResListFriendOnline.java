package com.vhviet.videocallpc.model;

public class ResListFriendOnline {

    private String signalKey;
    private RqUserInfo.UserInfo[] data;

    public ResListFriendOnline(String signalKey, RqUserInfo.UserInfo[] data) {
        this.signalKey = signalKey;
        this.data = data;
    }

    public String getSignalKey() {
        return signalKey;
    }

    public void setSignalKey(String signalKey) {
        this.signalKey = signalKey;
    }

    public RqUserInfo.UserInfo[] getData() {
        return data;
    }

    public void setData(RqUserInfo.UserInfo[] data) {
        this.data = data;
    }
}
