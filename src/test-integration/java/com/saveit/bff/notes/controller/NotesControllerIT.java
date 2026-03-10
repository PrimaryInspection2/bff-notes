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
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import tools.jackson.databind.ObjectMapper;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Set;

import static com.saveit.bff.notes.web.dto.NoteSource.REST_API;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

//fixme use auth here
@ActiveProfiles("it")
@WebMvcTest(controllers = NotesController.class)
@AutoConfigureMockMvc(addFilters = false)
class NotesControllerIT {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private NoteService noteService;

    @Autowired
    private ObjectMapper objectMapper;

    private NoteRequestDto validRequest;
    private GetNotesRequestDto validGetNotesRequest;
    private NoteResponseDto validResponse;

    @BeforeEach
    void setUp() {
        validGetNotesRequest = new GetNotesRequestDto("user1", null);

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

    // =======================
    // POST /note
    // =======================

    @Test
    void create_shouldReturnCreatedNote_whenValidRequest() throws Exception {
        when(noteService.processNote(any(NoteRequestDto.class), eq(NoteSource.REST_API)))
                .thenReturn(validResponse);

        mockMvc.perform(post("/note")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(validRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.noteId").value("note1"))
                .andExpect(jsonPath("$.content").value("Test content"))
                .andExpect(jsonPath("$.status").value("ACTIVE"));

        ArgumentCaptor<NoteRequestDto> captor = ArgumentCaptor.forClass(NoteRequestDto.class);
        verify(noteService).processNote(captor.capture(), eq(NoteSource.REST_API));
        assertEquals("user1", captor.getValue().userId());
    }

    @Test
    void create_shouldReturn400_whenNoteIdBlank() throws Exception {
        NoteRequestDto invalid = validRequest.toBuilder().noteId("").build();
        mockMvc.perform(post("/note")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalid)))
                .andExpect(status().isBadRequest());
        verifyNoInteractions(noteService);
    }

    @Test
    void create_shouldReturn400_whenUserIdBlank() throws Exception {
        NoteRequestDto invalid = validRequest.toBuilder().userId("").build();
        mockMvc.perform(post("/note")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalid)))
                .andExpect(status().isBadRequest());
        verifyNoInteractions(noteService);
    }

    @Test
    void create_shouldReturn400_whenContentBlank() throws Exception {
        NoteRequestDto invalid = validRequest.toBuilder().content("").build();
        mockMvc.perform(post("/note")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalid)))
                .andExpect(status().isBadRequest());
        verifyNoInteractions(noteService);
    }

    @Test
    void create_shouldReturn400_whenStatusInvalid() throws Exception {
        String invalidJson = """
            {
              "noteId":"note1",
              "userId":"user1",
              "title":"Test",
              "content":"Content",
              "status":"INVALID",
              "priority":"MEDIUM",
              "tags":[]
            }
            """;
        mockMvc.perform(post("/note")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalidJson))
                .andExpect(status().isBadRequest());
        verifyNoInteractions(noteService);
    }

    // =======================
    // GET /note/{id}
    // =======================

    @Test
    void getById_shouldReturnNote_whenExists() throws Exception {
        when(noteService.getById("note1")).thenReturn(validResponse);
        mockMvc.perform(get("/note/{id}", "note1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.noteId").value("note1"));
        verify(noteService).getById("note1");
    }

    @Test
    void getById_shouldReturn400_whenIdBlank() throws Exception {
        mockMvc.perform(get("/note/{id}", " "))
                .andExpect(status().isBadRequest());
        verifyNoInteractions(noteService);
    }

    @Test
    void getById_shouldReturn500_whenNotFound() throws Exception {
        when(noteService.getById("note1")).thenThrow(new RuntimeException("Not found"));
        mockMvc.perform(get("/note/{id}", "note1"))
                .andExpect(status().isInternalServerError());
        verify(noteService).getById("note1");
    }

    // =======================
    // DELETE /note/{id}
    // =======================

    @Test
    void delete_shouldReturn200_whenSuccess() throws Exception {
        doNothing().when(noteService).delete("note1");
        mockMvc.perform(delete("/note/{id}", "note1"))
                .andExpect(status().isOk());
        verify(noteService).delete("note1");
    }

    @Test
    void delete_shouldReturn500_whenServiceFails() throws Exception {
        doThrow(new RuntimeException("Delete failed")).when(noteService).delete("note1");
        mockMvc.perform(delete("/note/{id}", "note1"))
                .andExpect(status().isInternalServerError());
    }

    @Test
    void delete_shouldReturn400_whenIdBlank() throws Exception {
        mockMvc.perform(delete("/note/{id}", " "))
                .andExpect(status().isBadRequest());
    }
    // =======================
    // POST /note/search
    // =======================

    @Test
    void searchNotes_shouldReturnList_whenNotesExist() throws Exception {
        when(noteService.getAllByUserId(validGetNotesRequest))
                .thenReturn(Set.of(validResponse));
        mockMvc.perform(post("/note/search")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(validGetNotesRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].noteId").value("note1"));
        verify(noteService).getAllByUserId(validGetNotesRequest);
    }

    @Test
    void getAll_shouldReturnEmptyList_whenNoNotes() throws Exception {
        when(noteService.getAllByUserId(validGetNotesRequest)).thenReturn(Set.of());
        mockMvc.perform(post("/note/search")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(validGetNotesRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isEmpty());
        verify(noteService).getAllByUserId(validGetNotesRequest);
    }

    @Test
    void getAll_shouldReturn400_whenUserIdBlank() throws Exception {
        GetNotesRequestDto invalid = new GetNotesRequestDto("", null);
        mockMvc.perform(post("/note/search")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalid)))
                .andExpect(status().isBadRequest());
        verifyNoInteractions(noteService);
    }

    @Test
    void searchNotes_shouldReturnNotes_whenTagIdsProvided() throws Exception {
        GetNotesRequestDto request = new GetNotesRequestDto("user1", Set.of("tag1", "tag2"));
        when(noteService.getAllByUserId(request)).thenReturn(Set.of(validResponse));
        mockMvc.perform(post("/note/search")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].noteId").value("note1"));
        verify(noteService).getAllByUserId(request);
    }
}