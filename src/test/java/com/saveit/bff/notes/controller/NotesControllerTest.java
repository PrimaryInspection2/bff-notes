package com.saveit.bff.notes.controller;

import com.saveit.bff.notes.service.NoteService;
import com.saveit.bff.notes.web.dto.NoteRequestDto;
import com.saveit.bff.notes.web.dto.NoteResponseDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import tools.jackson.databind.ObjectMapper;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

//fixme use auth here
@WebMvcTest
@AutoConfigureMockMvc(addFilters = false)
class NotesControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private NoteService noteService;

    @Autowired
    private ObjectMapper objectMapper;

    private NoteRequestDto testRequest;
    private NoteResponseDto testResponse;
    private List<NoteResponseDto> testResponseList;

    @BeforeEach
    void setUp() {
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
    void create_shouldReturnCreatedNote() throws Exception {
        when(noteService.create(testRequest)).thenReturn(testResponse);

        mockMvc.perform(post("/note")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").value("Test content"))
                .andExpect(jsonPath("$.status").value("CREATED"));

        verify(noteService).create(testRequest);
    }

    @Test
    void getById_shouldReturnNote() throws Exception {
        when(noteService.getById("note1")).thenReturn(testResponse);

        mockMvc.perform(get("/note/{id}", "note1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").value("Test content"));

        verify(noteService).getById("note1");
    }

    @Test
    void update_shouldReturnUpdatedNote() throws Exception {
        when(noteService.update("note1", testRequest)).thenReturn(testResponse);

        mockMvc.perform(put("/note/{id}", "note1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").value("Test content"));

        verify(noteService).update("note1", testRequest);
    }

    @Test
    void delete_shouldCallService() throws Exception {
        mockMvc.perform(delete("/note/{id}", "note1"))
                .andExpect(status().isOk());

        verify(noteService).delete("note1");
    }

    @Test
    void getAll_shouldReturnNotes() throws Exception {
        when(noteService.getAllByUserId("user1")).thenReturn(testResponseList);

        mockMvc.perform(get("/note")
                        .param("userId", "user1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].content").value("Test content"));

        verify(noteService).getAllByUserId("user1");
    }
}
