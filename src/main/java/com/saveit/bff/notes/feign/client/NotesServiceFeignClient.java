package com.saveit.bff.notes.feign.client;

import com.saveit.bff.notes.feign.configuration.NotesServiceFeignClientConfiguration;
import com.saveit.bff.notes.web.dto.NoteRequestDto;
import com.saveit.bff.notes.web.dto.NoteResponseDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient(
        name = "notesServiceFeignClient",
        url = "${saveit.notes-service.url}",
        path = "service-notes/note ",
        configuration = NotesServiceFeignClientConfiguration.class)
public interface NotesServiceFeignClient {

    @PostMapping
    NoteResponseDto create(@RequestBody NoteRequestDto request);

    @GetMapping("/{id}")
    NoteResponseDto getById(@PathVariable("id") String id);

    @PutMapping("/{id}")
    NoteResponseDto update(@PathVariable("id") String id, @RequestBody NoteRequestDto request);

    @DeleteMapping("/{id}")
    void delete(@PathVariable("id") String id);

    @GetMapping
    List<NoteResponseDto> getAll(@RequestParam("userId") String userId);
}
