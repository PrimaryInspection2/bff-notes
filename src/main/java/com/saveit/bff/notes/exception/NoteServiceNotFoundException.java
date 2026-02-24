package com.saveit.bff.notes.exception;

import lombok.Getter;

@Getter
public class NoteServiceNotFoundException extends RuntimeException {

    private final String noteId;

    public NoteServiceNotFoundException(String noteId) {
        super("Note not found with id: " + noteId);
        this.noteId = noteId;
    }
}