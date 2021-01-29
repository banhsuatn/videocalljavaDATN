package com.vhviet.videocallpc.view.friend;

import com.vhviet.videocallpc.model.RqUserInfo;
import com.vhviet.videocallpc.utils.Constants;
import com.vhviet.videocallpc.utils.FontR;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class FriendPanel extends JPanel implements FriendItemCell.FriendItemCellDelegate {

    private FriendPanelDelegate delegate;
    private JLabel background;
    private JButton btnBack;
    private JLabel lbTitle;
    private JButton btnCall;
    private ArrayList<FriendItemCell> friendItemCells;

    private boolean isOneCall;
    private RqUserInfo.UserInfo[] friendOnlines;

    public FriendPanel(boolean isOneCall, RqUserInfo.UserInfo[] friendOnlines) {
        this.isOneCall = isOneCall;
        this.friendOnlines = friendOnlines;
        friendItemCells = new ArrayList<>();
        initUI();
        initListener();
        requestFocus();
    }

    private void initUI() {
        setLayout(null);
        setBackground(Color.WHITE);

        background = new JLabel(new ImageIcon(Constants.IMG_BACKGROUND));
        background.setBounds(0, 0, Constants.WIDTH,Constants.HEIGHT);

        btnBack = new JButton(new ImageIcon(Constants.IMG_BTN_BACK));
        btnBack.setBounds(0, 0, 50, 50);
        btnBack.setBackground(new Color(0, 0, 0, 0));
        btnBack.setOpaque(false);
        btnBack.setContentAreaFilled(false);
        btnBack.setBorderPainted(false);

        lbTitle = new JLabel("Chọn bạn bè", SwingConstants.CENTER);
        lbTitle.setBounds(50, 0, Constants.WIDTH - 100, 50);
        lbTitle.setFont(FontR.MEDIUM.deriveFont(Font.PLAIN,18));
        lbTitle.setForeground(Color.WHITE);

        btnCall = new JButton("Video Call");
        btnCall.setBounds(50, Constants.HEIGHT - 100, Constants.WIDTH - 100, 50);
        btnCall.setFont(FontR.MEDIUM.deriveFont(Font.PLAIN,18));

        add(btnCall);
        add(lbTitle);
        add(btnBack);
        add(background);

        reloadData(friendOnlines);
    }

    private void initListener() {
        btnBack.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (delegate != null) {
                    delegate.btnBackClicked();
                }
            }
        });
        btnCall.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (delegate != null) {
                    ArrayList<RqUserInfo.UserInfo> friends = new ArrayList<>();
                    for (int i =0; i < friendOnlines.length; i++) {
                        if (friendOnlines[i].isCheck) {
                            friends.add(friendOnlines[i]);
                        }
                    }
                    delegate.callFriend(friends);
                }
            }
        });
    }

    public void reloadData(RqUserInfo.UserInfo[] friendOnlines) {
        this.friendOnlines = friendOnlines;
        for(FriendItemCell cell: friendItemCells) {
            remove(cell);
        }
        friendItemCells.clear();
        for (int i = 0; i < friendOnlines.length; i++) {
            FriendItemCell friendItemCell = new FriendItemCell();
            friendItemCell.setBounds(30, 75 + i*70, Constants.WIDTH - 60, 70);
            friendItemCell.setDelegate(this);
            friendItemCell.initUI(friendOnlines[i]);
            add(friendItemCell, 0);
            friendItemCells.add(friendItemCell);
        }
        revalidate();
        repaint();
    }

    @Override
    public void cellClicked() {
        reloadData(friendOnlines);
    }

    public void setDelegate(FriendPanelDelegate delegate) {
        this.delegate = delegate;
    }

    public interface FriendPanelDelegate {
        void btnBackClicked();
        void callFriend(ArrayList<RqUserInfo.UserInfo> friends);
    }
}
