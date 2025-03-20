import { CanActivateFn } from '@angular/router';
import { inject } from '@angular/core';
import { Router } from '@angular/router';

export const authGuard: CanActivateFn = (route, state) => {
  const router = inject(Router); // Use inject to get the router
  const token = localStorage.getItem('token'); // Replace with your token logic
  if (token) {
    return true; // Allow access to the route
  } else {
    // Redirect to login if not authenticated
    router.navigate(['/login']);
    return false;
  }
};