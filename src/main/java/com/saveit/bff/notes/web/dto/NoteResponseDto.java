package com.saveit.bff.notes.web.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;

import java.time.LocalDateTime;

@Builder(toBuilder = true)
public record NoteResponseDto(@NotBlank String userId, String title, @NotBlank String content,
                              @NotBlank String source, @NotBlank String status,
                              @NotBlank LocalDateTime createdAt, LocalDateTime updatedAt) {
}
