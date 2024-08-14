import { render } from "@testing-library/react";
import React from "react";
import { QueryClient, QueryClientProvider } from "react-query";

export const renderWithClient = (
  client: QueryClient,
  ui: React.ReactElement,
) => {
  return render(
    <QueryClientProvider client={client}>{ui}</QueryClientProvider>,
  );
};
