import { format } from "date-fns";

import { ScrollArea } from "@/components/ui/scroll-area";
import { useTodos } from "@/context/todos-context";
import {
  TodoItem,
  TodoItemActions,
  TodoItemCheck,
  TodoItemDeleteButton,
  TodoItemDueDate,
  TodoItemEditButton,
  TodoItemPriority,
  TodoItemTitle,
} from "../todo-item/todo-item";
import { Checkbox } from "@/components/ui/checkbox";

export const TodoTable = () => {
  const { data } = useTodos();

  return (
    <ScrollArea className="relative h-full w-full rounded-sm border-2 border-primary">
      <TodoTableHeader />
      <ul className="flex min-w-full flex-col items-stretch gap-2 p-2">
        {data.map((todo) => (
          <TodoItem key={todo.id}>
            <TodoItemCheck />
            <TodoItemTitle>{todo.title}</TodoItemTitle>
            <TodoItemPriority>{todo.priority.toLowerCase()}</TodoItemPriority>
            <TodoItemDueDate>
              {todo.due ? format(todo.due, "yyyy/MM/dd") : "-"}
            </TodoItemDueDate>
            <div className="flex flex-grow"></div>
            <TodoItemActions>
              <TodoItemEditButton />
              <TodoItemDeleteButton />
            </TodoItemActions>
          </TodoItem>
        ))}
      </ul>
    </ScrollArea>
  );
};

const TodoTableHeader = () => {
  return (
    <div className="sticky left-0 top-0 flex w-full items-center gap-2 border-b border-b-slate-400 bg-secondary p-4 font-bold">
      <Checkbox />
      <TodoItemTitle>Title</TodoItemTitle>
      <TodoItemPriority>Priority</TodoItemPriority>
      <TodoItemDueDate>Due date</TodoItemDueDate>
      <div className="flex flex-grow"></div>
      <TodoItemActions>Actions</TodoItemActions>
    </div>
  );
};
