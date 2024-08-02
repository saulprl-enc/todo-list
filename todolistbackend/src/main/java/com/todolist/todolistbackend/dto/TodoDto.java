package com.todolist.todolistbackend.dto;

import com.todolist.todolistbackend.enums.TodoPriority;

import java.util.Date;

public record TodoDto(String id, String title, Date due, boolean done, Date completedAt, TodoPriority priority,
                      Date createdAt) {
}
