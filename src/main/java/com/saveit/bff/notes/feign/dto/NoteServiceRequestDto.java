package com.saveit.bff.notes.feign.dto;

import com.saveit.bff.notes.validation.ValidTags;
import com.saveit.bff.notes.web.dto.NotePriority;
import com.saveit.bff.notes.web.dto.NoteSource;
import com.saveit.bff.notes.web.dto.NoteStatus;
import com.saveit.bff.notes.web.dto.TagDto;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;

import java.util.Set;

@Builder(toBuilder = true)
public record NoteServiceRequestDto(@NotBlank String noteId, @NotBlank String userId, String title,
                                    @NotBlank String content,
                                    @NotBlank NoteSource source, @NotBlank NoteStatus status,
                                    @NotBlank NotePriority priority,
                                    @Valid @ValidTags Set<TagDto> tags) {
}
