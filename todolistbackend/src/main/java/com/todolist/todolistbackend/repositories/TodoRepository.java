package com.todolist.todolistbackend.repositories;

import com.todolist.todolistbackend.enums.TodoPriority;
import com.todolist.todolistbackend.model.Todo;
import com.todolist.todolistbackend.web.PaginatedData;
import jakarta.annotation.Nullable;
import org.springframework.stereotype.Repository;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.*;
import java.util.stream.Stream;

@Repository
public class TodoRepository {
    private Map<String, Todo> db;

    public TodoRepository() {
        this.db = new HashMap<String, Todo>() {{
            put("1", new Todo("1", "Build Spring Boot REST API", TodoPriority.HIGH, LocalDateTime.of(2024, Month.AUGUST, 12, 12, 0), LocalDateTime.now()));
            put("2", new Todo("2", "Build React ToDo List app", TodoPriority.MEDIUM, LocalDateTime.of(2024, Month.AUGUST, 12, 12, 0), LocalDateTime.now()));
            put("3", new Todo("3", "Type out Week 4 Essay", TodoPriority.HIGH, LocalDateTime.of(2024, Month.AUGUST, 6, 12, 0), LocalDateTime.now()));
            put("4", new Todo("4", "Update university servers", TodoPriority.HIGH, LocalDateTime.of(2024, Month.AUGUST, 8, 23, 0), LocalDateTime.now()));
            put("5", new Todo("5", "Update laboratory apps and services", TodoPriority.HIGH, LocalDateTime.of(2024, Month.AUGUST, 10, 23, 59), LocalDateTime.now()));
            put("6", new Todo("6", "Research testing libraries", TodoPriority.MEDIUM, LocalDateTime.of(2024, Month.AUGUST, 12, 12, 0), LocalDateTime.now()));
            put("7", new Todo("7", "Change car radio", TodoPriority.LOW, LocalDateTime.of(2024, Month.AUGUST, 7, 18, 0), LocalDateTime.now()));
            put("8", new Todo("8", "Make the bed", TodoPriority.HIGH, LocalDateTime.of(2024, Month.AUGUST, 6, 5, 0), LocalDateTime.now()));
            put("9", new Todo("9", "Dust off desk", TodoPriority.LOW, LocalDateTime.of(2024, Month.AUGUST, 12, 6, 0), LocalDateTime.now()));
            put("10", new Todo("10", "Update LinkedIn", TodoPriority.LOW, LocalDateTime.of(2024, Month.SEPTEMBER, 12, 12, 0), LocalDateTime.now()));
            put("11", new Todo("11", "Clean the kitchen", TodoPriority.MEDIUM, LocalDateTime.of(2024, Month.AUGUST, 16, 12, 0), LocalDateTime.now()));
        }};
    }

    public Todo saveTodo(Todo todo) {
        this.db.put(todo.getId(), todo);

        return todo;
    }

    public Collection<Todo> findTodos() {
        return this.db.values();
    }

    public PaginatedData<Todo> findTodos(int page, int size, String title, String sortByPriority, String sortByDate, String status, TodoPriority priority) {
        ArrayList<Todo> todosList = getTodoList(title, status);
        sortTodos(todosList, sortByPriority, sortByDate);

        int totalItems = todosList.size();
        long offsetStart = (long) page * size;
        long offsetEnd = offsetStart + size;
        int totalPages = (int) Math.ceil((double) totalItems / size);

        List<Todo> paginatedTodos = offsetStart == 0 ? todosList.stream().limit(offsetEnd).toList() : this.db.values().stream().skip(offsetStart).limit(offsetEnd).toList();

        PaginatedData<Todo> paginatedResponse = new PaginatedData<>(page + 1, totalPages, totalItems, size, paginatedTodos);

        ServletUriComponentsBuilder builder = ServletUriComponentsBuilder.fromCurrentRequest();
        builder.replaceQueryParam("size", size);

        if (page > 0) {
            builder.replaceQueryParam("page", page);
            String uri = builder.build().toUriString();

            paginatedResponse.setPreviousPage(uri);
        }

        if (page + 1 < totalPages) {
            builder.replaceQueryParam("page", page + 2);
            String uri = builder.build().toUriString();

            paginatedResponse.setNextPage(uri);
        }

        return paginatedResponse;
    }

    private ArrayList<Todo> getTodoList(String title, String status) {
        Stream<Todo> todoStream = this.db.values().stream();

        if (title != null && !title.isEmpty()) {
            todoStream = todoStream.filter(todo -> todo.getTitle().toLowerCase().contains(title.toLowerCase()));
        }


        if (status != null && !status.isEmpty()) {
            if (!status.contains("all")) {
                todoStream = todoStream.filter(todo -> {
                    if (todo.isDone()) {
                        return status.contains("completed");
                    }

                    return status.contains("pending");
                });
            }
        }

        ArrayList<Todo> todosList = new ArrayList<>(todoStream.toList());
        return todosList;
    }

    public Todo findTodo(String id) {
        Todo todo = this.db.get(id);

        return todo;
    }

    public Todo updateTodo(String id, Todo todo) {
        this.db.replace(id, todo);

        return todo;
    }

    public Todo deleteTodo(String id) {
        return this.db.remove(id);
    }

    private void sortTodos(ArrayList<Todo> todos, @Nullable String sortByPriority, @Nullable String sortByDueDate) {
        Comparator priorityComparator = Comparator.comparingInt(Todo::getPriorityOrdinal);
        Comparator dueDateComparator = Comparator.comparing(Todo::getDue);
        Comparator createdAtComparator = Comparator.comparing(Todo::getCreatedAt).reversed();

        Comparator comparator = createdAtComparator;

        if (sortByPriority != null && sortByDueDate != null) {
            comparator = buildConditionalComparator(sortByPriority, priorityComparator);
            comparator = comparator.thenComparing(buildConditionalComparator(sortByDueDate, dueDateComparator));
        } else if (sortByPriority != null) {
            comparator = buildConditionalComparator(sortByPriority, priorityComparator);
        } else if (sortByDueDate != null) {
            comparator = buildConditionalComparator(sortByDueDate, dueDateComparator);
        }

        comparator = comparator.thenComparing(createdAtComparator);

        Collections.sort(todos, comparator);
    }

    private Comparator<Todo> buildConditionalComparator(String filter, Comparator<Todo> comparator) {
        switch (filter) {
            case "asc":
                return comparator;
            default:
                return comparator.reversed();
        }
    }
}
