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
import {
  Popover,
  PopoverContent,
  PopoverTrigger,
} from "@/components/ui/popover";
import { Checkbox } from "@/components/ui/checkbox";

const TodoFiltersForm = z.object({
  name: z.string().optional(),
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

  const buildPriorityDisplayValue = (value: TodoFiltersForm["priority"]) => {
    if (!value.high && !value.medium && !value.low) {
      // form.setValue("priority", { high: true, medium: true, low: true });
      form.setValue("priority.high", true);
      form.setValue("priority.medium", true);
      form.setValue("priority.low", true);

      return;
    }

    const selectedPriorities: Array<string> = [];

    if (value.high) selectedPriorities.push("High");
    if (value.medium) selectedPriorities.push("Medium");
    if (value.low) selectedPriorities.push("Low");

    const displayValue =
      selectedPriorities.length === 3
        ? `All, ${selectedPriorities.join(", ")}`
        : selectedPriorities.join(", ");

    return displayValue;
  };

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
        <FormField
          control={form.control}
          name="priority"
          render={({ field }) => (
            <FormItem>
              <div className="flex items-center gap-2">
                <FormLabel>Priority</FormLabel>
                <FormControl>
                  <Popover>
                    <PopoverTrigger className="rounded-sm border-2 border-primary p-1">
                      {buildPriorityDisplayValue(field.value)}
                    </PopoverTrigger>
                    <PopoverContent className="flex w-fit flex-col gap-2">
                      <FormField
                        control={form.control}
                        name="priority.high"
                        render={({ field }) => (
                          <FormItem className="flex items-center gap-2 space-y-0">
                            <FormControl>
                              <Checkbox
                                checked={field.value}
                                onCheckedChange={field.onChange}
                              />
                            </FormControl>
                            <FormLabel>High</FormLabel>
                          </FormItem>
                        )}
                      />
                      <FormField
                        control={form.control}
                        name="priority.medium"
                        render={({ field }) => (
                          <FormItem className="flex items-center gap-2 space-y-0">
                            <FormControl>
                              <Checkbox
                                checked={field.value}
                                onCheckedChange={field.onChange}
                              />
                            </FormControl>
                            <FormLabel>Medium</FormLabel>
                          </FormItem>
                        )}
                      />
                      <FormField
                        control={form.control}
                        name="priority.low"
                        render={({ field }) => (
                          <FormItem className="flex items-center gap-2 space-y-0">
                            <FormControl>
                              <Checkbox
                                checked={field.value}
                                onCheckedChange={field.onChange}
                              />
                            </FormControl>
                            <FormLabel>Low</FormLabel>
                          </FormItem>
                        )}
                      />
                    </PopoverContent>
                  </Popover>
                </FormControl>
              </div>
            </FormItem>
          )}
        />
        <Button type="submit">Search</Button>
      </form>
    </Form>
  );
};
