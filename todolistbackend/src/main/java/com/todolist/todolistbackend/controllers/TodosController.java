package com.todolist.todolistbackend.controllers;

import com.todolist.todolistbackend.dto.Todo;
import com.todolist.todolistbackend.enums.TodoPriority;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.*;

@RestController
@RequestMapping("/todos")
public class TodosController {
    private Map<String, Todo> todos = new HashMap<>() {{
        put("1", new Todo("1", "Finish TodoList", TodoPriority.HIGH));
    }};

    @GetMapping
    public Collection<Todo> getTodos() {
        return this.todos.values();
    }

    @GetMapping("/{id}")
    public Todo getTodo(@PathVariable String id) {
        Todo todo = todos.get(id);

        if (todo == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }

        return todo;
    }

    @PostMapping
    public Todo createTodo(@RequestBody @Valid Todo todo) {
        todo.setId(UUID.randomUUID().toString());
        todo.setCreatedAt(new Date());
        todos.put(todo.getId(), todo);

        return todo;
    }

    @DeleteMapping("/{id}")
    public void deleteTodo(@PathVariable String id) {
        Todo deletedTodo = todos.remove(id);

        if (deletedTodo == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Map<String, String> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();

        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });

        return errors;
    }
}
