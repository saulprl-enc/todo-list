import { Button, buttonVariants } from "@/components/ui/button";
import { Checkbox } from "@/components/ui/checkbox";
import { useTodos } from "@/context/todos-context";
import { cn } from "@/lib/utils";
import { Todo } from "@/models/todo";
import { CheckedState } from "@radix-ui/react-checkbox";
import { format } from "date-fns";
import { FC, ReactNode, useState } from "react";
import { MdDelete } from "react-icons/md";
import { useMutation, useQueryClient } from "react-query";
import { UpdateTodo } from "../update-todo/update-todo";
import {
  AlertDialog,
  AlertDialogAction,
  AlertDialogCancel,
  AlertDialogContent,
  AlertDialogDescription,
  AlertDialogFooter,
  AlertDialogTitle,
  AlertDialogTrigger,
} from "@/components/ui/alert-dialog";

interface TodoItemProps {
  todo: Todo;
}

export const TodoItem: FC<TodoItemProps> = ({ todo }) => {
  return (
    <TodoItemWrapper className={cn(todo.done && "line-through")}>
      <TodoItemCheck checked={todo.done} todoId={todo.id} />
      <TodoItemTitle>{todo.title}</TodoItemTitle>
      <TodoItemPriority>{todo.priority.toLowerCase()}</TodoItemPriority>
      <TodoItemDueDate>
        {todo.due ? format(todo.due, "yyyy/MM/dd") : "-"}
      </TodoItemDueDate>
      <div className="flex-grow"></div>
      <TodoItemActions>
        <UpdateTodo todo={todo} />
        <TodoItemDeleteButton todoId={todo.id} />
      </TodoItemActions>
    </TodoItemWrapper>
  );
};

interface TodoItemWrapperProps {
  checked?: boolean;
  className?: string;
  children: ReactNode;
}

export const TodoItemWrapper: FC<TodoItemWrapperProps> = ({
  checked = false,
  className,
  children,
}) => {
  return (
    <li
      data-testid="todo-item"
      className={cn(
        "flex items-center gap-2 rounded-sm p-2 transition-colors hover:bg-muted",
        // checked && "hover:bg-purple-400",
        className,
      )}
    >
      {children}
    </li>
  );
};

interface CheckProps {
  checked?: boolean;
  todoId: string;
}

export const TodoItemCheck: FC<CheckProps> = ({ todoId, checked = false }) => {
  const [localCheck, setLocalCheck] = useState(checked);
  const { markAsDone, undoTodo } = useTodos();

  const handleCheckedChange = async (value: CheckedState) => {
    try {
      setLocalCheck(!!value);

      if (value) {
        await markAsDone.mutateAsync(todoId);
      } else {
        await undoTodo.mutateAsync(todoId);
      }
    } catch (error) {
      setLocalCheck(!value);
    }
  };

  return (
    <Checkbox
      data-testid="todo-checkbox"
      checked={localCheck}
      onCheckedChange={handleCheckedChange}
    />
  );
};

export const TodoItemTitle: FC<TodoItemWrapperProps> = ({
  className,
  children,
}) => {
  return (
    <span
      data-testid="todo-title"
      className={cn("flex w-2/5 flex-wrap", className)}
    >
      {children}
    </span>
  );
};

export const TodoItemPriority: FC<TodoItemWrapperProps> = ({
  className,
  children,
}) => {
  return (
    <span
      data-testid="todo-priority"
      className={cn("flex w-1/6 flex-wrap capitalize", className)}
    >
      {children}
    </span>
  );
};

export const TodoItemDueDate: FC<TodoItemWrapperProps> = ({
  className,
  children,
}) => {
  return (
    <span data-testid="todo-due" className={cn("flex w-1/6", className)}>
      {children}
    </span>
  );
};

export const TodoItemActions: FC<TodoItemWrapperProps> = ({
  className,
  children,
}) => {
  return (
    <div
      data-testid="todo-actions"
      className={cn("flex items-center justify-end gap-1", className)}
    >
      {children}
    </div>
  );
};

export const TodoItemEditButton = () => {
  return <Button variant="secondary">Edit</Button>;
};

interface DeleteProps {
  todoId: string;
}

export const TodoItemDeleteButton: FC<DeleteProps> = ({ todoId }) => {
  const queryClient = useQueryClient();

  const deleteTodo = useMutation(async () => {
    const res = await fetch(`http://localhost:9090/todos/${todoId}`, {
      method: "DELETE",
    });

    const data = await res.json();

    if (!res.ok) {
      console.error(data);

      throw new Error("Unable to delete ToDo.");
    }

    queryClient.invalidateQueries("todos");
    queryClient.invalidateQueries(["todos-stats"]);
  });

  return (
    <AlertDialog>
      <AlertDialogTrigger asChild>
        <Button variant="destructive" className="px-2 text-lg">
          <MdDelete />
        </Button>
      </AlertDialogTrigger>
      <AlertDialogContent>
        <AlertDialogTitle>Are you sure?</AlertDialogTitle>
        <AlertDialogDescription>
          This action cannot be undone. This ToDo will be permanently deleted.
        </AlertDialogDescription>
        <AlertDialogFooter>
          <AlertDialogCancel>Cancel</AlertDialogCancel>
          <AlertDialogAction
            onClick={() => deleteTodo.mutate()}
            className={buttonVariants({ variant: "destructive" })}
          >
            Delete
          </AlertDialogAction>
        </AlertDialogFooter>
      </AlertDialogContent>
    </AlertDialog>
  );
};
