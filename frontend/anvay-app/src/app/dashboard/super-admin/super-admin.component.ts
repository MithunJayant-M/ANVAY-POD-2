import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { HttpClient } from '@angular/common/http';
import { FormsModule } from '@angular/forms';
import { Router } from '@angular/router';
import { AuthService } from '../../services/auth.service';

interface InstitutionDto {
  institutionId: number;
  institutionName: string;
  email: string;
  phone: string;
  address: string;
  status: string;
  registeredAt: string;
  studentCount: number;
  eventCount: number;
  clubCount: number;
  totalPoints: number;
}

interface DashboardStats {
  totalColleges: number;
  activeColleges: number;
  pendingColleges: number;
  inactiveColleges: number;
  totalEvents: number;
  totalClubs: number;
  totalStudents: number;
  topColleges: InstitutionDto[];
  eventTrendsByMonth: Record<string, number>;
}

interface AnalyticsDto {
  totalUsers: number;
  totalStudents: number;
  totalInstitutions: number;
  totalEvents: number;
  totalClubs: number;
  totalPayments: number;
  totalRevenue: number;
  institutionRankings: InstitutionDto[];
}

@Component({
  selector: 'app-super-admin',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './super-admin.component.html',
  styleUrls: ['./super-admin.component.css']
})
export class SuperAdminComponent implements OnInit {
  private readonly API = '/api/super-admin';

  activeView: 'dashboard' | 'colleges' | 'analytics' | 'settings' = 'dashboard';
  adminName = '';

  // Dashboard
  stats: DashboardStats | null = null;
  dashboardLoading = false;

  // College Management
  institutions: InstitutionDto[] = [];
  searchQuery = '';
  collegesLoading = false;

  // Analytics
  analytics: AnalyticsDto | null = null;
  analyticsLoading = false;

  actionMessage = '';
  actionError = '';

  constructor(
    private http: HttpClient,
    private authService: AuthService,
    private router: Router
  ) {}

  ngOnInit(): void {
    const user = this.authService.getCurrentUser();
    this.adminName = user?.name ?? 'Super Admin';
    this.loadDashboard();
  }

  setView(view: 'dashboard' | 'colleges' | 'analytics' | 'settings'): void {
    this.activeView = view;
    this.clearMessages();
    if (view === 'dashboard' && !this.stats) this.loadDashboard();
    if (view === 'colleges') this.loadColleges();
    if (view === 'analytics' && !this.analytics) this.loadAnalytics();
  }

  loadDashboard(): void {
    this.dashboardLoading = true;
    this.http.get<DashboardStats>(`${this.API}/dashboard`).subscribe({
      next: (data) => { this.stats = data; this.dashboardLoading = false; },
      error: () => { this.dashboardLoading = false; }
    });
  }

  loadColleges(): void {
    this.collegesLoading = true;
    const url = this.searchQuery
      ? `${this.API}/institutions?search=${encodeURIComponent(this.searchQuery)}`
      : `${this.API}/institutions`;
    this.http.get<InstitutionDto[]>(url).subscribe({
      next: (data) => { this.institutions = data; this.collegesLoading = false; },
      error: () => { this.collegesLoading = false; }
    });
  }

  onSearch(): void {
    this.loadColleges();
  }

  loadAnalytics(): void {
    this.analyticsLoading = true;
    this.http.get<AnalyticsDto>(`${this.API}/analytics`).subscribe({
      next: (data) => { this.analytics = data; this.analyticsLoading = false; },
      error: () => { this.analyticsLoading = false; }
    });
  }

  approveInstitution(id: number): void {
    this.clearMessages();
    this.http.put<InstitutionDto>(`${this.API}/institutions/${id}/approve`, {}).subscribe({
      next: (updated) => {
        this.updateInstitutionInList(updated);
        this.actionMessage = `Institution approved successfully.`;
      },
      error: () => { this.actionError = 'Failed to approve institution.'; }
    });
  }

  deactivateInstitution(id: number): void {
    this.clearMessages();
    this.http.put<InstitutionDto>(`${this.API}/institutions/${id}/deactivate`, {}).subscribe({
      next: (updated) => {
        this.updateInstitutionInList(updated);
        this.actionMessage = `Institution deactivated.`;
      },
      error: () => { this.actionError = 'Failed to deactivate institution.'; }
    });
  }

  private updateInstitutionInList(updated: InstitutionDto): void {
    const idx = this.institutions.findIndex(i => i.institutionId === updated.institutionId);
    if (idx !== -1) this.institutions[idx] = updated;
  }

  getTrendKeys(): string[] {
    return this.stats ? Object.keys(this.stats.eventTrendsByMonth) : [];
  }

  getStatusClass(status: string): string {
    return { active: 'badge-active', pending: 'badge-pending', inactive: 'badge-inactive' }[status] ?? '';
  }

  getRankBadge(index: number): string {
    return index === 0 ? '🥇' : index === 1 ? '🥈' : index === 2 ? '🥉' : `#${index + 1}`;
  }

  logout(): void {
    this.authService.logout();
    this.router.navigate(['/login']);
  }

  private clearMessages(): void {
    this.actionMessage = '';
    this.actionError = '';
  }
}
