package com.storytel.model;

import com.storytel.exception.ResourceNotFoundException;
import org.springframework.web.bind.MissingServletRequestParameterException;

import javax.json.Json;
import javax.json.stream.JsonGenerator;
import javax.json.stream.JsonGeneratorFactory;
import java.io.StringWriter;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class Messages {

    private HashMap<Long, Message> messages = new HashMap(); // Choosing hashmap - cheap and easy to find the messages

    public void setMessage(Long id, Message message) {

        this.messages.put(id, message);
    }

    public Message getMessage(Long id) throws ResourceNotFoundException, MissingServletRequestParameterException {
        if (id ==  null){

            throw new MissingServletRequestParameterException("ID", "Long");
        }
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
            return allMessages();
        }
    }

    public String listMessages() {
           return allMessages();
    }


    public String allMessages() {
        Collection coll = this.messages.values();

        StringWriter stringWriter = new StringWriter();
        Map<String, Object> properties = new HashMap<>(1);

        // Putting any value sets the pretty printing to true... So test must be done
        properties.put(JsonGenerator.PRETTY_PRINTING, true);

        JsonGeneratorFactory factory = Json.createGeneratorFactory(properties);
        JsonGenerator generatedJson = factory.createGenerator(stringWriter);
            generatedJson
                    .writeStartObject().write("client", "chrome...")
                    .writeStartArray("messages");

        Iterator iter = coll.iterator();
        while (iter.hasNext()) {
            Message msg = (Message) iter.next();
            generatedJson.writeStartObject()
                    .write("id", msg.getId())
                    .write("text", msg.getMsg())
                    .writeEnd();
        }
        generatedJson
                .writeEnd()
                .writeEnd();
        generatedJson.close();

        System.out.print(stringWriter.toString());
        return stringWriter.toString();
    }
}
