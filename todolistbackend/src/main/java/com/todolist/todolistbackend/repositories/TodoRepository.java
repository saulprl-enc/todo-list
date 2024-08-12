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
    private final Map<String, Todo> db;

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
            put("11", new Todo("11", "Clean the kitchen", TodoPriority.MEDIUM, LocalDateTime.now()));
        }};
    }

    public Todo saveTodo(Todo todo) {
        this.db.put(todo.getId(), todo);

        return todo;
    }

    public ArrayList<Todo> findTodos() {
        return new ArrayList<>(this.db.values().stream().toList());
    }

    public PaginatedData<Todo> findTodos(int page, int size, String title, String sortByPriority, String sortByDate, String status, String priority) {
        ArrayList<Todo> todosList = getTodoList(title, status, priority);
        sortTodos(todosList, sortByPriority, sortByDate);

        int totalItems = todosList.size();
        long offsetStart = (long) page * size;
        int totalPages = (int) Math.ceil((double) totalItems / size);

        List<Todo> paginatedTodos = todosList.stream().skip(offsetStart).limit(size).toList();

        PaginatedData<Todo> paginatedResponse = new PaginatedData<>(page + 1, totalPages, totalItems, size, paginatedTodos);

        ServletUriComponentsBuilder builder = ServletUriComponentsBuilder.fromCurrentRequest();
        builder.replaceQueryParam("size", size);

        if (page > 0) {
            builder.replaceQueryParam("page", Math.min(page, totalPages));
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

    private ArrayList<Todo> getTodoList(String title, String status, String priority) {
        Stream<Todo> todoStream = this.db.values().stream();

        if (title != null && !title.isEmpty()) {
            String normalizedTitle = title.toLowerCase();
            todoStream = todoStream.filter(todo -> todo.getTitle().toLowerCase().contains(normalizedTitle));
        }


        if (status != null && !status.isEmpty()) {
            String normalizedStatus = status.toLowerCase();

            if (!normalizedStatus.contains("all")) {
                todoStream = todoStream.filter(todo -> {
                    if (todo.isDone()) {
                        return normalizedStatus.contains("done");
                    }

                    return normalizedStatus.contains("pending");
                });
            }
        }

        if (priority != null && !priority.isEmpty()) {
            String normalizedPriority = priority.toLowerCase();

            if (!normalizedPriority.contains("all")) {
                todoStream = todoStream.filter(todo -> switch (todo.getPriority()) {
                    case TodoPriority.HIGH -> normalizedPriority.contains("high");
                    case TodoPriority.MEDIUM -> normalizedPriority.contains("medium");
                    case TodoPriority.LOW -> normalizedPriority.contains("low");
                });
            }
        }

        return new ArrayList<>(todoStream.toList());
    }

    public Todo findTodo(String id) {
        return this.db.get(id);
    }

    public Todo updateTodo(String id, Todo todo) {
        this.db.replace(id, todo);

        return todo;
    }

    public Todo deleteTodo(String id) {
        return this.db.remove(id);
    }

    private void sortTodos(ArrayList<Todo> todos, @Nullable String sortByPriority, @Nullable String sortByDueDate) {
        Comparator<Todo> priorityComparator = Comparator.comparingInt(Todo::getPriorityOrdinal);
        Comparator<Todo> dueDateComparator = Comparator.comparing(Todo::getDue, Comparator.nullsFirst(Comparator.naturalOrder()));
        Comparator<Todo> createdAtComparator = Comparator.comparing(Todo::getCreatedAt).reversed();

        Comparator<Todo> comparator = createdAtComparator;

        if (sortByPriority != null && sortByDueDate != null) {
            comparator = buildConditionalComparator(sortByPriority, priorityComparator);
            comparator = Comparator.nullsFirst(comparator.thenComparing(buildConditionalComparator(sortByDueDate, dueDateComparator)));
        } else if (sortByPriority != null) {
            comparator = buildConditionalComparator(sortByPriority, priorityComparator);
        } else if (sortByDueDate != null) {
            comparator = Comparator.nullsFirst(buildConditionalComparator(sortByDueDate, dueDateComparator));
        }

        comparator = comparator.thenComparing(createdAtComparator);

        todos.sort(comparator);
    }

    private Comparator<Todo> buildConditionalComparator(String filter, Comparator<Todo> comparator) {
        if (filter.equals("asc")) {
            return comparator;
        }
        return comparator.reversed();
    }
}
