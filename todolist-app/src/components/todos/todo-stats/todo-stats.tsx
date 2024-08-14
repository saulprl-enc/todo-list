import { TodoStatsResponse } from "@/models/todo";
import { FC, ReactNode } from "react";
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
    <div className="bg-csi-purple/20 flex w-full gap-2 rounded-sm p-2">
      <GlobalAverage>{statsQuery.data?.globalAverage ?? "N/A"}</GlobalAverage>
      <div className="flex w-full flex-col items-center justify-center gap-2">
        <div className="flex w-full items-center justify-center gap-1">
          <LowAverage>{statsQuery.data?.lowAverage ?? "N/A"}</LowAverage>
          <MediumAverage>
            {statsQuery.data?.mediumAverage ?? "N/A"}
          </MediumAverage>
          <HighAverage>{statsQuery.data?.highAverage ?? "N/A"}</HighAverage>
        </div>
        <span>Average time to finish tasks by priority:</span>
      </div>
    </div>
  );
};

const GlobalAverage: FC<{ children: ReactNode }> = ({ children }) => {
  return (
    <div className="flex w-full flex-col items-center justify-center">
      <div className="bg-csi-purple/20 flex flex-col items-center justify-center gap-2 rounded-sm p-2">
        <span className="font-mono text-lg">{children}</span>
        <span>Average time to finish tasks</span>
      </div>
    </div>
  );
};

const LowAverage: FC<{ children: ReactNode }> = ({ children }) => {
  return (
    <div className="bg-safe/20 flex min-w-40 flex-col items-center justify-center gap-1 rounded-sm p-2">
      <span className="font-mono text-lg">{children}</span>
      <span>Low</span>
    </div>
  );
};

const MediumAverage: FC<{ children: ReactNode }> = ({ children }) => {
  return (
    <div className="bg-warning/20 flex min-w-40 flex-col items-center justify-center gap-1 rounded-sm p-2">
      <span className="font-mono text-lg">{children}</span>
      <span>Medium</span>
    </div>
  );
};

const HighAverage: FC<{ children: ReactNode }> = ({ children }) => {
  return (
    <div className="bg-danger/20 flex min-w-40 flex-col items-center justify-center gap-1 rounded-sm p-2">
      <span className="font-mono text-lg">{children}</span>
      <span>High</span>
    </div>
  );
};
