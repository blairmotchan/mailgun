package com.samurai.mailgun.exception;

public class BadRequestException extends RuntimeException {

    public BadRequestException() {
        super("Bad Request");
    }

}
