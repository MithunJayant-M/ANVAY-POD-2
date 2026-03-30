import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Club, ClubDashboard, ClubMember } from '../models/club.model';

@Injectable({
  providedIn: 'root'
})
export class ClubService {
  private apiUrl = 'http://localhost:9092/api/clubs';

  constructor(private http: HttpClient) {}

  /**
   * Get all clubs for an institution
   */
  getClubsByInstitution(institutionId: number): Observable<ClubDashboard[]> {
    return this.http.get<ClubDashboard[]>(
      `${this.apiUrl}/institution/${institutionId}`
    );
  }

  /**
   * Get club by ID
   */
  getClubById(clubId: number): Observable<Club> {
    return this.http.get<Club>(`${this.apiUrl}/${clubId}`);
  }

  /**
   * Get club members
   */
  getClubMembers(clubId: number): Observable<ClubMember[]> {
    return this.http.get<ClubMember[]>(`${this.apiUrl}/${clubId}/members`);
  }

  /**
   * Get approved club members
   */
  getApprovedMembers(clubId: number): Observable<ClubMember[]> {
    return this.http.get<ClubMember[]>(
      `${this.apiUrl}/${clubId}/members/approved`
    );
  }

  /**
   * Create new club
   */
  createClub(club: Club): Observable<Club> {
    return this.http.post<Club>(this.apiUrl, club);
  }

  /**
   * Update club
   */
  updateClub(clubId: number, club: Club): Observable<Club> {
    return this.http.put<Club>(`${this.apiUrl}/${clubId}`, club);
  }

  /**
   * Delete club
   */
  deleteClub(clubId: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${clubId}`);
  }

  /**
   * Get club count for institution
   */
  getClubCount(institutionId: number): Observable<number> {
    return this.http.get<number>(
      `${this.apiUrl}/institution/${institutionId}/count`
    );
  }
}
