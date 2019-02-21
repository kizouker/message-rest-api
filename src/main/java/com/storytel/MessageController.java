package com.storytel;

import java.util.Collection;
import java.util.concurrent.atomic.AtomicLong;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
public class MessageController {

    private final AtomicLong counter = new AtomicLong();

    @Resource (name = "applicationScopedBean")
    Messages applicationScopedBean;

    @RequestMapping("/post")
    public Message post(@RequestParam(value = "message") String message) {
        long id = counter.incrementAndGet();
         Message msg = new Message(id, String.format(message));
         applicationScopedBean.setMessage(id, msg);
        return msg;
    }

    @RequestMapping("/list")
    public Collection<Message> list() {
        return applicationScopedBean.listMessages();
    }

    @RequestMapping("/delete")
    public Collection<Message> delete(@RequestParam(value = "id") long id) {
        return applicationScopedBean.removeMessage(id);
    }

    @RequestMapping("/get")
    public Message get(@RequestParam(value = "id") long id) {
       return applicationScopedBean.getMessage(id);
    }

    @RequestMapping("/put")
    public Message put(@RequestParam(value = "id") long id, @RequestParam(value = "message") String message){
        Message msg = new Message(id, String.format(message));
        applicationScopedBean.setMessage(id, msg);
        return msg;
    }

    /**
     *
     * HTTP Verb	CRUD	Entire Collection (e.g. /customers)	Specific Item (e.g. /customers/{id})
     * POST	Create	201 (Created), 'Location' header with link to /customers/{id} containing new ID.	404 (Not Found), 409 (Conflict) if resource already exists..
     * GET	Read	200 (OK), list of customers. Use pagination, sorting and filtering to navigate big lists.	200 (OK), single customer. 404 (Not Found), if ID not found or invalid.
     * PUT	Update/Replace	405 (Method Not Allowed), unless you want to update/replace every resource in the entire collection.	200 (OK) or 204 (No Content). 404 (Not Found), if ID not found or invalid.
     * PATCH	Update/Modify	405 (Method Not Allowed), unless you want to modify the collection itself.	200 (OK) or 204 (No Content). 404 (Not Found), if ID not found or invalid.
     * DELETE	Delete	405 (Method Not Allowed), unless you want to delete the whole collection—not often desirable.	200 (OK). 404 (Not Found), if ID not found or invalid.
     */



}