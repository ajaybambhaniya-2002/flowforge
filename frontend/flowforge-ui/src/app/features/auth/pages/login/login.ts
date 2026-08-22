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

@Component({
  selector: 'app-login',
  imports: [MatFormFieldModule,
    MatInputModule,
    MatButtonModule,
    ReactiveFormsModule,
    MatIconModule,
    MatProgressSpinnerModule,
    RouterLink],
  templateUrl: './login.html',
  styleUrl: './login.scss',
})
export class Login {
  constructor(private authService:Auth, 
    private tokenService:TokenService,
    private authStateService:AuthStateService,
    private router :Router,
    private messageDisplayService:MessageDisplayService

  ){}
  hidePassword = true;
  isLoading = false;

    loginForm = new FormGroup({
    username: new FormControl('', [Validators.required]),
    password: new FormControl
    ('', [Validators.required,Validators.minLength(8),
      Validators.pattern(/^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)(?=.*[@$!%*?&])[A-Za-z\d@$!%*?&]{8,}$/)])
  });
  onTest(){
      this.authService.testApi().subscribe((res:any)=>{
        console.log(res);
      })
  }
 onLogin(): void {

    if (this.loginForm.invalid) {
      this.loginForm.markAllAsTouched();
      return;
    }else{
        this.isLoading = true;
       
      const payload = {
        username:this.loginForm?.controls?.username?.value || null,
        password:this.loginForm?.controls?.password?.value || null
      }
      this.authService.login(payload).subscribe({
        next:(res:any)=>{
          if(res?.data != null){
             this.tokenService.setAccessToken(res?.data?.accessToken);
              this.authStateService.setAuthenticated();
              this.messageDisplayService.success('Login successful.');
              this.router.navigate(['/dashboard']);
          }
        },
        error:(err)=>{
            if (err.status === 401) {
              this.messageDisplayService.error('Invalid username or password.');
            }
            else if (err.status === 400) {
              this.messageDisplayService.error('Please check your login details.');
            }
            else if (err.status === 0) {
              this.messageDisplayService.error('Unable to connect to server. Please try again.');
            }
            else {
              this.messageDisplayService.error('Something went wrong. Please try again later.');
            }
            this.isLoading = false;
        },
          complete: () => {
            this.isLoading = false;
          }
        
      })
    }
 
  }
}
