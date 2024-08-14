package com.todolist.todolistbackend.repositories;

import static org.assertj.core.api.Assertions.assertThat;

import com.todolist.todolistbackend.dto.TodoStats;
import com.todolist.todolistbackend.enums.TodoPriority;
import com.todolist.todolistbackend.model.Todo;
import com.todolist.todolistbackend.web.PaginatedData;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.HashMap;
import java.util.Map;

class TodoRepositoryImplTest {
    private LocalDateTime initTimestamp = LocalDateTime.now();

    private Map<String, Todo> mockDb = new HashMap<>() {{
        put("1", new Todo("1", "Update laboratory apps and services", TodoPriority.LOW, LocalDateTime.of(2024, Month.AUGUST, 20, 12, 0), initTimestamp));
        put("2", new Todo("2", "Restore university servers", TodoPriority.MEDIUM, LocalDateTime.of(2024, Month.AUGUST, 18, 12, 0), initTimestamp));
        put("3", new Todo("3", "Finish ToDo List app", TodoPriority.HIGH, LocalDateTime.of(2024, Month.AUGUST, 14, 17, 0), initTimestamp));
        put("4", new Todo("4", "Prepare for demo day", TodoPriority.HIGH, LocalDateTime.of(2024, Month.AUGUST, 16, 12, 0), initTimestamp));
        put("5", new Todo("5", "Change car radio", TodoPriority.MEDIUM, LocalDateTime.of(2024, Month.AUGUST, 8, 18, 0), initTimestamp));
        put("6", new Todo("6", "Fix car's manual transmission", TodoPriority.LOW, LocalDateTime.of(2024, Month.AUGUST, 12, 20, 0), initTimestamp));
    }};

    @Test
    void shouldRetrieveAllTodos() {
        TodoRepository repo = new TodoRepositoryImpl(this.mockDb);

        assertThat(repo.findTodos()).hasSize(this.mockDb.size());
    }

    @Test
    void shouldRetrievePaginatedData() {
        TodoRepository repo = new TodoRepositoryImpl(this.mockDb);

        PaginatedData<Todo> data = repo.findTodos(0, 3, null, null, null, null, null);
        assertThat(data.getData()).hasSize(3);
        assertThat(data.getCurrentPage()).isEqualTo(1);
        assertThat(data.getTotalItems()).isEqualTo(this.mockDb.size());
        assertThat(data.getTotalPages()).isEqualTo(2);
    }

    @Test
    void shouldRetrieveFilteredToDosByName() {
        TodoRepository repo = new TodoRepositoryImpl(this.mockDb);

        PaginatedData<Todo> data = repo.findTodos(0, 3, "car", null, null, null, null);
        assertThat(data.getData()).hasSize(2);
        assertThat(data.getTotalItems()).isEqualTo(2);
    }

    @Test
    void shouldRetrieveSortedToDosByPriorityDescending() {
        TodoRepository repo = new TodoRepositoryImpl(this.mockDb);

        PaginatedData<Todo> data = repo.findTodos(0, 3, null, "desc", null, null, null);
        assertThat(data.getData().getFirst().getId()).isEqualTo("3");
        assertThat(data.getData().getLast().getId()).isEqualTo("2");
    }

    @Test
    void shouldRetrieveSortedToDosByPriorityAscending() {
        TodoRepository repo = new TodoRepositoryImpl(this.mockDb);

        PaginatedData<Todo> data = repo.findTodos(0, 3, null, "asc", null, null, null);
        assertThat(data.getData().getFirst().getId()).isEqualTo("1");
        assertThat(data.getData().getLast().getId()).isEqualTo("2");
    }

    @Test
    void shouldReturnSortedToDosByDueDateDescending() {
        TodoRepository repo = new TodoRepositoryImpl(this.mockDb);

        PaginatedData<Todo> data = repo.findTodos(0, 3, null, null, "desc", null, null);
        assertThat(data.getData().getFirst().getId()).isEqualTo("1");
        assertThat(data.getData().getLast().getId()).isEqualTo("4");
    }

    @Test
    void shouldReturnSortedToDosByDueDateAscending() {
        TodoRepository repo = new TodoRepositoryImpl(this.mockDb);

        PaginatedData<Todo> data = repo.findTodos(0, 3, null, null, "asc", null, null);
        assertThat(data.getData().getFirst().getId()).isEqualTo("5");
        assertThat(data.getData().getLast().getId()).isEqualTo("3");
    }

    @Test
    void shouldReturnPendingToDos() {
        TodoRepository repo = new TodoRepositoryImpl(this.mockDb);
        Todo todo = repo.findTodo("1");
        todo.setDone(true);

        PaginatedData<Todo> data = repo.findTodos(0, 3, null, null, null, "pending", null);
        assertThat(data.getTotalItems()).isEqualTo(5);
    }

