import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { environment } from '../../../environments/environment';

@Injectable({
  providedIn: 'root',
})
export class Auth {
  constructor(private http:HttpClient){}
  login(payload:any ){
    return this.http.post(`${environment.apiUrl}/api/v1/auth/login`,payload,{
    withCredentials: true
  })  
  }
  testApi(){
    return this.http.get(`${environment.apiUrl}/api/v1/auth/test`)
  }
  refreshAccessToken() {
  return this.http.post<any>(
    `${environment.apiUrl}/api/v1/auth/refresh`,
    {},
    {
      withCredentials: true
      //This tells the browser to include cookies with the request.
    }
  );
}
}
