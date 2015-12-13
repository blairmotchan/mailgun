package com.samurai.mailgun.exception;

public class RequestFailedException extends RuntimeException {
    public RequestFailedException() {
        super("Request Failed - Parameters were valid but request failed");
    }
}
