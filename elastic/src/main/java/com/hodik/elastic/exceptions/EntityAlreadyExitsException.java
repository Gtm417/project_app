package com.hodik.elastic.exceptions;

public class EntityAlreadyExitsException extends Exception{

    public EntityAlreadyExitsException(String s) {
        super(s);
    }

    public EntityAlreadyExitsException(String message, Throwable cause) {
        super(message, cause);
    }

    public EntityAlreadyExitsException(Throwable cause) {
        super(cause);
    }

    public EntityAlreadyExitsException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

    public EntityAlreadyExitsException() {
    }
}
