package com.saveit.bff.notes.web.controller;

import com.saveit.bff.notes.service.NoteService;
import com.saveit.bff.notes.web.dto.GetNotesRequestDto;
import com.saveit.bff.notes.web.dto.NoteRequestDto;
import com.saveit.bff.notes.web.dto.NoteResponseDto;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

import static com.saveit.bff.notes.web.dto.NoteSource.REST_API;

@RestController
@Validated
@Slf4j
@RequestMapping("/note")
@RequiredArgsConstructor
//todo change all path variables to request bodies
public class NotesController {

    private final NoteService notesService;

    @PostMapping
    public NoteResponseDto processNote(@Valid @RequestBody NoteRequestDto request) {
        log.info("Create note for userId:{}", request.userId());
        return notesService.processNote(request, REST_API);
    }

    @GetMapping("/{id}")
    public NoteResponseDto getById(@PathVariable @NotBlank String id) {
        log.info("Get note id={}", id);
        return notesService.getById(id);
    }
    @DeleteMapping("/{id}")
    public void delete(@PathVariable @NotBlank String id) {
        log.info("Delete note id={}", id);
        notesService.delete(id);
    }

    @PostMapping("/search")
    public Set<NoteResponseDto> searchNotes(@Valid @RequestBody GetNotesRequestDto request) {
        log.info("Get all notes for userId={}", request.userId());
        return notesService.getAllByUserId(request);
    }

}
