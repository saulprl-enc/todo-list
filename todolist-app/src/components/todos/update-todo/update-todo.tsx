import { Button } from "@/components/ui/button";
import {
  Dialog,
  DialogContent,
  DialogDescription,
  DialogFooter,
  DialogHeader,
  DialogTitle,
  DialogTrigger,
} from "@/components/ui/dialog";
import { zodResolver } from "@hookform/resolvers/zod";
import { useForm } from "react-hook-form";
import { MdModeEdit } from "react-icons/md";
import { Form } from "@/components/ui/form";
import { useMutation, useQueryClient } from "react-query";
import { FC, useState } from "react";
import { EditTodoForm, UpdateTodoForm } from "./update-todo-form";
import { Todo } from "@/models/todo";

interface Props {
  todo: Todo;
}

export const UpdateTodo: FC<Props> = ({ todo }) => {
  const [dialogOpen, setDialogOpen] = useState(false);
  const queryClient = useQueryClient();

  const updateTodo = useMutation(async (data: UpdateTodoForm) => {
    const res = await fetch(`http://localhost:9090/todos/${todo.id}`, {
      method: "PUT",
      headers: {
        "Content-Type": "application/json",
      },
      body: JSON.stringify(data),
    });

    const resData = await res.json();

    if (!res.ok) {
      console.error(resData);

      throw new Error("Unable to update ToDo.");
    }

    queryClient.invalidateQueries(["todos"]);
    queryClient.invalidateQueries(["todos-stats"]);
  });

  const form = useForm<UpdateTodoForm>({
    resolver: zodResolver(UpdateTodoForm),
    defaultValues: {
      title: todo.title,
      due: todo.due ? new Date(todo.due) : undefined,
      priority: todo.priority,
    },
  });

  const onSubmit = (data: UpdateTodoForm) => {
    updateTodo.mutate(data);
    setDialogOpen(false);
  };

  return (
    <Dialog open={dialogOpen} onOpenChange={(open) => setDialogOpen(open)}>
      <DialogTrigger asChild>
        <Button
          onClick={() => setDialogOpen(true)}
          className="w-fit gap-1 bg-amber-500 px-2 text-lg hover:bg-amber-500/90"
        >
          <MdModeEdit />
        </Button>
      </DialogTrigger>
      <DialogContent>
        <DialogHeader>
          <DialogTitle>Update To-Do</DialogTitle>
          <DialogDescription>
            You can edit this To-Do&apos;s data. Click update when you&apos;re
            done
          </DialogDescription>
        </DialogHeader>
        <Form {...form}>
          <form onSubmit={form.handleSubmit(onSubmit)}>
            <div className="flex w-full flex-col items-stretch gap-2">
              <EditTodoForm control={form.control} />
            </div>
            <div className="py-2"></div>
            <DialogFooter>
              <Button
                type="button"
                variant="outline"
                onClick={() => setDialogOpen(false)}
              >
                Cancel
              </Button>
              <Button type="submit">Update</Button>
            </DialogFooter>
          </form>
        </Form>
      </DialogContent>
    </Dialog>
  );
};
