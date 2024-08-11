import { z } from "zod";

export const TodoPriority = z.enum(["HIGH", "MEDIUM", "LOW"]);

export type TodoPriority = z.infer<typeof TodoPriority>;

export const Todo = z.object({
  id: z.string(),
  title: z.string(),
  due: z.string().nullable(),
  done: z.boolean(),
  completedAt: z.string().nullable(),
  priority: TodoPriority,
  createdAt: z.string().nullable(),
});

export type Todo = z.infer<typeof Todo>;

export const TodoResponse = z.object({
  currentPage: z.number(),
  totalPages: z.number(),
  totalItems: z.number(),
  size: z.number(),
  data: Todo.array(),
  nextPage: z.string().url().nullable(),
  previousPage: z.string().url().nullable(),
});

export type TodoResponse = z.infer<typeof TodoResponse>;
