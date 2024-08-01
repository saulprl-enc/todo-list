package com.todolist.todolistbackend.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TodosController {
    @GetMapping("/todos")
    public String getTodos() {
        return "You have no todos yet. Start adding some!";
    }
}
