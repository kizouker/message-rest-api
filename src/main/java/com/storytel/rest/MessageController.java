package com.storytel.rest;

import java.net.URI;
import java.util.concurrent.atomic.AtomicLong;

import com.storytel.exception.ResourceNotFoundException;
import com.storytel.model.Message;
import com.storytel.model.Messages;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.web.util.UriComponentsBuilder;

import javax.annotation.Resource;

// The Rest API

@RestController
@Api(description = "Set of endpoints for creating, updating, deleting and listing messages.")
public class MessageController {

    private final AtomicLong counter = new AtomicLong(); // Good for incremental updates

    @Resource (name = "applicationScopedBean")
    Messages applicationScopedBean; // A shared resource, the scope is over the whole app


    @RequestMapping(value = "/1.0/messages",
            method = RequestMethod.POST,
            consumes = {"text/plain"},
            produces = {"application/json"})
    @ApiOperation(value = "Create a message resource.", notes = "Returns a Json with the message and a generated ID")
     public ResponseEntity<Message> post(@ApiParam(name = "The message provided in the payload/requestbody", required = true)
                            @RequestBody String message, UriComponentsBuilder ucb)  throws ResourceNotFoundException{
        long id = counter.incrementAndGet();
        Message msg = new Message(id, String.format(message));
        applicationScopedBean.setMessage(id, msg);

        HttpHeaders headers = getHttpHeaders(msg);

        return new ResponseEntity<>(msg, headers, HttpStatus.CREATED);
    }

    @RequestMapping(value = "/1.0/messages/{id}",
            method = RequestMethod.GET,
            consumes = {"application/x-www-form-urlencoded"},
            produces = {"application/json"})
    @ApiOperation(value = "Get a single message.", notes = " You have to provide a valid message ID")
    public ResponseEntity<Message> get(@ApiParam(name = "The ID of the Message.", required = true)
                                           @PathVariable Long id)
            throws ResourceNotFoundException {

        return new  ResponseEntity<>(applicationScopedBean.getMessage(id), HttpStatus.OK);
    }

    @RequestMapping(value = "/1.0/messages/{id}",
                method = RequestMethod.DELETE,
                consumes = {"application/x-www-form-urlencoded"},
                produces = {"application/json"})
    @ApiOperation(value = "Delete a message.", notes = "You have to provide a valid message ID in the url (ex: messages/1). " +
            "Returned is a Json with the remaining messages")
         public ResponseEntity<String> delete(@ApiParam(name = "The ID of the Message.", required = true)
                                                  @PathVariable Long id) throws ResourceNotFoundException {

        return new ResponseEntity<>(applicationScopedBean.removeMessage(id), HttpStatus.OK);
    }

    @RequestMapping(value = "/1.0/messages/{id}",
            method = RequestMethod.PUT,
            consumes = {"text/plain"},
            produces = {"application/json"})
    @ApiOperation(value = "Update a message.", notes = "You have to provide a valid message ID and the message in the payload body.")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Message> put(@ApiParam(value = "The ID of message,", required = true) @PathVariable Long id,
                       @ApiParam(name = "The changed message.", required = true) @RequestBody String message)
            throws ResourceNotFoundException {

        Message msg = new Message(id, String.format(message));
        HttpHeaders headers = getHttpHeaders(msg);

        return new ResponseEntity<>(applicationScopedBean.setMessage(id, msg), headers, HttpStatus.OK);
    }

    @RequestMapping(value = "/1.0/messages",
            method = RequestMethod.GET,
            produces = "application/json")
    @ApiOperation("List all available messages.")
    public ResponseEntity<String> list() throws ResourceNotFoundException{

        return new ResponseEntity<>(applicationScopedBean.listMessages(), HttpStatus.OK);
    }

    private HttpHeaders getHttpHeaders(Message msg) {
        HttpHeaders headers = new HttpHeaders();
        URI locationUri = ServletUriComponentsBuilder
                .fromCurrentContextPath()
                .path("/{id}")
                .buildAndExpand(msg.getId())
                .toUri();

        headers.setLocation(locationUri);
        return headers;
    }
}

/**
 *
 * HTTP Verb	CRUD	Entire Collection (e.g. /customers)	Specific Item (e.g. /customers/{id})
 *
 * POST	Create	201 (Created), 'Location' header with link to /customers/{id} containing new ID.	404 (Not Found), 409 (Conflict) if resource already exists..
 * GET	Read	200 (OK), list of customers. Use pagination, sorting and filtering to navigate big lists.	200 (OK), single customer. 404 (Not Found), if ID not found or invalid.
 * PUT	Update/Replace	405 (Method Not Allowed), unless you want to update/replace every resource in the entire collection.	200 (OK) or 204 (No Content). 404 (Not Found), if ID not found or invalid.
 * PATCH	Update/Modify	405 (Method Not Allowed), unless you want to modify the collection itself.	200 (OK) or 204 (No Content). 404 (Not Found), if ID not found or invalid.
 * DELETE	Delete	405 (Method Not Allowed), unless you want to delete the whole collection—not often desirable.	200 (OK). 404 (Not Found), if ID not found or invalid.
 */

