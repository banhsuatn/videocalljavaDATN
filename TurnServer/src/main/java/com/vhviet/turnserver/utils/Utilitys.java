package com.vhviet.turnserver.utils;

import java.nio.ByteBuffer;

public class Utilitys {
    static public Utilitys SHARE = new Utilitys();

    public int fromByteArray(byte[] bytes) {
        return ByteBuffer.wrap(bytes).getInt();
    }
}
