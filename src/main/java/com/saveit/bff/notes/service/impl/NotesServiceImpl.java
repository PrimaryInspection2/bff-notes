package com.saveit.bff.notes.service.impl;

import com.saveit.bff.notes.feign.client.NotesServiceFeignClient;
import com.saveit.bff.notes.service.NoteService;
import com.saveit.bff.notes.web.dto.NoteRequestDto;
import com.saveit.bff.notes.web.dto.NoteResponseDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class NotesServiceImpl implements NoteService {

    private final NotesServiceFeignClient notesServiceFeignClient;

    @Override
    public NoteResponseDto create(NoteRequestDto request) {
        log.info("Creating note for userId={}", request.userId());
        NoteResponseDto response = notesServiceFeignClient.create(request);
        log.info("Note created with id={}", response.userId());
        return response;
    }

    @Override
    public NoteResponseDto getById(String id) {
        log.info("Fetching note with id={}", id);
        NoteResponseDto response = notesServiceFeignClient.getById(id);
        log.info("Fetched note: {}", response);
        return response;
    }

    @Override
    public NoteResponseDto update(String id, NoteRequestDto request) {
        log.info("Updating note id={} for userId={}", id, request.userId());
        NoteResponseDto response = notesServiceFeignClient.update(id, request);
        log.info("Note updated: {}", response);
        return response;
    }

    @Override
    public void delete(String id) {
        log.info("Deleting note with id={}", id);
        notesServiceFeignClient.delete(id);
        log.info("Note with id={} deleted", id);
    }

    @Override
    public List<NoteResponseDto> getAllByUserId(String userId) {
        log.info("Fetching all notes for userId={}", userId);
        List<NoteResponseDto> notes = notesServiceFeignClient.getAll(userId);
        log.info("Fetched {} notes for userId={}", notes.size(), userId);
        return notes;
    }
}
