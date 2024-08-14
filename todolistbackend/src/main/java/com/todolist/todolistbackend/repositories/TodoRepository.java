package com.todolist.todolistbackend.repositories;

import com.todolist.todolistbackend.dto.TodoStats;
import com.todolist.todolistbackend.model.Todo;
import com.todolist.todolistbackend.web.PaginatedData;

import java.util.List;

public interface TodoRepository {
    Todo saveTodo(Todo todo);

    List<Todo> findTodos();

    PaginatedData<Todo> findTodos(int page, int size, String title, String sortByPriority, String sortByDate, String status, String priority);

    Todo findTodo(String id);

    Todo updateTodo(String id, Todo todo);

    Todo deleteTodo(String id);

    TodoStats<Long> calculateTodoStats();
}
