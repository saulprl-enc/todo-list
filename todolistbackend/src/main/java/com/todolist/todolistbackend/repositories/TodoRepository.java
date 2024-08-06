package com.todolist.todolistbackend.repositories;

import com.todolist.todolistbackend.enums.TodoPriority;
import com.todolist.todolistbackend.model.Todo;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public class TodoRepository {
    private Map<String, Todo> db;

    public TodoRepository() {
        this.db = new HashMap<String, Todo>() {{
            put("1", new Todo("1", "Build Spring Boot REST API", TodoPriority.HIGH));
            put("2", new Todo("2", "Build React ToDo List app", TodoPriority.MEDIUM));
            put("3", new Todo("3", "Type out Week 4 Essay", TodoPriority.HIGH));
            put("4", new Todo("4", "Update university servers", TodoPriority.HIGH));
            put("5", new Todo("5", "Update laboratory apps and services", TodoPriority.HIGH));
            put("6", new Todo("6", "Research testing libraries", TodoPriority.MEDIUM));
            put("7", new Todo("7", "Change car radio", TodoPriority.LOW));
            put("8", new Todo("8", "Make the bed", TodoPriority.HIGH));
            put("9", new Todo("9", "Dust off desk", TodoPriority.HIGH));
            put("10", new Todo("10", "Update LinkedIn", TodoPriority.HIGH));
            put("11", new Todo("11", "Clean the kitchen", TodoPriority.HIGH));
        }};
    }

    public Todo saveTodo(Todo todo) {
        this.db.put(todo.getId(), todo);

        return todo;
    }

    public Collection<Todo> findTodos() {
        return this.db.values();
    }

    public List<Todo> findTodos(int page, int size) {
        long offsetStart = page * size;
        long offsetEnd = offsetStart + size;

        List<Todo> paginatedTodos = offsetStart == 0 ? this.db.values().stream().limit(offsetEnd).toList() : this.db.values().stream().skip(offsetStart).limit(offsetEnd).toList();

        return paginatedTodos;
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
