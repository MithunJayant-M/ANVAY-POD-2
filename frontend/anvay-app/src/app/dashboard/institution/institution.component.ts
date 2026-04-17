import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule, ReactiveFormsModule, FormBuilder, FormGroup, Validators } from '@angular/forms';
import { HttpClient } from '@angular/common/http';
import { Router } from '@angular/router';
import { AuthService } from '../../services/auth.service';

interface Event {
  eventId: number; eventName: string; description: string; category: string;
  location: string; startDate: string; endDate: string; registrationDeadline: string;
  maxParticipants: number; registrationFee: number; status: string; eventRules: string;
  participantType: string; clubId: number;
  winnersStatus?: string; winner1UserId?: number; winner2UserId?: number; winner3UserId?: number;
}
interface Club { clubId: number; clubName: string; category: string; membersCount: number; joinRequestsCount: number; leadershipAppsCount: number; createdDate: string; }
interface ClubMember { id: number; userId: number; clubId: number; status: string; user?: {firstName: string; email: string}; }
interface LeadershipApp { applicationId: number; userId: number; clubId: number; experience: string; status: string; appliedAt: string; user?: {firstName: string; lastName: string; email: string}; }
interface EventParticipant { id: number; userId: number; eventId: number; status: string; points_earned?: number; user?: {firstName: string; lastName: string; email: string}; }
interface Student { userId: number; firstName: string; lastName: string; email: string; totalPoints: number; registeredEventsCount: number; joinedClubsCount: number; achievements?: Achievement[]; }
interface Achievement { title: string; description: string; badgeType: string; }
interface InstitutionRank { institutionId: number; institutionName: string; totalPoints: number; studentCount: number; eventCount: number; }

@Component({
  selector: 'app-institution',
  standalone: true,
  imports: [CommonModule, FormsModule, ReactiveFormsModule],
  templateUrl: './institution.component.html',
  styleUrls: ['./institution.component.css']
})
export class InstitutionComponent implements OnInit {
  activeView: 'dashboard' | 'events' | 'clubs' | 'students' = 'dashboard';
  sidebarOpen = true;
  adminName = '';
  institutionName = '';
  institutionId: number = 0;
  isPending = false;
  institutionStatus = '';

  // Dashboard
  dashStats = { totalEvents: 0, totalClubs: 0, totalStudents: 0, totalPoints: 0 };

  // Events
  events: Event[] = [];
  eventsLoading = false;
  showEventModal = false;
  editingEvent: Event | null = null;
  selectedEvent: Event | null = null;
  selectedEventParticipants: EventParticipant[] = [];
  selectedEventParticipantsLoading = false;
  eventForm!: FormGroup;
  categories = ['Technical', 'Cultural', 'Sports', 'Academic', 'Workshop', 'Seminar', 'Hackathon', 'Other'];

  // Clubs
  clubs: Club[] = [];
  clubsLoading = false;
  showClubModal = false;
  editingClub: Club | null = null;
  clubForm!: FormGroup;
  clubCategories = ['Computer', 'Robotics', 'Entrepreneurship', 'Cultural', 'Sports', 'Science', 'Arts', 'Other'];
  selectedClub: Club | null = null;
  clubMembers: ClubMember[] = [];
  clubLeadershipApps: LeadershipApp[] = [];
  clubDetailTab: 'members' | 'requests' | 'leadership' = 'members';

  // Students
  students: Student[] = [];
  studentsLoading = false;

  collegeLb: InstitutionRank[] = [];
  collegeLbLoading = false;

  message = ''; messageType = '';

  // Winners modal
  winnersModal = false;
  winnersEventId: number | null = null;
  winnersEventName = '';
  eventParticipants: EventParticipant[] = [];
  winner1: number | null = null;
  winner2: number | null = null;
  winner3: number | null = null;
  winnersLoading = false;

  approveModal = false;
  approveVerified = false;
  pendingClubId: number | null = null;
  pendingMemberId: number | null = null;

  constructor(private http: HttpClient, private authService: AuthService, private router: Router, private fb: FormBuilder) {}

  ngOnInit() {
    const user = this.authService.getCurrentUser();
    this.adminName = user?.name ?? 'Admin';
    this.institutionId = user?.institutionId ?? 0;
    this.initForms();
    if (this.institutionId) {
      this.http.get<any>(`/api/institutions/${this.institutionId}`).subscribe({
        next: inst => {
          this.institutionName = inst.name ?? '';
          this.institutionStatus = inst.status ?? '';
          this.isPending = inst.status !== 'active';
          if (!this.isPending) this.loadDashboardStats();
        },
        error: () => { this.loadDashboardStats(); }
      });
    }
  }

  initForms() {
    this.eventForm = this.fb.group({
      eventName: ['', Validators.required],
      description: [''],
      category: ['', Validators.required],
      location: ['', Validators.required],
      startDate: ['', Validators.required],
      endDate: [''],
      registrationDeadline: [''],
      maxParticipants: [100, [Validators.required, Validators.min(1)]],
      registrationFee: [0],
      eventRules: [''],
      participantType: ['all'],  // always public
      status: ['active'],
      clubId: [null, Validators.required]
    });
    this.clubForm = this.fb.group({
      clubName: ['', Validators.required],
      category: ['', Validators.required]
    });
  }

