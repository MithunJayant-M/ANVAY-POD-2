import { Component, OnInit, Input, Output, EventEmitter, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule, ReactiveFormsModule, FormBuilder, FormGroup, Validators } from '@angular/forms';
import { EventService } from '../../../services/event.service';
import { ClubService } from '../../../services/club.service';
import { Event, EventCreateRequest } from '../../../models/event.model';
import { ClubDashboard } from '../../../models/club.model';

@Component({
  selector: 'app-create-event-modal',
  standalone: true,
  imports: [CommonModule, FormsModule, ReactiveFormsModule],
  templateUrl: './create-event-modal.component.html',
  styleUrls: ['./create-event-modal.component.css']
})
export class CreateEventModalComponent implements OnInit {
  @Input() institutionId: number = 1;
  @Output() close = new EventEmitter<void>();
  @Output() eventCreated = new EventEmitter<Event>();

  eventForm!: FormGroup;
  clubs = signal<ClubDashboard[]>([]);
  loading = signal(false);
  submitting = signal(false);
  error = signal<string | null>(null);
  success = signal(false);

  categories = [
    { value: 'SPORTS', label: 'Sports' },
    { value: 'ACADEMIC', label: 'Academic' },
    { value: 'SOCIAL', label: 'Social' },
    { value: 'CULTURAL', label: 'Cultural' },
    { value: 'TECHNICAL', label: 'Technical' },
    { value: 'OTHER', label: 'Other' }
  ];

  constructor(
    private fb: FormBuilder,
    private eventService: EventService,
    private clubService: ClubService
  ) {
    this.initializeForm();
  }

  ngOnInit(): void {
    this.loadClubs();
  }

  /**
   * Initialize form with validators
   */
  private initializeForm(): void {
    this.eventForm = this.fb.group({
      eventTitle: ['', [Validators.required, Validators.minLength(3)]],
      description: ['', Validators.maxLength(500)],
      category: ['', Validators.required],
      eventDate: ['', Validators.required],
      eventTime: [''],
      location: ['', [Validators.required, Validators.minLength(3)]],
      capacity: [null, [Validators.min(1)]],
      clubId: [null]
    });
  }

  /**
   * Load clubs for selection
   */
  private loadClubs(): void {
    this.loading.set(true);
    this.clubService.getClubsByInstitution(this.institutionId).subscribe({
      next: (clubs) => {
        this.clubs.set(clubs);
        this.loading.set(false);
      },
      error: (err) => {
        console.error('Error loading clubs:', err);
        this.loading.set(false);
      }
    });
  }

  /**
   * Submit form and create event
   */
  onSubmit(): void {
    if (this.eventForm.invalid) {
      this.markFormGroupTouched(this.eventForm);
      return;
    }

    this.submitting.set(true);
    this.error.set(null);

    const formValue = this.eventForm.value;
    const createRequest: EventCreateRequest = {
      institutionId: this.institutionId,
      eventTitle: formValue.eventTitle.trim(),
      description: formValue.description?.trim() || undefined,
      category: formValue.category,
      eventDate: formValue.eventDate,
      eventTime: formValue.eventTime || undefined,
      location: formValue.location.trim(),
      capacity: formValue.capacity || undefined,
      clubId: formValue.clubId ? parseInt(formValue.clubId) : undefined
    };

    this.eventService.createEvent(createRequest).subscribe({
      next: (event) => {
        this.success.set(true);
        this.submitting.set(false);
        setTimeout(() => {
          this.eventCreated.emit(event);
          this.closeModal();
        }, 1000);
      },
      error: (err) => {
        console.error('Error creating event:', err);
        this.error.set(err.error?.message || 'Failed to create event. Please try again.');
        this.submitting.set(false);
      }
    });
  }

  /**
   * Close modal
   */
  closeModal(): void {
    this.close.emit();
    this.reset();
  }

  /**
   * Reset form
   */
  reset(): void {
    this.eventForm.reset();
    this.error.set(null);
    this.success.set(false);
  }

  /**
   * Mark all fields as touched to show validation errors
   */
  private markFormGroupTouched(formGroup: FormGroup): void {
    Object.keys(formGroup.controls).forEach(key => {
      const control = formGroup.get(key);
      control?.markAsTouched();
    });
  }

  /**
   * Get field error message
   */
  getFieldError(fieldName: string): string {
    const control = this.eventForm.get(fieldName);

    if (!control || !control.errors || !control.touched) {
      return '';
    }

    const errors = control.errors;

    if (errors['required']) {
      return `${this.getFieldLabel(fieldName)} is required`;
    }
    if (errors['minLength']) {
      return `${this.getFieldLabel(fieldName)} must be at least ${errors['minLength'].requiredLength} characters`;
    }
    if (errors['maxLength']) {
      return `${this.getFieldLabel(fieldName)} must not exceed ${errors['maxLength'].requiredLength} characters`;
    }
    if (errors['min']) {
      return `${this.getFieldLabel(fieldName)} must be at least ${errors['min'].min}`;
    }

    return 'Invalid input';
  }

  /**
   * Get field label
   */
  private getFieldLabel(fieldName: string): string {
    const labels: Record<string, string> = {
      eventTitle: 'Event Title',
      description: 'Description',
      category: 'Category',
      eventDate: 'Event Date',
      eventTime: 'Event Time',
      location: 'Location',
      capacity: 'Capacity',
      clubId: 'Club'
    };
    return labels[fieldName] || fieldName;
  }

  /**
   * Check if field has error
   */
  hasFieldError(fieldName: string): boolean {
    const control = this.eventForm.get(fieldName);
    return !!(control && control.errors && control.touched);
  }

  /**
   * Get today's date in YYYY-MM-DD format
   */
  getTodayDate(): string {
    const today = new Date();
    return today.toISOString().split('T')[0];
  }
}
