export interface Project {
  id: number;
  name: string;
  description: string;
  status: 'ACTIVE' | 'COMPLETED' | 'PLANNING' | 'ON_HOLD';
  totalTasks: number;
  completedTasks: number;
  lastUpdated: string;
}