package com.saveit.bff.notes.web.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;

import java.util.Set;

@Builder(toBuilder = true)
public record NoteRequestDto(String noteId, @NotBlank String userId, String title, NoteStatus status,
                             @NotBlank String content, NotePriority priority, @Valid Set<TagDto> tags) {
}