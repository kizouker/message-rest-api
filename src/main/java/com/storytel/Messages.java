package com.storytel;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import javax.json.Json;
import javax.json.stream.JsonGenerator;
import javax.json.stream.JsonGeneratorFactory;
import java.io.StringWriter;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class Messages {

    private HashMap messages = new HashMap(); // Choosing hashmap - cheap and easy to find the messages

    public void setMessage(long id, Message message) {
        this.messages.put(id, message);
    }

    public Message getText(Long id) {
        return (Message) this.messages.get(id);
    }

    public  String removeMessage(Long id) {
        this.messages.remove(id);
        return toString_();
    }

    public String listMessages()  {
        //return this.messages.values();

        return toString_();
    } //TODO: can I format the response with line break?

private String getText(String jsonText) throws ParseException {
    JSONParser parser = new JSONParser();
    Object jsonObj = parser.parse(jsonText);
    JSONObject jsonObject = (JSONObject) jsonObj;

    return (String) jsonObject.get("text");
}

public String toString_(){

    Collection coll = this.messages.values();

    StringWriter stringWriter = new StringWriter();
    Map<String, Object> properties = new HashMap<>(1);

        // Putting any value sets the pretty printing to true... So test must be done
        properties.put(JsonGenerator.PRETTY_PRINTING, true);

        JsonGeneratorFactory factory = Json.createGeneratorFactory(properties);
        JsonGenerator gen = factory.createGenerator(stringWriter);
        gen
                .writeStartObject().write("client", "chrome...")
                .writeStartArray("messages");
                    Iterator iter = coll.iterator();
                    while(iter.hasNext()){
                      Message msg = (Message)iter.next();
                      gen
                              .writeStartObject()
                              .write("id", msg.getId())
                              .write("text", msg.getMsg())
                              .writeEnd();
                }
                gen
                .writeEnd()
                .writeEnd();

        gen.close();

        System.out.print(stringWriter.toString());
        return stringWriter.toString();
    }
}
