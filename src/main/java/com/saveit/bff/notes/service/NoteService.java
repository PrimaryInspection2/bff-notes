package com.saveit.bff.notes.service;

import com.saveit.bff.notes.web.dto.NoteRequestDto;
import com.saveit.bff.notes.web.dto.NoteResponseDto;
import com.saveit.bff.notes.web.dto.NoteSource;

import java.util.List;

public interface NoteService {

    NoteResponseDto create(NoteRequestDto request, NoteSource source);

    NoteResponseDto getById(String id);

    NoteResponseDto update(NoteRequestDto request, NoteSource source);

    void delete(String id);

    List<NoteResponseDto> getAllByUserId(String userId);
}
