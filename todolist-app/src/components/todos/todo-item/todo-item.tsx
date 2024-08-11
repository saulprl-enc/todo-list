import { cn } from "@/lib/utils";
import { FC, ReactNode } from "react";

interface Props {
  className?: string;
  children: ReactNode;
}

export const TodoItem: FC<Props> = ({ className, children }) => {
  return <li className={cn(className)}>{children}</li>;
};

export const TodoItemTitle: FC<Props> = ({ className, children }) => {
  return <span className={cn(className)}>{children}</span>;
};

export const TodoItemPriority: FC<Props> = ({ className, children }) => {
  return <span className={cn(className)}>{children}</span>;
};

export const TodoItemDueDate: FC<Props> = ({ className, children }) => {
  return <span className={cn(className)}>{children}</span>;
};
