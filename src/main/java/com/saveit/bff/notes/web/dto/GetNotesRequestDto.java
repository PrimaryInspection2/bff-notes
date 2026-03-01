package com.saveit.bff.notes.web.dto;

import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;

import java.util.Collections;
import java.util.Set;

public record GetNotesRequestDto(@NotBlank String userId,
                                 @Nullable Set<String> tagIds) {
    public GetNotesRequestDto {
        tagIds = tagIds == null ? Collections.emptySet() : Set.copyOf(tagIds);
    }
}
