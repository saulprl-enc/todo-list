package com.todolist.todolistbackend.model;

import com.todolist.todolistbackend.enums.TodoPriority;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.Date;

public class Todo {
    private String id;

    @NotEmpty(message = "You must provide a title")
    @Max(value = 120, message = "The title must not be longer than 120 characters")
    private String title;
    private Date due;
    private boolean done = false;
    private Date completed;

    @NotNull(message = "You must provide a ToDo priority level")
    private TodoPriority priority;
    private Date createdAt;

    public Todo() {
    }

    public Todo(String id, String title, Date due, boolean done, Date completed, TodoPriority priority, Date createdAt) {
        this.id = id;
        this.title = title;
        this.due = due;
        this.done = done;
        this.completed = completed;
        this.priority = priority;
        this.createdAt = createdAt;
    }

    public Todo(String title, TodoPriority priority) {
        this.title = title;
        this.priority = priority;
    }

    public Todo(String title, Date due, TodoPriority priority) {
        this.title = title;
        this.due = due;
        this.priority = priority;
    }

    public Todo(String id, String title, TodoPriority priority) {
        this.id = id;
        this.title = title;
        this.priority = priority;
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

    public Date getDue() {
        return due;
    }

    public void setDue(Date due) {
        this.due = due;
    }

    public boolean isDone() {
        return done;
    }

    public void setDone(boolean done) {
        this.done = done;
    }

    public Date getCompleted() {
        return completed;
    }

    public void setCompleted(Date completed) {
        this.completed = completed;
    }

    public TodoPriority getPriority() {
        return priority;
    }

    public void setPriority(TodoPriority priority) {
        this.priority = priority;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }
}
