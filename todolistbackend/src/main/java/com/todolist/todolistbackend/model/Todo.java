package com.todolist.todolistbackend.model;

import com.todolist.todolistbackend.enums.TodoPriority;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class Todo implements Cloneable {
    private String id;

    @NotEmpty(message = "You must provide a title")
    @Size(min = 3, max = 120, message = "The title's length must be between 3 and 120 characters")
    private String title;
    private LocalDateTime due;
    private boolean done = false;
    private LocalDateTime completedAt;

    @NotNull(message = "You must provide a ToDo priority level")
    private TodoPriority priority;
    private LocalDateTime createdAt;

    public Todo() {
    }

    public Todo(String id, String title, LocalDateTime due, boolean done, LocalDateTime completedAt, TodoPriority priority, LocalDateTime createdAt) {
        this.id = id;
        this.title = title;
        this.due = due;
        this.done = done;
        this.completedAt = completedAt;
        this.priority = priority;
        this.createdAt = createdAt;
    }

    public Todo(String title, TodoPriority priority) {
        this.title = title;
        this.priority = priority;
    }

    public Todo(String title, LocalDateTime due, TodoPriority priority) {
        this.title = title;
        this.due = due;
        this.priority = priority;
    }

    public Todo(String id, String title, TodoPriority priority) {
        this.id = id;
        this.title = title;
        this.priority = priority;
    }

    public Todo(String id, String title, TodoPriority priority, LocalDateTime createdAt) {
        this.id = id;
        this.title = title;
        this.priority = priority;
        this.createdAt = createdAt;
    }

    public Todo(String id, String title, TodoPriority priority, LocalDateTime dueDate, LocalDateTime createdAt) {
        this.id = id;
        this.title = title;
        this.priority = priority;
        this.due = dueDate;
        this.createdAt = createdAt;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public boolean isDone() {
        return done;
    }

    public void setDone(boolean done) {
        this.done = done;
    }

    public TodoPriority getPriority() {
        return priority;
    }

    public int getPriorityOrdinal() {
        return priority.ordinal();
    }

    public void setPriority(TodoPriority priority) {
        this.priority = priority;
    }

    public LocalDateTime getDue() {
        return due;
    }

    public void setDue(LocalDateTime due) {
        this.due = due;
    }

    public LocalDateTime getCompletedAt() {
        return completedAt;
    }

    public void setCompletedAt(LocalDateTime completedAt) {
        this.completedAt = completedAt;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
}
