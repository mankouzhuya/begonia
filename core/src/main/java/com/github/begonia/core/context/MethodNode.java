package com.github.begonia.core.context;

import lombok.Data;
import lombok.ToString;
import java.io.Serializable;
import java.util.List;

@ToString
@Data
public class MethodNode implements Serializable {

    private String methodId;//id

    private String methodFullName;//全限定名

    private List<String> args;//方法入参


    private String returnInfo;//返回信息

    private Integer excuteTime;//执行时间
}
