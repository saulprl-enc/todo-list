import {
  Pagination,
  PaginationContent,
  PaginationItem,
  PaginationLink,
  PaginationNext,
  PaginationPrevious,
} from "@/components/ui/pagination";

export const TodoPagination = () => {
  return (
    <Pagination>
      <PaginationContent className="rounded-sm border-2 border-primary p-2">
        <PaginationPrevious href="#" />
        <PaginationItem>
          <PaginationLink href="#">1</PaginationLink>
        </PaginationItem>
        <PaginationNext href="#" />
      </PaginationContent>
    </Pagination>
  );
};