  setView(v: 'dashboard'|'events'|'clubs'|'students') {
    this.activeView = v;
    this.message = ''; this.selectedClub = null; this.selectedEvent = null;
    if (v === 'events') { this.loadClubs(); this.loadEvents(); }
    if (v === 'clubs') this.loadClubs();
    if (v === 'students') this.loadStudents();
  }

  loadDashboardStats() {
    if (!this.institutionId) return;
    this.http.get<Event[]>(`/api/events/institution/${this.institutionId}`).subscribe({ next: d => this.dashStats.totalEvents = d.length, error: () => {} });
    this.http.get<Club[]>(`/api/clubs/institution/${this.institutionId}`).subscribe({ next: d => this.dashStats.totalClubs = d.length, error: () => {} });
    this.http.get<Student[]>(`/api/students/institution/${this.institutionId}/leaderboard`).subscribe({ next: d => { this.dashStats.totalStudents = d.length; this.dashStats.totalPoints = d.reduce((s, st) => s + (st.totalPoints||0), 0); }, error: () => {} });
    this.loadCollegeLeaderboard();
  }

  loadCollegeLeaderboard() {
    this.collegeLbLoading = true;
    this.http.get<InstitutionRank[]>('/api/institutions/leaderboard').subscribe({
      next: d => {
        this.collegeLb = d;
        this.collegeLbLoading = false;
        const mine = d.find(i => i.institutionId === this.institutionId);
        if (mine) this.dashStats.totalPoints = mine.totalPoints;
      },
      error: () => { this.collegeLbLoading = false; }
    });
  }

  // EVENTS
  loadEvents() {
    this.eventsLoading = true;
    this.http.get<Event[]>(`/api/events/institution/${this.institutionId}`).subscribe({
      next: e => { this.events = e; this.eventsLoading = false; },
      error: () => { this.eventsLoading = false; }
    });
  }

  openCreateEvent() {
    this.editingEvent = null;
    const defaultClubId = this.clubs[0]?.clubId ?? null;
    this.eventForm.reset({ maxParticipants: 100, registrationFee: 0, participantType: 'all', status: 'active', clubId: defaultClubId });
    this.showEventModal = true;
  }
  openEditEvent(ev: Event) { this.editingEvent = ev; this.eventForm.patchValue({ ...ev, startDate: ev.startDate?.substring(0,16), endDate: ev.endDate?.substring(0,16), registrationDeadline: ev.registrationDeadline?.substring(0,16) }); this.showEventModal = true; }

  saveEvent() {
    if (this.eventForm.invalid) return;
    const data = this.eventForm.value;
    if (this.editingEvent) {
      this.http.put(`/api/events/${this.editingEvent.eventId}`, data).subscribe({
        next: () => { this.showMessage('Event updated!', 'success'); this.showEventModal = false; this.loadEvents(); },
        error: () => this.showMessage('Failed to update event', 'error')
      });
    } else {
      this.http.post('/api/events', data).subscribe({
        next: () => { this.showMessage('Event created!', 'success'); this.showEventModal = false; this.loadEvents(); },
        error: () => this.showMessage('Failed to create event', 'error')
      });
    }
  }

  deleteEvent(id: number) {
    if (!confirm('Delete this event?')) return;
    this.http.delete(`/api/events/${id}`).subscribe({ next: () => { this.showMessage('Event deleted', 'success'); this.loadEvents(); }, error: () => this.showMessage('Failed to delete','error') });
  }

  // CLUBS
  loadClubs() {
    this.clubsLoading = true;
    this.http.get<Club[]>(`/api/clubs/institution/${this.institutionId}`).subscribe({
      next: c => { this.clubs = c; this.clubsLoading = false; },
      error: () => this.clubsLoading = false
    });
  }

  openCreateClub() { this.editingClub = null; this.clubForm.reset(); this.showClubModal = true; }
  openEditClub(c: Club) { this.editingClub = c; this.clubForm.patchValue({ clubName: c.clubName, category: (c as any).type || c.category }); this.showClubModal = true; }

  saveClub() {
    if (this.clubForm.invalid) return;
    const d = { ...this.clubForm.value, institutionId: this.institutionId };
    if (this.editingClub) {
      this.http.put(`/api/clubs/${this.editingClub.clubId}`, d).subscribe({
        next: () => { this.showMessage('Club updated!','success'); this.showClubModal=false; this.loadClubs(); },
        error: () => this.showMessage('Failed','error')
      });
    } else {
      this.http.post('/api/clubs', d).subscribe({
        next: () => { this.showMessage('Club created!','success'); this.showClubModal=false; this.loadClubs(); },
        error: () => this.showMessage('Failed','error')
      });
    }
  }

  viewClubDetail(c: Club) {
    this.selectedClub = c;
    this.clubDetailTab = 'members';
    this.loadClubMembers(c.clubId);
    this.loadLeadershipApps(c.clubId);
  }

