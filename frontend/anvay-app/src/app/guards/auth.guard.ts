import { inject } from '@angular/core';
import { CanActivateFn, Router } from '@angular/router';
import { AuthService } from '../services/auth.service';

export const authGuard: CanActivateFn = (route) => {
  const authService = inject(AuthService);
  const router = inject(Router);

  if (!authService.isLoggedIn()) {
    router.navigate(['/login']);
    return false;
  }

  // Defense in depth: even if a token somehow exists, refuse dashboard access
  // for accounts that have not been approved.
  const user: any = authService.getCurrentUser();
  if (user?.status === 'pending' || user?.status === 'rejected') {
    authService.logout();
    router.navigate(['/login'], { queryParams: { reason: user.status } });
    return false;
  }

  const requiredRole = route.data?.['role'];
  const userRole = authService.getRole();

  if (requiredRole && userRole !== requiredRole) {
    router.navigate(['/login']);
    return false;
  }

  return true;
};

export const noAuthGuard: CanActivateFn = () => {
  const authService = inject(AuthService);
  const router = inject(Router);

  if (authService.isLoggedIn()) {
    const role = authService.getRole();
    if (role === 'super_admin') router.navigate(['/dashboard/super-admin']);
    else if (role === 'institution') router.navigate(['/dashboard/institution']);
    else if (role === 'club_leader') router.navigate(['/dashboard/leader']);
    else router.navigate(['/dashboard/student']);
    return false;
  }
  return true;
};
