package com.vhviet.videocallpc.view.call;

import com.vhviet.videocallpc.model.RqUserInfo;
import com.vhviet.videocallpc.network.SessionManager;
import com.vhviet.videocallpc.utils.Constants;
import com.vhviet.videocallpc.utils.Utils;
import com.vhviet.videocallpc.view.HomeUI;
import org.bytedeco.javacv.*;
import org.bytedeco.javacv.Frame;

import javax.sound.sampled.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.*;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.ShortBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import static org.bytedeco.ffmpeg.global.avcodec.AV_CODEC_ID_AAC;
import static org.bytedeco.ffmpeg.global.avcodec.AV_CODEC_ID_H265;
import static org.bytedeco.flycapture.global.FlyCapture2.FRAME_RATE;

public class VideoCallPanel extends JPanel implements SessionManager.SessionManagerDelegate {

    private JButton btnEndCall;
    private PeerCameraPanel mainCameraView;
    private PeerCameraPanel localCameraView;
    private ArrayList<PeerCameraPanel> peerCameraPanels = new ArrayList<>();
    private boolean isRun = true;
    private ArrayList<Frame> framesLocal = new ArrayList<>();
    private ArrayList<RqUserInfo.UserInfo> friendCalls = new ArrayList<>();
    private SessionManager sessionManager;
    private RqUserInfo.UserInfo currentUserMain = null;

    public VideoCallPanel() {
        initUI();
        initListener();
        initCamera();
        initMic();
        sessionManager = new SessionManager();
        sessionManager.setDelegate(this);
    }

    private void initUI() {
        setLayout(null);
        setBackground(Color.BLACK);

        mainCameraView = new PeerCameraPanel(null);
        mainCameraView.setBounds(0, (Constants.HEIGHT - Constants.WIDTH * 720 / 1280) / 2,
                Constants.WIDTH, Constants.WIDTH * 720 / 1280);

        btnEndCall = new JButton(new ImageIcon(Constants.IMG_BTN_ENDCALL));
        btnEndCall.setBounds(Constants.WIDTH / 2 - 56 / 2, Constants.HEIGHT - 150, 60, 60);
        btnEndCall.setBackground(new Color(0, 0, 0, 0));
        btnEndCall.setOpaque(false);
        btnEndCall.setContentAreaFilled(false);
        btnEndCall.setBorderPainted(false);

        localCameraView = new PeerCameraPanel(HomeUI.MY_ACC);
        int w = Constants.WIDTH / 4;
        localCameraView.setBounds(20, Constants.HEIGHT - 200 - w, w, w);

        add(localCameraView);
        add(btnEndCall);
        add(mainCameraView);
    }

    private void initCamera() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    OpenCVFrameGrabber grabber = new OpenCVFrameGrabber(0);
                    grabber.start();
                    while (isRun) {
                        int time = (int) ((new Date().getTime()) % 10000000);
                        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                        FrameRecorder recorder = new FFmpegFrameRecorder(outputStream, grabber.getImageWidth(), grabber.getImageHeight());
                        recorder.setVideoCodec(AV_CODEC_ID_H265);
                        recorder.setFormat("matroska");
                        recorder.start();
                        Frame frame = grabber.grab();
                        recorder.record(frame);
                        genarateViewLocal(frame);
                        recorder.stop();
                        sessionManager.sendData(makeHeader(outputStream.toByteArray(), true, time));
                    }
                    grabber.stop();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    private void initMic() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    TargetDataLine line = null;
                    Mixer.Info[] mixerInfo = AudioSystem.getMixerInfo();
                    for (int i = 0; i < mixerInfo.length; i++) {
                        Mixer mixer = AudioSystem.getMixer(mixerInfo[i]);
                        Line.Info[] targetLineInfo = mixer.getTargetLineInfo();
                        if (targetLineInfo.length > 0) {
                            line = (TargetDataLine) mixer.getLine(targetLineInfo[0]);
                            break;
                        }
                    }
                    if (line == null)
                        throw new UnsupportedOperationException("No recording device found");
                    AudioFormat af = new AudioFormat(8000, 8, 1, true, false);
                    line.open(af);
                    while (isRun) {
                        int time = (int) ((new Date().getTime()) % 10000000);
                        line.start();
                        ByteArrayOutputStream baos = new ByteArrayOutputStream();
                        byte[] buf = new byte[(int) af.getSampleRate() * af.getFrameSize()];
                        baos.write(buf, 0, line.read(buf, 0, buf.length));
                        sessionManager.sendData(makeHeader(baos.toByteArray(), false, time));
                        line.stop();
                        baos.close();
                    }
                    line.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    public BufferedImage resize(BufferedImage img, int newW, int newH) {
        Image tmp = img.getScaledInstance(newW, newH, Image.SCALE_SMOOTH);
        BufferedImage dimg = new BufferedImage(newW, newH, BufferedImage.TYPE_INT_ARGB);

        Graphics2D g2d = dimg.createGraphics();
        g2d.drawImage(tmp, 0, 0, null);
        g2d.dispose();

        return dimg;
    }

