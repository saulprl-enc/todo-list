package com.todolist.todolistbackend.services;

import com.todolist.todolistbackend.model.Todo;
import com.todolist.todolistbackend.repositories.TodoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;

@Service
public class TodoServiceImpl {

    @Autowired
    private TodoRepository repo;

    public Collection<Todo> getTodos() {
        return List.of();
    }

    public Todo getTodo(String id) {
        return null;
    }

    public Todo createTodo(Todo todo) {
        return null;
    }

    public Todo updateTodo(String id, Todo data) {
        return null;
    }

    public Todo markAsDone(String id) {
        return null;
    }

    public Todo undoTodo(String id) {
        return null;
    }
}
