import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { environment } from '../../../environments/environment';

@Injectable({
  providedIn: 'root',
})
export class Auth {
  constructor(private http: HttpClient) {}
  register(payload: any) {
    return this.http.post(`${environment.apiUrl}/api/v1/auth/register`, payload);
  }

  login(payload: any) {
    return this.http.post(`${environment.apiUrl}/api/v1/auth/login`, payload, {
      withCredentials: true,
    });
  }

  logout() {
    return this.http.post(
      `${environment.apiUrl}/api/v1/auth/logout`,
      {},
      {
        withCredentials: true,
      },
    );
  }
  getProfileData(){
    return this.http.get(`${environment.apiUrl}/api/v1/auth/profile`)
  }
  forgotPassword(payload: any) {
    return this.http.post(`${environment.apiUrl}/api/v1/auth/forgot-password`, payload);
  }

  resetPassword(payload: any) {
    return this.http.post(`${environment.apiUrl}/api/v1/auth/reset-password`, payload);
  }

  testApi() {
    return this.http.get(`${environment.apiUrl}/api/v1/auth/test`);
  }

  verifyEmail(token: string) {
    return this.http.get(`${environment.apiUrl}/api/v1/auth/verify-email`, { params: { token } });
  }

  refreshAccessToken() {
    return this.http.post<any>(
      `${environment.apiUrl}/api/v1/auth/refresh`,
      {},
      {
        withCredentials: true,
        //This tells the browser to include cookies with the request.
      },
    );
  }

}
