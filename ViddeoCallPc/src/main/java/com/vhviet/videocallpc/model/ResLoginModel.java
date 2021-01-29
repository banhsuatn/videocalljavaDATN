package com.vhviet.videocallpc.model;

public class ResLoginModel {
    private String signalKey;
    private String data;

    public ResLoginModel() {
    }

    public ResLoginModel(String signalKey, String data) {
        this.signalKey = signalKey;
        this.data = data;
    }

    public String getSignalKey() {
        return signalKey;
    }

    public void setSignalKey(String signalKey) {
        this.signalKey = signalKey;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }
}
