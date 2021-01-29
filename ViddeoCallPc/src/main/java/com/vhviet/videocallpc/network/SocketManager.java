package com.vhviet.videocallpc.network;

import com.google.gson.Gson;
import com.vhviet.videocallpc.model.RequestSocketModel;
import com.vhviet.videocallpc.utils.Constants;

import java.io.*;
import java.net.Socket;

public class SocketManager {
    static public SocketManager SHARE = new SocketManager();
    private Socket socket;
    private InputStream input;
    private OutputStream output;
    private PrintWriter writer;
    private BufferedReader reader;
    private SocketManagerDelegate delegate;

    public void startSocket() {
        try {
            socket = new Socket(Constants.SIGNALING_SERVER, 6666);
            System.out.println("conneted to server " + socket.getInetAddress());

            input = socket.getInputStream();
            output = socket.getOutputStream();

            writer = new PrintWriter(output, true);
            reader = new BufferedReader(new InputStreamReader(input));

            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        String line;
                        while ((line = reader.readLine()) != null) {
                            Gson gson = new Gson();
                            System.out.println("--------------------server send--------------------\n" + line + "\n----------------------------------------");
                            RequestSocketModel res = gson.fromJson(line, RequestSocketModel.class);
                            if (delegate != null) {
                                delegate.didReciveMessage(res, line);
                            }
                        }
                        if (delegate != null) {
                            delegate.socketDisconnected("Server disconnected");
                        }
                    } catch (Exception e) {
                        if (delegate != null) {
                            delegate.socketDisconnected("Server disconnected");
                        }
                    }
                }
            }).start();

            if (delegate != null) {
                delegate.socketDidConnected();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void sendCommand(String message) {
        if (writer != null) {
            writer.println(message);
        }
    }

    public Socket getSocket() {
        return socket;
    }

    public void setSocket(Socket socket) {
        this.socket = socket;
    }

    public SocketManagerDelegate getDelegate() {
        return delegate;
    }

    public void setDelegate(SocketManagerDelegate delegate) {
        this.delegate = delegate;
    }

    public interface SocketManagerDelegate {
        void socketDidConnected();
        void socketDisconnected(String error);
        void didReciveMessage(RequestSocketModel responces, String message);
    }
}
