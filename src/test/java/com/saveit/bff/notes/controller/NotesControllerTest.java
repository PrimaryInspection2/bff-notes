package com.saveit.bff.notes.controller;

import com.saveit.bff.notes.service.NoteService;
import com.saveit.bff.notes.web.controller.NotesController;
import com.saveit.bff.notes.web.dto.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import tools.jackson.databind.ObjectMapper;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

//fixme use auth here
@WebMvcTest(controllers = NotesController.class)
@AutoConfigureMockMvc(addFilters = false)
class NotesControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private NoteService noteService;

    @Autowired
    private ObjectMapper objectMapper;

    private NoteRequestDto validRequest;
    private NoteResponseDto validResponse;

    @BeforeEach
    void setUp() {
        validRequest = NoteRequestDto.builder()
                .noteId("note1")
                .userId("user1")
                .title("Test Title")
                .content("Test content")
                .status(NoteStatus.ACTIVE)
                .priority(NotePriority.MEDIUM)
                .tags(Set.of())
                .build();

        validResponse = NoteResponseDto.builder()
                .noteId("note1")
                .userId("user1")
                .title("Test Title")
                .content("Test content")
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
    void create_shouldReturnCreatedNote_whenValidRequest() throws Exception {
        when(noteService.create(any(NoteRequestDto.class), eq(NoteSource.REST_API)))
                .thenReturn(validResponse);

        mockMvc.perform(post("/note")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(validRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.noteId").value("note1"))
                .andExpect(jsonPath("$.content").value("Test content"))
                .andExpect(jsonPath("$.status").value("ACTIVE"));

        ArgumentCaptor<NoteRequestDto> captor = ArgumentCaptor.forClass(NoteRequestDto.class);
        verify(noteService).create(captor.capture(), eq(NoteSource.REST_API));

        assertEquals("user1", captor.getValue().userId());
    }

    @Test
    void create_shouldReturn400_whenInvalidRequest() throws Exception {
        NoteRequestDto invalidRequest = NoteRequestDto.builder()
                .noteId("") // @NotBlank violation
                .userId("")
                .content("")
                .build();

        mockMvc.perform(post("/note")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidRequest)))
                .andExpect(status().isBadRequest());

        verifyNoInteractions(noteService);
    }

    // =========================================================
    // GET BY ID
    // =========================================================

    @Test
    void getById_shouldReturnNote_whenExists() throws Exception {
        when(noteService.getById("note1")).thenReturn(validResponse);

        mockMvc.perform(get("/note/{id}", "note1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.noteId").value("note1"))
                .andExpect(jsonPath("$.userId").value("user1"));

        verify(noteService).getById("note1");
    }

    @Test
    void getById_shouldReturn404_whenNotFound() throws Exception {
        when(noteService.getById("note1"))
                .thenThrow(new RuntimeException("Not found"));

        mockMvc.perform(get("/note/{id}", "note1"))
                .andExpect(status().isInternalServerError());
        // если у тебя есть @ControllerAdvice — поменяй на isNotFound()

        verify(noteService).getById("note1");
    }

    // =========================================================
    // UPDATE
    // =========================================================

    @Test
    void update_shouldReturnUpdatedNote_whenValidRequest() throws Exception {
        when(noteService.update(any(NoteRequestDto.class), eq(NoteSource.REST_API)))
                .thenReturn(validResponse);

        mockMvc.perform(put("/note/{id}", "note1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(validRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").value("Test content"));

        verify(noteService).update(any(NoteRequestDto.class), eq(NoteSource.REST_API));
    }

    @Test
    void update_shouldReturn400_whenInvalidRequest() throws Exception {
        NoteRequestDto invalidRequest = validRequest.toBuilder()
                .content("") // @NotBlank violation
                .build();

        mockMvc.perform(put("/note/{id}", "note1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidRequest)))
                .andExpect(status().isBadRequest());

        verifyNoInteractions(noteService);
    }

    // =========================================================
    // DELETE
    // =========================================================

    @Test
    void delete_shouldReturn200_whenSuccess() throws Exception {
        doNothing().when(noteService).delete("note1");

        mockMvc.perform(delete("/note/{id}", "note1"))
                .andExpect(status().isOk());

        verify(noteService).delete("note1");
    }

    @Test
    void delete_shouldReturn500_whenServiceFails() throws Exception {
        doThrow(new RuntimeException("Delete failed"))
                .when(noteService).delete("note1");

        mockMvc.perform(delete("/note/{id}", "note1"))
                .andExpect(status().isInternalServerError());
    }

    // =========================================================
    // GET ALL
    // =========================================================

    @Test
    void getAll_shouldReturnList_whenNotesExist() throws Exception {
        when(noteService.getAllByUserId("user1"))
                .thenReturn(List.of(validResponse));

        mockMvc.perform(get("/note")
                        .param("userId", "user1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].noteId").value("note1"))
                .andExpect(jsonPath("$[0].content").value("Test content"));

        verify(noteService).getAllByUserId("user1");
    }

    @Test
    void getAll_shouldReturnEmptyList_whenNoNotes() throws Exception {
        when(noteService.getAllByUserId("user1"))
                .thenReturn(List.of());

        mockMvc.perform(get("/note")
                        .param("userId", "user1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isEmpty());

        verify(noteService).getAllByUserId("user1");
    }

    @Test
    void getAll_shouldReturn400_whenUserIdMissing() throws Exception {
        mockMvc.perform(get("/note"))
                .andExpect(status().isBadRequest());

        verifyNoInteractions(noteService);
    }
}