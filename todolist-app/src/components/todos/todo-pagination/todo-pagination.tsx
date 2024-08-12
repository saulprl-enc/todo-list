import {
  Pagination,
  PaginationContent,
  PaginationFirst,
  PaginationItem,
  PaginationLast,
  PaginationLink,
  PaginationNext,
  PaginationPrevious,
} from "@/components/ui/pagination";
import { useTodos } from "@/context/todos-context";
import { useSearchParams } from "react-router-dom";

export const TodoPagination = () => {
  const [searchParams] = useSearchParams();

  const {
    data: { currentPage, totalPages, nextPage, previousPage },
  } = useTodos();

  const createQueryString = (page: string) => {
    const params = new URLSearchParams(searchParams);
    params.set("page", page);

    return `?${params.toString()}`;
  };

  const nextPageUrl = nextPage
    ? createQueryString(
        new URLSearchParams(nextPage).get("page") ??
          (currentPage + 1).toString(),
      )
    : "#";

  const previousPageUrl = previousPage
    ? createQueryString(
        new URLSearchParams(previousPage).get("page") ??
          (currentPage - 1).toString(),
      )
    : "#";

  return (
    <Pagination>
      <PaginationContent className="rounded-sm border-2 border-primary p-2">
        <PaginationFirst
          to={createQueryString("1")}
          disabled={currentPage === 1}
        />
        <PaginationPrevious to={previousPageUrl} disabled={!previousPage} />
        {Array.from({ length: totalPages }).map((_, index) => (
          <PaginationItem key={`Todo page ${index}`}>
            <PaginationLink
              isActive={currentPage === index + 1}
              to={createQueryString((index + 1).toString())}
            >
              {index + 1}
            </PaginationLink>
          </PaginationItem>
        ))}
        <PaginationNext to={nextPageUrl} disabled={!nextPage} />
        <PaginationLast
          to={createQueryString(totalPages.toString())}
          disabled={currentPage === totalPages || totalPages === 0}
        />
      </PaginationContent>
    </Pagination>
  );
};
