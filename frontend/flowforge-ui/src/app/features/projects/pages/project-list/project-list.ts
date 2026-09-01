import { Component, inject, OnInit } from '@angular/core';
import { Project } from '../../model/project/project.model';
import {
  FormBuilder,
  FormControl,
  FormGroup,
  FormsModule,
  ReactiveFormsModule,
  Validators
} from '@angular/forms';
import { Router } from '@angular/router';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { ProjectService } from '../../../project-service';
import { MatDialog } from '@angular/material/dialog';
import { ConfirmDialog } from '../../../../core/components/confirm-dialog/confirm-dialog';
import { MatTooltipModule } from '@angular/material/tooltip';
@Component({
  selector: 'app-project-list',
  imports: [ReactiveFormsModule,
    MatFormFieldModule,
      MatTooltipModule,
    FormsModule,
    MatInputModule,
    MatButtonModule,
    MatIconModule],
  templateUrl: './project-list.html',
  styleUrl: './project-list.scss',
})
export class ProjectList implements OnInit{
   constructor(private router :Router, private projectService:ProjectService){} 
     private dialog = inject(MatDialog); 
  ngOnInit(): void {
    this.projects = this.projectService.getProjects();
  }
    searchText: string = '';
  selectedStatus: string = 'All';
projects: Project[] = [
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
  get filteredProjects(): Project[] {
    return this.projects.filter(project => {

      const matchesSearch =
        project.name
          .toLowerCase()
          .includes(this.searchText.toLowerCase()) ||
        project.description
          .toLowerCase()
          .includes(this.searchText.toLowerCase());

      const matchesStatus =
        this.selectedStatus === 'All' ||
        project.status === this.selectedStatus;

      return matchesSearch && matchesStatus;
    });
  }
editProject(projectId: number): void {
  this.router.navigate(['/projects/edit', projectId]);
}
  onClickRouteToCreateProject(){
    this.router.navigate(['/createProject'])
  }
  deleteProject(projectId: number): void {

  const project = this.projects.find(
    project => project.id === projectId
  );

  if (!project) {
    return;
  }

  const dialogRef = this.dialog.open(ConfirmDialog, {
      width: '400px',
      data: {
        title: 'Logout',
        message: `Are you sure you want to delete "${project.name}"?`,
        confirmText: 'Yes',
        cancelText: 'No'
      }
    });
    dialogRef.afterClosed().subscribe((confirmed: boolean) => {
      if (confirmed) {
              this.projects = this.projects.filter(
          project => project.id !== projectId
        );
      }
  
    });
}
viewProject(projectId: number): void {

  this.router.navigate([
    '/project/details',
    projectId
  ]);
}
}
