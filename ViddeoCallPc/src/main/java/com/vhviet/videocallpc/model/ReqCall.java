package com.vhviet.videocallpc.model;

public class ReqCall {
    private String signalKey;
    private String[] data;

    public ReqCall(String signalKey, String[] data) {
        this.signalKey = signalKey;
        this.data = data;
    }

    public String getSignalKey() {
        return signalKey;
    }

    public String[] getData() {
        return data;
    }
}
