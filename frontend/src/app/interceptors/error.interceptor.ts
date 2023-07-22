import { Injectable } from '@angular/core';
import { HttpErrorResponse, HttpEvent, HttpHandler, HttpInterceptor, HttpRequest } from '@angular/common/http';
import { catchError, Observable, of, throwError } from 'rxjs';
import { Router } from "@angular/router";
import { AuthenticationService } from "../services/authentication.service";

@Injectable()
export class ErrorInterceptor implements HttpInterceptor {

    constructor(private authService: AuthenticationService,
        private router: Router) {
    }

    intercept(request: HttpRequest<unknown>, next: HttpHandler): Observable<HttpEvent<unknown>> {
        return next.handle(request).pipe(
            catchError(error => {
                console.error(error);

                let handled = false;
                if (error instanceof HttpErrorResponse) {
                    if (error.error instanceof ErrorEvent) {
                        console.error("Error Event");
                    }
                    else {
                        switch (error.status) {
                            case 401:
                                this.authService.logout();
                                void this.router.navigate(["/login"]);
                                handled = true;
                                break;
                        }
                    }
                }

                return handled ? of(error) : throwError(error);
            })
        );
    }
}
