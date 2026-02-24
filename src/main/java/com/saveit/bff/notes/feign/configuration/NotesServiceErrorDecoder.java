package com.saveit.bff.notes.feign.configuration;

import com.saveit.bff.notes.exception.NoteServiceBadRequestException;
import com.saveit.bff.notes.exception.NoteServiceNotFoundException;
import com.saveit.bff.notes.exception.NoteServiceGeneralException;
import feign.Response;
import feign.codec.ErrorDecoder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class NotesServiceErrorDecoder implements ErrorDecoder {

    @Override
    public Exception decode(String methodKey, Response response) {

        HttpStatus status = HttpStatus.valueOf(response.status());

        log.error("Feign error: method={}, status={}", methodKey, status);

        return switch (status) {
            case NOT_FOUND -> new NoteServiceNotFoundException("test_note_id");
            case BAD_REQUEST -> new NoteServiceBadRequestException("test_note_id");
            default -> new NoteServiceGeneralException("Notes service error: " + status, null);
        };
    }
}