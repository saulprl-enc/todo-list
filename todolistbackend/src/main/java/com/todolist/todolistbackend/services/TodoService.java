package com.todolist.todolistbackend.services;

import com.todolist.todolistbackend.dto.Todo;

import java.util.Collection;
import java.util.List;

public interface TodoService {
    public abstract Collection<Todo> getTodos();

    public abstract Todo getTodo(String id);

    public abstract Todo createTodo(Todo todo);

    public abstract Todo updateTodo(String id, Todo data);

    public abstract Todo markAsDone(String id);

    public abstract Todo undoTodo(String id);
}

