import { Component, OnInit } from '@angular/core';
import { Auth } from '../../../../core/services/auth';
import { FormBuilder, FormControl, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { MatButtonModule } from '@angular/material/button';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatIconModule } from '@angular/material/icon';
import { MatInputModule } from '@angular/material/input';
import { MessageDisplayService } from '../../../../core/services/message-display-service';
import { RouterLink } from '@angular/router';
@Component({
  selector: 'app-profile',
  imports: [MatFormFieldModule,
    MatInputModule,
    MatButtonModule,
    RouterLink,
    ReactiveFormsModule,
    MatIconModule,],
  templateUrl: './profile.html',
  styleUrl: './profile.scss',
})
export class Profile implements OnInit{
constructor(private authService:Auth,private messageDisplayService:MessageDisplayService){}
loading:boolean = false
profileForm = new FormGroup({
  username : new FormControl(null),
  email : new FormControl(null),

})
ngOnInit(): void {
  this.authService.getProfileData().subscribe(
    {
    next: (response:any) => {
      this.profileForm.controls.username.setValue(response?.data?.username);
      this.profileForm.controls.email.setValue(response?.data?.email);
      this.makeFormFieldDisable();
    },
    error: (error) => {
     this.messageDisplayService.error('Unable to load profile. Please try again.') ;
    }
  })
}
makeFormFieldDisable(){
  this.profileForm.controls.email.disable();
  this.profileForm.controls.username.disable();
}
}
