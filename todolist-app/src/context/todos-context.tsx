import { createContext, FC, ReactNode, useContext } from "react";
import { useSearchParams } from "react-router-dom";
import {
  useMutation,
  UseMutationResult,
  useQuery,
  useQueryClient,
} from "react-query";

import { Todo, TodoResponse } from "@/models/todo";

interface ContextProps {
  data: TodoResponse;
  markAsDone: UseMutationResult<void, unknown, string, unknown>;
  undoTodo: UseMutationResult<void, unknown, string, unknown>;
}

const TodosContext = createContext<ContextProps | null>(null);

interface FetchTodosParams {
  page: string;
  size: string;
  title: string;
  priority: string;
  status: string;
  sortByPriority?: string;
  sortByDate?: string;
}

const fetchTodos = async (searchParams: FetchTodosParams) => {
  const params = new URLSearchParams({ ...searchParams });

  if (!searchParams.sortByPriority) {
    params.delete("sortByPriority");
  }

  if (!searchParams.sortByDate) {
    params.delete("sortbyDate");
  }

  const res = await fetch(`http://localhost:9090/todos?${params.toString()}`);

  const data = await res.json();

  if (!res.ok) {
    console.error(data);

    throw new Error("Unable to connect to the server");
  }

  const todoResParse = TodoResponse.safeParse(data);

  if (!todoResParse.success) {
    console.error(todoResParse.error.toString());

    throw new Error("Received unexpected data");
  }

  return todoResParse.data;
};

interface ProviderProps {
  children: ReactNode;
}

export const TodosProvider: FC<ProviderProps> = ({ children }) => {
  const queryClient = useQueryClient();
  const [searchParams] = useSearchParams();

  const currentPage = searchParams.get("page") ?? "1";
  const size = searchParams.get("size") ?? "10";
  const searchTitle = searchParams.get("title") ?? "";
  const priority = searchParams.get("priority") ?? "all";
  const status = searchParams.get("status") ?? "all";
  const sortByPriority = searchParams.get("sortByPriority") ?? undefined;
  const sortByDate = searchParams.get("sortByDate") ?? undefined;

  const todosQuery = useQuery({
    queryKey: [
      "todos",
      currentPage,
      { title: searchTitle, priority, status, sortByPriority, sortByDate },
    ],
    queryFn: () =>
      fetchTodos({
        page: currentPage,
        size,
        title: searchTitle,
        priority,
        status,
        sortByDate,
        sortByPriority,
      }),
    keepPreviousData: true,
  });

  const markAsDone = useMutation(async (id: string) => {
    const res = await fetch(`http://localhost:9090/todos/${id}/done`, {
      method: "POST",
    });

    const data = await res.json();

    if (!res.ok) {
      console.error(data);

      throw new Error("Unable to mark ToDo as done.");
    }

    const todoParse = Todo.safeParse(data);

    if (!todoParse.success) {
      console.error(todoParse.error.toString());

      throw new Error("Received unexpected response from the server.");
    }

    queryClient.invalidateQueries(["todos"]);
    queryClient.invalidateQueries(["todos-stats"]);
  });

  const undoTodo = useMutation(async (id: string) => {
    const res = await fetch(`http://localhost:9090/todos/${id}/undone`, {
      method: "PUT",
    });

    const data = await res.json();

    if (!res.ok) {
      console.error(data);

      throw new Error("Unable to mark ToDo as done.");
    }

    const todoParse = Todo.safeParse(data);

    if (!todoParse.success) {
      console.error(todoParse.error.toString());

      throw new Error("Received unexpected response from the server.");
    }

    queryClient.invalidateQueries(["todos"]);
    queryClient.invalidateQueries(["todos-stats"]);
  });

  if (todosQuery.isLoading) {
    return <p>Loading...</p>;
  }

  if (todosQuery.isError) {
    return <p>Something went wrong</p>;
  }

  return (
    <TodosContext.Provider
      value={{ data: todosQuery.data!, markAsDone, undoTodo }}
    >
      {children}
    </TodosContext.Provider>
  );
};

export const useTodos = () => {
  const ctx = useContext(TodosContext);

  if (!ctx) {
    throw new Error("useTodos must be used within a TodosProvider");
  }

  return ctx;
};
