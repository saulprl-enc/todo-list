package com.todolist.todolistbackend.services;

import com.todolist.todolistbackend.enums.TodoPriority;
import com.todolist.todolistbackend.model.Todo;
import com.todolist.todolistbackend.repositories.TodoRepository;
import com.todolist.todolistbackend.web.PaginatedData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.UUID;

@Service
public class TodoService {

    @Autowired
    private TodoRepository repo;

    public Collection<Todo> getTodos() {
        return this.repo.findTodos();
    }

    public PaginatedData<Todo> getTodos(int page, int size, String title, String sortByPriority, String sortByDate, String status, TodoPriority priority) {
        return this.repo.findTodos(page, size, title, sortByPriority, sortByDate, status, priority);
    }

    public Todo getTodo(String id) {
        return this.repo.findTodo(id);
    }

    public Todo createTodo(Todo todo) {
        todo.setId(UUID.randomUUID().toString());
        todo.setCreatedAt(LocalDateTime.now());

        Todo createdTodo = this.repo.saveTodo(todo);

        return createdTodo;
    }

    public Todo updateTodo(Todo updatedTodo) {
        return this.repo.updateTodo(updatedTodo.getId(), updatedTodo);
    }

    public Todo deleteTodo(String id) {
        return this.repo.deleteTodo(id);
    }

    public void markAsDone(Todo todo) {
        if (todo.isDone()) {
            return;
        }

        todo.setDone(true);
        todo.setCompletedAt(LocalDateTime.now());

        this.repo.updateTodo(todo.getId(), todo);
    }

    public void undoTodo(Todo todo) {
        if (!todo.isDone()) {
            return;
        }

        todo.setDone(false);
        todo.setCompletedAt(null);

        this.repo.updateTodo(todo.getId(), todo);
    }
}
