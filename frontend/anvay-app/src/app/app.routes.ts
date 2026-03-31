import { Routes } from '@angular/router';

export const routes: Routes = [
  // Redirect the root URL directly to the Club Management page
  { path: '', redirectTo: 'club-mgmt', pathMatch: 'full' },

  {
    path: '',
    loadComponent: () => 
      import('./dashboard/institution/club-mgmt/club-mgmt')
        .then(m => m.ClubMgmtComponent)
  },

  /* Temporary: Keeping other routes commented out to avoid 
    'authGuard' or 'login' module errors while you test the UI.
  */
  // { path: 'login', loadComponent: ... },
  // { path: 'dashboard/institution', ... }
];

/*import { Routes } from '@angular/router';
import { authGuard } from './guards/auth.guard';

export const routes: Routes = [
  { path: '', redirectTo: '/login', pathMatch: 'full' },
  {
    path: 'login',
    loadComponent: () => import('./auth/login/login.component').then(m => m.LoginComponent)
  },
  {
    path: 'signup',
    loadComponent: () => import('./auth/signup/signup.component').then(m => m.SignupComponent)
  },
  {
    path: 'dashboard/super-admin',
    loadComponent: () => import('./dashboard/super-admin/super-admin.component').then(m => m.SuperAdminComponent),
    canActivate: [authGuard],
    data: { role: 'super_admin' }
  },
  {
    path: 'dashboard/institution',
    loadComponent: () => import('./dashboard/institution/institution.component').then(m => m.InstitutionComponent),
    canActivate: [authGuard],
    data: { role: 'institution' }
  },
  {
    path: 'dashboard/student',
    loadComponent: () => import('./dashboard/student/student.component').then(m => m.StudentComponent),
    canActivate: [authGuard],
    data: { role: 'student' }
  },
  { path: '**', redirectTo: '/login' }
];*/
