import { ScrollArea } from "@/components/ui/scroll-area";
import { useTodos } from "@/context/todos-context";
import {
  TodoItemActions,
  TodoItemDueDate,
  TodoItemPriority,
  TodoItemTitle,
  TodoItem,
} from "../todo-item/todo-item";
import { Checkbox } from "@/components/ui/checkbox";
import { Button } from "@/components/ui/button";
import { useSearchParams } from "react-router-dom";
import { FaSort, FaSortDown, FaSortUp } from "react-icons/fa6";
import { FC } from "react";

export const TodoList = () => {
  const {
    data: { data },
  } = useTodos();

  return (
    <ScrollArea className="relative h-full w-full rounded-sm">
      <TodoListHeader />
      <ul
        data-testid="todo-list"
        className="bg-csi-purple/20 flex min-w-full flex-col items-stretch gap-2 p-2 px-1"
      >
        {data.map((todo) => (
          <TodoItem key={todo.id} todo={todo} />
        ))}
      </ul>
    </ScrollArea>
  );
};

const TodoListHeader = () => {
  return (
    <div className="bg-csi-purple sticky left-0 top-0 flex w-full items-center gap-2 border-b border-b-slate-400 p-4 font-bold text-white">
      <Checkbox disabled className="border-white" />
      <TodoItemTitle>Title</TodoItemTitle>
      <TodoItemPriority className="items-center gap-2">
        Priority
        <TodoSortButton paramName="sortByPriority" />
      </TodoItemPriority>
      <TodoItemDueDate className="items-center gap-2">
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
      className="justify-start gap-1 px-1 text-base font-bold"
    >
      <Icon />
    </Button>
  );
};
