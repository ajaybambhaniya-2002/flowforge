import { Injectable } from '@angular/core';
import { Project } from './projects/model/project/project.model';
import { environment } from '../../environments/environment';
import { HttpClient } from '@angular/common/http';

@Injectable({
  providedIn: 'root',
})
export class ProjectService {
  constructor(private http:HttpClient){}
    private projects: Project[] = [];
   getAllProjects(){
    return this.http.get(`${environment.apiUrl}/api/projects`)
  }

  createProject(payload: any) {
    return this.http.post(`${environment.apiUrl}/api/projects/createProject`,payload);
  }
  getProjectById(id:any) {

    return this.http.get(`${environment.apiUrl}/api/projects/getProjectById?id=${id}`)
  }

  updateProject(id:any,payload:any){
    return this.http.post(`${environment.apiUrl}/api/projects/updateProject?id=${id}`,payload);
  }
  deleteProject(id:any){
    return this.http.delete(`${environment.apiUrl}/api/projects/deleteProject?id=${id}`);
  }
}
