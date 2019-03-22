package com.storytel.model;

import com.google.gson.Gson;
import com.storytel.exception.ResourceNotFoundException;
import org.springframework.web.bind.MissingServletRequestParameterException;

import java.util.Collection;
import java.util.HashMap;

public class Messages {

    private HashMap<Long, Message> messages = new HashMap(); // Choosing hashmap - cheap and easy to find the messages

    public Message setMessage(Long id, Message message) {
        this.messages.put(id, message);
        return this.messages.get(id);
    }

    public Message getMessage(Long id) throws ResourceNotFoundException {

        Message msg = this.messages.get(id);
        String cause = "Message with ID=" + id + " does not exist";

        if (msg == null) {
            throw new ResourceNotFoundException(cause);
        } else {
            return msg;
        }
    }

    public String removeMessage(Long id) throws ResourceNotFoundException {
        Message msg = this.messages.get(id);
        String cause = "Message with ID=" + id + " does not exist, so it cannot be removed.";

        if (msg == null) {
            throw new ResourceNotFoundException(cause);
        } else {
            this.messages.remove(id);
            return listMessages();
        }
    }

    public String listMessages() {
        Collection coll = this.messages.values();
        Gson gson = new Gson();
        return gson.toJson(coll);
    }

}
