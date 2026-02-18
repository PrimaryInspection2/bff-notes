package com.saveit.bff.notes.service.impl;

import com.saveit.bff.notes.service.NoteService;
import com.saveit.bff.notes.web.dto.NoteRequestDto;
import com.saveit.bff.notes.web.dto.NoteResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class NotesServiceImpl implements NoteService {

    @Override
    public NoteResponseDto create(NoteRequestDto request) {
        return null;
    }

    @Override
    public NoteResponseDto getById(String id) {
        return null;
    }

    @Override
    public NoteResponseDto update(String id, NoteRequestDto request) {
        return null;
    }

    @Override
    public void delete(String id) {

    }

    @Override
    public List<NoteResponseDto> getAllByUserId(String userId) {
        return List.of();
    }
}