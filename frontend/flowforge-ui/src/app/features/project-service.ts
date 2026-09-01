import { Injectable } from '@angular/core';
import { Project } from './projects/model/project/project.model';

@Injectable({
  providedIn: 'root',
})
export class ProjectService {
    private projects: Project[] = [
    {
      id: 1,
      name: 'FlowForge',
      description: 'Core platform development',
      status: 'Active',
      completedTasks: 18,
      totalTasks: 24,
      lastUpdated: '2 hours ago'
    },
    {
      id: 2,
      name: 'Customer Portal',
      description: 'Customer management portal',
      status: 'Active',
      completedTasks: 12,
      totalTasks: 20,
      lastUpdated: 'Yesterday'
    },
    {
      id: 3,
      name: 'Payment Service',
      description: 'Payment processing service',
      status: 'Completed',
      completedTasks: 15,
      totalTasks: 15,
      lastUpdated: '2 days ago'
    },
    {
      id: 4,
      name: 'Notification System',
      description: 'Email and notification service',
      status: 'Planning',
      completedTasks: 3,
      totalTasks: 18,
      lastUpdated: '3 days ago'
    }
  ];

  getProjects(): Project[] {
    return this.projects;
  }

  createProject(project: Project): void {
    this.projects.push(project);
  }
getProjectById(id: number): Project | undefined {

  return this.projects.find(
    project => project.id === id
  );
}

updateProject(updatedProject: Project): void {

  const index = this.projects.findIndex(
    project => project.id === updatedProject.id
  );

  if (index !== -1) {
    this.projects[index] = updatedProject;
  }
}
}
