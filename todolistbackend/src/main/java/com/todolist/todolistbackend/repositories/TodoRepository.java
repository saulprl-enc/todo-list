package com.todolist.todolistbackend.repositories;

import com.todolist.todolistbackend.model.Todo;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Repository
public class TodoRepository {
    private Map<String, Todo> db;

    public TodoRepository() {
        this.db = new HashMap<>();
    }

    public Todo saveTodo(Todo todo) {
        this.db.put(todo.getId(), todo);

        return todo;
    }

    public Collection<Todo> findTodos() {
        return this.db.values();
    }

    public Todo findTodo(String id) {
        Todo todo = this.db.get(id);

        return todo;
    }

    public Todo updateTodo(String id, Todo todo) {
        this.db.replace(id, todo);

        return todo;
    }

    public Todo deleteTodo(String id) {
        return this.db.remove(id);
    }
}
