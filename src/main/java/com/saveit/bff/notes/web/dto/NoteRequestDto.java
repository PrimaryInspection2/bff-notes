package com.saveit.bff.notes.web.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;

@Builder(toBuilder = true)
public record NoteRequestDto(String userId, String title, @NotBlank String content, String source) {
}