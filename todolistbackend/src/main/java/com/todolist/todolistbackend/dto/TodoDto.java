package com.todolist.todolistbackend.dto;

import com.todolist.todolistbackend.enums.TodoPriority;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.Date;

public record TodoDto(String id,
                      @NotEmpty(message = "You must provide a title") @Size(min = 3, max = 120, message = "The title's length must be between 3 and 120 characters") String title,
                      Date due, boolean done,
                      Date completedAt,
                      @NotNull(message = "You must provide a ToDo priority level") TodoPriority priority,
                      Date createdAt) {
}
