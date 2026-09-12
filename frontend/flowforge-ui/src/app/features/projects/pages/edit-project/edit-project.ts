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
import { MessageDisplayService } from '../../../../core/services/message-display-service';
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
  projectId!: any;
  project:any = []

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
    private projectService: ProjectService,
    private messageDisplayService:MessageDisplayService
  ) {}

  ngOnInit(): void {

    this.projectId = this.route.snapshot.paramMap.get('id')
    
   this.projectService.getProjectById(this.projectId).subscribe({
    next:(res:any)=>{
      this.loadProject(res?.data);
    },
    error:(error)=>{
      console.error('Failed to load projects', error);
    }
    });

    
  }
idForUpdate:any;
  loadProject(restemp:any): void {


    if (!restemp) {
      this.router.navigate(['/projects']);
      return;
    }
    this.idForUpdate = restemp?.id;
    this.projectForm.controls.name.setValue(
      restemp?.name
    );

    this.projectForm.controls.description.setValue(
      restemp?.description
    );

    this.projectForm.controls.status.setValue(
      restemp?.status
    );
  }

  updateProject(): void {

    if (this.projectForm.invalid) {
      this.projectForm.markAllAsTouched();
      return;
    }

    this.loading = true;

    const payload: any = {
     
      name: this.projectForm?.controls?.name?.value,
      description: this.projectForm?.controls?.description?.value,
      status: this.projectForm?.controls?.status?.value,
    };

    this.projectService.updateProject(this.idForUpdate,payload).subscribe((res:any)=>{
      if(res){
        this.loading = false;
        this.messageDisplayService.success(res?.message);
        this.router.navigate(['/projectList']);
        
      }
    });

  }

  cancel(): void {
    this.router.navigate(['/projectList']);
  }
}
