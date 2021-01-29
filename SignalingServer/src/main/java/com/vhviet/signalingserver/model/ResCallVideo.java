package com.vhviet.signalingserver.model;

public class ResCallVideo {
    private String signalKey;
    private Boolean data;

    public ResCallVideo(String signalKey, Boolean data) {
        this.signalKey = signalKey;
        this.data = data;
    }

    public String getSignalKey() {
        return signalKey;
    }

    public void setSignalKey(String signalKey) {
        this.signalKey = signalKey;
    }

    public Boolean getData() {
        return data;
    }

    public void setData(Boolean data) {
        this.data = data;
    }
}
