import { describe, expect, it, test, vi } from "vitest";
import { render, screen } from "@testing-library/react";
import { userEvent } from "@testing-library/user-event";
import {
  TodoItem,
  TodoItemActions,
  TodoItemCheck,
  TodoItemDueDate,
  TodoItemPriority,
  TodoItemTitle,
} from "./todo-item";
import { TodoContextProps, TodosContext } from "@/context/todos-context";
import { QueryClient, QueryClientProvider, useMutation } from "react-query";
import { TodoResponse } from "@/models/todo";
import { ReactNode } from "react";

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
      due: null,
      completedAt: null,
      done: false,
      createdAt: new Date().toString(),
    },
  ],
};

describe("TodoItem", async () => {
  const queryClient = new QueryClient();

  const queryClientWrapper = ({ children }: { children: ReactNode }) => (
    <QueryClientProvider client={queryClient}>{children}</QueryClientProvider>
  );

  it("should render properly", async () => {
    render(
      <TodoItem>
        <TodoItemTitle>{todoTitle}</TodoItemTitle>
        <TodoItemPriority>{todoPriority}</TodoItemPriority>
        <TodoItemDueDate>{todoDueDate}</TodoItemDueDate>
      </TodoItem>,
    );

    const todoItem = screen.getByRole("listitem");

    expect(todoItem).not.toBeNull();
    expect(screen.getByText(todoTitle)).toBeDefined();
    expect(screen.getByText(todoPriority)).toBeDefined();
    expect(screen.getByText(todoDueDate)).toBeDefined();
  });

  it("checkbox should change state when clicked", async () => {
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
          <TodoItem>
            <TodoItemCheck todoId="1" />
            <TodoItemTitle>{todoTitle}</TodoItemTitle>
            <TodoItemPriority>{todoPriority}</TodoItemPriority>
            <TodoItemDueDate>{todoDueDate}</TodoItemDueDate>
          </TodoItem>
        </TodosContext.Provider>
      );
    }

    render(<TodoItemTest />, { wrapper: queryClientWrapper });

    const todoItemCheck = screen.getByRole("checkbox");

    expect(todoItemCheck).toBeDefined();
    expect(todoItemCheck.getAttribute("aria-checked")).toBe("false");

    await userEvent.click(todoItemCheck);

    expect(mockMarkAsDone).toHaveBeenCalled();
    expect(mockUndoTodo).not.toHaveBeenCalled();
    expect(todoItemCheck.getAttribute("aria-checked")).toBe("true");

    await userEvent.click(todoItemCheck);

    expect(mockUndoTodo).toHaveBeenCalled();
  });
});
