package com.vhviet.videocallpc.view.home;

import com.vhviet.videocallpc.utils.Constants;
import com.vhviet.videocallpc.utils.FontR;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class HomePanel extends JPanel {

    private HomePanelDelegate delegate;
    private JLabel background;
    private JLabel lbTitle;
    private JLabel lbUsername;
    private JTextField tfUsername;
    private JButton btnLogin;
    private JPanel bgBtn;
    private JButton btnCallFriend;
    private JButton btnCallGroup;

    public HomePanel() {
        initUI();
        initListener();
    }

    private void initUI() {
        setLayout(null);
        setBackground(Color.WHITE);
        //init
        background = new JLabel(new ImageIcon(Constants.IMG_BACKGROUND));
        background.setBounds(0, 0, Constants.WIDTH,Constants.HEIGHT);

        lbTitle = new JLabel("Video Call", SwingConstants.CENTER);
        lbTitle.setBounds(0, 100, Constants.WIDTH,50);
        lbTitle.setFont(FontR.BOLD.deriveFont(Font.PLAIN,48));
        lbTitle.setForeground(Color.WHITE);

        lbUsername = new JLabel("Tên người dùng", SwingConstants.LEADING);
        lbUsername.setBounds(16, 200, Constants.WIDTH - 16,50);
        lbUsername.setFont(FontR.MEDIUM.deriveFont(Font.PLAIN,18));
        lbUsername.setForeground(Color.WHITE);

        tfUsername = new JTextField();
        tfUsername.setBounds(16, 260, Constants.WIDTH - 110,50);
        tfUsername.setFont(FontR.MEDIUM.deriveFont(Font.PLAIN,18));
        tfUsername.setForeground(Color.WHITE);
        tfUsername.setBackground(new Color(255, 255, 255, 0));
        tfUsername.setCaretColor(Color.WHITE);
        LineBorder lineBorder = new LineBorder(Color.white, 1, true);
        tfUsername.setBorder(lineBorder);

        btnLogin = new JButton(new ImageIcon(Constants.IMG_BTN_LOGIN));
        btnLogin.setBounds(Constants.WIDTH - 85, 260, 80, 50);

        bgBtn = new JPanel();
        bgBtn.setBounds(16, Constants.HEIGHT - 200 - 64, Constants.WIDTH - 32, 200);
        bgBtn.setBackground(new Color(255, 255, 255, 50));

        btnCallFriend = new JButton("\nBạn bè");
        btnCallFriend.setBounds(65, Constants.HEIGHT - 150 - 64, 100, 100);
        btnCallFriend.setBackground(new Color(0, 0, 0, 0));

        btnCallGroup = new JButton("\nGọi nhóm");
        btnCallGroup.setBounds(Constants.WIDTH - 65 - 100, Constants.HEIGHT - 150 - 64, 100, 100);
        btnCallGroup.setBackground(new Color(0, 0, 0, 0));

        add(btnCallGroup);
        add(btnCallFriend);
        add(bgBtn);
        add(btnLogin);
        add(lbTitle);
        add(lbUsername);
        add(tfUsername);
        add(background);
    }

    private void initListener() {
        btnLogin.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (delegate != null) {
                    delegate.loginClicked();
                }
            }
        });
        btnCallFriend.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (delegate != null) {
                    delegate.callFriendClicked();
                }
            }
        });
        btnCallGroup.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (delegate != null) {
                    delegate.callGroupClicked();
                }
            }
        });
    }

    public void setupUILogin() {
        tfUsername.setBackground(new Color(255, 255, 255, 0));
        tfUsername.setEnabled(true);
        btnLogin.setIcon(new ImageIcon(Constants.IMG_BTN_LOGIN));
    }

    public void setupUILoginSuccess() {
        tfUsername.setBackground(new Color(255, 255, 255, 50));
        tfUsername.setEnabled(false);
        btnLogin.setIcon(new ImageIcon(Constants.IMG_BTN_RENAME));
    }

    public String getUsername() {
        return tfUsername.getText();
    }

    public HomePanelDelegate getDelegate() {
        return delegate;
    }

    public void setDelegate(HomePanelDelegate delegate) {
        this.delegate = delegate;
    }

    public interface HomePanelDelegate {
        void loginClicked();
        void callFriendClicked();
        void callGroupClicked();
    }
}
