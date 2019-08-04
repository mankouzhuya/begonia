package com.github.begonia.communication.protocol;

import java.io.IOException;
import java.io.Serializable;

public abstract class Packet implements Serializable {

    public static final int HEADER_LENGHT = 8;//消息头的长度 1+1+4+2

    //消息包:
    /***
     *
     * 消息:  [byte]  |   [byte]  |  [4]  | [2] | 业务数据
     *      magic num | command | msg leg | [预留]  | biz data
     *        magicNum = -128<-></->127
     *        salt = uuid
     *        sign = md5([migicNum+salt+bizData)
     *
     * **/

    private byte magicNum;

    private byte type;//消息类型

    private byte[] length;//长度 4

    private byte[] reserve;//预留 2

    private byte[] data;//data ???

    public static void main(String[] args) throws IOException {
        Integer a = 12323556;
        byte[] bytes = writeInt(a);

        System.out.println(byte2Int(bytes));
    }

    public static byte[] writeInt(int v) throws IOException {
        byte[] cacheByte = new byte[4];
        cacheByte[0] = (byte) ((v >>> 24) & 0xFF);
        cacheByte[1] = (byte) ((v >>> 16) & 0xFF);
        cacheByte[2] = (byte) ((v >>> 8) & 0xFF);
        cacheByte[3] = (byte) ((v >>> 0) & 0xFF);
        return cacheByte;
    }

    public static int byte2Int(byte[] bytes) {
        return (bytes[0] & 0xff) << 24
                | (bytes[1] & 0xff) << 16
                | (bytes[2] & 0xff) << 8
                | (bytes[3] & 0xff);
    }

}
