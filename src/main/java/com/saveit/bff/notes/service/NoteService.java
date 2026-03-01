package com.saveit.bff.notes.service;

import com.saveit.bff.notes.web.dto.GetNotesRequestDto;
import com.saveit.bff.notes.web.dto.NoteRequestDto;
import com.saveit.bff.notes.web.dto.NoteResponseDto;
import com.saveit.bff.notes.web.dto.NoteSource;

import java.util.Set;

public interface NoteService {

    NoteResponseDto processNote(NoteRequestDto request, NoteSource source);

    NoteResponseDto getById(String id);

    void delete(String id);

    Set<NoteResponseDto> getAllByUserId(GetNotesRequestDto request);
}
