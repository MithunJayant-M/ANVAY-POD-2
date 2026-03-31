import { inject } from '@angular/core';
import { CanActivateFn, Router } from '@angular/router';

export const authGuard: CanActivateFn = (route, state) => {
  const router = inject(Router);
  
  // For now, let's return true so you can actually see your UI
  // Later, you can add your logic: localStorage.getItem('token') ? true : false;
  const isAuthenticated = true; 

  if (isAuthenticated) {
    return true;
  } else {
    router.navigate(['/login']);
    return false;
  }
};