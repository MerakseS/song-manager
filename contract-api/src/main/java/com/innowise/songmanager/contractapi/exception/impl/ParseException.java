package com.innowise.songmanager.contractapi.exception.impl;

public class ParseException extends RuntimeException {

    public ParseException(Throwable cause) {
        super(cause);
    }

    public ParseException(String message, Throwable cause) {
        super(message, cause);
    }
}
