import { Component } from '@angular/core';
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
import { MatSelectModule } from '@angular/material/select';
import { ProjectService } from '../../../project-service';
@Component({
  selector: 'app-project-create',
  imports: [ReactiveFormsModule,
    MatFormFieldModule,
    FormsModule,
    MatInputModule,
    MatButtonModule,
    MatIconModule,
  MatSelectModule],
  templateUrl: './project-create.html',
  styleUrl: './project-create.scss',
})
export class ProjectCreate {
  constructor(private router :Router,private projectService:ProjectService){}  
  loading:boolean = false;
 projectForm = new FormGroup({

    name: new FormControl(null, [
      Validators.required,
      Validators.minLength(3)
    ]),

    description: new FormControl(null, [
      Validators.required,
      Validators.minLength(10)
    ]),

    status: new FormControl('Planning', [
      Validators.required
    ])

  });
   onSubmit(): void {
if (this.projectForm.invalid) {
    this.projectForm.markAllAsTouched();
    return;
  }

  this.loading = true;

  const project : any = {
    id: Date.now(),
    name: this.projectForm.controls.name.value!,
    description: this.projectForm.controls.description.value!,
    status: this.projectForm.controls.status.value!,
    completedTasks: 0,
    totalTasks: 0,
    lastUpdated: 'Just now'
  };

  this.projectService.createProject(project);

  this.loading = false;

  this.router.navigate(['/projectList']);
  }


  onCancel(): void {

    this.projectForm.reset({
      name: null,
      description: null,
      status: 'Planning'
    });
      this.router.navigate(['/projectList']);
  }
}
