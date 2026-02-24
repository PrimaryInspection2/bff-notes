package com.saveit.bff.notes.service.impl;

import com.saveit.bff.notes.feign.dto.NoteServiceRequestDto;
import com.saveit.bff.notes.feign.client.NotesServiceFeignClient;
import com.saveit.bff.notes.mapper.NoteServiceNoteRequestMapper;
import com.saveit.bff.notes.web.dto.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class NotesServiceImplTest {

    @Mock
    private NotesServiceFeignClient feignClient;

    @Mock
    private NoteServiceNoteRequestMapper mapper;

    @InjectMocks
    private NotesServiceImpl notesService;

    private NoteRequestDto requestDto;
    private NoteServiceRequestDto serviceRequestDto;
    private NoteResponseDto responseDto;

    @BeforeEach
    void setUp() {
        requestDto = NoteRequestDto.builder()
                .noteId("note1")
                .userId("user1")
                .title("Title")
                .content("Content")
                .status(NoteStatus.ACTIVE)
                .priority(NotePriority.MEDIUM)
                .tags(Set.of())
                .build();

        serviceRequestDto = NoteServiceRequestDto.builder()
                .noteId("note1")
                .userId("user1")
                .title("Title")
                .content("Content")
                .source(NoteSource.REST_API)
                .status(NoteStatus.ACTIVE)
                .priority(NotePriority.MEDIUM)
                .tags(Set.of())
                .build();

        responseDto = NoteResponseDto.builder()
                .noteId("note1")
                .userId("user1")
                .title("Title")
                .content("Content")
                .source(NoteSource.REST_API)
                .status(NoteStatus.ACTIVE)
                .priority(NotePriority.MEDIUM)
                .tags(Set.of())
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
    }

    // =========================================================
    // CREATE
    // =========================================================

    @Test
    void create_shouldMapAndCallFeignClient() {
        when(mapper.toNoteServiceRequestDto(requestDto, NoteSource.REST_API))
                .thenReturn(serviceRequestDto);

        when(feignClient.create(serviceRequestDto))
                .thenReturn(responseDto);

        NoteResponseDto result = notesService.create(requestDto, NoteSource.REST_API);

        assertNotNull(result);
        assertEquals("note1", result.noteId());

        verify(mapper).toNoteServiceRequestDto(requestDto, NoteSource.REST_API);
        verify(feignClient).create(serviceRequestDto);
    }

    @Test
    void create_shouldPropagateException_whenFeignFails() {
        when(mapper.toNoteServiceRequestDto(requestDto, NoteSource.REST_API))
                .thenReturn(serviceRequestDto);

        when(feignClient.create(serviceRequestDto))
                .thenThrow(new RuntimeException("Feign error"));

        assertThrows(RuntimeException.class,
                () -> notesService.create(requestDto, NoteSource.REST_API));

        verify(feignClient).create(serviceRequestDto);
    }

    // =========================================================
    // GET BY ID
    // =========================================================

    @Test
    void getById_shouldReturnNote() {
        when(feignClient.getById("note1"))
                .thenReturn(responseDto);

        NoteResponseDto result = notesService.getById("note1");

        assertEquals("note1", result.noteId());
        verify(feignClient).getById("note1");
        verifyNoInteractions(mapper);
    }

    @Test
    void getById_shouldPropagateException_whenFeignFails() {
        when(feignClient.getById("note1"))
                .thenThrow(new RuntimeException("Not found"));

        assertThrows(RuntimeException.class,
                () -> notesService.getById("note1"));
    }

    // =========================================================
    // UPDATE
    // =========================================================

    @Test
    void update_shouldMapAndCallFeignClient() {
        when(mapper.toNoteServiceRequestDto(requestDto, NoteSource.REST_API))
                .thenReturn(serviceRequestDto);

        when(feignClient.update(serviceRequestDto))
                .thenReturn(responseDto);

        NoteResponseDto result = notesService.update(requestDto, NoteSource.REST_API);

        assertEquals("Content", result.content());

        verify(mapper).toNoteServiceRequestDto(requestDto, NoteSource.REST_API);
        verify(feignClient).update(serviceRequestDto);
    }

    @Test
    void update_shouldPropagateException_whenFeignFails() {
        when(mapper.toNoteServiceRequestDto(requestDto, NoteSource.REST_API))
                .thenReturn(serviceRequestDto);

        when(feignClient.update(serviceRequestDto))
                .thenThrow(new RuntimeException("Update failed"));

        assertThrows(RuntimeException.class,
                () -> notesService.update(requestDto, NoteSource.REST_API));
    }

    // =========================================================
    // DELETE
    // =========================================================

    @Test
    void delete_shouldCallFeignClient() {
        notesService.delete("note1");

        verify(feignClient).delete("note1");
        verifyNoInteractions(mapper);
    }

    @Test
    void delete_shouldPropagateException_whenFeignFails() {
        doThrow(new RuntimeException("Delete failed"))
                .when(feignClient).delete("note1");

        assertThrows(RuntimeException.class,
                () -> notesService.delete("note1"));
    }

    // =========================================================
    // GET ALL
    // =========================================================

    @Test
    void getAllByUserId_shouldReturnList() {
        when(feignClient.getAll("user1"))
                .thenReturn(List.of(responseDto));

        List<NoteResponseDto> result = notesService.getAllByUserId("user1");

        assertEquals(1, result.size());
        assertEquals("note1", result.getFirst().noteId());

        verify(feignClient).getAll("user1");
        verifyNoInteractions(mapper);
    }

    @Test
    void getAllByUserId_shouldReturnEmptyList() {
        when(feignClient.getAll("user1"))
                .thenReturn(List.of());

        List<NoteResponseDto> result = notesService.getAllByUserId("user1");

        assertTrue(result.isEmpty());
        verify(feignClient).getAll("user1");
    }
}