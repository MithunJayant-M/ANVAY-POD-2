import { Routes } from '@angular/router';

import { StudentLayoutComponent } from './dashboard/student/student-layout/student-layout.component';
import { StudentDashboardComponent } from './dashboard/student/student-dashboard/student-dashboard.component';
import { EventFeedComponent } from './dashboard/student/event-feed/event-feed.component';

export const routes: Routes = [
  // 1. Land on the Student Dashboard by default
  { path: '', redirectTo: 'dashboard/student', pathMatch: 'full' },

  {
    path: 'dashboard/student',
    loadComponent: () => 
      import('./dashboard/student/student.component')
        .then(m => m.StudentComponent),
    children: [
      {
        path: 'home',
        loadComponent: () => 
          import('./dashboard/student/student-dashboard/student-dashboard.component')
            .then(m => m.StudentDashboardComponent)
      },
      {
        path: 'event-feed',
        loadComponent: () => 
          import('./dashboard/student/event-feed/event-feed.component')
            .then(m => m.EventFeedComponent)
      },
      {
        path: 'clubs',
        loadComponent: () => 
          import('./dashboard/student/clubs/clubs.component')
            .then(m => m.ClubsComponent)
      },
      {
        path: 'leaderboard',
        loadComponent: () => 
          import('./dashboard/student/leaderboard/leaderboard.component')
            .then(m => m.LeaderboardComponent)
      },
      {
        path: 'profile',
        loadComponent: () => 
          import('./dashboard/student/student-profile/student-profile.component')
            .then(m => m.StudentProfileComponent)
      },
      // 2. Default child: If they hit /dashboard/student, show /home
      { path: '', redirectTo: 'home', pathMatch: 'full' }
    ]
  },

  // 3. Fallback for any undefined routes
  { path: '**', redirectTo: 'dashboard/student' }
];

// export const routes: Routes = [
//   // Redirect the root URL directly to the Club Management page
//   { path: '', redirectTo: 'club-mgmt', pathMatch: 'full' },

//   {
//     path: '',
//     loadComponent: () => 
//       import('./dashboard/institution/club-mgmt/club-mgmt')
//         .then(m => m.ClubMgmtComponent)
//   },

//   /* Temporary: Keeping other routes commented out to avoid 
//     'authGuard' or 'login' module errors while you test the UI.
//   */
//   // { path: 'login', loadComponent: ... },
//   // { path: 'dashboard/institution', ... }
// ];

/*import { Routes } from '@angular/router';
import { authGuard } from './guards/auth.guard';

export const routes: Routes = [
  // Redirect root to login (handled by teammate)
  { path: '', redirectTo: 'dashboard/student', pathMatch: 'full' },

  // External routes (Not your responsibility)
  {
    path: 'login',
    loadComponent: () => import('./auth/login/login.component').then(m => m.LoginComponent)
  },
  {
    path: 'signup',
    loadComponent: () => import('./auth/signup/signup.component').then(m => m.SignupComponent)
  },

  // Your Responsibility: The Student Portal
  {
    path: 'dashboard/student',
    loadComponent: () => import('./dashboard/student/student.component').then(m => m.StudentComponent),
    canActivate: [authGuard],
    data: { role: 'student' },
    children: [
      {
        path: 'home',
        loadComponent: () => import('./dashboard/student/student-dashboard/student-dashboard.component').then(m => m.StudentDashboardComponent)
      },
      {
        path: 'event-feed',
        loadComponent: () => import('./dashboard/student/event-feed/event-feed.component').then(m => m.EventFeedComponent)
      },
      {
        path: 'clubs',
        loadComponent: () => import('./dashboard/student/clubs/clubs.component').then(m => m.ClubsComponent)
      },
      {
        path: 'leaderboard',
        loadComponent: () => import('./dashboard/student/leaderboard/leaderboard.component').then(m => m.LeaderboardComponent)
      },
      {
        path: 'profile',
        loadComponent: () => import('./dashboard/student/student-profile/student-profile.component').then(m => m.StudentProfileComponent)
      },
      // Default view when arriving at /dashboard/student
      { path: '', redirectTo: 'home', pathMatch: 'full' }
    ]
  },

  // Catch-all
  { path: '**', redirectTo: '/login' }
];
*/