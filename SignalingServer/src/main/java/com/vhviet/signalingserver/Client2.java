package com.vhviet.signalingserver;

import java.io.*;
import java.net.Socket;

public class Client2 {
    public static void main(String[] args) throws IOException, InterruptedException {
        Socket socket = new Socket("34.87.150.46", 6666);

        System.out.println("conneted to server " +socket.getInetAddress());

        InputStream input = socket.getInputStream();
        OutputStream output = socket.getOutputStream();

        PrintWriter writer = new PrintWriter(output, true);
        BufferedReader reader = new BufferedReader(new InputStreamReader(input));

        String line;

        writer.println("{\"signalKey\":\"LOGIN\",\"data\":{\"userName\":\"vhviet2\",\"os\":\"IOS\",\"isWifi\":true}}");

        while ((line = reader.readLine()) != null) {
            System.out.println("server say: " + line);
            if (line.contains("CALL_REQUEST")) {
                Thread.sleep(5000);
                writer.println("{\"signalKey\":\"CALL_RESPONSE\",\"data\": true}");
            }
        }
        System.out.println("socket closed");
    }
}
