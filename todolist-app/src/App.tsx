import { CreateTodoButton } from "./components/todos/create-todo/create-todo-button";
import { TodoFilters } from "./components/todos/todo-filters/todo-filters";
import { TodoStats } from "./components/todos/todo-stats/todo-stats";
import { TodoTable } from "./components/todos/todo-table/todo-table";

function App() {
  return (
    <div className="h-screen w-screen bg-muted p-2">
      <main className="flex size-full flex-col gap-2 rounded-md bg-white p-2">
        <TodoFilters />
        <CreateTodoButton />
        <TodoTable />
        <TodoStats />
      </main>
    </div>
  );
}

export default App;
