package com.saveit.bff.notes.service.impl;

import com.saveit.bff.notes.feign.client.NotesServiceFeignClient;
import com.saveit.bff.notes.web.dto.NoteRequestDto;
import com.saveit.bff.notes.web.dto.NoteResponseDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class NotesServiceImplTest {

    private NotesServiceFeignClient feignClient;
    private NotesServiceImpl notesService;

    private NoteRequestDto testRequest;
    private NoteResponseDto testResponse;
    private List<NoteResponseDto> testResponseList;

    @BeforeEach
    void setUp() {
        feignClient = mock(NotesServiceFeignClient.class);
        notesService = new NotesServiceImpl(feignClient);

        testRequest = NoteRequestDto.builder()
                .userId("user1")
                .title("Test Title")
                .content("Test content")
                .source("source1")
                .build();

        testResponse = NoteResponseDto.builder()
                .userId("user1")
                .title("Test Title")
                .content("Test content")
                .source("source1")
                .status("CREATED")
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        testResponseList = List.of(testResponse);
    }

    @Test
    void create_shouldCallFeignClient() {
        when(feignClient.create(testRequest)).thenReturn(testResponse);

        NoteResponseDto result = notesService.create(testRequest);

        assertEquals("Test content", result.content());
        verify(feignClient, times(1)).create(testRequest);
    }

    @Test
    void getById_shouldReturnNote() {
        when(feignClient.getById("note1")).thenReturn(testResponse);

        NoteResponseDto result = notesService.getById("note1");

        assertEquals("Test content", result.content());
        verify(feignClient).getById("note1");
    }

    @Test
    void update_shouldCallFeignClient() {
        when(feignClient.update("note1", testRequest)).thenReturn(testResponse);

        NoteResponseDto result = notesService.update("note1", testRequest);

        assertEquals("Test content", result.content());
        verify(feignClient).update("note1", testRequest);
    }

    @Test
    void delete_shouldCallFeignClient() {
        notesService.delete("note1");

        verify(feignClient).delete("note1");
    }

    @Test
    void getAllByUserId_shouldReturnList() {
        when(feignClient.getAll("user1")).thenReturn(testResponseList);

        List<NoteResponseDto> result = notesService.getAllByUserId("user1");

        assertEquals(1, result.size());
        assertEquals("Test content", result.get(0).content());
        verify(feignClient).getAll("user1");
    }
}
