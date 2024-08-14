import { render } from "@testing-library/react";
import React from "react";
import { QueryClient, QueryClientProvider } from "react-query";
import { vi } from "vitest";

export const renderWithClient = (
  client: QueryClient,
  ui: React.ReactElement,
) => {
  return render(
    <QueryClientProvider client={client}>{ui}</QueryClientProvider>,
  );
};

export const mockMarkAsDone = vi
  .fn()
  .mockImplementation(() => Promise.resolve());
export const mockUndoTodo = vi.fn().mockImplementation(() => Promise.resolve());

export const mockMarkAsDoneReject = vi
  .fn()
  .mockImplementation(() => Promise.reject());
export const mockUndoTodoReject = vi
  .fn()
  .mockImplementation(() => Promise.reject());
