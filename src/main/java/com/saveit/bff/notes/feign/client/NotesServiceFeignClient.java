package com.saveit.bff.notes.feign.client;

import com.saveit.bff.notes.feign.configuration.NotesServiceFeignClientConfiguration;
import com.saveit.bff.notes.feign.dto.NoteServiceRequestDto;
import com.saveit.bff.notes.web.dto.GetNotesRequestDto;
import com.saveit.bff.notes.web.dto.NoteResponseDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@FeignClient(
        name = "notesServiceFeignClient",
        url = "${saveit.notes-service.url}",
        path = "service-notes/note",
        configuration = NotesServiceFeignClientConfiguration.class)
//todo change all path variables to request bodies
public interface NotesServiceFeignClient {

    @PostMapping
    NoteResponseDto create(@RequestBody NoteServiceRequestDto request);

    @GetMapping("/{id}")
    NoteResponseDto getById(@PathVariable("id") String id);

    @DeleteMapping("/{id}")
    void delete(@PathVariable("id") String id);

    @GetMapping("/all")
    Set<NoteResponseDto> getAll(@RequestBody GetNotesRequestDto request);
}
