package com.todolist.todolistbackend.repositories;

import com.todolist.todolistbackend.enums.TodoPriority;
import com.todolist.todolistbackend.model.Todo;
import com.todolist.todolistbackend.web.PaginatedData;
import org.springframework.stereotype.Repository;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.util.*;
import java.util.stream.Stream;

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

    public PaginatedData<Todo> findTodos(int page, int size, String title, String sortByPriority, String sortByDate, String status, TodoPriority priority) {
        Stream<Todo> todoStream = this.db.values().stream();

        if (title != null && !title.isEmpty()) {
            todoStream = todoStream.filter(todo -> todo.getTitle().toLowerCase().contains(title.toLowerCase()));
        }

//        if (sortByPriority != null && !sortByPriority.isEmpty() && !sortByPriority.isBlank()) {
//            boolean ascending = sortByPriority == "asc";
//
//            todoStream = todoStream.sorted((a, b) -> {
//                if (ascending) {
//
//                }
//            });
//        }

        if (status != null && !status.isEmpty()) {
            if (!status.contains("all")) {
                todoStream = todoStream.filter(todo -> {
                    if (todo.isDone()) {
                        return status.contains("completed");
                    }

                    return status.contains("pending");
                });
            }
        }

        List<Todo> todosList = todoStream.toList();

        long offsetStart = page * size;
        long offsetEnd = offsetStart + size;
        int totalPages = (int) Math.ceil(todosList.size() / size);

        List<Todo> paginatedTodos = offsetStart == 0 ? todosList.stream().limit(offsetEnd).toList() : this.db.values().stream().skip(offsetStart).limit(offsetEnd).toList();

        PaginatedData<Todo> paginatedResponse = new PaginatedData<>(page + 1, totalPages, size, paginatedTodos);

        ServletUriComponentsBuilder builder = ServletUriComponentsBuilder.fromCurrentRequest();
        builder.replaceQueryParam("size", size);

        if (page > 0) {
            builder.replaceQueryParam("page", page);
            String uri = builder.build().toUriString();

            paginatedResponse.setPreviousPage(uri);
        }

        if (page + 1 < totalPages) {
            builder.replaceQueryParam("page", page + 2);
            String uri = builder.build().toUriString();

            paginatedResponse.setNextPage(uri);
        }

        return paginatedResponse;
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
