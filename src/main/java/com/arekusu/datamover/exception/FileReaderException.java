package com.arekusu.datamover.exception;

public class FileReaderException extends RuntimeException {
    public FileReaderException(String message, Exception e) {
        super(message, e);
    }

    public FileReaderException(String message) {
        super(message);
    }
}
