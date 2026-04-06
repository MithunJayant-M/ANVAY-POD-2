import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormBuilder, FormGroup, Validators, ReactiveFormsModule } from '@angular/forms';
import { Router, RouterLink, ActivatedRoute } from '@angular/router';
import { AuthService } from '../../services/auth.service';

@Component({
  selector: 'app-signup',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule, RouterLink],
  templateUrl: './signup.component.html',
  styleUrls: ['./signup.component.css']
})
export class SignupComponent implements OnInit {
  signupType: 'institution' | 'student' = 'institution';
  form!: FormGroup;
  errorMessage = '';
  loading = false;

  constructor(
    private fb: FormBuilder,
    private authService: AuthService,
    private router: Router,
    private route: ActivatedRoute
  ) {}

  ngOnInit(): void {
    this.route.queryParams.subscribe(params => {
      this.signupType = params['type'] === 'student' ? 'student' : 'institution';
      this.buildForm();
    });
  }

  buildForm(): void {
    if (this.signupType === 'institution') {
      this.form = this.fb.group({
        institutionName: ['', Validators.required],
        adminName: ['', Validators.required],
        email: ['', [Validators.required, Validators.email]],
        password: ['', [Validators.required, Validators.minLength(6)]],
        phone: [''],
        address: ['']
      });
    } else {
      this.form = this.fb.group({
        name: ['', Validators.required],
        email: ['', [Validators.required, Validators.email]],
        password: ['', [Validators.required, Validators.minLength(6)]],
        institutionId: [null]
      });
    }
  }

  onSubmit(): void {
    if (this.form.invalid) return;
    this.loading = true;
    this.errorMessage = '';

    const obs = this.signupType === 'institution'
      ? this.authService.registerInstitution(this.form.value)
      : this.authService.registerStudent(this.form.value);

    obs.subscribe({
      next: (response) => {
        this.loading = false;
        const route = response.role === 'institution' ? '/dashboard/institution' : '/dashboard/student';
        this.router.navigate([route]);
      },
      error: (err) => {
        this.loading = false;
        this.errorMessage = err.error?.message || 'Registration failed.';
      }
    });
  }
}
