package com.todolist.todolistbackend.services;

import com.todolist.todolistbackend.dto.TodoStats;
import com.todolist.todolistbackend.dto.TodoStatsImpl;
import com.todolist.todolistbackend.enums.TodoPriority;
import com.todolist.todolistbackend.model.Todo;
import com.todolist.todolistbackend.repositories.TodoRepository;
import com.todolist.todolistbackend.web.PaginatedData;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

@Service
public class TodoService {

    private TodoRepository repo;

    public TodoService(TodoRepository repo) {
        this.repo = repo;
    }

    public Collection<Todo> getTodos() {
        return this.repo.findTodos();
    }

    public PaginatedData<Todo> getTodos(int page, int size, String title, String sortByPriority, String sortByDate, String status, String priority) {
        return this.repo.findTodos(page, size, title, sortByPriority, sortByDate, status, priority);
    }

    public Todo getTodo(String id) {
        return this.repo.findTodo(id);
    }

    public Todo createTodo(Todo todo) {
        todo.setId(UUID.randomUUID().toString());
        todo.setCreatedAt(LocalDateTime.now());

        return this.repo.saveTodo(todo);
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

    public TodoStats<String> calculateTodoStats() {
        TodoStats<Long> numericStats = this.repo.calculateTodoStats();

        return new TodoStatsImpl<>(
                buildTimeString(numericStats.getGlobalAverage()),
                buildTimeString(numericStats.getLowAverage()),
                buildTimeString(numericStats.getMediumAverage()),
                buildTimeString(numericStats.getHighAverage())
        );
    }

    private String buildTimeString(Long totalSeconds) {
        if (totalSeconds == null) {
            return null;
        }

        long hours = totalSeconds / (60 * 60);
        long minutes = (totalSeconds % (60 * 60)) / 60;
        long seconds = totalSeconds % 60;

        return String.format("%dh %dm %ds", hours, minutes, seconds);
    }
}
