import { Component, inject } from '@angular/core';
import { FormBuilder, FormControl, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { MatButtonModule } from '@angular/material/button';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatIconModule } from '@angular/material/icon';
import { MatInputModule } from '@angular/material/input';
import { Router, RouterLink } from '@angular/router';
import { Auth } from '../../../../core/services/auth';
import { MessageDisplayService } from '../../../../core/services/message-display-service';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';
import { finalize } from 'rxjs';
@Component({
  selector: 'app-forgot-password',
  imports: [MatFormFieldModule,
    MatInputModule,
    MatButtonModule,
    ReactiveFormsModule,
    MatIconModule,
    MatProgressSpinnerModule,
    RouterLink],
  templateUrl: './forgot-password.html',
  styleUrl: './forgot-password.scss',
})
export class ForgotPassword {
  constructor(private authService:Auth,private messageDisplayService:MessageDisplayService
    , private router :Router
  ){}
  forgotPasswordForm = new FormGroup({
    email:new FormControl('',[Validators.required,Validators.email])
  })
  isLoading = false;
  onSubmit(): void {

  if (this.forgotPasswordForm.invalid) {
    this.forgotPasswordForm.markAllAsTouched();
    return;
  }
  this.isLoading = true;
  const payload = {
     email : this.forgotPasswordForm?.controls?.email?.value?.trim()

  }
  this.authService.forgotPassword(payload) .pipe(
    //The finalize operator comes from RxJS. Its role is to run a callback once the observable completes or errors out, regardless of success or failure.
      finalize(() => {
        this.isLoading = false; //// always executed if success or fail it work
      })
    )
    .subscribe({

      next: (response:any) => {
        
        this.messageDisplayService.success(response?.message);
        this.router.navigate(['/auth/reset-password']);
      },

      error: (error) => {

        console.error('Forgot password error:', error);

        this.messageDisplayService.error(
          'Unable to process forgot password request.'
        );
        this.router.navigate(['/auth/login']);
      }

    });
  
}
}
