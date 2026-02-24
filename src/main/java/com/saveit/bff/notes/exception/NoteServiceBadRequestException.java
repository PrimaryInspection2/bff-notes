package com.saveit.bff.notes.exception;

import lombok.Getter;

@Getter
public class NoteServiceBadRequestException extends RuntimeException {

    private final String noteId;

    public NoteServiceBadRequestException(String noteId) {
        super("Invalid note request for noteId: " + noteId);
        this.noteId = noteId;
    }
}