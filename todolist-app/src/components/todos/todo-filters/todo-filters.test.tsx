import { describe, expect, it, vi } from "vitest";
import { TodoFilters } from "./todo-filters";
import { render, screen } from "@testing-library/react";
import { MemoryRouter } from "react-router-dom";
import { useState } from "react";

const mockParams = {
  title: "dishes",
  priority: "high,medium",
  status: "done",
};

vi.mock("react-router-dom", async (importOriginal) => {
  const actual = await importOriginal();

  return {
    ...actual,
    useSearchParams: () => [
      new URLSearchParams(mockParams),
      () => {
        const [params, setParams] = useState(new URLSearchParams(mockParams));
        return [
          params,
          (newParams: string) => {
            setParams(new URLSearchParams(newParams));
          },
        ];
      },
    ],
  };
});

describe("TodoFilters", async () => {
  it("should render with populated title", async () => {
    render(
      <MemoryRouter>
        <TodoFilters />
      </MemoryRouter>,
    );

    const titleInput = screen.getByTestId("title-field");
    const titleInputValue = titleInput.getAttribute("value");

    const priorityField = screen.getByTestId("priority-field");
    const statusField = screen.getByTestId("status-field");

    expect(titleInputValue).toBe("dishes");
    expect(priorityField.textContent).toMatch(/medium|high/i);
    expect(priorityField.textContent).not.toMatch(/low/i);
    expect(statusField.textContent).toMatch(/done/i);
    expect(statusField.textContent).not.toMatch(/pending/i);
  });
});
