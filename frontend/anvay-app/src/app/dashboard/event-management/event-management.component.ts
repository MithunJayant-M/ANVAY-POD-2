import { Component, OnInit, Input, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { EventService } from '../../../services/event.service';
import { ClubService } from '../../../services/club.service';
import { Event } from '../../../models/event.model';
import { ClubDashboard } from '../../../models/club.model';

@Component({
  selector: 'app-event-management',
  standalone: true,
  imports: [CommonModule, FormsModule, ReactiveFormsModule],
  templateUrl: './event-management.component.html',
  styleUrls: ['./event-management.component.css']
})
export class EventManagementComponent implements OnInit {
  @Input() institutionId: number = 1;

  events = signal<Event[]>([]);
  clubs = signal<ClubDashboard[]>([]);
  loading = signal(false);
  error = signal<string | null>(null);
  selectedClubFilter = signal<number | null>(null);
  selectedStatusFilter = signal<string>('');
  searchTerm = signal('');

  constructor(
    private eventService: EventService,
    private clubService: ClubService
  ) {}

  ngOnInit(): void {
    this.loadEvents();
    this.loadClubs();
  }

  /**
   * Load all events for the institution
   */
  private loadEvents(): void {
    this.loading.set(true);
    this.error.set(null);

    this.eventService.getEventsByInstitution(this.institutionId).subscribe({
      next: (events) => {
        this.events.set(events);
        this.loading.set(false);
      },
      error: (err) => {
        console.error('Error loading events:', err);
        this.error.set('Failed to load events');
        this.loading.set(false);
      }
    });
  }

  /**
   * Load clubs for filtering
   */
  private loadClubs(): void {
    this.clubService.getClubsByInstitution(this.institutionId).subscribe({
      next: (clubs) => {
        this.clubs.set(clubs);
      },
      error: (err) => {
        console.error('Error loading clubs:', err);
      }
    });
  }

  /**
   * Get filtered events based on filters and search
   */
  getFilteredEvents(): Event[] {
    let filtered = this.events();

    // Filter by club
    if (this.selectedClubFilter() !== null) {
      filtered = filtered.filter(e => e.clubId === this.selectedClubFilter());
    }

    // Filter by status
    if (this.selectedStatusFilter()) {
      filtered = filtered.filter(e => e.status === this.selectedStatusFilter());
    }

    // Filter by search term
    if (this.searchTerm()) {
      const term = this.searchTerm().toLowerCase();
      filtered = filtered.filter(e =>
        e.eventTitle.toLowerCase().includes(term) ||
        e.location.toLowerCase().includes(term) ||
        e.description?.toLowerCase().includes(term)
      );
    }

    return filtered;
  }

  /**
   * Get badge class for category
   */
  getCategoryBadgeClass(category: string): string {
    const categoryMap: Record<string, string> = {
      'SPORTS': 'category-sports',
      'ACADEMIC': 'category-academic',
      'SOCIAL': 'category-social',
      'CULTURAL': 'category-cultural',
      'TECHNICAL': 'category-technical',
      'OTHER': 'category-other'
    };
    return categoryMap[category] || 'category-other';
  }

  /**
   * Get status display label
   */
  getStatusLabel(status: string): string {
    const statusMap: Record<string, string> = {
      'UPCOMING': 'Upcoming',
      'ONGOING': 'Ongoing',
      'COMPLETED': 'Completed',
      'CANCELLED': 'Cancelled'
    };
    return statusMap[status] || status;
  }

  /**
   * Edit event
   */
  editEvent(event: Event): void {
    console.log('Edit event:', event);
    // TODO: Implement edit functionality
  }

  /**
   * Delete event
   */
  deleteEvent(eventId: number): void {
    if (confirm('Are you sure you want to delete this event?')) {
      this.eventService.deleteEvent(eventId).subscribe({
        next: () => {
          this.loadEvents();
        },
        error: (err) => {
          console.error('Error deleting event:', err);
          this.error.set('Failed to delete event');
        }
      });
    }
  }

  /**
   * Refresh events
   */
  refresh(): void {
    this.loadEvents();
  }

  /**
   * Clear filters
   */
  clearFilters(): void {
    this.selectedClubFilter.set(null);
    this.selectedStatusFilter.set('');
    this.searchTerm.set('');
  }
}
