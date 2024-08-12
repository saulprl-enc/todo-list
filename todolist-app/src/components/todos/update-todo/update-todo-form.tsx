import { z } from "zod";

import { TodoPriority } from "@/models/todo";
import {
  FormControl,
  FormField,
  FormItem,
  FormLabel,
  FormMessage,
} from "@/components/ui/form";
import { Control } from "react-hook-form";
import { FC } from "react";
import { Input } from "@/components/ui/input";
import { DatePicker } from "@/components/ui/date-picker";
import {
  Select,
  SelectContent,
  SelectItem,
  SelectTrigger,
  SelectValue,
} from "@/components/ui/select";

export const UpdateTodoForm = z
  .object({
    title: z
      .string()
      .min(3, { message: "The title must contain at least 3 characters." })
      .max(120, {
        message: "The title must not be longer than 120 characters.",
      }),
    due: z.date().optional(),
    priority: TodoPriority,
  })
  .partial();

export type UpdateTodoForm = z.infer<typeof UpdateTodoForm>;

interface UpdateTodoProps {
  control: Control<UpdateTodoForm, any>;
}

export const EditTodoForm: FC<UpdateTodoProps> = ({ control }) => {
  return (
    <>
      <FormField
        control={control}
        name="title"
        render={({ field }) => (
          <FormItem>
            <FormLabel>Title</FormLabel>
            <FormControl>
              <Input placeholder="Initialize project" {...field} />
            </FormControl>
            <FormMessage />
          </FormItem>
        )}
      />
      <FormField
        control={control}
        name="priority"
        render={({ field }) => (
          <FormItem>
            <FormLabel>Priority</FormLabel>
            <Select onValueChange={field.onChange} defaultValue={field.value}>
              <FormControl>
                <SelectTrigger>
                  <SelectValue placeholder="Select a priority level" />
                </SelectTrigger>
              </FormControl>
              <SelectContent>
                <SelectItem value="HIGH">High</SelectItem>
                <SelectItem value="MEDIUM">Medium</SelectItem>
                <SelectItem value="LOW">Low</SelectItem>
              </SelectContent>
            </Select>
            <FormMessage />
          </FormItem>
        )}
      />
      <FormField
        control={control}
        name="due"
        render={({ field }) => (
          <FormItem className="flex flex-col">
            <FormLabel>Due date</FormLabel>
            <FormControl>
              <DatePicker date={field.value} onSelectDate={field.onChange} />
            </FormControl>
            <FormMessage />
          </FormItem>
        )}
      />
    </>
  );
};
