package com.todolist.todolistbackend.controllers;

import com.todolist.todolistbackend.dto.TodoDto;
import com.todolist.todolistbackend.mapper.TodoMapper;
import com.todolist.todolistbackend.model.Todo;
import com.todolist.todolistbackend.services.TodoService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.text.ParseException;
import java.util.*;

@RestController
@RequestMapping("/todos")
public class TodosController {
    @Autowired
    private TodoService todoService;

    @Autowired
    private TodoMapper todoMapper;

    @GetMapping
    public Collection<TodoDto> getTodos() {
        Collection<Todo> todos = todoService.getTodos();
        return todoMapper.todosListToDto(todos);
    }

    @GetMapping("/{id}")
    public Todo getTodo(@PathVariable String id) {
        Todo todo = todoService.getTodo(id);

        if (todo == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }

        return todo;
    }

    @PostMapping
    public TodoDto createTodo(@RequestBody @Valid TodoDto todoDto) {
//        todo.setId(UUID.randomUUID().toString());
//        todo.setCreatedAt(new Date());
//        todoService.createTodo(todo);
        try {
            Todo todo = todoMapper.convertToEntity(todoDto);

            TodoDto createdDto = todoMapper.convertToDto(todoService.createTodo(todo));

            return createdDto;
        } catch (ParseException ex) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "The server was not able to parse the incoming data.");
        }
    }

    @PutMapping("/{id}")
    public TodoDto updateTodo(@RequestBody TodoDto data, @PathVariable String id) {
        Todo existingTodo = todoService.getTodo(id);

        if (existingTodo == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No ToDo with provided ID found");
        }

        todoMapper.updateTodoFromDto(data, existingTodo);

        TodoDto updatedTodo = todoMapper.convertToDto(existingTodo);

        System.out.println(updatedTodo.toString());
        return updatedTodo;
    }

    @DeleteMapping("/{id}")
    public void deleteTodo(@PathVariable String id) {
//        Todo deletedTodo = todoService.remove(id);

//        if (deletedTodo == null) {
//            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
//        }
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
