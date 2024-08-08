import { Button } from "@/components/ui/button";
import { IoAdd } from "react-icons/io5";

export const CreateTodoButton = () => {
  return (
    <Button className="w-fit gap-1">
      <IoAdd />
      New To Do
    </Button>
  );
};
