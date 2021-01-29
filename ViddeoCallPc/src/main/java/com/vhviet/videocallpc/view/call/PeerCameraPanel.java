package com.vhviet.videocallpc.view.call;

import com.vhviet.videocallpc.model.RqUserInfo;

import javax.swing.*;
import java.awt.*;

public class PeerCameraPanel extends JPanel {

    private RqUserInfo.UserInfo user;
    private JLabel videoView;

    public PeerCameraPanel(RqUserInfo.UserInfo user) {
        this.user = user;
        initUI();
    }

    private void initUI() {
        videoView = new JLabel();
        add(videoView);
    }

    private void initListener() {

    }

    public void setIcon(ImageIcon icon) {
        videoView.setIcon(icon);
        if (icon.getIconHeight() > icon.getIconWidth()) {
            int x = 0;
            int y = (getHeight() - icon.getIconHeight())/2;
            int w = icon.getIconWidth();
            int h = icon.getIconHeight();
            videoView.setBounds(x,y,w,h);
        } else {
            int x = (getWidth() - icon.getIconWidth())/2;
            int y = 0;
            int w = icon.getIconWidth();
            int h = icon.getIconHeight();
            videoView.setBounds(x,y,w,h);
        }
        repaint();
    }

    public RqUserInfo.UserInfo getUser() {
        return user;
    }
}
