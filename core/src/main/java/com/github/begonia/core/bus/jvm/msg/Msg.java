package com.github.begonia.core.bus.jvm.msg;

import java.io.Serializable;

public class Msg implements Serializable {

    public Integer type;

    public static final Integer MSG_TYPE_METHOD_NODE = 10;

    public Object msgBody;

    public Msg(Integer type, Object msgBody) {
        this.type = type;
        this.msgBody = msgBody;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Object getMsgBody() {
        return msgBody;
    }

    public void setMsgBody(Object msgBody) {
        this.msgBody = msgBody;
    }
}
