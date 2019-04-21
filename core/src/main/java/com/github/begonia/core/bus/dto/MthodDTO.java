package com.github.begonia.core.bus.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class MthodDTO implements Serializable{

    private String trackId;

    private String mthodId;

    private String fromMethodId;

    private String methodName;

    private String agrs;

    private String result;

    private Integer rt;

}
