package com.vhviet.videocallpc.utils;

import java.net.URL;

public class Constants {
    //network
    public static String SIGNALING_SERVER = "localhost";//"34.87.150.46";//"192.168.25.108";
    public static String TURN_SERVER = "localhost";//"34.87.150.46";//"192.168.25.108";
    //
    public static int WIDTH = 375;
    public static int HEIGHT = 812;
    //Image
    public static URL IMG_BACKGROUND = Constants.class.getResource("/background.png");
    public static URL IMG_BTN_LOGIN = Constants.class.getResource("/ic_btn_login.png");
    public static URL IMG_BTN_RENAME = Constants.class.getResource("/ic_btn_rename.png");
    public static URL IMG_BTN_BACK = Constants.class.getResource("/ic_btn_back.png");
    public static URL IMG_ICON_AVATAR = Constants.class.getResource("/ic_avatar.png");
    public static URL IMG_ICON_CHECK = Constants.class.getResource("/ic_check.png");
    public static URL IMG_ICON_UNCHECK = Constants.class.getResource("/ic_uncheck.png");
    public static URL IMG_BTN_ENDCALL = Constants.class.getResource("/ic_btn_end_call.png");
    public static URL IMG_CALL_INCOMMING = Constants.class.getResource("/ic_call_incomming.png");
}
