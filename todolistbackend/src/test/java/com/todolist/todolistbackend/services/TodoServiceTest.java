package com.todolist.todolistbackend.services;

import static org.assertj.core.api.Assertions.assertThat;

import com.todolist.todolistbackend.enums.TodoPriority;
import com.todolist.todolistbackend.model.Todo;
import com.todolist.todolistbackend.repositories.TodoRepositoryImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.UUID;

class TodoServiceTest {
    private TodoService service;

    @BeforeEach
    void init() {
        this.service = new TodoService(new TodoRepositoryImpl());
        this.service.createTodo(new Todo("Random title", TodoPriority.LOW));
    }

    @Test
    void shouldCreateToDoWithUuidAndCreatedAtFields() {
        Todo todo = service.createTodo(new Todo("Random title", TodoPriority.LOW));

        assertThat(UUID.fromString(todo.getId()).toString()).isEqualTo(todo.getId());
        assertThat(todo.getCreatedAt()).isBefore(LocalDateTime.now());
    }

    @Test
    void shouldMarkToDoAsDoneAndSetCompletedAtField() {
        Todo todo = service.getTodos().getFirst();

        service.markAsDone(todo);
        Todo updatedTodo = service.getTodo(todo.getId());

        assertThat(updatedTodo.isDone()).isEqualTo(true);
        assertThat(updatedTodo.getCreatedAt()).isBefore(LocalDateTime.now());
    }

    @Test
    void shouldDoNothing_WhenCompletingADoneToDo() {
        LocalDateTime timestamp = LocalDateTime.now();

        Todo todo = service.getTodos().getFirst();
        todo.setDone(true);
        todo.setCompletedAt(timestamp);

        assertThat(todo.isDone()).isEqualTo(true);
        assertThat(todo.getCompletedAt()).isEqualTo(timestamp);

        service.markAsDone(todo);
        Todo doneTodo = service.getTodo(todo.getId());

        assertThat(doneTodo.isDone()).isEqualTo(true);
        assertThat(doneTodo.getCompletedAt()).isEqualTo(timestamp);
    }

    @Test
    void shouldUndoToDoAndClearCompletedAtField() {
        LocalDateTime timestamp = LocalDateTime.now();

        Todo todo = service.getTodos().getFirst();
        todo.setDone(true);
        todo.setCompletedAt(timestamp);

        Todo updatedTodo = service.getTodo(todo.getId());
        assertThat(updatedTodo.isDone()).isEqualTo(true);
        assertThat(updatedTodo.getCompletedAt()).isEqualTo(timestamp);

        service.undoTodo(todo);
        Todo undoneTodo = service.getTodo(todo.getId());
        assertThat(undoneTodo.isDone()).isEqualTo(false);
        assertThat(undoneTodo.getCompletedAt()).isNull();
    }

    @Test
    void shouldDoNothing_WhenUndoingPendingToDo() {
        Todo todo = service.getTodos().getFirst();

        service.undoTodo(todo);

        Todo undoneTodo = service.getTodo(todo.getId());
        assertThat(undoneTodo.isDone()).isEqualTo(false);
        assertThat(undoneTodo.getCompletedAt()).isNull();
    }
}