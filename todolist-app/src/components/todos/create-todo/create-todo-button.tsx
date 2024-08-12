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
import { IoAdd } from "react-icons/io5";
import { CreateTodoForm, NewTodoForm } from "./create-todo-form";
import { Form } from "@/components/ui/form";
import { useMutation, useQueryClient } from "react-query";
import { useState } from "react";

export const CreateTodoButton = () => {
  const [dialogOpen, setDialogOpen] = useState(false);
  const queryClient = useQueryClient();

  const saveTodo = useMutation(async (data: NewTodoForm) => {
    const res = await fetch(`http://localhost:9090/todos`, {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
      },
      body: JSON.stringify(data),
    });

    const resData = await res.json();

    if (!res.ok) {
      console.error(resData);

      throw new Error("Unable to save ToDo.");
    }

    queryClient.invalidateQueries(["todos"]);
    queryClient.invalidateQueries(["todos-stats"]);
  });

  const form = useForm<NewTodoForm>({
    resolver: zodResolver(NewTodoForm),
    defaultValues: {
      title: "",
      due: undefined,
      priority: undefined,
    },
  });

  const onSubmit = (data: NewTodoForm) => {
    saveTodo.mutate(data);
    setDialogOpen(false);
  };

  return (
    <Dialog open={dialogOpen} onOpenChange={(open) => setDialogOpen(open)}>
      <DialogTrigger asChild>
        <Button onClick={() => setDialogOpen(true)} className="w-fit gap-1">
          <IoAdd />
          New To Do
        </Button>
      </DialogTrigger>
      <DialogContent>
        <DialogHeader>
          <DialogTitle>Create New To-Do</DialogTitle>
          <DialogDescription>
            Create a new To-Do here. Click save when you&apos;re done
          </DialogDescription>
        </DialogHeader>
        <Form {...form}>
          <form onSubmit={form.handleSubmit(onSubmit)}>
            <div className="flex w-full flex-col items-stretch gap-2">
              <CreateTodoForm control={form.control} />
            </div>
            <div className="py-2"></div>
            <DialogFooter>
              <Button type="submit">Save</Button>
            </DialogFooter>
          </form>
        </Form>
      </DialogContent>
    </Dialog>
  );
};
