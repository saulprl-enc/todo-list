import { TodoStatsResponse } from "@/models/todo";
import { useQuery } from "react-query";

export const TodoStats = () => {
  const statsQuery = useQuery({
    queryKey: ["todos-stats"],
    queryFn: async () => {
      const res = await fetch(`http://localhost:9090/todos/stats`);

      const data = await res.json();

      if (!res.ok) {
        console.error(data);

        throw new Error("Unable to fetch ToDo stats.");
      }

      const statsParse = TodoStatsResponse.safeParse(data);

      if (!statsParse.success) {
        console.error(statsParse.error.toString());

        throw new Error("Received unexpected response from the server.");
      }

      return statsParse.data;
    },
    refetchInterval: 20000,
  });

  if (statsQuery.isLoading) {
    return (
      <div className="flex w-full items-center justify-center gap-2 rounded-sm border-2 border-primary p-2">
        <span>Loading...</span>
      </div>
    );
  }

  if (statsQuery.isError) {
    return (
      <div className="flex w-full items-center justify-center gap-2 rounded-sm border-2 border-primary p-2">
        <span>Unable to fetch ToDo stats</span>
      </div>
    );
  }

  return (
    <div className="flex w-full gap-2 rounded-sm border-2 border-primary p-2">
      <div className="flex w-full flex-col items-center justify-center gap-2">
        <p>Average time to finish tasks:</p>
        <p>{statsQuery.data?.globalAverage ?? "N/A"}</p>
      </div>
      <div className="flex w-full flex-col items-center justify-center gap-2">
        <p>Average time to finish tasks by priority:</p>
        <div className="flex w-full flex-col items-center gap-1">
          <p>{`Low: ${statsQuery.data?.lowAverage ?? "N/A"}`}</p>
          <p>{`Medium: ${statsQuery.data?.mediumAverage ?? "N/A"}`}</p>
          <p>{`High: ${statsQuery.data?.highAverage ?? "N/A"}`}</p>
        </div>
      </div>
    </div>
  );
};
