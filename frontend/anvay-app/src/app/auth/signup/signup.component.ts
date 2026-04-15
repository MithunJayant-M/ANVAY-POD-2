import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';

@Component({
  selector: 'app-signup',
  standalone: true,
  imports: [CommonModule, RouterModule],
  template: `
    <div class="flex flex-col items-center justify-center min-h-screen bg-gray-50">
      <div class="p-8 bg-white shadow-xl rounded-2xl w-96 text-center">
        <h2 class="text-2xl font-bold mb-4">Create Account</h2>
        <p class="text-gray-500 mb-6">Signup page is under development by the team.</p>
        <a routerLink="/login" class="text-indigo-600 hover:underline">Back to Login</a>
      </div>
    </div>
  `
})
export class SignupComponent {}