package com.storytel;

import java.util.Collection;
import java.util.HashMap;

public class Messages {

    private HashMap messages = new HashMap();

    public void setMessage(long id, Message msg) {
        this.messages.put(id, msg);
    }

    public Message getMessage(Long id) {
        return (Message) this.messages.get(id);
    }

    public  Collection<Message> removeMessage(Long id) {
        this.messages.remove(id);
        return this.messages.values();
    }

    public Collection<Message> listMessages() {
        return this.messages.values();
    }

}
