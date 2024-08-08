export const TodoStats = () => {
  return (
    <div className="flex w-full gap-2 rounded-sm border-2 border-primary p-2">
      <div className="flex w-full flex-col items-center justify-center gap-2">
        <p>Average time to finish tasks:</p>
        <p>20:00 minutes</p>
      </div>
      <div className="flex w-full flex-col items-center justify-center gap-2">
        <p>Average time to finish tasks by priority:</p>
        <div className="flex w-full flex-col items-center gap-1">
          <p>Low: 20:00 minutes</p>
          <p>Medium: 20:00 minutes</p>
          <p>High: 20:00 minutes</p>
        </div>
      </div>
    </div>
  );
};
