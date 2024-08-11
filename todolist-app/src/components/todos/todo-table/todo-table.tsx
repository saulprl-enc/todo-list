import { ScrollArea } from "@/components/ui/scroll-area";
import { useTodos } from "@/context/todos-context";
import {
  TodoItem,
  TodoItemDueDate,
  TodoItemPriority,
  TodoItemTitle,
} from "../todo-item/todo-item";

export const TodoTable = () => {
  const { data } = useTodos();

  return (
    <ScrollArea className="h-full w-full rounded-sm border-2 border-primary">
      <ul className="flex min-w-full flex-col items-stretch gap-2 p-2">
        {data.map((todo) => (
          <TodoItem key={todo.id}>
            <TodoItemTitle>{todo.title}</TodoItemTitle>
            <TodoItemPriority>{todo.priority.toUpperCase()}</TodoItemPriority>
            <TodoItemDueDate>{todo.due}</TodoItemDueDate>
          </TodoItem>
        ))}
      </ul>
    </ScrollArea>
  );
};
