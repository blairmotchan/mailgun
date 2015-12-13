package com.samurai.mailgun.data;

public class MessageResponse {

    public MessageResponse(String id, String message) {
        this.id = id;
        this.message = message;
    }

    String id;
    String message;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
