import { Injectable } from '@angular/core';
import { Auth } from './auth';
import { TokenService } from './token-service';
import { AuthStateService } from './auth-state-service';
import {  Router } from '@angular/router';
import { catchError, of, tap } from 'rxjs';
@Injectable({
  providedIn: 'root',
})
export class SessionService {
  constructor(
    private authService: Auth,
    private tokenService: TokenService,
    private authStateService: AuthStateService,
    private router:Router
  ){}
  // it call when application refresh for this method call happen in app.ts
   restoreSession() {

    return this.authService.refreshAccessToken().pipe(

    tap((response) => {

      if (response?.accessToken) {

        this.tokenService.setAccessToken(
          response.accessToken
        );

        this.authStateService.setAuthenticated();
      }

    }),

    catchError((error) => {

      this.tokenService.clearAccessToken();

      this.authStateService.setUnauthenticated();

      return of(null);
    })
  );
  }
}
