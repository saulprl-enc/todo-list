import { useForm } from "react-hook-form";
import { z } from "zod";

import { zodResolver } from "@hookform/resolvers/zod";

import {
  Form,
  FormControl,
  FormField,
  FormItem,
  FormLabel,
  FormMessage,
} from "@/components/ui/form";
import { Input } from "@/components/ui/input";
import { Button } from "@/components/ui/button";

const TodoFiltersForm = z.object({
  name: z
    .string()
    .min(3, { message: "The title must be at least 3 characters long." })
    .optional(),
  priority: z.object({
    high: z.boolean().default(true),
    medium: z.boolean().default(true),
    low: z.boolean().default(true),
  }),
  status: z.object({
    completed: z.boolean().default(true),
    pending: z.boolean().default(true),
  }),
});

type TodoFiltersForm = z.infer<typeof TodoFiltersForm>;

export const TodoFilters = () => {
  const form = useForm<TodoFiltersForm>({
    resolver: zodResolver(TodoFiltersForm),
    defaultValues: {
      name: "",
      priority: {
        high: true,
        medium: true,
        low: true,
      },
      status: {
        completed: true,
        pending: true,
      },
    },
  });

  const onSubmit = (data: TodoFiltersForm) => {
    console.log(data);
  };

  return (
    <Form {...form}>
      <form
        onSubmit={form.handleSubmit(onSubmit)}
        className="flex w-full flex-col gap-2 rounded-sm border-2 border-primary p-2"
      >
        <FormField
          control={form.control}
          name="name"
          render={({ field }) => (
            <FormItem>
              <div className="flex items-center gap-2">
                <FormLabel>Name</FormLabel>
                <FormControl>
                  <Input placeholder="Do the dishes" {...field} />
                </FormControl>
              </div>
              <FormMessage />
            </FormItem>
          )}
        />
        <Button type="submit">Search</Button>
      </form>
    </Form>
  );
};
