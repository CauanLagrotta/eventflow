package com.cauanlagrotta.exceptions;

public class InvalidPageTokenException extends RuntimeException {
    public InvalidPageTokenException(String message, Throwable cause) {
        super(message, cause);
    }
}
