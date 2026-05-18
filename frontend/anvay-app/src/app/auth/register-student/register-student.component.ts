import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormBuilder, FormGroup, Validators, ReactiveFormsModule } from '@angular/forms';
import { Router } from '@angular/router';
import { HttpClient } from '@angular/common/http';
import { AuthService } from '../../services/auth.service';

@Component({
  selector: 'app-register-student',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './register-student.component.html',
  styleUrls: ['./register-student.component.css']
})
// Patterns mirror the backend DTO validators exactly — keep them in sync.
const EMAIL_PATTERN  = /^[A-Za-z0-9._%+-]+@(gmail\.com|anvay\.com|anvay\.in)$/;
const NAME_PATTERN   = /^[A-Za-z][A-Za-z\s.'-]{1,49}$/;
const STUDENT_ID_PATTERN = /^\d{2,4}[A-Za-z]{2,5}\d{2,5}$/;

export class RegisterStudentComponent implements OnInit {
  form!: FormGroup;
  institutions: { institutionId: number; institutionName: string }[] = [];
  errorMessage = '';
  loading = false;
  pending = false;          // true once registration succeeded with status="pending"

  constructor(private fb: FormBuilder, private authService: AuthService, private router: Router, private http: HttpClient) {}

  ngOnInit() {
    this.form = this.fb.group({
      name: ['', [Validators.required, Validators.minLength(2), Validators.pattern(NAME_PATTERN)]],
      email: ['', [Validators.required, Validators.email, Validators.pattern(EMAIL_PATTERN)]],
      password: ['', [Validators.required, Validators.minLength(6)]],
      institutionId: [null, Validators.required],
      studentIdNumber: ['', [Validators.required, Validators.pattern(STUDENT_ID_PATTERN)]]
    });
    this.http.get<any[]>('/api/institutions/active').subscribe({
      next: ins => this.institutions = ins.map(i => ({ institutionId: i.institutionId, institutionName: i.name })),
      error: () => {}
    });
  }

  onSubmit() {
    if (this.form.invalid) {
      this.form.markAllAsTouched();
      return;
    }
    this.loading = true; this.errorMessage = '';
    this.authService.registerStudent(this.form.value).subscribe({
      next: (res: any) => {
        this.loading = false;
        // Backend now returns status="pending" with no token. Do NOT route to
        // dashboard — show the awaiting-approval screen instead.
        if (res?.status === 'pending' || !res?.token) {
          this.pending = true;
        } else {
          // Legacy auto-approval path (e.g., older backend) — keep working.
          this.router.navigate(['/dashboard/student']);
        }
      },
      error: (e) => {
        this.loading = false;
        // Backend validation errors arrive as { fieldName: "message" } from
        // GlobalExceptionHandler. Surface the first one if present.
        if (e.error && typeof e.error === 'object' && !e.error.message) {
          const firstField = Object.keys(e.error)[0];
          this.errorMessage = firstField ? `${firstField}: ${e.error[firstField]}` : 'Registration failed. Please try again.';
        } else {
          this.errorMessage = e.error?.message || 'Registration failed. Please try again.';
        }
      }
    });
  }

  back() { this.router.navigate(['/register']); }
  goToLogin() { this.router.navigate(['/login']); }
}
