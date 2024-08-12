import { CalendarIcon } from "lucide-react";
import { Button } from "./button";
import { Popover, PopoverContent, PopoverTrigger } from "./popover";
import { format } from "date-fns";
import { Calendar } from "./calendar";
import { cn } from "@/lib/utils";
import { SelectSingleEventHandler } from "react-day-picker";
import { FC } from "react";

interface Props {
  date?: Date;
  onSelectDate?: SelectSingleEventHandler;
}

export const DatePicker: FC<Props> = ({ date, onSelectDate }) => {
  return (
    <Popover>
      <PopoverTrigger asChild>
        <Button
          variant={"outline"}
          className={cn(
            "w-[280px] justify-start text-left font-normal",
            !date && "text-muted-foreground",
          )}
        >
          <CalendarIcon className="mr-2 h-4 w-4" />
          {date ? format(date, "PPP") : <span>Pick a date</span>}
        </Button>
      </PopoverTrigger>
      <PopoverContent className="w-auto p-0">
        <Calendar
          mode="single"
          selected={date}
          onSelect={onSelectDate}
          initialFocus
        />
      </PopoverContent>
    </Popover>
  );
};
