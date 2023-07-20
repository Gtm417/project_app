package com.hodik.elastic.exceptions;

public class EntityAlreadyExistsException extends Exception{

    public EntityAlreadyExistsException(String s) {
        super(s);
    }

    public EntityAlreadyExistsException(String message, Throwable cause) {
        super(message, cause);
    }

    public EntityAlreadyExistsException(Throwable cause) {
        super(cause);
    }

    public EntityAlreadyExistsException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

    public EntityAlreadyExistsException() {
    }
}
