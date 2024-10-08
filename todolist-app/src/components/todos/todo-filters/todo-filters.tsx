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
import { useSearchParams } from "react-router-dom";
import { FaCaretDown } from "react-icons/fa6";

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
  const [searchParams, setSearchParams] = useSearchParams();

  const titleParam = searchParams.get("title") ?? "";
  const priorityParam = searchParams.get("priority") ?? "all";
  const statusParam = searchParams.get("status") ?? "all";

  const form = useForm<TodoFiltersForm>({
    resolver: zodResolver(TodoFiltersForm),
    defaultValues: {
      name: titleParam,
      priority: {
        high: /high|all/i.test(priorityParam),
        medium: /medium|all/i.test(priorityParam),
        low: /low|all/i.test(priorityParam),
      },
      status: {
        completed: /done|all/i.test(statusParam),
        pending: /pending|all/i.test(statusParam),
      },
    },
  });

  const joinPriority = (
    value: TodoFiltersForm["priority"],
    separator: string = ",",
  ) => {
    const selectedPriorities: Array<string> = [];

    if (value.high) selectedPriorities.push("High");
    if (value.medium) selectedPriorities.push("Medium");
    if (value.low) selectedPriorities.push("Low");

    const displayValue =
      selectedPriorities.length === 3
        ? `All`
        : selectedPriorities.join(separator);

    return displayValue;
  };

  const joinStatus = (
    value: TodoFiltersForm["status"],
    separator: string = ",",
  ) => {
    const selectedStatuses: Array<string> = [];

    if (value.completed) selectedStatuses.push("Done");
    if (value.pending) selectedStatuses.push("Pending");

    const displayValue =
      selectedStatuses.length === 2 ? `All` : selectedStatuses.join(separator);

    return displayValue;
  };

  const onSubmit = (data: TodoFiltersForm) => {
    if (!data.priority.high && !data.priority.medium && !data.priority.low) {
      form.setValue("priority.high", true);
      form.setValue("priority.medium", true);
      form.setValue("priority.low", true);
    }

    if (!data.status.completed && !data.status.pending) {
      form.setValue("status.completed", true);
      form.setValue("status.pending", true);
    }

    setSearchParams((params) => {
      params.set("page", "1");

      data.name ? params.set("title", data.name) : params.delete("title");
      params.set("priority", joinPriority(data.priority).toLowerCase());
      params.set("status", joinStatus(data.status).toLowerCase());

      return params;
    });
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
                <FormLabel className="min-w-14">Name</FormLabel>
                <FormControl>
                  <Input
                    data-testid="title-field"
                    placeholder="Do the dishes"
                    {...field}
                  />
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
                <FormLabel className="min-w-14">Priority</FormLabel>
                <FormControl>
                  <Popover>
                    <PopoverTrigger
                      data-testid="priority-field"
                      className="flex w-full items-center justify-between rounded-sm border-2 border-primary p-1 sm:w-56"
                    >
                      <div className="w-fit">
                        {joinPriority(field.value, ", ")}
                      </div>
                      <div className="flex size-8 items-center justify-center rounded bg-muted text-xl">
                        <FaCaretDown />
                      </div>
                    </PopoverTrigger>
                    <PopoverContent className="flex flex-col gap-1 p-2">
                      <FormField
                        control={form.control}
                        name="priority.high"
                        render={({ field }) => (
                          <FormItem className="flex items-center gap-2 space-y-0 rounded p-2 transition-colors hover:bg-muted">
                            <FormControl>
                              <Checkbox
                                checked={field.value}
                                onCheckedChange={field.onChange}
                              />
                            </FormControl>
                            <FormLabel className="w-full cursor-pointer">
                              High
                            </FormLabel>
                          </FormItem>
                        )}
                      />
                      <FormField
                        control={form.control}
                        name="priority.medium"
                        render={({ field }) => (
                          <FormItem className="flex items-center gap-2 space-y-0 rounded p-2 transition-colors hover:bg-muted">
                            <FormControl>
                              <Checkbox
                                checked={field.value}
                                onCheckedChange={field.onChange}
                              />
                            </FormControl>
                            <FormLabel className="w-full cursor-pointer">
                              Medium
                            </FormLabel>
                          </FormItem>
                        )}
                      />
                      <FormField
                        control={form.control}
                        name="priority.low"
                        render={({ field }) => (
                          <FormItem className="flex items-center gap-2 space-y-0 rounded p-2 transition-colors hover:bg-muted">
                            <FormControl>
                              <Checkbox
                                checked={field.value}
                                onCheckedChange={field.onChange}
                              />
                            </FormControl>
                            <FormLabel className="w-full cursor-pointer">
                              Low
                            </FormLabel>
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
        <div className="flex w-full items-center justify-between">
          <FormField
            control={form.control}
            name="status"
            render={({ field }) => (
              <FormItem>
                <div className="flex items-center gap-2">
                  <FormLabel className="min-w-14">Status</FormLabel>
                  <FormControl>
                    <Popover>
                      <PopoverTrigger
                        data-testid="status-field"
                        className="flex w-full items-center justify-between rounded-sm border-2 border-primary p-1 sm:w-56"
                      >
                        <div className="w-fit">
                          {joinStatus(field.value, ", ")}
                        </div>
                        <div className="flex size-8 items-center justify-center rounded bg-muted text-xl">
                          <FaCaretDown />
                        </div>
                      </PopoverTrigger>
                      <PopoverContent className="flex flex-col gap-1 p-2">
                        <FormField
                          control={form.control}
                          name="status.completed"
                          render={({ field }) => (
                            <FormItem className="flex items-center gap-2 space-y-0 rounded p-2 transition-colors hover:bg-muted">
                              <FormControl>
                                <Checkbox
                                  checked={field.value}
                                  onCheckedChange={field.onChange}
                                />
                              </FormControl>
                              <FormLabel className="w-full cursor-pointer">
                                Done
                              </FormLabel>
                            </FormItem>
                          )}
                        />
                        <FormField
                          control={form.control}
                          name="status.pending"
                          render={({ field }) => (
                            <FormItem className="flex items-center gap-2 space-y-0 rounded p-2 transition-colors hover:bg-muted">
                              <FormControl>
                                <Checkbox
                                  checked={field.value}
                                  onCheckedChange={field.onChange}
                                />
                              </FormControl>
                              <FormLabel className="w-full cursor-pointer">
                                Pending
                              </FormLabel>
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
        </div>
      </form>
    </Form>
  );
};
