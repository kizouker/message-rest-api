package com.storytel.model;

public class Message {

    private final long id;
    private final String msg;

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