    @Test
    void shouldReturnDoneToDos() {
        TodoRepository repo = new TodoRepositoryImpl(this.mockDb);
        Todo todo = repo.findTodo("1");
        todo.setDone(true);

        PaginatedData<Todo> data = repo.findTodos(0, 3, null, null, null, "done", null);
        assertThat(data.getTotalItems()).isEqualTo(1);
    }

    @Test
    void shouldReturnHighPriorityToDos() {
        TodoRepository repo = new TodoRepositoryImpl(this.mockDb);
        PaginatedData<Todo> data = repo.findTodos(0, 3, null, null, null, null, "high");
        assertThat(data.getTotalItems()).isEqualTo(2);
        assertThat(data.getData().getFirst().getPriority()).isEqualTo(TodoPriority.HIGH);
    }

    @Test
    void shouldReturnMediumPriorityToDos() {
        TodoRepository repo = new TodoRepositoryImpl(this.mockDb);
        PaginatedData<Todo> data = repo.findTodos(0, 3, null, null, null, null, "medium");
        assertThat(data.getTotalItems()).isEqualTo(2);
        assertThat(data.getData().getFirst().getPriority()).isEqualTo(TodoPriority.MEDIUM);
    }

    @Test
    void shouldReturnLowPriorityToDos() {
        TodoRepository repo = new TodoRepositoryImpl(this.mockDb);
        PaginatedData<Todo> data = repo.findTodos(0, 3, null, null, null, null, "low");
        assertThat(data.getTotalItems()).isEqualTo(2);
        assertThat(data.getData().getFirst().getPriority()).isEqualTo(TodoPriority.LOW);
    }

    @Test
    void shouldReturnSortedToDos_ByPriorityAscending_ByDueDateDescending() {
        TodoRepository repo = new TodoRepositoryImpl(this.mockDb);
        PaginatedData<Todo> data = repo.findTodos(0, 3, null, "asc", "desc", null, null);
        assertThat(data.getData().getFirst().getId()).isEqualTo("1");
        assertThat(data.getData().getLast().getId()).isEqualTo("2");
    }

    @Test
    void shouldReturnSortedToDos_ByPriorityDescending_ByDueDateAscending() {
        TodoRepository repo = new TodoRepositoryImpl(this.mockDb);
        PaginatedData<Todo> data = repo.findTodos(0, 3, null, "desc", "asc", null, null);
        assertThat(data.getData().getFirst().getId()).isEqualTo("3");
        assertThat(data.getData().getLast().getId()).isEqualTo("5");
    }

    @Test
    void shouldReturnCorrectAverages() {
        TodoRepository repo = new TodoRepositoryImpl(this.mockDb);

        for (Todo todo : repo.findTodos()) {
            todo.setDone(true);
            switch (todo.getPriority()) {
                case TodoPriority.HIGH:
                    todo.setCompletedAt(initTimestamp.plusMinutes(1));
                    break;
                case TodoPriority.MEDIUM:
                    todo.setCompletedAt(initTimestamp.plusMinutes(2));
                    break;
                case TodoPriority.LOW:
                    todo.setCompletedAt(initTimestamp.plusMinutes(3));
                    break;
            }
        }

        TodoStats<Long> stats = repo.calculateTodoStats();
        assertThat(stats.getGlobalAverage()).isEqualTo(120);
        assertThat(stats.getHighAverage()).isEqualTo(60);
        assertThat(stats.getMediumAverage()).isEqualTo(120);
        assertThat(stats.getLowAverage()).isEqualTo(180);
    }

    @Test
    void shouldUpdateTodoTitle_Priority_DueDate() throws CloneNotSupportedException {
        LocalDateTime updatedDueDate = LocalDateTime.now();

        TodoRepository repo = new TodoRepositoryImpl(this.mockDb);
        Todo todo = (Todo) repo.findTodo("1").clone();
        todo.setTitle("Random title");
        todo.setPriority(TodoPriority.HIGH);
        todo.setDue(updatedDueDate);

        repo.updateTodo("1", todo);
        Todo updatedTodo = repo.findTodo("1");

        assertThat(updatedTodo.getTitle()).isEqualTo("Random title");
        assertThat(updatedTodo.getPriority()).isEqualTo(TodoPriority.HIGH);
        assertThat(updatedTodo.getDue()).isEqualTo(updatedDueDate);
    }

    @Test
    void shouldDeleteTodo() {
        TodoRepository repo = new TodoRepositoryImpl(this.mockDb);
        repo.deleteTodo("1");

        assertThat(repo.findTodo("1")).isEqualTo(null);
    }

    @Test
    void shouldCreateTodo() {
        TodoRepository repo = new TodoRepositoryImpl(this.mockDb);
        repo.saveTodo(new Todo("10", "Random title", TodoPriority.HIGH, LocalDateTime.now(), LocalDateTime.now()));

        assertThat(repo.findTodo("10").getTitle()).isEqualTo("Random title");
    }
}