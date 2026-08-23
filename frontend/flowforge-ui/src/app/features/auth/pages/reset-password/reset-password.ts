import { Component } from '@angular/core';
import { AbstractControl, FormControl, FormGroup, ReactiveFormsModule, ValidationErrors, Validators } from '@angular/forms';
import { MatButtonModule } from '@angular/material/button';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatIconModule } from '@angular/material/icon';
import { MatInputModule } from '@angular/material/input';
import { Router, RouterLink } from '@angular/router';
import { Auth } from '../../../../core/services/auth';
import { TokenService } from '../../../../core/services/token-service';
import { AuthStateService } from '../../../../core/services/auth-state-service';
import { MessageDisplayService } from '../../../../core/services/message-display-service';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';
import { finalize } from 'rxjs';

@Component({
  selector: 'app-reset-password',
  imports: [MatFormFieldModule,
    MatInputModule,
    MatButtonModule,
    ReactiveFormsModule,
    MatIconModule,
    MatProgressSpinnerModule,
    RouterLink],
  templateUrl: './reset-password.html',
  styleUrl: './reset-password.scss',
})
export class ResetPassword {
  constructor(private authService:Auth, 
    private tokenService:TokenService,
    private authStateService:AuthStateService,
    private router :Router,
    private messageDisplayService:MessageDisplayService){}
  isLoading:boolean = false;
  hideConfirmPassword:boolean = true;
  hidePassword:boolean = true;
  resetPasswordForm = new FormGroup({
    token: new FormControl('', [Validators.required]),
    newPassword: new FormControl
    ('', [Validators.required,Validators.minLength(8),
      Validators.pattern(/^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)(?=.*[@$!%*?&])[A-Za-z\d@$!%*?&]{8,}$/)]),
    confirmPassword: new FormControl
    ('', [Validators.required])
  },
  {
    validators: this.passwordMatchValidator.bind(this)
  });
  
  private passwordMatchValidator(control: AbstractControl): ValidationErrors | null {

  const password = control.get('newPassword')?.value;
  const confirmPassword = control.get('confirmPassword')?.value;
  const confirmControl = control.get('confirmPassword');
  if (!confirmPassword || !password) {
  return null;
}
  if (password !== confirmPassword) {
    // Set error directly on the confirmPassword control
    confirmControl?.setErrors({ ...confirmControl.errors, passwordMismatch: true });
    return { passwordMismatch: true };
  } else {
    // Clear the passwordMismatch error if they match
    const errors = confirmControl?.errors;
  if (errors) {
    delete errors['passwordMismatch'];
    if (Object.keys(errors).length === 0) {
      confirmControl?.setErrors(null);
    } else {
      confirmControl?.setErrors(errors);
    }
  }
  }

  return null;

  return null;
}

  onResetPassword(){
     if (this.resetPasswordForm.invalid) {
    this.resetPasswordForm.markAllAsTouched();
    return;
  }

  this.isLoading = true;

  const payload = {
    token: this.resetPasswordForm.controls.token.value!,
    newPassword: this.resetPasswordForm.controls.newPassword.value!
  };

  this.authService.resetPassword(payload)
    .pipe(
      finalize(() => {
        this.isLoading = false;
      })
    )
    .subscribe({
      next: (res:any) => {

        this.messageDisplayService.success(
          res?.message
        );

        this.router.navigate(['/auth/login']);
      },

      error: (error:any) => {
     
        this.messageDisplayService.error(
          error?.error?.message ||
          'Unable to reset password. Please try again.'
        );
      }
    });
  }
}
