import { inject } from '@angular/core';
import { ActivatedRouteSnapshot, Router, RouterStateSnapshot } from '@angular/router';
import { AuthenticationService } from "../services/authentication.service";

export const authGuard = (route: ActivatedRouteSnapshot, state: RouterStateSnapshot) => {
    if (inject(AuthenticationService).isAuthenticated()) {
        return true;
    }

    void inject(Router).navigate(['/login'],
        {queryParams: {returnUrl: state.url}});

    return false;
}
