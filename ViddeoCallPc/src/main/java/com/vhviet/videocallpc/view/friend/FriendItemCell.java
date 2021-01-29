package com.vhviet.videocallpc.view.friend;

import com.vhviet.videocallpc.model.RqUserInfo;
import com.vhviet.videocallpc.utils.Constants;
import com.vhviet.videocallpc.utils.FontR;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class FriendItemCell extends JPanel {

    private FriendItemCellDelegate delegate;
    private JPanel bg;
    private JLabel imgAvatar;
    private JLabel lbTitle;
    private JLabel lbCheck;

    private RqUserInfo.UserInfo userInfo;

    public FriendItemCell() {
    }

    public void initUI(RqUserInfo.UserInfo userInfo) {
        this.userInfo = userInfo;
        setLayout(null);
        setBackground(new Color(255,255,255,0));

        bg = new JPanel();
        bg.setBounds(0,5, getWidth(), 60);
        bg.setBackground(new Color(255,255,255,50));

        imgAvatar = new JLabel(new ImageIcon(Constants.IMG_ICON_AVATAR));
        imgAvatar.setBounds(13,13, 44, 44);

        lbTitle = new JLabel(userInfo.getUserName());
        lbTitle.setBounds(70,13, getWidth() - 120, 44);
        lbTitle.setFont(FontR.MEDIUM.deriveFont(Font.PLAIN,18));
        lbTitle.setForeground(Color.WHITE);

        lbCheck = new JLabel(userInfo.isCheck ? new ImageIcon(Constants.IMG_ICON_CHECK) : new ImageIcon(Constants.IMG_ICON_UNCHECK));
        lbCheck.setBounds(getWidth() - 30,25, 20, 20);

        add(lbCheck);
        add(lbTitle);
        add(imgAvatar);
        add(bg);
        initListener();
    }

    private void initListener() {
        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                super.mousePressed(e);
                userInfo.isCheck = !userInfo.isCheck;
                if (delegate != null) {
                    delegate.cellClicked();
                }
            }
        });
    }

    public void setDelegate(FriendItemCellDelegate delegate) {
        this.delegate = delegate;
    }

    public interface FriendItemCellDelegate {
        void cellClicked();
    }
}
