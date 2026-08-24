import { Component } from '@angular/core';
import { FormControl, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
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
  selector: 'app-registration-form',
  imports: [MatFormFieldModule,
    MatInputModule,
    MatButtonModule,
    ReactiveFormsModule,
    MatIconModule,
    MatProgressSpinnerModule,
    RouterLink],
  templateUrl: './registration-form.html',
  styleUrl: './registration-form.scss',
})
export class RegistrationForm {
constructor(private authService:Auth, 
    private router :Router,
    private messageDisplayService:MessageDisplayService

  ){}
  hidePassword:boolean = true;
  isLoading:boolean = false;
   registerForm  = new FormGroup({
    username: new FormControl('', [Validators.required]),
    email: new FormControl('', [Validators.required,Validators.email]),
    password: new FormControl
    ('', [Validators.required,Validators.minLength(8),Validators.pattern(/^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)(?=.*[@$!%*?&])[A-Za-z\d@$!%*?&]{8,}$/)])
  });

  onRegister(): void {

    if (this.registerForm.invalid) {
      this.registerForm.markAllAsTouched();
      return;
    }
   this.isLoading = true;
     const payload = {
        username: this.registerForm?.controls?.username?.value,
        email : this.registerForm?.controls?.email?.value,
        password : this.registerForm?.controls?.password?.value,
   
     }
     this.authService.register(payload) .pipe(
       //The finalize operator comes from RxJS. Its role is to run a callback once the observable completes or errors out, regardless of success or failure.
         finalize(() => {
           this.isLoading = false; //// always executed if success or fail it work
         })
       )
       .subscribe({
   
         next: (response:any) => {
           
           this.messageDisplayService.success(response?.message);
           this.router.navigate(['/auth/login']);
         },
   
         error: (error) => {
   
           this.messageDisplayService.error(
             'Unable to process registration request.'
           );
          //  this.router.navigate(['/auth/login']);
         }
   
       });
  }

}
