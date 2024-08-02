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

    public Todo markAsDone(String id) {
        return null;
    }

    public Todo undoTodo(String id) {
        return null;
    }
}
