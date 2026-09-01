export interface Project {
  id: number;
  name: string;
  description: string;
  status: 'Active' | 'Completed' | 'Planning' | 'On Hold';
  totalTasks: number;
  completedTasks: number;
  lastUpdated: string;
}