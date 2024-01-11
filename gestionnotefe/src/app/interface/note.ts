import { Level } from "../enumaration/level";

export interface Note {
  id: number;
  title: string;
  description: string;
  level: Level;
  createAt: Date;
}
