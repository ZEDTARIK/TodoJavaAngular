package com.ettarak.todo.exception;

public class NoteNotFoundException extends  RuntimeException {
    public  NoteNotFoundException(String message) {
        super(message);
    }
}
