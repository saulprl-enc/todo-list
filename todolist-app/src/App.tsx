import { CreateTodoButton } from "./components/todos/create-todo/create-todo-button";
import { TodoFilters } from "./components/todos/todo-filters/todo-filters";
import { TodoPagination } from "./components/todos/todo-pagination/todo-pagination";
import { TodoStats } from "./components/todos/todo-stats/todo-stats";
import { TodoList } from "./components/todos/todo-list/todo-list";
import { TodosProvider } from "./context/todos-context";

function App() {
  return (
    <TodosProvider>
      <div className="h-screen w-screen bg-muted p-2">
        <main className="flex size-full flex-col gap-2 rounded-md bg-white p-2">
          <TodoFilters />
          <CreateTodoButton />
          <TodoList />
          <TodoPagination />
          <TodoStats />
        </main>
      </div>
    </TodosProvider>
  );
}

export default App;
