package com.vhviet.videocallpc.view;

import com.google.gson.Gson;
import com.vhviet.videocallpc.model.*;
import com.vhviet.videocallpc.network.SocketManager;
import com.vhviet.videocallpc.network.VideoCallCommand;
import com.vhviet.videocallpc.utils.Constants;
import com.vhviet.videocallpc.view.call.VideoCallPanel;
import com.vhviet.videocallpc.view.callrequest.CallRequestPanel;
import com.vhviet.videocallpc.view.friend.FriendPanel;
import com.vhviet.videocallpc.view.home.HomePanel;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;

public class HomeUI extends JFrame implements HomePanel.HomePanelDelegate,
        SocketManager.SocketManagerDelegate, FriendPanel.FriendPanelDelegate, CallRequestPanel.CallRequestPanelDelegate {

    public static RqUserInfo.UserInfo MY_ACC = null;
    private ArrayList<JPanel> panels = new ArrayList<>();
    private HomePanel homePanel;
    private FriendPanel friendPanel;
    private VideoCallPanel videoCallPanel;
    private CallRequestPanel callRequestPanel;

    private RqUserInfo.UserInfo[] friendOnlines;

    public HomeUI() {
        friendOnlines = new RqUserInfo.UserInfo[0];
        initUI();
        SocketManager.SHARE.setDelegate(this);
    }

    private void initUI() {
        setTitle("Video Call");
        setSize(Constants.WIDTH, Constants.HEIGHT);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new CardLayout());

        homePanel = new HomePanel();
        homePanel.setDelegate(this);
        panels.add(homePanel);
        add(homePanel);
    }

    @Override
    public void socketDidConnected() {
        SocketManager.SHARE.sendCommand(VideoCallCommand.SHARE.login(homePanel.getUsername()));
    }

    @Override
    public void socketDisconnected(String error) {
        homePanel.setupUILogin();
    }

    @Override
    public void didReciveMessage(RequestSocketModel responces, String message) {
        Gson gson = new Gson();
        switch (responces.getSignalKey()) {
            case "LOGIN_RES":
                ResLoginModel resLoginModel = gson.fromJson(message, ResLoginModel.class);
                if (resLoginModel.getData().isEmpty()) {
                    homePanel.setupUILoginSuccess();
                }
                break;
            case "LIST_USER":
                ResListFriendOnline resListFriendOnline = gson.fromJson(message, ResListFriendOnline.class);
                ArrayList<RqUserInfo.UserInfo> fs = new ArrayList<>();
                for (int i = 0; i < resListFriendOnline.getData().length; i++) {
                    if (!resListFriendOnline.getData()[i].getUserName().equalsIgnoreCase(homePanel.getUsername())) {
                        fs.add(resListFriendOnline.getData()[i]);
                    } else {
                        MY_ACC = resListFriendOnline.getData()[i];
                    }
                }
                RqUserInfo.UserInfo[] friendOnlines = new RqUserInfo.UserInfo[fs.size()];
                for (int i = 0; i < fs.size(); i++) {
                    friendOnlines[i] = fs.get(i);
                }
                this.friendOnlines = friendOnlines;
                if (friendPanel != null) {
                    friendPanel.reloadData(friendOnlines);
                }
                if (videoCallPanel != null ) {
                    videoCallPanel.reloadVideo(friendOnlines);
                }
                break;
            case "CALL_REQUEST":
                ReqCall reqCall = gson.fromJson(message, ReqCall.class);
                callRequestPanel = new CallRequestPanel(reqCall.getData());
                callRequestPanel.setDelegate(this);
                remove(panels.get(panels.size() - 1));
                panels.add(callRequestPanel);
                add(callRequestPanel, 0);
                validate();
                break;
            default:
                break;
        }
    }

    //HomePanelDelegate
    @Override
    public void loginClicked() {
        if (SocketManager.SHARE.getSocket() != null) {
            try {
                SocketManager.SHARE.getSocket().close();
                SocketManager.SHARE.setSocket(null);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            SocketManager.SHARE.startSocket();
        }
    }

    @Override
    public void callFriendClicked() {
        if (SocketManager.SHARE.getSocket() != null) {
            friendPanel = new FriendPanel(true, friendOnlines);
            friendPanel.setDelegate(this);
            remove(homePanel);
            panels.add(friendPanel);
            add(friendPanel);
            validate();
        } else {
            JOptionPane.showMessageDialog(null, "Bạn chưa đăng nhập vào hệ thống!",
                    "Chú ý", JOptionPane.WARNING_MESSAGE);
        }
    }

    @Override
    public void callGroupClicked() {
        if (SocketManager.SHARE.getSocket() != null) {
            friendPanel = new FriendPanel(false, friendOnlines);
            friendPanel.setDelegate(this);
            remove(homePanel);
            panels.add(friendPanel);
            add(friendPanel);
            validate();
        } else {
            JOptionPane.showMessageDialog(null, "Bạn chưa đăng nhập vào hệ thống!",
                    "Chú ý", JOptionPane.WARNING_MESSAGE);
        }
    }

    //FriendPanelDelegate
    @Override
    public void btnBackClicked() {
        panels.remove(friendPanel);
        remove(friendPanel);
        friendPanel = null;
        add(homePanel);
        validate();
    }

    @Override
    public void callFriend(ArrayList<RqUserInfo.UserInfo> friends) {
        if (friends == null) {
            return;
        }
        String[] strs = new String[friends.size() + 1];
        strs[0] = HomeUI.MY_ACC.getUserName();
        for (int i = 0; i < friends.size(); i++) {
            strs[i + 1] = friends.get(i).getUserName();
        }
        SocketManager.SHARE.sendCommand(VideoCallCommand.SHARE.callRequest(strs));
        videoCallPanel = new VideoCallPanel();
        panels.add(videoCallPanel);
        remove(friendPanel);
        add(videoCallPanel);
        validate();
    }

    //CallRequestPanelDelegate
    @Override
    public void btnAcceptClicked() {
        panels.remove(callRequestPanel);
        remove(callRequestPanel);
        callRequestPanel = null;
        videoCallPanel = new VideoCallPanel();
        panels.add(videoCallPanel);
        add(videoCallPanel);
        validate();
        SocketManager.SHARE.sendCommand(VideoCallCommand.SHARE.callResponse(true));
    }

    @Override
    public void btnRejectClicked() {
        SocketManager.SHARE.sendCommand(VideoCallCommand.SHARE.callResponse(false));
        panels.remove(callRequestPanel);
        remove(callRequestPanel);
        callRequestPanel = null;
        add(panels.get(panels.size() - 1));
        validate();
    }
}
