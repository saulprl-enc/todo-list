import { TodoContextProps, TodosContext } from "@/context/todos-context";
import {
  mockMarkAsDone,
  mockUndoTodo,
  renderWithClient,
} from "@/lib/test-utils";
import { TodoResponse } from "@/models/todo";
import { QueryClient, useMutation } from "react-query";
import { describe, expect, it } from "vitest";
import { screen } from "@testing-library/react";
import { TodoList } from "./todo-list";
import { MemoryRouter } from "react-router-dom";

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
    {
      id: "2",
      title: "Buy cat food",
      priority: "HIGH",
      due: "2024-08-14",
      completedAt: null,
      done: false,
      createdAt: new Date().toString(),
    },
    {
      id: "3",
      title: "Finish testing",
      priority: "HIGH",
      due: "2024-08-14",
      completedAt: null,
      done: false,
      createdAt: new Date().toString(),
    },
  ],
};

describe("TodoList", async () => {
  const queryClient = new QueryClient();

  it("should render three items", async () => {
    function TodoListTest() {
      const contextValue: TodoContextProps = {
        data: todoResData,
        markAsDone: useMutation((_: string) => mockMarkAsDone()),
        undoTodo: useMutation((_: string) => mockUndoTodo()),
      };

      return (
        <MemoryRouter>
          <TodosContext.Provider value={contextValue}>
            <TodoList />
          </TodosContext.Provider>
        </MemoryRouter>
      );
    }

    renderWithClient(queryClient, <TodoListTest />);

    const todoList = screen.getByTestId("todo-list");

    expect(todoList.children.length).toBe(3);
  });
});
