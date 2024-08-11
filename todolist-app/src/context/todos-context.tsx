import { createContext, FC, ReactNode, useContext } from "react";
import { useQuery } from "react-query";

import { TodoResponse } from "@/models/todo";

const TodosContext = createContext<TodoResponse | null>(null);

const fetchTodos = async () => {
  const res = await fetch("http://localhost:9090/todos");

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
  const todosQuery = useQuery({ queryKey: ["todos"], queryFn: fetchTodos });

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
