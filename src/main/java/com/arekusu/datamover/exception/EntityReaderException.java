package com.arekusu.datamover.exception;

public class EntityReaderException extends RuntimeException {
    public EntityReaderException(String message, Exception e) {
        super(message, e);
    }

    public EntityReaderException(String message) {
        super(message);
    }
}
