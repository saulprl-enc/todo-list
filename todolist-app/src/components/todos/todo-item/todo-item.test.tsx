import { describe, expect, it, vi } from "vitest";
import { render, screen } from "@testing-library/react";
import { userEvent } from "@testing-library/user-event";
import {
  TodoItemWrapper,
  TodoItemDueDate,
  TodoItemPriority,
  TodoItemTitle,
  TodoItem,
} from "./todo-item";
import { TodoContextProps, TodosContext } from "@/context/todos-context";
import { QueryClient, useMutation } from "react-query";
import { TodoResponse } from "@/models/todo";
import { renderWithClient } from "@/lib/test-utils";
import { format } from "date-fns";

const todoTitle = "Do the dishes";
const todoPriority = "MEDIUM";
const todoDueDate = "2024/08/12";

const todoResData: TodoResponse = {
  currentPage: 1,
  totalPages: 2,
  totalItems: 11,
  nextPage: null,
  previousPage: null,
  size: 10,
  data: [
    {
      id: "1",
      title: "Do the dishes",
      priority: "MEDIUM",
      due: "2024-08-14",
      completedAt: null,
      done: false,
      createdAt: new Date().toString(),
    },
  ],
};

describe("TodoItem", async () => {
  const queryClient = new QueryClient();

  it("should render properly", async () => {
    render(
      <TodoItemWrapper>
        <TodoItemTitle>{todoTitle}</TodoItemTitle>
        <TodoItemPriority>{todoPriority}</TodoItemPriority>
        <TodoItemDueDate>{todoDueDate}</TodoItemDueDate>
      </TodoItemWrapper>,
    );

    const todoItem = screen.getByRole("listitem");

    expect(todoItem).not.toBeNull();
    expect(screen.getByText(todoTitle)).toBeDefined();
    expect(screen.getByText(todoPriority)).toBeDefined();
    expect(screen.getByText(todoDueDate)).toBeDefined();
  });

  it("checkbox should set to true when clicked and call corresponding mutation", async () => {
    const mockMarkAsDone = vi.fn().mockImplementation(() => Promise.resolve());
    const mockUndoTodo = vi.fn().mockImplementation(() => Promise.resolve());

    function TodoItemTest() {
      const contextValue: TodoContextProps = {
        data: { ...todoResData },
        markAsDone: useMutation((_: string) => mockMarkAsDone()),
        undoTodo: useMutation((_: string) => mockUndoTodo()),
      };

      return (
        <TodosContext.Provider value={contextValue}>
          <TodoItem todo={contextValue.data.data[0]} />
        </TodosContext.Provider>
      );
    }

    renderWithClient(queryClient, <TodoItemTest />);

    const todoItemCheck = screen.getByTestId("todo-checkbox");

    expect(todoItemCheck.getAttribute("aria-checked")).toBe("false");

    await userEvent.click(todoItemCheck);

    expect(mockMarkAsDone).toHaveBeenCalled();
    expect(mockUndoTodo).not.toHaveBeenCalled();
    expect(todoItemCheck.getAttribute("aria-checked")).toBe("true");
  });

  it("checkbox should set to false when clicked and call corresponding mutation", async () => {
    const mockMarkAsDone = vi.fn().mockImplementation(() => Promise.resolve());
    const mockUndoTodo = vi.fn().mockImplementation(() => Promise.resolve());

    function TodoItemTest() {
      const contextValue: TodoContextProps = {
        data: { ...todoResData },
        markAsDone: useMutation((_: string) => mockMarkAsDone()),
        undoTodo: useMutation((_: string) => mockUndoTodo()),
      };

      return (
        <TodosContext.Provider value={contextValue}>
          <TodoItem todo={{ ...contextValue.data.data[0], done: true }} />
        </TodosContext.Provider>
      );
    }

    renderWithClient(queryClient, <TodoItemTest />);

    const todoItemCheck = screen.getByTestId("todo-checkbox");

    expect(todoItemCheck.getAttribute("aria-checked")).toBe("true");

    await userEvent.click(todoItemCheck);

    expect(mockUndoTodo).toHaveBeenCalled();
    expect(mockMarkAsDone).not.toHaveBeenCalled();
    expect(todoItemCheck.getAttribute("aria-checked")).toBe("false");
  });

  it("checkbox should reset state when mutation is rejected", async () => {
    const mockMarkAsDone = vi.fn().mockImplementation(() => Promise.reject());
    const mockUndoTodo = vi.fn().mockImplementation(() => Promise.reject());

    function TodoItemTest() {
      const contextValue: TodoContextProps = {
        data: { ...todoResData },
        markAsDone: useMutation((_: string) => mockMarkAsDone()),
        undoTodo: useMutation((_: string) => mockUndoTodo()),
      };

      return (
        <TodosContext.Provider value={contextValue}>
          <TodoItem todo={contextValue.data.data[0]} />
        </TodosContext.Provider>
      );
    }

    renderWithClient(queryClient, <TodoItemTest />);

    const todoItemCheck = screen.getByTestId("todo-checkbox");

    expect(todoItemCheck.getAttribute("aria-checked")).toBe("false");

    await userEvent.click(todoItemCheck);

    expect(mockMarkAsDone).toHaveBeenCalled();
    expect(mockUndoTodo).not.toHaveBeenCalled();
    expect(todoItemCheck.getAttribute("aria-checked")).toBe("false");
  });

  it("date should have the correct format", async () => {
    const mockMarkAsDone = vi.fn().mockImplementation(() => Promise.reject());
    const mockUndoTodo = vi.fn().mockImplementation(() => Promise.reject());

    function TodoItemTest() {
      const contextValue: TodoContextProps = {
        data: { ...todoResData },
        markAsDone: useMutation((_: string) => mockMarkAsDone()),
        undoTodo: useMutation((_: string) => mockUndoTodo()),
      };

      return (
        <TodosContext.Provider value={contextValue}>
          <TodoItem todo={contextValue.data.data[0]} />
        </TodosContext.Provider>
      );
    }

    renderWithClient(queryClient, <TodoItemTest />);

    const todoItemDate = screen.getByTestId("todo-due");

    expect(todoItemDate.textContent).toBe(
      format(todoResData.data[0].due!, "yyyy/MM/dd"),
    );
  });

  it("date should be a hyphen when null", async () => {
    const mockMarkAsDone = vi.fn().mockImplementation(() => Promise.reject());
    const mockUndoTodo = vi.fn().mockImplementation(() => Promise.reject());

    function TodoItemTest() {
      const contextValue: TodoContextProps = {
        data: { ...todoResData },
        markAsDone: useMutation((_: string) => mockMarkAsDone()),
        undoTodo: useMutation((_: string) => mockUndoTodo()),
      };

      return (
        <TodosContext.Provider value={contextValue}>
          <TodoItem todo={{ ...contextValue.data.data[0], due: null }} />
        </TodosContext.Provider>
      );
    }

    renderWithClient(queryClient, <TodoItemTest />);

    const todoItemDate = screen.getByTestId("todo-due");

    expect(todoItemDate.textContent).toBe("-");
  });
});
