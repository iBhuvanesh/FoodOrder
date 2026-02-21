import { CanActivateFn, ActivatedRouteSnapshot, Router } from '@angular/router';
import { inject } from '@angular/core';

export const roleGuard: CanActivateFn = (route: ActivatedRouteSnapshot) => {
  const role = localStorage.getItem('role');
  const requiredRoles = route.data['roles'] as string[];
  if (role && requiredRoles?.includes(role)) {
    return true;
  }
  return inject(Router).createUrlTree(['/restaurants']);
};
