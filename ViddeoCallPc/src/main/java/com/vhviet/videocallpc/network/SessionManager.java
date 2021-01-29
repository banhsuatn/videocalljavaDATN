package com.vhviet.videocallpc.network;

import com.vhviet.videocallpc.utils.Constants;

import java.io.IOException;
import java.net.*;

public class SessionManager {
    private SessionManagerDelegate delegate;
    private DatagramSocket socket;
    private boolean isRun = true;

    public SessionManager() {
        try {
            socket = new DatagramSocket();
            new Thread(new Runnable() {
                @Override
                public void run() {
                    byte[] buffer = new byte[20 * 1024];
                    DatagramPacket server = new DatagramPacket(buffer, buffer.length);
                    while (isRun) {
                        try {
                            socket.receive(server);
                            if (delegate != null) {
                                delegate.didReceiveData(server.getData());
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }).start();
            new Thread(new Runnable() {
                @Override
                public void run() {
                    while (isRun) {
                        pingpong();
                        try {
                            Thread.sleep(3000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }).start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void pingpong() {
        try {
            byte[] result = new byte[1];
            result[0] = 0;
            DatagramPacket request = new DatagramPacket(result, result.length, InetAddress.getByName(Constants.TURN_SERVER), 6667);
            socket.send(request);
            System.out.println("pingpong");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void sendData(byte[] data) {
        try {
            DatagramPacket request = new DatagramPacket(data, data.length, InetAddress.getByName(Constants.TURN_SERVER), 6667);
            socket.send(request);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setDelegate(SessionManagerDelegate delegate) {
        this.delegate = delegate;
    }

    public interface SessionManagerDelegate {
        void didReceiveData(byte[] data);
    }
}
