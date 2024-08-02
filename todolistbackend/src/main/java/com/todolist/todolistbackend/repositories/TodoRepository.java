package com.todolist.todolistbackend.repositories;

import com.todolist.todolistbackend.dto.Todo;
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

    public Collection<Todo> findStudents() {
        return this.db.values();
    }

    public Todo findTodo(String id) {
        Todo todo = this.db.get(id);

        return todo;
    }

    public Todo updateTodo(String id, Todo todo) {
        return this.db.replace(id, todo);
    }

    public Todo deleteTodo(String id) {
        return this.db.remove(id);
    }
}
