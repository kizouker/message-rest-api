package com.storytel;

import java.util.Date;

public class Message {

    private final long id;
    private final String msg;
    private Date date;
    private String Client;

    public Message(long id, String msg) {

        this.msg = msg;
        this.id = id;
    }
    public long getId() {
        return id;
    }

    public String getMsg() {
        return msg;
    }
}