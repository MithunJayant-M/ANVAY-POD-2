import { Component, OnInit, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { EventService } from '../../services/event.service';
import { ClubService } from '../../services/club.service';
import { AuthService } from '../../services/auth.service';
import { Event, DashboardStats } from '../../models/event.model';
import { ClubDashboard } from '../../models/club.model';
import { CreateEventModalComponent } from '../event-management/create-event-modal/create-event-modal.component';
import { EventManagementComponent } from '../event-management/event-management.component';

@Component({
  selector: 'app-institution',
  standalone: true,
  imports: [
    CommonModule,
    FormsModule,
    ReactiveFormsModule,
    CreateEventModalComponent,
    EventManagementComponent
  ],
  templateUrl: './institution.component.html',
  styleUrls: ['./institution.component.css']
})
export class InstitutionComponent implements OnInit {
  // Dashboard Statistics
  dashboardStats = signal<DashboardStats>({
    totalEvents: 0,
    totalClubs: 0,
    totalStudents: 0,
    totalRegistrations: 0
  });

  // Data
  upcomingEvents = signal<Event[]>([]);
  clubs = signal<ClubDashboard[]>([]);

  // UI State
  loading = signal(false);
  showCreateEventModal = signal(false);
  error = signal<string | null>(null);

  // Current User
  currentUser = this.authService.getCurrentUser();
  institutionId = this.currentUser?.institutionId || 1;

  constructor(
    private eventService: EventService,
    private clubService: ClubService,
    private authService: AuthService
  ) {}

  ngOnInit(): void {
    this.loadDashboardData();
  }

  /**
   * Load all dashboard data
   */
  private loadDashboardData(): void {
    this.loading.set(true);
    this.error.set(null);

    // Load stats
    this.eventService.getDashboardStats(this.institutionId).subscribe({
      next: (stats) => {
        this.dashboardStats.set(stats);
      },
      error: (err) => {
        console.error('Error loading dashboard stats:', err);
        this.error.set('Failed to load dashboard statistics');
      }
    });

    // Load upcoming events
    this.eventService.getUpcomingEvents(this.institutionId).subscribe({
      next: (events) => {
        this.upcomingEvents.set(events);
        this.loading.set(false);
      },
      error: (err) => {
        console.error('Error loading upcoming events:', err);
        // If endpoint doesn't exist, fall back to all events
        this.eventService.getEventsByInstitution(this.institutionId).subscribe({
          next: (events) => {
            const upcoming = events.filter(e => e.status === 'UPCOMING');
            this.upcomingEvents.set(upcoming);
            this.loading.set(false);
          },
          error: () => {
            this.error.set('Failed to load events');
            this.loading.set(false);
          }
        });
      }
    });

    // Load clubs
    this.clubService.getClubsByInstitution(this.institutionId).subscribe({
      next: (clubs) => {
        this.clubs.set(clubs);
      },
      error: (err) => {
        console.error('Error loading clubs:', err);
        this.error.set('Failed to load clubs');
      }
    });
  }

  /**
   * Open create event modal
   */
  openCreateEventModal(): void {
    this.showCreateEventModal.set(true);
  }

  /**
   * Close create event modal
   */
  closeCreateEventModal(): void {
    this.showCreateEventModal.set(false);
  }

  /**
   * Handle event created
   */
  onEventCreated(event: Event): void {
    this.showCreateEventModal.set(false);
    this.loadDashboardData();
  }

  /**
   * Refresh dashboard data
   */
  refresh(): void {
    this.loadDashboardData();
  }
}
