import { Component } from '@angular/core';
import { Project } from '../../model/project/project.model';
import { ActivatedRoute, Router } from '@angular/router';
import { ProjectService } from '../../../project-service';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatTooltipModule } from '@angular/material/tooltip';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { DatePipe } from '@angular/common';
@Component({
  selector: 'app-project-detail',
  imports: [ReactiveFormsModule,
    DatePipe,
    MatFormFieldModule,
      MatTooltipModule,
    FormsModule,
    MatInputModule,
    MatButtonModule,
    MatIconModule],
  templateUrl: './project-detail.html',
  styleUrl: './project-detail.scss',
})
export class ProjectDetail {
 project: any = [];

  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private projectService: ProjectService
  ) {}

  ngOnInit(): void {

    const projectId = 
        this.route.snapshot.paramMap.get('id')
    
    this.projectService.getProjectById(projectId).subscribe({
    next:(res:any)=>{
      
    this.project = res?.data
    },
    error:(error)=>{
      console.error('Failed to load projects', error);
    }
    });

    if (!this.project) {
      this.router.navigate(['/projects']);
    }
  }

  editProject(): void {

    if (!this.project) {
      return;
    }

    this.router.navigate([
      '/projects/edit',
      this.project.id
    ]);
  }

  backToProjects(): void {
    this.router.navigate(['/projectList']);
  }

  getProgress(): number {

    if (!this.project || this.project.totalTasks === 0) {
      return 0;
    }

    return Math.round(
      (this.project.completedTasks / this.project.totalTasks) * 100
    );
  }
}
