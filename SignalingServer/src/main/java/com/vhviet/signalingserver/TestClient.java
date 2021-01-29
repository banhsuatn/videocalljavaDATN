package com.vhviet.signalingserver;

import java.io.*;
import java.net.Socket;

public class TestClient {
    public static void main(String[] args) throws IOException, InterruptedException {
        Socket socket = new Socket("localhost", 6666);

        System.out.println("conneted to server " +socket.getInetAddress());

        InputStream input = socket.getInputStream();
        OutputStream output = socket.getOutputStream();

        PrintWriter writer = new PrintWriter(output, true);
        BufferedReader reader = new BufferedReader(new InputStreamReader(input));

        String line;

        writer.println("{\"signalKey\":\"LOGIN\",\"data\":{\"userName\":\"vhviet\",\"os\":\"IOS\",\"isWifi\":true}}");

        boolean isf = true;
        while ((line = reader.readLine()) != null) {
            System.out.println("server say: " + line);
            if (line.contains("vhviet2") && isf) {
                isf = false;
//                Thread.sleep(5000);
//                writer.println("{\"signalKey\":\"CALL_REQUEST\",\"data\":[\"vhviet2\"]}");
            }
        }
        System.out.println("socket closed");
    }
}
