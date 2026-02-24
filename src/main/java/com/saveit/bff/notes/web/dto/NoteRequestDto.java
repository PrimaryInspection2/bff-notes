package com.saveit.bff.notes.web.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;

import java.util.Set;

@Builder(toBuilder = true)
public record NoteRequestDto(@NotBlank String noteId, @NotBlank String userId, String title, NoteStatus status,
                             @NotBlank String content, NotePriority priority, Set<TagDto> tags) {
}