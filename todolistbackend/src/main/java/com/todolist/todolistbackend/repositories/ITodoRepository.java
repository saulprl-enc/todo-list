package com.todolist.todolistbackend.repositories;

import com.todolist.todolistbackend.model.Todo;

import java.util.List;

public interface ITodoRepository {
    List<Todo> findTodos();
}
