package com.todolist.todolistbackend.services;

import com.todolist.todolistbackend.model.Todo;
import com.todolist.todolistbackend.repositories.TodoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
public class TodoService {

    @Autowired
    private TodoRepository repo;

    public Collection<Todo> getTodos() {
        return this.repo.findTodos();
    }

    public List<Todo> getTodos(int page, int size) {
        return this.repo.findTodos(page, size);
    }

    public Todo getTodo(String id) {
        return this.repo.findTodo(id);
    }

    public Todo createTodo(Todo todo) {
        todo.setId(UUID.randomUUID().toString());
        todo.setCreatedAt(new Date());

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
        todo.setCompletedAt(new Date());

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
