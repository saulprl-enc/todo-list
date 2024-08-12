import { Button } from "@/components/ui/button";
import { Checkbox } from "@/components/ui/checkbox";
import { useTodos } from "@/context/todos-context";
import { cn } from "@/lib/utils";
import { CheckedState } from "@radix-ui/react-checkbox";
import { FC, ReactNode, useState } from "react";
import { MdDelete } from "react-icons/md";
import { useMutation, useQueryClient } from "react-query";

interface Props {
  checked?: boolean;
  className?: string;
  children: ReactNode;
}

export const TodoItem: FC<Props> = ({
  checked = false,
  className,
  children,
}) => {
  return (
    <li
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

  const handleCheckedChange = (value: CheckedState) => {
    try {
      setLocalCheck(!!value);

      if (value) {
        markAsDone.mutate(todoId);
      } else {
        undoTodo.mutate(todoId);
      }
    } catch (error) {
      setLocalCheck(!value);
    }
  };

  return (
    <Checkbox checked={localCheck} onCheckedChange={handleCheckedChange} />
  );
};

export const TodoItemTitle: FC<Props> = ({ className, children }) => {
  return (
    <span className={cn("flex w-2/5 flex-wrap", className)}>{children}</span>
  );
};

export const TodoItemPriority: FC<Props> = ({ className, children }) => {
  return (
    <span className={cn("flex w-1/6 flex-wrap capitalize", className)}>
      {children}
    </span>
  );
};

export const TodoItemDueDate: FC<Props> = ({ className, children }) => {
  return <span className={cn("flex w-1/6", className)}>{children}</span>;
};

export const TodoItemActions: FC<Props> = ({ className, children }) => {
  return (
    <div className={cn("flex items-center justify-end gap-1", className)}>
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
  });

  return (
    <Button
      variant="destructive"
      onClick={() => deleteTodo.mutate()}
      className="px-2 text-lg"
    >
      <MdDelete />
    </Button>
  );
};
