import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Router } from '@angular/router';
import { AuthService } from '../../services/auth.service';

@Component({
  selector: 'app-institution',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './institution.component.html',
  styleUrls: ['./institution.component.css']
})
export class InstitutionComponent {
  constructor(private authService: AuthService, private router: Router) {}
  logout(): void { this.authService.logout(); this.router.navigate(['/login']); }
}
