package com.vhviet.signalingserver.model;

public class RequestSocketModel {
    private String signalKey;
    private Object data;

    public RequestSocketModel() {
    }

    public RequestSocketModel(String signalKey, Object data) {
        this.signalKey = signalKey;
        this.data = data;
    }

    public String getSignalKey() {
        return signalKey;
    }

    public void setSignalKey(String signalKey) {
        this.signalKey = signalKey;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}
