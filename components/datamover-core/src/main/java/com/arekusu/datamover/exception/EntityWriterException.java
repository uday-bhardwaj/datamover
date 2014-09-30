package com.arekusu.datamover.exception;

public class EntityWriterException extends RuntimeException {
    public EntityWriterException(String message, Exception e) {
        super(message, e);
    }

    public EntityWriterException(String message) {
        super(message);
    }
}
