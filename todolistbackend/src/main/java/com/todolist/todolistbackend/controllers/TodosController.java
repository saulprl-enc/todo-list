package com.todolist.todolistbackend.controllers;

import com.todolist.todolistbackend.dto.TodoDto;
import com.todolist.todolistbackend.enums.TodoPriority;
import com.todolist.todolistbackend.mapper.TodoMapper;
import com.todolist.todolistbackend.model.Todo;
import com.todolist.todolistbackend.services.TodoService;
import com.todolist.todolistbackend.web.PaginatedData;
import jakarta.servlet.http.HttpServletResponse;
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

    @GetMapping()
    public PaginatedData<TodoDto> getTodos(@RequestParam(required = false) Integer page, @RequestParam(required = false) Integer size, @RequestParam(required = false) String title, @RequestParam(required = false) String sortByPriority, @RequestParam(required = false) String sortByDate, @RequestParam(required = false) String status, @RequestParam(required = false) String priority) {
        if (page == null) {
            page = 0;
        } else if (page > 0) {
            page -= 1;
        }

        if (size == null) {
            size = 10;
        }


        PaginatedData<Todo> paginatedData = todoService.getTodos(page, size, title, sortByPriority, sortByDate, status, priority);
        PaginatedData<TodoDto> dtoPaginatedData = new PaginatedData<>(paginatedData.getCurrentPage(), paginatedData.getTotalPages(), paginatedData.getTotalItems(), paginatedData.getSize(), todoMapper.todosListToDto(paginatedData.getData()), paginatedData.getNextPage(), paginatedData.getPreviousPage());

        return dtoPaginatedData;
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
        try {
            Todo todo = todoMapper.convertToEntity(todoDto);

            TodoDto createdDto = todoMapper.convertToDto(todoService.createTodo(todo));

            return createdDto;
        } catch (ParseException ex) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "The server was not able to parse the incoming data.");
        }
    }

    @PostMapping("/{id}/done")
    public TodoDto completeTodo(@PathVariable String id) {
        try {
            Todo existingTodo = (Todo) todoService.getTodo(id).clone();

            if (existingTodo == null) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No ToDo with the provided ID was found");
            }

            todoService.markAsDone(existingTodo);

            return todoMapper.convertToDto(existingTodo);
        } catch (CloneNotSupportedException ex) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "The server was unable to update this ToDo");
        }
    }

    @PutMapping("/{id}/undone")
    public TodoDto undoTodo(@PathVariable String id) {
        try {
            Todo existingTodo = (Todo) todoService.getTodo(id).clone();

            if (existingTodo == null) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No ToDo with the provided ID was found");
            }

            todoService.undoTodo(existingTodo);

            return todoMapper.convertToDto(existingTodo);
        } catch (CloneNotSupportedException ex) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "The server was unable to update this ToDo");
        }
    }

    @PutMapping("/{id}")
    public TodoDto updateTodo(@RequestBody TodoDto data, @PathVariable String id) {
        try {
            Todo existingTodo = (Todo) (todoService.getTodo(id).clone());

            if (existingTodo == null) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No ToDo with provided ID found");
            }

            todoMapper.updateTodoFromDto(data, existingTodo);

            Todo updatedTodo = todoService.updateTodo(existingTodo);

            return todoMapper.convertToDto(updatedTodo);
        } catch (CloneNotSupportedException ex) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "The server was unable to update the todo");
        }
    }

    @DeleteMapping("/{id}")
    public TodoDto deleteTodo(@PathVariable String id) {
        Todo deletedTodo = todoService.deleteTodo(id);

        if (deletedTodo == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }

        return todoMapper.convertToDto(deletedTodo);
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
