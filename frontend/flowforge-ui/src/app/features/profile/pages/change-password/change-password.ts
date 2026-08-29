import { Component, OnInit } from '@angular/core';
import {
  FormBuilder,
  FormControl,
  FormGroup,
  ReactiveFormsModule,
  Validators
} from '@angular/forms';
import { Router, RouterLink } from '@angular/router';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { Auth } from '../../../../core/services/auth';
import { MessageDisplayService } from '../../../../core/services/message-display-service';
import { AuthStateService } from '../../../../core/services/auth-state-service';
import { TokenService } from '../../../../core/services/token-service';
@Component({
  selector: 'app-change-password',
  imports: [ReactiveFormsModule,
    RouterLink,
    MatFormFieldModule,
    MatInputModule,
    MatButtonModule,
    MatIconModule],
  templateUrl: './change-password.html',
  styleUrl: './change-password.scss',
})
export class ChangePassword implements OnInit {
  constructor(private authService :Auth,
    private router :Router,private messageDisplayService:MessageDisplayService,
    private authStateService:AuthStateService,private tokenService :TokenService
  ){}
  ngOnInit(): void {
    this.changePasswordForm.controls.currentPassword.reset();
  }
  hideCurrentPassword = true;
  hideNewPassword = true;
  hideConfirmPassword = true;
changePasswordForm = new FormGroup({

  currentPassword: new FormControl(['',Validators.required]),
  newPassword:  new FormControl('',[Validators.required,Validators.pattern(/^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)(?=.*[@$!%*?&]).{8,}$/)]),
  confirmPassword: new FormControl('',[Validators.required])

})
passwordMatcher(): void {
    const password = this.changePasswordForm.controls.newPassword.value;

    const confirmControl =this.changePasswordForm.controls.confirmPassword;

    const confirmPassword =confirmControl.value;
    
    if (!password || !confirmPassword) {
      return;
    }
    if (password !== confirmPassword) {
      confirmControl.setErrors({...confirmControl.errors,passwordMismatch: true});
    } else {
      const errors = confirmControl.errors;
      if (errors) {
        delete errors['passwordMismatch'];
        if (Object.keys(errors).length === 0) {
          confirmControl.setErrors(null);
        } else {
          confirmControl.setErrors(errors);
        }
      }
    }
  }

  onChangePassword(): void {
    this.passwordMatcher();
    if (this.changePasswordForm.invalid) {
      this.changePasswordForm.markAllAsTouched();
      return;
    }
    const payload = {
      currentPassword:this.changePasswordForm?.controls?.currentPassword?.value,
      newPassword:this.changePasswordForm?.controls?.newPassword?.value,
    }
      this.authService.changePassword(payload).subscribe(
    {
    next: (response:any) => {
      this.messageDisplayService.success('Change Password hasbeen successfully done');
      this.authService.logout().subscribe((res:any)=>{
        this.authStateService.setUnauthenticated();
        this.tokenService.clearAccessToken();
        this.router.navigate(['/auth/login']);
      })
    },
    error: (error) => {
     this.messageDisplayService.error('Somthing went wrong, Please try again.') ;
    }
  })


  }
}
