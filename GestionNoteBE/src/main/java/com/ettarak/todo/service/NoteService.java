package com.ettarak.todo.service;

import com.ettarak.todo.enumeration.Level;
import com.ettarak.todo.exception.NoteNotFoundException;
import com.ettarak.todo.model.HttpResponse;
import com.ettarak.todo.model.Note;
import com.ettarak.todo.repository.NoteJpaRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static com.ettarak.todo.utils.Utils.dateTimeFormatter;
import static java.util.Collections.*;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;

@Service
@Transactional
@Slf4j
@RequiredArgsConstructor
public class NoteService {
    private final NoteJpaRepository noteJpaRepository;

    public HttpResponse<Note> getNotes() {
        log.debug("Fetching all notes from the database");
        return HttpResponse
                .<Note>builder()
                .notes(noteJpaRepository.findAll())
                .message(noteJpaRepository.count() > 0 ? noteJpaRepository.count() + " notes retrieved" : "No notes to display")
                .status(OK)
                .statusCode(OK.value())
                .timeStamp(LocalDateTime.now().format(dateTimeFormatter()))
                .build();
    }

    public HttpResponse<Note> filterNoteByLevel(Level level) {
        List<Note> notes = noteJpaRepository.findByLevel(level);
        log.debug("Filtering notes By level {}", level);
        return HttpResponse
                .<Note>builder()
                .notes(notes)
                .message(notes.size() + " notes are of " + level + " importance.")
                .status(OK)
                .statusCode(OK.value())
                .timeStamp(LocalDateTime.now().format(dateTimeFormatter()))
                .build();
    }

    public HttpResponse<Note> saveNote(Note note) {
        log.debug("Saving note inside the database");
        note.setCreatedAt(LocalDateTime.now());
        return HttpResponse
                .<Note>builder()
                .notes(singleton(noteJpaRepository.save(note)))
                .message("Note created successfully")
                .status(CREATED)
                .statusCode(CREATED.value())
                .timeStamp(LocalDateTime.now().format(dateTimeFormatter()))
                .build();
    }

    public HttpResponse<Note> updateNote(Note note) throws NoteNotFoundException {
        log.debug("Updating note to the database");
        Optional<Note> optionalNote = Optional.ofNullable(noteJpaRepository.findById(note.getId()))
                .orElseThrow(() -> new NoteNotFoundException("The note was not found in the server"));
        Note updateNote = optionalNote.get();
        updateNote.setId(note.getId());
        updateNote.setTitle(note.getTitle());
        updateNote.setDescription(note.getDescription());
        updateNote.setLevel(note.getLevel());
        noteJpaRepository.save(updateNote);
        return HttpResponse
                .<Note>builder()
                .notes(singleton(updateNote))
                .message("Note updated successfully")
                .status(OK)
                .statusCode(OK.value())
                .timeStamp(LocalDateTime.now().format(dateTimeFormatter()))
                .build();
    }

    public HttpResponse<Note> deleteNote(Long id) throws NoteNotFoundException {
        log.debug("Deleting note by {} from the database", id);
        Optional<Note> optionalNote = Optional.ofNullable(noteJpaRepository.findById(id))
                .orElseThrow(() -> new NoteNotFoundException("The note was not found in the server"));
        optionalNote.ifPresent(noteJpaRepository::delete);
        return HttpResponse
                .<Note>builder()
                .notes(singleton(optionalNote.get()))
                .message("Note deleted successfully")
                .status(OK)
                .statusCode(OK.value())
                .timeStamp(LocalDateTime.now().format(dateTimeFormatter()))
                .build();
    }


}
