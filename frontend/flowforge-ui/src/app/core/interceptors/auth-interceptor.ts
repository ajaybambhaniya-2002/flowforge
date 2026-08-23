import { HttpInterceptorFn } from '@angular/common/http';
import { TokenService } from '../services/token-service';
import { inject } from '@angular/core';
import { catchError, switchMap, tap, throwError } from 'rxjs';
import { Auth } from '../services/auth';
import { AuthStateService } from '../services/auth-state-service';
import { TokenRefreshService } from '../services/token-refresh-service';
export const authInterceptor: HttpInterceptorFn = (req, next) => {
  const tokenService = inject(TokenService);
  const authService = inject(Auth);
  const authStateService = inject(AuthStateService);
  const tokenRefreshService = inject(TokenRefreshService);

  const isAuthRequest =
    req.url.includes('/auth/login') ||
    req.url.includes('/auth/refresh') ||
    req.url.includes('/auth/register') ||
    req.url.includes('/auth/logout'); 
//These requests don’t need an Authorization header because they are used to obtain or refresh tokens.
  if (isAuthRequest) {
    return next(req);
  }


  const accessToken = tokenService.getAccessToken();

  if (!accessToken) {
    return next(req);
  }
//Creates a copy of the original request (req.clone()).Adds an Authorization header with the JWT token in the format:
   if (accessToken && !isAuthRequest) {
    req = req.clone({
      setHeaders: {
        Authorization: `Bearer ${accessToken}`
      }
    });
  }

  return next(req).pipe(
    // here we check if access token is expired then need to generate new access token retry of api 
    catchError((error) => {
      if (
        (error.status !== 401 &&  error.status !== 403) ||
        isAuthRequest
      ) {
        return throwError(() => error);
      }
      
      // when 401 or 403 happen need to generate new one
      return tokenRefreshService.refreshToken().pipe(

        switchMap(() => {

          const newToken =
            tokenService.getAccessToken();

          const retryRequest = req.clone({
            setHeaders: {
              Authorization: `Bearer ${newToken}`
            }
          });

          return next(retryRequest);
        }),

        catchError((refreshError) => {

          tokenService.clearAccessToken();

          authStateService.setUnauthenticated();

          return throwError(() => refreshError);
        })
      );
    })
  );
};
