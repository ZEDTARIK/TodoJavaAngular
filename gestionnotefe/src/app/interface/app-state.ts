import { DataState } from "../enumaration/data-state";

export interface AppState<T> {
  dataState: DataState;
  data?: T;
  error?: string;
}
