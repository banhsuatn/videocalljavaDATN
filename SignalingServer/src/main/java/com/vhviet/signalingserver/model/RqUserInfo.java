package com.vhviet.signalingserver.model;

public class RqUserInfo {
    private String signalKey;
    private UserInfo data;

    public RqUserInfo(String signalKey, UserInfo data) {
        this.signalKey = signalKey;
        this.data = data;
    }

    public String getSignalKey() {
        return signalKey;
    }

    public void setSignalKey(String signalKey) {
        this.signalKey = signalKey;
    }

    public UserInfo getData() {
        return data;
    }

    public void setData(UserInfo data) {
        this.data = data;
    }

    public class UserInfo {
        private String userName;
        private String os;
        private boolean isWifi;
        private int roomId;
        private int status; //0-normal, 1-callincome, 2-incall

        public String getUserName() {
            return userName;
        }

        public void setUserName(String userName) {
            this.userName = userName;
        }

        public String getOs() {
            return os;
        }

        public void setOs(String os) {
            this.os = os;
        }

        public boolean getIsWifi() {
            return isWifi;
        }

        public void setIsWifi(boolean isWifi) {
            this.isWifi = isWifi;
        }

        public int getRoomId() {
            return roomId;
        }

        public void setRoomId(int roomId) {
            this.roomId = roomId;
        }

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }
    }
}
