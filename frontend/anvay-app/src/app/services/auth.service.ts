import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, tap } from 'rxjs';
import { LoginRequest, LoginResponse, RegisterInstitutionRequest, RegisterStudentRequest } from '../models/user.model';
import { environment } from 'src/environments/environment';
@Injectable({ providedIn: 'root' })
export class AuthService {
  private readonly API_URL = `${environment.apiBaseUrl}/api/auth`;
  private readonly TOKEN_KEY = 'anvay_token';
  private readonly USER_KEY = 'anvay_user';

  constructor(private http: HttpClient) {}

  login(request: LoginRequest): Observable<LoginResponse> {
    return this.http.post<LoginResponse>(`${this.API_URL}/login`, request).pipe(
      tap(response => this.storeUser(response))
    );
  }

  registerInstitution(request: RegisterInstitutionRequest): Observable<LoginResponse> {
    return this.http.post<LoginResponse>(`${this.API_URL}/register/institution`, request).pipe(
      tap(response => this.storeUser(response))
    );
  }

  registerStudent(request: RegisterStudentRequest): Observable<LoginResponse> {
    // No .tap(storeUser) — student registration is an APPLICATION, not a session.
    // Backend returns { token: null, status: "pending" } so even if we stored
    // it, isLoggedIn() would (correctly) be false. But we don't store at all
    // here, so the component can route the user to /login cleanly afterwards.
    return this.http.post<LoginResponse>(`${this.API_URL}/register/student`, request);
  }

  private storeUser(response: LoginResponse): void {
    // Refuse to persist a session when there's no real token. Without this guard,
    // a pending-account response (token: null) would write the literal string
    // "null" into localStorage, which then satisfies isLoggedIn() falsely.
    if (!response || !response.token || typeof response.token !== 'string') {
      return;
    }
    localStorage.setItem(this.TOKEN_KEY, response.token);
    localStorage.setItem(this.USER_KEY, JSON.stringify(response));
  }

  getToken(): string | null {
    const t = localStorage.getItem(this.TOKEN_KEY);
    // Treat the literal strings "null" / "undefined" — produced by historical
    // bad writes — as no token. Otherwise stale junk locks users out / in.
    if (!t || t === 'null' || t === 'undefined') return null;
    return t;
  }

  getCurrentUser(): LoginResponse | null {
    const data = localStorage.getItem(this.USER_KEY);
    if (!data || data === 'null' || data === 'undefined') return null;
    try { return JSON.parse(data); } catch { return null; }
  }

  getRole(): string | null {
    return this.getCurrentUser()?.role ?? null;
  }

  isLoggedIn(): boolean {
    return !!this.getToken();
  }

  logout(): void {
    localStorage.removeItem(this.TOKEN_KEY);
    localStorage.removeItem(this.USER_KEY);
  }
}
