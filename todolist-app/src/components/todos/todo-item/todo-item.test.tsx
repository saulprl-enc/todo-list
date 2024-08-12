import { expect, test } from "vitest";
import { render, screen } from "@testing-library/react";
import {
  TodoItem,
  TodoItemDueDate,
  TodoItemPriority,
  TodoItemTitle,
} from "./todo-item";

test("The TodoItem component renders properly", async () => {
  render(
    <TodoItem>
      <TodoItemTitle>Do the dishes</TodoItemTitle>
      <TodoItemPriority>MEDIUM</TodoItemPriority>
      <TodoItemDueDate>2024/08/12</TodoItemDueDate>
    </TodoItem>,
  );

  const todoItem = screen.getByRole("listitem");

  expect(todoItem).not.toBeNull();
});
