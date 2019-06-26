package com.github.begonia.communication.protocol;

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

}
