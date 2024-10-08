package com.todolist.todolistbackend.repositories;

import com.todolist.todolistbackend.dto.TodoStats;
import com.todolist.todolistbackend.dto.TodoStatsImpl;
import com.todolist.todolistbackend.enums.TodoPriority;
import com.todolist.todolistbackend.model.Todo;
import com.todolist.todolistbackend.web.PaginatedData;
import jakarta.annotation.Nullable;
import org.springframework.stereotype.Repository;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.time.LocalDateTime;
import java.time.Month;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Stream;

@Repository
public class TodoRepositoryImpl implements TodoRepository {
    private final Map<String, Todo> db;

    public TodoRepositoryImpl() {
        this.db = new HashMap<String, Todo>() {{
            put("1", new Todo("1", "Build Spring Boot REST API", TodoPriority.HIGH, LocalDateTime.of(2024, Month.AUGUST, 12, 12, 0), LocalDateTime.now()));
            put("2", new Todo("2", "Build React ToDo List app", TodoPriority.MEDIUM, LocalDateTime.of(2024, Month.AUGUST, 12, 12, 0), LocalDateTime.now()));
            put("3", new Todo("3", "Type out Week 4 Essay", TodoPriority.HIGH, LocalDateTime.of(2024, Month.AUGUST, 6, 12, 0), LocalDateTime.now()));
            put("4", new Todo("4", "Update university servers", TodoPriority.HIGH, LocalDateTime.of(2024, Month.AUGUST, 8, 23, 0), LocalDateTime.now()));
            put("5", new Todo("5", "Update laboratory apps and services", TodoPriority.HIGH, LocalDateTime.of(2024, Month.AUGUST, 10, 23, 59), LocalDateTime.now()));
            put("11", new Todo("11", "Clean the kitchen", TodoPriority.MEDIUM, LocalDateTime.now()));
            put("6", new Todo("6", "Research testing libraries", TodoPriority.MEDIUM, LocalDateTime.of(2024, Month.AUGUST, 12, 12, 0), LocalDateTime.now()));
            put("7", new Todo("7", "Change car radio", TodoPriority.LOW, LocalDateTime.of(2024, Month.AUGUST, 7, 18, 0), LocalDateTime.now()));
            put("8", new Todo("8", "Make the bed", TodoPriority.HIGH, LocalDateTime.of(2024, Month.AUGUST, 6, 5, 0), LocalDateTime.now()));
            put("9", new Todo("9", "Dust off desk", TodoPriority.LOW, LocalDateTime.of(2024, Month.AUGUST, 12, 6, 0), LocalDateTime.now()));
            put("10", new Todo("10", "Update LinkedIn", TodoPriority.LOW, LocalDateTime.of(2024, Month.SEPTEMBER, 12, 12, 0), LocalDateTime.now()));
        }};
    }

    TodoRepositoryImpl(Map<String, Todo> db) {
        this.db = db;
    }

    @Override
    public Todo saveTodo(Todo todo) {
        this.db.put(todo.getId(), todo);

        return todo;
    }

    @Override
    public List<Todo> findTodos() {
        return this.db.values().stream().toList();
    }

    @Override
    public PaginatedData<Todo> findTodos(int page, int size, String title, String sortByPriority, String sortByDate, String status, String priority) {
        ArrayList<Todo> todosList = new ArrayList<>(getTodoList(title, status, priority));
        sortTodos(todosList, sortByPriority, sortByDate);

        int totalItems = todosList.size();
        long offsetStart = (long) page * size;
        int totalPages = (int) Math.ceil((double) totalItems / size);

        List<Todo> paginatedTodos = todosList.stream().skip(offsetStart).limit(size).toList();

        PaginatedData<Todo> paginatedResponse = new PaginatedData<>(page + 1, totalPages, totalItems, size, paginatedTodos);

        return paginatedResponse;
    }

    List<Todo> getTodoList(String title, String status, String priority) {
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

        return todoStream.toList();
    }

    @Override
    public Todo findTodo(String id) {
        return this.db.get(id);
    }

    @Override
    public Todo updateTodo(String id, Todo todo) {
        this.db.replace(id, todo);

        return todo;
    }

    @Override
    public Todo deleteTodo(String id) {
        return this.db.remove(id);
    }

    @Override
    public TodoStats<Long> calculateTodoStats() {
        List<Todo> completedTodos = this.findCompletedTodos();

        long globalTotal = 0;
        long lowTotal = 0;
        long mediumTotal = 0;
        long highTotal = 0;

        long lowCount = 0;
        long mediumCount = 0;
        long highCount = 0;

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

        TodoStats<Long> stats = new TodoStatsImpl<>();

        if (!completedTodos.isEmpty()) {
            stats.setGlobalAverage(globalTotal / completedTodos.size());
        }
        if (lowCount > 0) {
            stats.setLowAverage(lowTotal / lowCount);
        }
        if (mediumCount > 0) {
            stats.setMediumAverage(mediumTotal / mediumCount);
        }
        if (highCount > 0) {
            stats.setHighAverage(highTotal / highCount);
        }

        return stats;
    }

    List<Todo> findCompletedTodos() {
        return this.db.values().stream().filter(Todo::isDone).toList();
    }

    void sortTodos(ArrayList<Todo> todos, @Nullable String sortByPriority, @Nullable String sortByDueDate) {
        Comparator<Todo> priorityComparator = Comparator.comparingInt(Todo::getPriorityOrdinal);
        Comparator<Todo> dueDateComparator = Comparator.comparing(Todo::getDue, Comparator.nullsFirst(Comparator.naturalOrder()));
        Comparator<Todo> createdAtComparator = Comparator.comparing(Todo::getCreatedAt).reversed();

        Comparator<Todo> comparator = createdAtComparator;

        if (sortByPriority != null && sortByDueDate != null) {
            comparator = buildConditionalComparator(sortByPriority, priorityComparator);
            comparator = comparator.thenComparing(buildConditionalComparator(sortByDueDate, dueDateComparator));
        } else if (sortByPriority != null) {
            comparator = buildConditionalComparator(sortByPriority, priorityComparator);
        } else if (sortByDueDate != null) {
            comparator = buildConditionalComparator(sortByDueDate, dueDateComparator);
        }

        comparator = comparator.thenComparing(createdAtComparator);

        todos.sort(comparator);
    }

    Comparator<Todo> buildConditionalComparator(String filter, Comparator<Todo> comparator) {
        if (filter.equals("asc")) {
            return comparator;
        }
        return comparator.reversed();
    }
}
