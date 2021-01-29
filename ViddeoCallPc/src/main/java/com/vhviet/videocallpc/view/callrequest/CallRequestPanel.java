package com.vhviet.videocallpc.view.callrequest;

import com.vhviet.videocallpc.utils.Constants;
import com.vhviet.videocallpc.utils.FontR;
import com.vhviet.videocallpc.view.HomeUI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class CallRequestPanel extends JPanel {

    private CallRequestPanelDelegate delegate;
    private JLabel background;
    private JButton btnAccept;
    private JButton btnReject;
    private JLabel bgVideo;

    private String[] usernames;

    public CallRequestPanel(String[] usernames) {
        this.usernames = usernames;
        initUI();
        initListener();
    }

    private void initUI() {
        setLayout(null);
        setBackground(new Color(255, 255, 255, 50));

        background = new JLabel(new ImageIcon(Constants.IMG_BACKGROUND));
        background.setBounds(0, 0, Constants.WIDTH, Constants.HEIGHT);

        btnAccept = new JButton("Chấp nhận");
        btnAccept.setBounds(50, Constants.HEIGHT - 250, 100, 100);

        btnReject = new JButton("Từ chối");
        btnReject.setBounds(Constants.WIDTH - 50 - 100, Constants.HEIGHT - 250, 100, 100);

        bgVideo = new JLabel(new ImageIcon(Constants.IMG_CALL_INCOMMING));
        bgVideo.setBounds((Constants.WIDTH - 150) / 2, (Constants.HEIGHT - 150) / 2 - 50 - usernames.length * 50, 150, 150);

        int line = 0;
        for (int i = 0; i < usernames.length; i++) {
            if (!usernames[i].equalsIgnoreCase(HomeUI.MY_ACC.getUserName())) {
                JLabel name = new JLabel(usernames[i], SwingConstants.CENTER);
                name.setBounds(0, (Constants.HEIGHT - 150) / 2 + 100 - line * 50, Constants.WIDTH, 50);
                name.setFont(FontR.BOLD.deriveFont(Font.PLAIN, 48));
                name.setForeground(Color.WHITE);
                add(name);
                line ++;
            }
        }
        add(bgVideo);
        add(btnAccept);
        add(btnReject);
        add(background);
    }

    private void initListener() {
        btnAccept.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (delegate != null) {
                    delegate.btnAcceptClicked();
                }
            }
        });
        btnReject.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (delegate != null) {
                    delegate.btnRejectClicked();
                }
            }
        });
    }

    public void setDelegate(CallRequestPanelDelegate delegate) {
        this.delegate = delegate;
    }

    public interface CallRequestPanelDelegate {
        void btnAcceptClicked();

        void btnRejectClicked();
    }
}
