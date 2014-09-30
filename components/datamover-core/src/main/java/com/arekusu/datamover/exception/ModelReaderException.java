package com.arekusu.datamover.exception;

public class ModelReaderException extends RuntimeException {
    public ModelReaderException(String message, Exception e) {
        super(message, e);
    }

    public ModelReaderException(String message) {
        super(message);
    }
}