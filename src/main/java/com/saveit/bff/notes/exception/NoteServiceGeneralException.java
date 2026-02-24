package com.saveit.bff.notes.exception;

public class NoteServiceGeneralException extends RuntimeException {

    public NoteServiceGeneralException(String message, Throwable cause) {
        super(message, cause);
    }
}