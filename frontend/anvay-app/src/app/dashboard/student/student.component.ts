import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { HttpClient } from '@angular/common/http';
import { Router } from '@angular/router';
import { AuthService } from '../../services/auth.service';

interface StudentDashboard {
  firstName: string; institutionName: string; totalPoints: number; rank: number;
  registeredEventsCount: number; joinedClubsCount: number; achievementCount: number;
  upcomingEvents: EventRecord[]; joinedClubs: ClubRecord[]; achievements: AchievementRecord[];
}
interface EventRecord { title: string; institution: string; dateTime: string; type: string; }
interface ClubRecord { name: string; memberCount: number; category: string; }
interface AchievementRecord { title: string; year: string; badgeType: string; }
interface EventFeed { eventId: number; title: string; location: string; institution: string; institutionId: number; type: string; participantType: string; registeredCount: number; totalCapacity: number; isRegistered: boolean; }
interface Club { clubId: number; clubName: string; category: string; type: string; membersCount: number; memberCount: number; institutionId: number; }
interface LeaderboardUser { userId: number; firstName: string; lastName: string; email: string; totalPoints: number; joinedClubsCount: number; registeredEventsCount: number; }
interface UserProfile { userId: number; firstName: string; lastName: string; email: string; totalPoints: number; rank: number; registeredEventsCount: number; joinedClubsCount: number; role: string; }

@Component({
  selector: 'app-student',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './student.component.html',
  styleUrls: ['./student.component.css']
})
export class StudentComponent implements OnInit {
  activeView: 'dashboard' | 'events' | 'clubs' | 'leaderboard' | 'profile' = 'dashboard';
  sidebarOpen = true;
  studentId: number = 0;
  institutionId: number = 0;

  dashboard: StudentDashboard | null = null;
  dashLoading = false;

  events: EventFeed[] = [];
  eventsLoading = false;
  searchEvent = '';

  allClubs: Club[] = [];
  myClubIds: number[] = [];
  clubsLoading = false;

  leaderboard: LeaderboardUser[] = [];
  lbLoading = false;

  profile: UserProfile | null = null;
  profileLoading = false;

  message = ''; messageType = '';

  constructor(private http: HttpClient, private authService: AuthService, private router: Router) {}

  ngOnInit() {
    const user = this.authService.getCurrentUser();
    this.studentId = user?.userId ?? 0;
    this.institutionId = user?.institutionId ?? 0;
    this.loadDashboard();
  }

  setView(v: 'dashboard'|'events'|'clubs'|'leaderboard'|'profile') {
    this.activeView = v; this.message = '';
    if (v === 'dashboard') this.loadDashboard();
    if (v === 'events') this.loadEvents();
    if (v === 'clubs') this.loadClubs();
    if (v === 'leaderboard') this.loadLeaderboard();
    if (v === 'profile') this.loadProfile();
  }

  loadDashboard() {
    if (!this.studentId) return;
    this.dashLoading = true;
    this.http.get<StudentDashboard>(`/api/students/${this.studentId}/dashboard`).subscribe({
      next: d => { this.dashboard = d; this.dashLoading = false; },
      error: () => this.dashLoading = false
    });
  }

  loadEvents() {
    this.eventsLoading = true;
    this.http.get<EventFeed[]>(`/api/events/feed?userId=${this.studentId}&institutionId=${this.institutionId}`).subscribe({
      next: e => { this.events = e; this.eventsLoading = false; },
      error: () => this.eventsLoading = false
    });
  }

  get filteredEvents() {
    if (!this.searchEvent) return this.events;
    const q = this.searchEvent.toLowerCase();
    return this.events.filter(e => e.title.toLowerCase().includes(q) || e.institution?.toLowerCase().includes(q) || e.type?.toLowerCase().includes(q));
  }

  registerEvent(eventId: number) {
    this.http.post('/api/events/register', { eventId, userId: this.studentId }).subscribe({
      next: () => { this.showMessage('Registered successfully!', 'success'); const ev = this.events.find(e => e.eventId === eventId); if (ev) { ev.isRegistered = true; ev.registeredCount = (ev.registeredCount || 0) + 1; } },
      error: () => this.showMessage('Registration failed', 'error')
    });
  }

  loadClubs() {
    this.clubsLoading = true;
    this.http.get<any[]>(`/api/clubs/user/${this.studentId}`).subscribe({
      next: memberships => { this.myClubIds = memberships.map(m => m.clubId); },
      error: () => {}
    });
    this.http.get<Club[]>(`/api/clubs/institution/${this.institutionId}`).subscribe({
      next: c => { this.allClubs = c; this.clubsLoading = false; },
      error: () => this.clubsLoading = false
    });
  }

  joinClub(clubId: number) {
    this.http.post(`/api/clubs/${clubId}/join`, { userId: this.studentId }).subscribe({
      next: () => { this.showMessage('Join request sent!', 'success'); this.myClubIds.push(clubId); },
      error: (e) => this.showMessage(e.error || 'Already requested', 'error')
    });
  }

  isJoined(clubId: number) { return this.myClubIds.includes(clubId); }

  applyLeadership(clubId: number) {
    const experience = prompt('Describe your experience for this leadership role:');
    if (!experience) return;
    this.http.post('/api/leadership-applications', { clubId, userId: this.studentId, experience }).subscribe({
      next: () => this.showMessage('Leadership application submitted!', 'success'),
      error: () => this.showMessage('Failed to apply', 'error')
    });
  }

  loadLeaderboard() {
    this.lbLoading = true;
    const url = this.institutionId ? `/api/students/institution/${this.institutionId}/leaderboard` : '/api/students';
    this.http.get<LeaderboardUser[]>(url).subscribe({
      next: l => { this.leaderboard = l.sort((a,b) => (b.totalPoints||0) - (a.totalPoints||0)); this.lbLoading = false; },
      error: () => this.lbLoading = false
    });
  }

  loadProfile() {
    if (!this.studentId) return;
    this.profileLoading = true;
    this.http.get<UserProfile>(`/api/students/${this.studentId}/profile`).subscribe({
      next: p => { this.profile = p; this.profileLoading = false; },
      error: () => this.profileLoading = false
    });
  }

  showMessage(msg: string, type: string) { this.message = msg; this.messageType = type; setTimeout(() => this.message = '', 3000); }
  getRankLabel(i: number) { return i===0?'1st':i===1?'2nd':i===2?'3rd':`#${i+1}`; }
  logout() { this.authService.logout(); this.router.navigate(['/login']); }
  getCapacityPct(r: number, t: number) { return t ? Math.min(100, Math.round((r/t)*100)) : 0; }
}
