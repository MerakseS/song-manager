import { Component } from '@angular/core';
import { AuthenticationService } from "../../services/authentication.service";
import { Router } from "@angular/router";

@Component({
    selector: 'app-header',
    templateUrl: './header.component.html',
})
export class HeaderComponent {

    constructor(private authenticationService: AuthenticationService,
        private router: Router) {
    }

    logout() {
        this.authenticationService.logout();
        return this.router.navigate(['/home']);
    }
}
