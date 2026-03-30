import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Event, EventCreateRequest, EventParticipant, DashboardStats } from '../models/event.model';

@Injectable({
  providedIn: 'root'
})
export class EventService {
  private apiUrl = 'http://localhost:9092/api/events';

  constructor(private http: HttpClient) {}

  /**
   * Get all events for an institution
   */
  getEventsByInstitution(institutionId: number): Observable<Event[]> {
    return this.http.get<Event[]>(
      `${this.apiUrl}/institution/${institutionId}`
    );
  }

  /**
   * Get all events
   */
  getAllEvents(): Observable<Event[]> {
    return this.http.get<Event[]>(this.apiUrl);
  }

  /**
   * Get event by ID
   */
  getEventById(eventId: number): Observable<Event> {
    return this.http.get<Event>(`${this.apiUrl}/${eventId}`);
  }

  /**
   * Create new event
   */
  createEvent(event: EventCreateRequest): Observable<Event> {
    return this.http.post<Event>(this.apiUrl, event);
  }

  /**
   * Update event
   */
  updateEvent(eventId: number, event: Partial<Event>): Observable<Event> {
    return this.http.put<Event>(`${this.apiUrl}/${eventId}`, event);
  }

  /**
   * Delete event
   */
  deleteEvent(eventId: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${eventId}`);
  }

  /**
   * Get event participants
   */
  getEventParticipants(eventId: number): Observable<EventParticipant[]> {
    return this.http.get<EventParticipant[]>(
      `${this.apiUrl}/${eventId}/participants`
    );
  }

  /**
   * Get participant count for an event
   */
  getParticipantCount(eventId: number): Observable<number> {
    return this.http.get<number>(
      `${this.apiUrl}/${eventId}/participants/count`
    );
  }

  /**
   * Get events by club
   */
  getEventsByClub(clubId: number): Observable<Event[]> {
    return this.http.get<Event[]>(`${this.apiUrl}/club/${clubId}`);
  }

  /**
   * Get upcoming events
   */
  getUpcomingEvents(institutionId: number): Observable<Event[]> {
    return this.http.get<Event[]>(
      `${this.apiUrl}/institution/${institutionId}/upcoming`
    );
  }

  /**
   * Get dashboard stats
   */
  getDashboardStats(institutionId: number): Observable<DashboardStats> {
    return this.http.get<DashboardStats>(
      `${this.apiUrl}/institution/${institutionId}/stats`
    );
  }

  /**
   * Register user for event
   */
  registerForEvent(eventId: number, userId: number): Observable<EventParticipant> {
    return this.http.post<EventParticipant>(
      `${this.apiUrl}/${eventId}/register`,
      { userId }
    );
  }

  /**
   * Unregister user from event
   */
  unregisterFromEvent(eventId: number, userId: number): Observable<void> {
    return this.http.delete<void>(
      `${this.apiUrl}/${eventId}/unregister/${userId}`
    );
  }
}
