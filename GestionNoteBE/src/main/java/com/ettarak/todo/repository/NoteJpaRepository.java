package com.ettarak.todo.repository;

import com.ettarak.todo.enumeration.Level;
import com.ettarak.todo.model.Note;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NoteJpaRepository extends JpaRepository<Note, Long> {
    List<Note> findByLevel(Level level);
}
