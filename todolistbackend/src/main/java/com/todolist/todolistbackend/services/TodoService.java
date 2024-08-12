package com.todolist.todolistbackend.services;

import com.todolist.todolistbackend.dto.TodoStats;
import com.todolist.todolistbackend.enums.TodoPriority;
import com.todolist.todolistbackend.model.Todo;
import com.todolist.todolistbackend.repositories.TodoRepository;
import com.todolist.todolistbackend.web.PaginatedData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

@Service
public class TodoService {

    @Autowired
    private TodoRepository repo;

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

    public TodoStats calculateTodoStats() {
        ArrayList<Todo> todos = this.repo.findTodos();
        List<Todo> completedTodos = todos.stream().filter(Todo::isDone).toList();


        long globalTotal = 0;
        long lowTotal = 0;
        long mediumTotal = 0;
        long highTotal = 0;

        int lowCount = 0;
        int mediumCount = 0;
        int highCount = 0;

        for (Todo todo : completedTodos) {
            long difference = todo.getCreatedAt().until(todo.getCompletedAt(), ChronoUnit.SECONDS);
            globalTotal += difference;

            switch (todo.getPriority()) {
                case TodoPriority.LOW:
                    lowTotal += difference;
                    lowCount++;
                    break;
                case TodoPriority.MEDIUM:
                    mediumTotal += difference;
                    mediumCount++;
                    break;
                case TodoPriority.HIGH:
                    highTotal += difference;
                    highCount++;
                    break;
            }
        }

        TodoStats stats = new TodoStats();

        if (!completedTodos.isEmpty()) {
            stats.setGlobalAverage(buildTimeString(globalTotal / completedTodos.size()));
        }
        if (lowCount > 0) {
            stats.setLowAverage(buildTimeString(lowTotal / lowCount));
        }
        if (mediumCount > 0) {
            stats.setMediumAverage(buildTimeString(mediumTotal / mediumCount));
        }
        if (highCount > 0) {
            stats.setHighAverage(buildTimeString(highTotal / highCount));
        }

        return stats;
    }

    private String buildTimeString(long totalSeconds) {
        long hours = totalSeconds / (60 * 60);
        long minutes = (totalSeconds % (60 * 60)) / 60;
        long seconds = totalSeconds % 60;

        return String.format("%dh %dm %ds", hours, minutes, seconds);
    }
}
