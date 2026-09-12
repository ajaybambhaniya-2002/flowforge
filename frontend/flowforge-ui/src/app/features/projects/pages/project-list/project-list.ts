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
import { MessageDisplayService } from '../../../../core/services/message-display-service';
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
   constructor(private router :Router, private projectService:ProjectService,
    private messageDisplayService :MessageDisplayService
   ){} 
     private dialog = inject(MatDialog); 
  ngOnInit(): void {
     this.projectService.getAllProjects().subscribe({
          next: (response: any) => {

            this.projects = response.data;

          },
          error: (error) => {

            console.error('Failed to load projects', error);

          }
        });
  }
    searchText: string = '';
  selectedStatus: string = 'ALL';
  projects: Project[]  = [];
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
          this.selectedStatus === 'ALL' ||
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
        title: 'Delete Project',
        message: `Are you sure you want to delete "${project.name}"?`,
        confirmText: 'Yes',
        cancelText: 'No'
      }
    });
    dialogRef.afterClosed().subscribe((confirmed: boolean) => {
      if (confirmed) {
            this.projectService.deleteProject(projectId).subscribe((res:any)=>{
               this.messageDisplayService.success('Project Deleted Successfully');
                const index = this.projects.findIndex(p => p.id === projectId);
                if (index !== -1) {
                  this.projects.splice(index, 1);
                }
            })
      }
  
    });
}
viewProject(projectId: any): void {

  this.router.navigate([
    '/project/details',
    projectId
  ]);
}
}
