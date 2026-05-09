import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { StudentDashboardData } from '../models/student-dashboard.model'; 
import { environment } from 'src/environments/environment';
@Injectable({ providedIn: 'root' })
export class StudentService {
  private http = inject(HttpClient);
  private apiUrl = `${environment.apiBaseUrl}/api/students`;

  getDashboard(id: number): Observable<StudentDashboardData> {
    return this.http.get<StudentDashboardData>(`${this.apiUrl}/${id}/dashboard`);
  }
}