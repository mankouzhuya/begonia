package com.github.begonia.communication.protocol;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

@AllArgsConstructor
@ToString
@Getter
@Setter
public class YqnPacket implements Serializable {

    public static final Integer TYPE_HEART = 1;

    public static final Integer TYPE_TRACK = 10;

    private Integer type;

    private String body;

    private YqnPacket() {
    }

    public static void main(String[] args) {
        byte[] data = "smart-socket".getBytes();
        byte[] data2 = "smart-so cket".getBytes();
        System.out.println(data);
        System.out.println(data2);
    }


}
