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
import { Button } from "@/components/ui/button";
import { useSearchParams } from "react-router-dom";
import { FaSort, FaSortDown, FaSortUp } from "react-icons/fa6";
import { FC } from "react";
import { UpdateTodo } from "../update-todo/update-todo";

export const TodoTable = () => {
  const {
    data: { data },
  } = useTodos();

  return (
    <ScrollArea className="relative h-full w-full rounded-sm border-2 border-primary">
      <TodoTableHeader />
      <ul className="flex min-w-full flex-col items-stretch gap-2 p-2">
        {data.map((todo) => (
          <TodoItem key={todo.id} checked={todo.done}>
            <TodoItemCheck todoId={todo.id} checked={todo.done} />
            <TodoItemTitle>{todo.title}</TodoItemTitle>
            <TodoItemPriority>{todo.priority.toLowerCase()}</TodoItemPriority>
            <TodoItemDueDate>
              {todo.due ? format(todo.due, "yyyy/MM/dd") : "-"}
            </TodoItemDueDate>
            <div className="flex flex-grow"></div>
            <TodoItemActions>
              <UpdateTodo todo={todo} />
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
      <Checkbox disabled />
      <TodoItemTitle>Title</TodoItemTitle>
      <TodoItemPriority className="items-center">
        Priority
        <TodoSortButton paramName="sortByPriority" />
      </TodoItemPriority>
      <TodoItemDueDate className="items-center">
        Due date
        <TodoSortButton paramName="sortByDate" />
      </TodoItemDueDate>
      <div className="flex flex-grow"></div>
      <TodoItemActions>Actions</TodoItemActions>
    </div>
  );
};

const sortIcons = {
  asc: FaSortUp,
  desc: FaSortDown,
  unsorted: FaSort,
};

interface TodoSortButtonProps {
  paramName: string;
}

const TodoSortButton: FC<TodoSortButtonProps> = ({ paramName }) => {
  const [searchParams, setSearchParams] = useSearchParams();

  const sortByPriority = searchParams.get(paramName) ?? "unsorted";

  const Icon = sortIcons[sortByPriority as keyof typeof sortIcons];

  const toggleSort = () => {
    setSearchParams((params) => {
      switch (params.get(paramName)) {
        case "asc":
          params.set(paramName, "desc");
          break;
        case "desc":
          params.delete(paramName);
          break;
        default:
          params.set(paramName, "asc");
          break;
      }

      return params;
    });
  };

  return (
    <Button
      variant="ghost"
      onClick={toggleSort}
      className="justify-start gap-1 text-base font-bold"
    >
      <Icon />
    </Button>
  );
};
