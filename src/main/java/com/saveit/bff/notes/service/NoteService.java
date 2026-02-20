package com.saveit.bff.notes.service;

import com.saveit.bff.notes.web.dto.NoteRequestDto;
import com.saveit.bff.notes.web.dto.NoteResponseDto;

import java.util.List;

public interface NoteService {

    NoteResponseDto create(NoteRequestDto request);

    NoteResponseDto getById(String id);

    NoteResponseDto update(String id, NoteRequestDto request);

    void delete(String id);

    List<NoteResponseDto> getAllByUserId(String userId);
}
