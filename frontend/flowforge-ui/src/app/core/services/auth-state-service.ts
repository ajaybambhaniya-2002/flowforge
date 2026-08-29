import { Injectable, signal } from '@angular/core';
// export type AuthStatus =
//   | 'unknown'
//   | 'authenticated'
//   | 'unauthenticated';
@Injectable({
  providedIn: 'root'
})
export class AuthStateService {
  constructor(){}
// signal
//   ↓
// Angular knows something changed
//   ↓
// components can react
  private authenticated = false;
  private initialized = false;

  setAuthenticated(): void {
    this.authenticated = true;
    this.initialized = true;
  }

  setUnauthenticated(): void {
    this.authenticated = false;
    this.initialized = true;  
  }

  isAuthenticated(): boolean {
    return this.authenticated;
  }
  isInitialized(): boolean {
  return this.initialized;
}
//   restoreSession() {
//   return this.authService.refreshAccessToken();
// }
}
