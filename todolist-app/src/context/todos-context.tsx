import { createContext, FC, ReactNode, useContext } from "react";
import { useSearchParams } from "react-router-dom";
import { useQuery } from "react-query";

import { TodoResponse } from "@/models/todo";

// interface ContextProps {
//   data: TodoResponse;
//   currentPage: number;
// }

const TodosContext = createContext<TodoResponse | null>(null);

interface FetchTodosParams {
  page: string;
  size: string;
}

const fetchTodos = async (searchParams: FetchTodosParams) => {
  const params = new URLSearchParams({ ...searchParams });

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
  const [searchParams, setSearchParams] = useSearchParams();

  const currentPage = searchParams.get("page") ?? "1";
  const size = searchParams.get("size") ?? "10";

  const todosQuery = useQuery({
    queryKey: ["todos", currentPage],
    queryFn: () => fetchTodos({ page: currentPage, size }),
    keepPreviousData: true,
  });

  if (todosQuery.isLoading) {
    return <p>Loading...</p>;
  }

  if (todosQuery.isError) {
    return <p>Something went wrong</p>;
  }

  return (
    <TodosContext.Provider value={todosQuery.data!}>
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
