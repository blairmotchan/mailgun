package com.samurai.mailgun.exception;

public class ServerErrorException extends RuntimeException {
    public ServerErrorException() {
        super("Server Errors - something is wrong on Mailgun’s end");
    }
}
