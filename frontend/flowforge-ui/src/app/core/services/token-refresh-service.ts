import { inject, Injectable } from '@angular/core';
import { Auth } from './auth';
import { TokenService } from './token-service';
import { finalize, Observable, shareReplay, tap } from 'rxjs';

@Injectable({
  providedIn: 'root',
})
export class TokenRefreshService {
   private authService = inject(Auth);
  private tokenService = inject(TokenService);

  private refreshRequest$: Observable<any> | null = null;

  refreshToken(): Observable<any> {

    if (!this.refreshRequest$) {

      this.refreshRequest$ =
        this.authService.refreshAccessToken().pipe(

          tap((response) => {

            this.tokenService.setAccessToken(
              response.accessToken
            );

          }),
           finalize(() => {
            this.refreshRequest$ = null;
          }),
          shareReplay(1)
        );
    }

    return this.refreshRequest$;
  }
}
