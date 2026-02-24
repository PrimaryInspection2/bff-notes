package com.saveit.bff.notes.mapper;

import com.saveit.bff.notes.dto.NoteServiceRequestDto;
import com.saveit.bff.notes.web.dto.NoteRequestDto;
import com.saveit.bff.notes.web.dto.NoteSource;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class NoteServiceRequestMapper {

    public NoteServiceRequestDto toNoteServiceRequestDto(NoteRequestDto noteRequestDto, NoteSource source) {
        return NoteServiceRequestDto.builder()
                .noteId(noteRequestDto.noteId())
                .userId(noteRequestDto.userId()) // todo change it for value from token in future
                .title(noteRequestDto.title())
                .content(noteRequestDto.content())
                .source(source) // todo change it for value from token in future
                .status(noteRequestDto.status())
                .priority(noteRequestDto.priority())
                .tags(noteRequestDto.tags())
                .build();
    }

}
