package com.todolist.todolistbackend.services;

import com.todolist.todolistbackend.dto.Todo;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;

@Service
public class TodoServiceImpl implements TodoService {

    @Override
    public Collection<Todo> getTodos() {
        return List.of();
    }

    @Override
    public Todo getTodo(String id) {
        return null;
    }

    @Override
    public Todo createTodo(Todo todo) {
        return null;
    }

    @Override
    public Todo updateTodo(String id, Todo data) {
        return null;
    }

    @Override
    public Todo markAsDone(String id) {
        return null;
    }

    @Override
    public Todo undoTodo(String id) {
        return null;
    }
}