    public void genarateViewLocal(final Frame frame) {
        if (framesLocal.size() < 2) {
            framesLocal.add(0, frame);
        } else {
            System.out.println("drop frame");
            return;
        }
        new Thread(new Runnable() {
            @Override
            public void run() {
                Frame f = framesLocal.get(framesLocal.size() - 1);
                BufferedImage bfImage = resize(new Java2DFrameConverter().getBufferedImage(f),
                        localCameraView.getWidth() * 1280 / 720, localCameraView.getHeight());
                localCameraView.setIcon(new ImageIcon(bfImage));
                framesLocal.remove(framesLocal.size() - 1);
            }
        }).start();
    }

    int framerate = 0;
    long timeframerate = 0;

    public void showVideoCallPeer(final byte[] data, PeerCameraPanel panel, int w, int h, boolean isIos, long time) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    ByteArrayInputStream inputStream = new ByteArrayInputStream(data);
                    FFmpegFrameGrabber grabber = new FFmpegFrameGrabber(inputStream);
                    if (isIos) {
                        grabber.setFormat("mp4");
                        grabber.setImageWidth(w);
                        grabber.setImageHeight(h);
                    }
                    grabber.start();
                    Frame frame = grabber.grabImage();
                    framerate++;
                    if (new Date().getTime() - timeframerate > 1000) {
                        timeframerate = new Date().getTime();
                        System.out.println(framerate);
                        framerate = 0;
                    }
                    if (w > h) {
                        BufferedImage bi = resize(new Java2DFrameConverter().getBufferedImage(frame),
                                panel.getHeight() * w / h, panel.getHeight());
                        panel.setIcon(new ImageIcon(bi));
                    } else {
                        BufferedImage bi = resize(new Java2DFrameConverter().getBufferedImage(frame),
                                panel.getWidth(), panel.getWidth() * h / w);
                        panel.setIcon(new ImageIcon(bi));
                    }
                    grabber.stop();
                } catch (FrameGrabber.Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    private void initListener() {
        btnEndCall.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

            }
        });
    }

    private byte[] makeHeader(byte[] data, boolean isVideo, int time) throws Exception {
        byte[] result = new byte[20 * 1024];
        byte[] type = {1};
        byte[] id = Utils.integerToFourBytes(HomeUI.MY_ACC.getRoomId());
        byte[] user = HomeUI.MY_ACC.getUserName().getBytes();
        byte[] mediaType = {(byte) (isVideo ? 0 : 1)};
        System.out.println(mediaType);
        byte[] os = {0}; //java pc
        byte[] w = Utils.integerToTwoBytes(1280);
        byte[] h = Utils.integerToTwoBytes(720);
        byte[] t = Utils.integerToFourBytes(time);

        System.arraycopy(type, 0, result, 0, 1);
        System.arraycopy(id, 0, result, 1, 4);
        System.arraycopy(user, 0, result, 5, user.length);
        System.arraycopy(mediaType, 0, result, 21, 1);
        System.arraycopy(os, 0, result, 22, 1);
        System.arraycopy(w, 0, result, 23, 2);
        System.arraycopy(h, 0, result, 25, 2);
        System.arraycopy(t, 0, result, 27, 4);
        System.arraycopy(data, 0, result, 32, data.length);
        return result;
    }

    public void reloadVideo(RqUserInfo.UserInfo[] friends) {
        friendCalls.clear();
        for (PeerCameraPanel peer : peerCameraPanels) {
            remove(peer);
        }
        peerCameraPanels.clear();
        for (int i = 0; i < friends.length; i++) {
            if (friends[i].getRoomId() == HomeUI.MY_ACC.getRoomId()
                    && !friends[i].getUserName().equalsIgnoreCase(HomeUI.MY_ACC.getUserName())) {
                friendCalls.add(friends[i]);
                currentUserMain = friendCalls.get(0);
                peerCameraPanels.add(new PeerCameraPanel(friends[i]));
            }
        }
        int v = 0;
        int w = Constants.WIDTH / 4;
        for (int i = 0; i < peerCameraPanels.size(); i++) {
            add(peerCameraPanels.get(i));
            if (currentUserMain == null || !peerCameraPanels.get(i).getUser()
                    .getUserName().equalsIgnoreCase(currentUserMain.getUserName())) {
                peerCameraPanels.get(i).setBounds(localCameraView.getWidth() +
                                localCameraView.getX() + 20 * (v + 1) + v * w,
                        localCameraView.getY(), w, w);
                v++;
            }
        }
        repaint();
    }

    private void playAudio(byte[] data, AudioFormat format, long time) {
        SourceDataLine line;
        DataLine.Info info = new DataLine.Info(SourceDataLine.class, format);
        try {
            line = (SourceDataLine) AudioSystem.getLine(info);
            line.open(format);
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }
        line.start();
        line.write(data, 0, data.length);
        line.drain();
        line.close();
        System.out.println("start audio " + time);
        System.out.println("time end audio " + new Date().getTime());
    }

    @Override
    public void didReceiveData(byte[] data) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    String username = new String(Arrays.copyOfRange(data, 5, 21)).trim();
                    boolean isVideo = Utils.oneByteToInteger(Arrays.copyOfRange(data, 21, 22)[0]) == 0;
                    long t = Utils.fourBytesToLong(Arrays.copyOfRange(data, 27, 32));
                    if (isVideo) {
                        int w = Utils.twoBytesToInteger(Arrays.copyOfRange(data, 23, 25));
                        int h = Utils.twoBytesToInteger(Arrays.copyOfRange(data, 25, 27));
                        boolean isIos = Utils.oneByteToInteger(Arrays.copyOfRange(data, 22, 23)[0]) == 2;
                        PeerCameraPanel peerView = null;
                        for (PeerCameraPanel peer : peerCameraPanels) {
                            if (peer.getUser().getUserName().equalsIgnoreCase(username)) {
                                peerView = peer;
                            }
                        }
                        if (currentUserMain != null && currentUserMain.getUserName().equalsIgnoreCase(username)) {
                            byte[] mediaData = Arrays.copyOfRange(data, 32, data.length);
                            peerView.setBounds(0, 0, 0, 0);
                            showVideoCallPeer(mediaData, mainCameraView, w, h, isIos, t);
                        } else {
                            for (int i = 0; i < peerCameraPanels.size(); i++) {
                                if (peerCameraPanels.get(i).getUser().getUserName().equalsIgnoreCase(username)) {
                                    byte[] mediaData = Arrays.copyOfRange(data, 32, data.length);
                                    showVideoCallPeer(mediaData, peerCameraPanels.get(i), w, h, isIos, t);
                                }
                            }
                        }
                    } else {
                        byte[] mediaData = Arrays.copyOfRange(data, 32, data.length - 33);
                        AudioFormat af = new AudioFormat(8000, 8, 1, true, false);
                        playAudio(mediaData, af, t);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
}
