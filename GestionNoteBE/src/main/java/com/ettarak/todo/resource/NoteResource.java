package com.ettarak.todo.resource;

import com.ettarak.todo.enumeration.Level;
import com.ettarak.todo.exception.NoteNotFoundException;
import com.ettarak.todo.model.HttpResponse;
import com.ettarak.todo.model.Note;
import com.ettarak.todo.service.NoteService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.time.LocalDateTime;
import java.util.concurrent.TimeUnit;

import static com.ettarak.todo.utils.Utils.dateTimeFormatter;

@RestController
@RequestMapping(path = "/note")
@RequiredArgsConstructor
public class NoteResource {
    private final NoteService noteService;
    @GetMapping(path = "/all")
    public ResponseEntity<HttpResponse<Note>> getNotes() throws InterruptedException {
        //TimeUnit.SECONDS.sleep(1);
        return  ResponseEntity.ok().body(noteService.getNotes());
    }
    @PostMapping(path = "/add")
    public  ResponseEntity<HttpResponse<Note>> saveNote(@RequestBody @Valid Note note) {
        return  ResponseEntity
                .created(URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/note/all").toUriString()))
                .body(noteService.saveNote(note));
    }

    @GetMapping(path = "/filter")
    public ResponseEntity<HttpResponse<Note>> filterNotes(@RequestParam(value = "level")Level level) {
        return ResponseEntity.ok().body(noteService.filterNoteByLevel(level));
    }

    @PutMapping(path = "/update")
    public ResponseEntity<HttpResponse<Note>> updateNote(@RequestBody @Valid Note note) throws NoteNotFoundException {
        return ResponseEntity.ok().body(noteService.updateNote(note));
    }
    @DeleteMapping(path = "/delete/{id}")
    public ResponseEntity<HttpResponse<Note>> deleteNote(@PathVariable("id") Long id) throws NoteNotFoundException {
        return ResponseEntity.ok().body(noteService.deleteNote(id));
    }
    @RequestMapping(path = "/error")
    public ResponseEntity<HttpResponse<?>> handleError(HttpServletRequest request) {
        return new ResponseEntity<>(
                HttpResponse.builder()
                        .reason("There are no mapping for a " + request.getMethod() + " request for this path on the server")
                        .developerMessage("There are no mapping for a " + request.getMethod() + " request for this path on the server")
                        .status(HttpStatus.NOT_FOUND)
                        .statusCode(HttpStatus.NOT_FOUND.value())
                        .timeStamp(LocalDateTime.now().format(dateTimeFormatter()))
                        .build(),HttpStatus.NOT_FOUND);
    }

}
