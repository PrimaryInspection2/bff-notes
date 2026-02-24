package com.saveit.bff.notes.web.controller;

import com.saveit.bff.notes.service.NoteService;
import com.saveit.bff.notes.web.dto.NoteRequestDto;
import com.saveit.bff.notes.web.dto.NoteResponseDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.saveit.bff.notes.web.dto.NoteSource.REST_API;

@RestController
@Slf4j
@RequestMapping("/note")
@RequiredArgsConstructor
//todo change all path variables to request bodies
public class NotesController {

    private final NoteService notesService;

    @PostMapping
    public NoteResponseDto create(@Valid @RequestBody NoteRequestDto request) {
        log.info("Create note for");
        return notesService.create(request, REST_API);
    }

    @GetMapping("/{id}")
    public NoteResponseDto getById(@PathVariable String id) {
        log.info("Get note id={}", id);
        return notesService.getById(id);
    }

    @PutMapping("/{id}")
    public NoteResponseDto update(@Valid @RequestBody NoteRequestDto request) {
        log.info("Update note id={}", request.noteId());
        return notesService.update(request, REST_API);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable String id) {
        log.info("Delete note id={}", id);
        notesService.delete(id);
    }

    @GetMapping
    public List<NoteResponseDto> getAll(@RequestParam String userId) {
        log.info("Get all notes for userId={}", userId);
        return notesService.getAllByUserId(userId);
    }

}