  loadClubMembers(clubId: number) {
    this.http.get<ClubMember[]>(`/api/clubs/${clubId}/members`).subscribe({ next: m => this.clubMembers = m, error: () => {} });
  }

  loadJoinRequests(clubId: number) {
    this.http.get<ClubMember[]>(`/api/clubs/${clubId}/join-requests`).subscribe({ next: m => this.clubMembers = m, error: () => {} });
  }

  loadLeadershipApps(clubId: number) {
    this.http.get<LeadershipApp[]>(`/api/leadership-applications/club/${clubId}/pending`).subscribe({ next: a => this.clubLeadershipApps = a, error: () => {} });
  }

  switchClubTab(tab: 'members'|'requests'|'leadership') {
    this.clubDetailTab = tab;
    if (tab === 'members') this.loadClubMembers(this.selectedClub!.clubId);
    if (tab === 'requests') this.loadJoinRequests(this.selectedClub!.clubId);
    if (tab === 'leadership') this.loadLeadershipApps(this.selectedClub!.clubId);
  }

  openApproveModal(clubId: number, memberId: number) {
    this.pendingClubId = clubId;
    this.pendingMemberId = memberId;
    this.approveVerified = false;
    this.approveModal = true;
  }

  confirmApprove() {
    if (!this.approveVerified || !this.pendingClubId || !this.pendingMemberId) return;
    this.http.put(`/api/clubs/${this.pendingClubId}/join-requests/${this.pendingMemberId}/approve`, {}).subscribe({
      next: () => { this.showMessage('Approved!','success'); this.loadJoinRequests(this.pendingClubId!); this.loadClubs(); this.approveModal = false; },
      error: () => { this.showMessage('Failed','error'); this.approveModal = false; }
    });
  }

  approveJoinRequest(clubId: number, memberId: number) { this.openApproveModal(clubId, memberId); }

  rejectJoinRequest(clubId: number, memberId: number) {
    this.http.put(`/api/clubs/${clubId}/join-requests/${memberId}/reject`, {}).subscribe({
      next: () => { this.showMessage('Rejected','success'); this.loadJoinRequests(clubId); this.loadClubs(); },
      error: () => this.showMessage('Failed','error')
    });
  }

  approveLeadership(appId: number) {
    this.http.put(`/api/leadership-applications/${appId}/approve`, {}).subscribe({
      next: () => { this.showMessage('Leadership application approved!','success'); this.loadLeadershipApps(this.selectedClub!.clubId); },
      error: () => this.showMessage('Failed','error')
    });
  }

  rejectLeadership(appId: number) {
    this.http.put(`/api/leadership-applications/${appId}/reject`, {}).subscribe({
      next: () => { this.showMessage('Application rejected','success'); this.loadLeadershipApps(this.selectedClub!.clubId); },
      error: () => this.showMessage('Failed','error')
    });
  }

  // STUDENTS
  loadStudents() {
    this.studentsLoading = true;
    this.http.get<Student[]>(`/api/students/institution/${this.institutionId}/leaderboard`).subscribe({
      next: s => { this.students = s.sort((a,b) => (b.totalPoints||0)-(a.totalPoints||0)); this.studentsLoading = false; },
      error: () => this.studentsLoading = false
    });
  }

  viewEventParticipants(ev: Event) {
    this.selectedEvent = ev;
    this.selectedEventParticipants = [];
    this.selectedEventParticipantsLoading = true;
    this.http.get<EventParticipant[]>(`/api/events/${ev.eventId}/participants`).subscribe({
      next: p => { this.selectedEventParticipants = p; this.selectedEventParticipantsLoading = false; },
      error: () => { this.selectedEventParticipantsLoading = false; }
    });
  }

  openWinnersModal(ev: Event) {
    if (ev.winnersStatus) return; // frozen once submitted
    this.winnersEventId = ev.eventId;
    this.winnersEventName = ev.eventName;
    this.winner1 = null; this.winner2 = null; this.winner3 = null;
    this.eventParticipants = [];
    this.winnersModal = true;
    this.winnersLoading = true;
    this.http.get<EventParticipant[]>(`/api/events/${ev.eventId}/participants`).subscribe({
      next: p => { this.eventParticipants = p; this.winnersLoading = false; },
      error: () => { this.winnersLoading = false; }
    });
  }

  submitWinners() {
    if (!this.winner1 && !this.winner2 && !this.winner3) { this.showMessage('Please select at least one rank winner', 'error'); return; }
    this.http.post(`/api/events/${this.winnersEventId}/award-winners`, {
      firstUserId: this.winner1,
      secondUserId: this.winner2 || null,
      thirdUserId: this.winner3 || null
    }).subscribe({
      next: () => { this.showMessage('Winners submitted! Awaiting super admin approval.', 'success'); this.winnersModal = false; this.loadEvents(); },
      error: () => this.showMessage('Failed to submit winners', 'error')
    });
  }

  showMessage(msg: string, type: string) { this.message = msg; this.messageType = type; setTimeout(() => this.message = '', 3000); }
  getRankLabel(i: number) { return i===0?'1st':i===1?'2nd':i===2?'3rd':`#${i+1}`; }
  logout() { this.authService.logout(); this.router.navigate(['/login']); }
}
