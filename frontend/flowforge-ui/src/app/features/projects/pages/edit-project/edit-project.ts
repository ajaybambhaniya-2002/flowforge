import { Component } from '@angular/core';
import { Project } from '../../model/project/project.model';
import {
  FormBuilder,
  FormControl,
  FormGroup,
  FormsModule,
  ReactiveFormsModule,
  Validators
} from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { ProjectService } from '../../../project-service';
import { MatSelectModule } from '@angular/material/select';
@Component({
  selector: 'app-edit-project',
  imports: [ReactiveFormsModule,
    MatSelectModule,
    MatFormFieldModule,
    FormsModule,
    MatInputModule,
    MatButtonModule,
    MatIconModule],
  templateUrl: './edit-project.html',
  styleUrl: './edit-project.scss',
})
export class EditProject {
 loading = false;
  projectId!: number;
  project: Project | undefined;

  projectForm = new FormGroup({
    name: new FormControl('', [
      Validators.required,
      Validators.minLength(3),
      Validators.maxLength(100)
    ]),

    description: new FormControl('', [
      Validators.required,
      Validators.minLength(10),
      Validators.maxLength(500)
    ]),

    status: new FormControl('Planning', [
      Validators.required
    ])
  });

  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private projectService: ProjectService
  ) {}

  ngOnInit(): void {

    this.projectId = Number(
      this.route.snapshot.paramMap.get('id')
    );

    this.loadProject();
  }

  loadProject(): void {

    this.project = this.projectService.getProjectById(this.projectId);

    if (!this.project) {
      this.router.navigate(['/projects']);
      return;
    }

    this.projectForm.controls.name.setValue(
      this.project.name
    );

    this.projectForm.controls.description.setValue(
      this.project.description
    );

    this.projectForm.controls.status.setValue(
      this.project.status
    );
  }

  updateProject(): void {

    if (this.projectForm.invalid) {
      this.projectForm.markAllAsTouched();
      return;
    }

    this.loading = true;

    const updatedProject: any = {
      ...this.project!,
      name: this.projectForm.controls.name.value!,
      description: this.projectForm.controls.description.value!,
      status: this.projectForm.controls.status.value!,
      lastUpdated: 'Just now'
    };

    this.projectService.updateProject(updatedProject);

    this.loading = false;

    this.router.navigate(['/projectList']);
  }

  cancel(): void {
    this.router.navigate(['/projectList']);
  }
}
