import { inject } from '@angular/core';
import { CanActivateFn, Router } from '@angular/router';

export const authGuard: CanActivateFn = (route, state) => {
  const router = inject(Router);
  
<<<<<<< HEAD
  // For now, let's return true so you can actually see your UI
  // Later, you can add your logic: localStorage.getItem('token') ? true : false;
  const isAuthenticated = true; 

  if (isAuthenticated) {
    return true;
  } else {
    router.navigate(['/login']);
    return false;
  }
=======
  // MOCK AUTH CHECK: 
  // Change this to 'false' later when real authentication is ready.
  const isLoggedIn = true; 
  const userRole = 'student'; // Mocking the role as student

  if (isLoggedIn) {
    // Optional: Check if the route requires a specific role
    const expectedRole = route.data['role'];
    if (expectedRole && userRole !== expectedRole) {
      router.navigate(['/login']);
      return false;
    }
    return true;
  }

  // If not logged in, redirect to login
  router.navigate(['/login']);
  return false;
>>>>>>> cca786f (feat: implement persistent event registration and user count updates)
};