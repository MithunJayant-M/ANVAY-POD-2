import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { ClubDashboardDTO } from '../models/club.model';
import { environment } from 'src/environments/environment';
@Injectable({ providedIn: 'root' })
export class ClubService {
  private baseUrl = `${environment.apiBaseUrl}/api/clubs`; // Ensure port 9092

  constructor(private http: HttpClient) {}

  // Verify this method name is exactly 'deleteClub'
  deleteClub(id: number): Observable<void> {
    return this.http.delete<void>(`${this.baseUrl}/${id}`);
  }

  getClubsByInstitution(id: number): Observable<ClubDashboardDTO[]> {
    return this.http.get<ClubDashboardDTO[]>(`${this.baseUrl}/institution/${id}`);
  }
}