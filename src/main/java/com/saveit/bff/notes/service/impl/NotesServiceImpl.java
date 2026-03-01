package com.saveit.bff.notes.service.impl;

import com.saveit.bff.notes.feign.client.NotesServiceFeignClient;
import com.saveit.bff.notes.feign.dto.NoteServiceRequestDto;
import com.saveit.bff.notes.mapper.NoteServiceNoteRequestMapper;
import com.saveit.bff.notes.service.NoteService;
import com.saveit.bff.notes.web.dto.GetNotesRequestDto;
import com.saveit.bff.notes.web.dto.NoteRequestDto;
import com.saveit.bff.notes.web.dto.NoteResponseDto;
import com.saveit.bff.notes.web.dto.NoteSource;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
@Slf4j
public class NotesServiceImpl implements NoteService {

    private final NotesServiceFeignClient notesServiceFeignClient;
    private final NoteServiceNoteRequestMapper noteServiceNoteRequestMapper;

    @Override
    public NoteResponseDto processNote(NoteRequestDto request, NoteSource source) {
        log.info("Creating note for userId={}", request.userId());
        NoteServiceRequestDto noteServiceRequestDto = noteServiceNoteRequestMapper.toNoteServiceRequestDto(request, source);
        NoteResponseDto response = notesServiceFeignClient.create(noteServiceRequestDto);
        log.info("Note created with id={}", response.userId());
        return response;
    }

    @Override
    public NoteResponseDto getById(String noteId) {
        log.info("Fetching note with id={}", noteId);
        NoteResponseDto response = notesServiceFeignClient.getById(noteId);
        log.info("Fetched note: {}", response);
        return response;
    }

    @Override
    public void delete(String id) {
        log.info("Deleting note with id={}", id);
        notesServiceFeignClient.delete(id);
        log.info("Note with id={} deleted", id);
    }

    @Override
    public Set<NoteResponseDto> getAllByUserId(GetNotesRequestDto request) {
        String userId = request.userId();
        log.info("Fetching all notes for userId={}", userId);
        Set<NoteResponseDto> notes = notesServiceFeignClient.getAll(request);
        log.info("Fetched {} notes for userId={}", notes.size(), userId);
        return notes;
    }
}
