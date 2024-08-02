package com.todolist.todolistbackend.model;

import com.todolist.todolistbackend.enums.TodoPriority;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.Date;

public class Todo {
    private String id;

    @NotEmpty(message = "You must provide a title")
    @Size(min = 3, max = 120, message = "The title's length must be between 3 and 120 characters")
    private String title;
    private Date due;
    private boolean done = false;
    private Date completedAt;

    @NotNull(message = "You must provide a ToDo priority level")
    private TodoPriority priority;
    private Date createdAt;

    public Todo() {
    }

    public Todo(String id, String title, Date due, boolean done, Date completedAt, TodoPriority priority, Date createdAt) {
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

    public Date getCompletedAt() {
        return completedAt;
    }

    public void setCompletedAt(Date completedAt) {
        this.completedAt = completedAt;
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
